package com.example.blocktradefinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final int DELAY_MILLIS = 3000; // 3 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Find the ProgressBar by its ID
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        // Show the progress bar when the activity is created
        progressBar.setVisibility(View.VISIBLE);

        // Simulate a loading delay (you can adjust this as needed)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the progress bar after the delay
                progressBar.setVisibility(View.GONE);

                // Redirect to HomeActivity after the delay
                Intent intent = new Intent(WelcomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish(); // Close WelcomeActivity so the user can't go back
            }
        }, DELAY_MILLIS);
    }
}
