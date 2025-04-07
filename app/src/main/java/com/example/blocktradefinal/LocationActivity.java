package com.example.blocktradefinal;

import android.os.Bundle;

public class LocationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //NAVIGATING BOTTOM NAV MENU
        // Setup Bottom Navigation and highlight 'Home'
        setupBottomNavigation(R.id.nav_loc);
    }
}
