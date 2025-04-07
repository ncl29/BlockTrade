package com.example.blocktradefinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView productImage;
    TextView captionText, locationText, typeText, ownerText, priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //BACK BUTTON PRODUCT
        ImageButton backButton = findViewById(R.id.backButtonProduct);
            backButton.setOnClickListener(v -> {
                finish(); // This will return to the previous activity (HomeActivity)
            });

            //CHAT NOW PRODUCT
        LinearLayout chatNowContainer = findViewById(R.id.chatNowContainer);
        chatNowContainer.setOnClickListener(v -> {
            Intent chatIntent = new Intent(ProductDetailsActivity.this, MessageActivity.class);
            startActivity(chatIntent);
        });

        productImage = findViewById(R.id.productImage);
        captionText = findViewById(R.id.captionText);
        locationText = findViewById(R.id.locationText);
        typeText = findViewById(R.id.typeText);
        ownerText = findViewById(R.id.ownerText);
        priceText = findViewById(R.id.priceText);

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

            // Set other details
            captionText.setText(intent.getStringExtra("caption"));
            locationText.setText("Location: " + intent.getStringExtra("location"));
            typeText.setText(intent.getStringExtra("type"));
            ownerText.setText("Posted by: " + intent.getStringExtra("owner"));
            priceText.setText("Price: " + intent.getStringExtra("price"));
        }
    }
}
