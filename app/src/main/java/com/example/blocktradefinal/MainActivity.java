package com.example.blocktradefinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Delay for 3 seconds then decide where to go
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);

            SharedPreferences prefs = getSharedPreferences("UserProfilePrefs", MODE_PRIVATE);
            boolean isSignedUp = prefs.getBoolean("isSignedUp", false);

            Intent intent;
            if (isSignedUp) {
                // User has already signed up, redirect to Login
                intent = new Intent(MainActivity.this, LogInActivity.class);
            } else {
                // First-time user, go to Sign Up
                intent = new Intent(MainActivity.this, SignUpActivity.class);
            }

            startActivity(intent);
            finish();

        }, 3000); // 3-second splash delay
    }
}
