package com.example.blocktradefinal;

import android.os.Bundle;

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // NAVIGATING BOTTOM NAV MENU
        setupBottomNavigation(R.id.nav_message);
    }
}