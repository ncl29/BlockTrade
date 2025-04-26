package com.example.blocktradefinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView productImage;
    TextView captionText, locationText, typeText, ownerText, priceText;
    Button deleteProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize views
        ImageButton backButton = findViewById(R.id.backButtonProduct);
        backButton.setOnClickListener(v -> finish());

        LinearLayout chatNowContainer = findViewById(R.id.chatNowContainer);
        chatNowContainer.setOnClickListener(v -> {
            // Get the product owner's information
            String productOwner = ownerText.getText().toString().replace("Posted by: ", "");
            // Start ChatActivity and pass the product owner's id
            Intent chatIntent = new Intent(ProductDetailsActivity.this, ChatActivity.class);
            chatIntent.putExtra("PARTNER_ID", productOwner); // Pass product owner's id
            startActivity(chatIntent);
        });

        productImage = findViewById(R.id.productImage);
        captionText = findViewById(R.id.captionText);
        locationText = findViewById(R.id.locationText);
        typeText = findViewById(R.id.typeText);
        ownerText = findViewById(R.id.ownerText);
        priceText = findViewById(R.id.priceText);
        deleteProductButton = findViewById(R.id.deleteProductButton); // Delete button

        // Get product details from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String imageUri = intent.getStringExtra("imageUri");
            int imageResId = intent.getIntExtra("imageResId", -1); // Default -1 if not passed

            if (imageUri != null && !imageUri.isEmpty()) {
                Glide.with(this)
                        .load(Uri.parse(imageUri))
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(productImage);
            } else if (imageResId != -1) {
                Glide.with(this)
                        .load(imageResId)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.ic_launcher_background);
            }

            // Set other product details
            String productOwner = intent.getStringExtra("owner");
            captionText.setText(intent.getStringExtra("caption"));
            locationText.setText("Location: " + intent.getStringExtra("location"));
            typeText.setText(intent.getStringExtra("type"));
            ownerText.setText("Posted by: " + productOwner);
            priceText.setText("Price: " + intent.getStringExtra("price"));

            // Get current logged-in user
            String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

            // If the current user is the owner of the product, show the delete button
            if (currentUserName != null && currentUserName.equals(productOwner)) {
                deleteProductButton.setVisibility(View.VISIBLE);

                // Set delete button click listener
                deleteProductButton.setOnClickListener(v -> {
                    // Show confirmation dialog before deleting
                    showDeleteConfirmationDialog(intent.getStringExtra("productId"));
                });
            }
        }
    }

    private void showDeleteConfirmationDialog(String productId) {
        // Create the AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // If yes, delete the product from the database
                    deleteProduct(productId);
                })
                .setNegativeButton("No", null) // "No" simply closes the dialog
                .create()
                .show();
    }

    private void deleteProduct(String productId) {
        // Log the productId to ensure it's being passed correctly
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);

        productRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Successful deletion, show a message
                    Toast.makeText(ProductDetailsActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();

                    // Notify the HomeActivity that the product has been deleted (if needed)
                    Intent intent = new Intent();
                    intent.putExtra("deletedProductId", productId); // Pass the deleted productId
                    setResult(RESULT_OK, intent); // Set the result for the previous activity
                    finish(); // Navigate back to the previous screen
                })
                .addOnFailureListener(e -> {
                    // Log failure details and show an error message
                    Log.e("ProductDeletion", "Failed to delete product: " + e.getMessage());
                    Toast.makeText(ProductDetailsActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                });
    }
}
