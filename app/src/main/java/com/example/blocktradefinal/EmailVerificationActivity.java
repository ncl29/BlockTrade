package com.example.blocktradefinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Handler handler = new Handler();
    private Runnable checkVerificationRunnable;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        mAuth = FirebaseAuth.getInstance();
        TextView infoText = findViewById(R.id.verificationMessage);
        progressBar = findViewById(R.id.progressBar);

        infoText.setText("We've sent a verification link to your email. Please verify to continue.");

        ImageButton backButton = findViewById(R.id.backButtonProduct);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(EmailVerificationActivity.this, SignUpActivity.class));
            finish();
        });

        checkVerificationRunnable = new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.reload().addOnSuccessListener(unused -> {
                        if (user.isEmailVerified()) {
                            Toast.makeText(EmailVerificationActivity.this, "Email verified!", Toast.LENGTH_SHORT).show();

                            // Show progress bar
                            progressBar.setVisibility(View.VISIBLE);

                            // Wait for 2 seconds before navigating
                            new Handler().postDelayed(() -> {
                                mAuth.signOut();
                                startActivity(new Intent(EmailVerificationActivity.this, LogInActivity.class));
                                finish();
                            }, 2000);

                        } else {
                            handler.postDelayed(this, 3000);
                        }
                    });
                }
            }
        };

        handler.postDelayed(checkVerificationRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkVerificationRunnable);
    }
}
