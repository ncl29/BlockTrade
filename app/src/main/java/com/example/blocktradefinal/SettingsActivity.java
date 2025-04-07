package com.example.blocktradefinal;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends BaseActivity {

    private Switch switchNotification;
    private CheckBox checkChats, checkTransactionUpdate, checkTradeStatus;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initialize Views
        switchNotification = findViewById(R.id.switchNotification);
        checkChats = findViewById(R.id.checkChats);
        checkTransactionUpdate = findViewById(R.id.checkTransactionUpdate);
        checkTradeStatus = findViewById(R.id.checkTradeStatus);

        // Load Saved States
        boolean isSwitchOn = sharedPreferences.getBoolean("switchNotification", false);
        switchNotification.setChecked(isSwitchOn);
        updateSwitchColor(isSwitchOn); // Apply color based on saved state

        checkChats.setChecked(sharedPreferences.getBoolean("checkChats", false));
        checkTransactionUpdate.setChecked(sharedPreferences.getBoolean("checkTransactionUpdate", false));
        checkTradeStatus.setChecked(sharedPreferences.getBoolean("checkTradeStatus", false));

        // Save & Update Switch State
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("switchNotification", isChecked);
            editor.apply();
            updateSwitchColor(isChecked); // Update switch color
        });

        // Save Checkbox States
        checkChats.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("checkChats", isChecked));
        checkTransactionUpdate.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("checkTransactionUpdate", isChecked));
        checkTradeStatus.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("checkTradeStatus", isChecked));

        // Initialize back button functionality
        setupBackButton();

    }

    // Method to save checkbox states
    private void savePreference(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Method to update switch color
    private void updateSwitchColor(boolean isChecked) {
        if (isChecked) {
            switchNotification.getThumbDrawable().setTint(Color.parseColor("#0F3D3E"));
            switchNotification.getTrackDrawable().setTint(Color.parseColor("#4CAF50"));
        } else {
            switchNotification.getThumbDrawable().setTint(Color.parseColor("#262A56"));
            switchNotification.getTrackDrawable().setTint(Color.parseColor("#0E46A3"));
        }
    }
}