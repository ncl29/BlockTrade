package com.example.blocktradefinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TransactionActivity extends BaseActivity {
    private RadioGroup radioGroup;
    private static final String PREFS_NAME = "TransactionPrefs";
    private static final String KEY_SELECTED_BUTTON = "selectedButton";
    private static final String KEY_SELECTED_RADIO = "selectedRadio";

    Button btnOngoingTransaction, btnCompletedTransaction;
    RadioButton radioForTrade, radioForSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //FOR ONGOING/COMPLETED TRANSACTION
        // Initialize buttons
        btnOngoingTransaction = findViewById(R.id.btnOngoingTransaction);
        btnCompletedTransaction = findViewById(R.id.btnCompletedTransaction);

        // Set click listeners for Ongoing Transaction button
        btnOngoingTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change color for Ongoing Transaction button
                btnOngoingTransaction.setTextColor(getResources().getColor(R.color.new_font_color));
                btnOngoingTransaction.setBackgroundTintList(getResources().getColorStateList(R.color.new_btn_color));

                // Reset Completed Transaction button color
                btnCompletedTransaction.setTextColor(getResources().getColor(R.color.white));
                btnCompletedTransaction.setBackgroundTintList(getResources().getColorStateList(R.color.btn));

                // Enable Completed Transaction button for future switching
                btnCompletedTransaction.setEnabled(true);
            }
        });

        // Set click listeners for Completed Transaction button
        btnCompletedTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change color for Completed Transaction button
                btnCompletedTransaction.setTextColor(getResources().getColor(R.color.new_font_color));
                btnCompletedTransaction.setBackgroundTintList(getResources().getColorStateList(R.color.new_btn_color));

                // Reset Ongoing Transaction button color
                btnOngoingTransaction.setTextColor(getResources().getColor(R.color.white));
                btnOngoingTransaction.setBackgroundTintList(getResources().getColorStateList(R.color.btn));

                // Enable Ongoing Transaction button for future switching
                btnOngoingTransaction.setEnabled(true);
            }
        });

        //FOR FILTER
        radioForTrade = findViewById(R.id.radioForTrade);
        radioForSale = findViewById(R.id.radioForSale);
        radioForTrade.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                radioForSale.setChecked(false);
            }
        });
        radioForSale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                radioForTrade.setChecked(false);
            }
        });

        //FOR LOADING SAVED SELECTION
        radioGroup = findViewById(R.id.filter);
        radioForTrade = findViewById(R.id.radioForTrade);
        radioForSale = findViewById(R.id.radioForSale);

        // Load saved selections
        loadPreferences();

        // Button Click Listeners
        btnOngoingTransaction.setOnClickListener(v -> {
            saveSelectedButton("ongoing");
            updateButtonColors("ongoing");
        });

        btnCompletedTransaction.setOnClickListener(v -> {
            saveSelectedButton("completed");
            updateButtonColors("completed");
        });

        // Radio Button Change Listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioForTrade) {
                saveSelectedRadio("trade");
            } else if (checkedId == R.id.radioForSale) {
                saveSelectedRadio("sale");
            }
        });

        //NAVIGATING BOTTOM NAV MENU
        // Setup Bottom Navigation and highlight 'Home'
        setupBottomNavigation(R.id.nav_transaction);
    }

    //METHODS FOR LOADING SAVED SELECTION
    private void saveSelectedButton(String button) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_SELECTED_BUTTON, button);
        editor.apply();
    }

    private void saveSelectedRadio(String radio) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_SELECTED_RADIO, radio);
        editor.apply();
    }

    private void loadPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String selectedButton = preferences.getString(KEY_SELECTED_BUTTON, "ongoing");
        String selectedRadio = preferences.getString(KEY_SELECTED_RADIO, "trade");

        // Restore button selection
        updateButtonColors(selectedButton);

        // Restore radio button selection
        if (selectedRadio.equals("trade")) {
            radioForTrade.setChecked(true);
        } else {
            radioForSale.setChecked(true);
        }
    }

    private void updateButtonColors(String selectedButton) {
        if (selectedButton.equals("ongoing")) {
            btnOngoingTransaction.setBackgroundTintList(getColorStateList(R.color.ic_color));
            btnCompletedTransaction.setBackgroundTintList(getColorStateList(R.color.btn));
        } else {
            btnCompletedTransaction.setBackgroundTintList(getColorStateList(R.color.ic_color));
            btnOngoingTransaction.setBackgroundTintList(getColorStateList(R.color.btn));
        }
    }
}
