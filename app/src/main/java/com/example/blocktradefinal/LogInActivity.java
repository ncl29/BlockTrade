package com.example.blocktradefinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText nameInput, passwordInput;
    private Button logInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.name); // name, not email
        passwordInput = findViewById(R.id.password);
        logInButton = findViewById(R.id.loginButton);

        TextView signUpTextView = findViewById(R.id.signup_btn);
        signUpTextView.setOnClickListener(v -> {
            startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
        });

        logInButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String name = nameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Name and password must not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserProfilePrefs", MODE_PRIVATE);
        String email = prefs.getString("name_" + name, null);

        if (email == null) {
            Toast.makeText(this, "No account found with this name.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
                        // ✅ Save the name for future use
                        SharedPreferences namePrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = namePrefs.edit();
                        editor.putString("name", name);  // Save the name used to login
                        editor.apply();

                        Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, WelcomeActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(LogInActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LogInActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
