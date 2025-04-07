package com.example.blocktradefinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddCaptionActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ImageView capturedImage;
    private EditText captionEditText, locationEditText, priceEditText;
    private RadioGroup tradeSaleRadioGroup;
    private RadioButton radioForTrade, radioForSale;
    private Spinner classificationSpinner;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_caption);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Initialize views
        capturedImage = findViewById(R.id.capturedImage);
        captionEditText = findViewById(R.id.captionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        priceEditText = findViewById(R.id.priceEditText);
        tradeSaleRadioGroup = findViewById(R.id.tradeSaleRadioGroup);
        radioForTrade = findViewById(R.id.radioForTrade);
        radioForSale = findViewById(R.id.radioForSale);
        classificationSpinner = findViewById(R.id.classificationSpinner);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Load image URI
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString == null) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        imageUri = Uri.parse(imageUriString);
        capturedImage.setImageURI(imageUri);

        // Button listeners
        findViewById(R.id.backButton).setOnClickListener(v -> showBackConfirmation());
        findViewById(R.id.submitButton).setOnClickListener(v -> handleSubmit());
        findViewById(R.id.cancelButton).setOnClickListener(v -> showCancelConfirmation());

        setupPriceFormatting();
        setupClassificationSpinner();
        requestLocationPermission();
    }

    private void setupPriceFormatting() {
        priceEditText.addTextChangedListener(new TextWatcher() {
            boolean isEditing = false;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!isEditing) {
                    isEditing = true;
                    String cleanText = s.toString().replace("₱", "").trim();
                    if (!cleanText.isEmpty()) {
                        priceEditText.setText("₱ " + cleanText);
                        priceEditText.setSelection(priceEditText.getText().length());
                    }
                    isEditing = false;
                }
            }
        });
    }

    private void setupClassificationSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classification_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classificationSpinner.setAdapter(adapter);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        locationEditText.setText(addresses.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    locationEditText.setText("Unable to detect location");
                }
            } else {
                locationEditText.setText("Location unavailable");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSubmit() {
        String caption = captionEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().replace("₱", "").trim();
        String classification = classificationSpinner.getSelectedItem().toString();

        if (caption.isEmpty() || location.isEmpty() || price.isEmpty() ||
                tradeSaleRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = radioForTrade.isChecked() ? "For Trade" : "For Sale";

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String owner = (currentUser != null && currentUser.getDisplayName() != null)
                ? currentUser.getDisplayName()
                : "Unknown User";

        new AlertDialog.Builder(this)
                .setTitle("Confirm Post")
                .setMessage("Are you sure you want to post this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String id = databaseReference.push().getKey();
                    if (id != null && imageUri != null) {
                        Product product = new Product(
                                caption,          // name
                                location,
                                price,
                                owner,
                                classification,   // category
                                type,             // tradeType
                                imageUri.toString(),
                                0                 // imageResId (not used here, so just pass 0)
                        );

                        databaseReference.child(id).setValue(product);
                        Toast.makeText(this, "Item posted successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Error posting item", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showBackConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("Any unsaved data will be lost. Continue?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(new Intent(this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showCancelConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Post?")
                .setMessage("Any unsaved data will be lost. Continue?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(new Intent(this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
