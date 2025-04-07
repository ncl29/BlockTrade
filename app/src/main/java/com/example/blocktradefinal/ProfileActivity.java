package com.example.blocktradefinal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends BaseActivity {

    private static final String PREFS_NAME = "ProfilePrefs";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private SharedPreferences sharedPreferences;
    private ImageView profileImageView, editImageIcon;
    private TextView tvUserName, tvUserEmail;
    private EditText etLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // UI References
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        profileImageView = findViewById(R.id.profileImage);
        editImageIcon = findViewById(R.id.editImageIcon);
        etLocation = findViewById(R.id.etLocation);
        Button btnDetectLocation = findViewById(R.id.btnDetectLocation);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        setupBackButton();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load Firebase user info
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            tvUserName.setText(name != null ? name : "Your Name");
            tvUserEmail.setText(email != null ? email : "your@email.com");
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LogInActivity.class));
            finish();
            return;
        }

        // Image handling
        loadSavedImage();
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bitmap selectedImage = null;
                        if (result.getData().getData() != null) {
                            try {
                                InputStream imageStream = getContentResolver().openInputStream(result.getData().getData());
                                selectedImage = BitmapFactory.decodeStream(imageStream);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            selectedImage = (Bitmap) result.getData().getExtras().get("data");
                        }

                        if (selectedImage != null) {
                            profileImageView.setImageBitmap(selectedImage);
                            saveImageToPreferences(selectedImage);
                        }
                    }
                }
        );
        editImageIcon.setOnClickListener(view -> showImagePickerOptions());

        // Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        String savedLocation = sharedPreferences.getString("saved_location", "");
        if (!savedLocation.isEmpty()) {
            etLocation.setText(savedLocation);
        }
        btnDetectLocation.setOnClickListener(v -> checkLocationPermission());

        // Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(ProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
            finish();
        });

        // Delete Account
        btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to permanently delete your account?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.delete().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    sharedPreferences.edit().clear().apply();
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(ProfileActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // Location
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            getAddressFromLocation(location);
                        } else {
                            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                etLocation.setText(addressText);
                sharedPreferences.edit().putString("saved_location", addressText).apply();
            } else {
                etLocation.setText("Address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            etLocation.setText("Geocoder error");
        }
    }

    // Image Picker
    private void showImagePickerOptions() {
        String[] options = {"Upload Photo", "Take Photo"};
        new AlertDialog.Builder(this)
                .setTitle("Choose an option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) openImagePicker();
                    else openCamera();
                })
                .show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            imagePickerLauncher.launch(cameraIntent);
        }
    }

    private void openImagePicker() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(pickPhoto);
    }

    private void saveImageToPreferences(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        sharedPreferences.edit().putString("profile_image", encodedImage).apply();
    }

    private void loadSavedImage() {
        String encodedImage = sharedPreferences.getString("profile_image", null);
        if (encodedImage != null) {
            byte[] byteArray = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            profileImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
