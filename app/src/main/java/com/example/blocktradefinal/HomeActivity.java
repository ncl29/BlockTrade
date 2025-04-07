package com.example.blocktradefinal;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private ImageView menuIcon;
    private ProductAdapter adapter;
    ImageView messageIcon;
    FloatingActionButton fabAdd;
    private List<Product> originalProductList;
    private List<Product> filteredProductList;
    private ImageView searchIcon, searchBackButton;
    private LinearLayout searchContainer;
    private TextView appName;
    private boolean isSearchVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inside onCreate()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // PRODUCT
        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize the product lists
        originalProductList = new ArrayList<>();
        filteredProductList = new ArrayList<>();

        // Load any product passed from AddCaptionActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("newProduct")) {
            Product newProduct = (Product) intent.getSerializableExtra("newProduct");
            if (newProduct != null) {
                originalProductList.add(newProduct);
            }
        }

        // Optionally add sample data ONLY if list is empty (for testing/demo purposes)
        if (originalProductList.isEmpty()) {
            originalProductList.add(new Product("Hollyford Green Checked Shirt", "F. Pimentel Ave, Daet, Camarines Norte", "₱550", "Janica Salvador", "CLOTHES", "FOR SALE", null, R.drawable.images));
            originalProductList.add(new Product("Large Canvas Beach Tote Bag", "F. Pimentel Ave, Daet, Camarines Norte", "₱250", "Jharen Lastimosa", "BAGS", "FOR TRADE", null, R.drawable.bag));
            originalProductList.add(new Product("Cole Ceramic Non-stick Frying Pan", "Dulongbayan Ilaod, Daet, Camarines Norte", "₱380", "Lander Castrillo", "UTENSILS", "FOR SALE", null, R.drawable.pan));
            originalProductList.add(new Product("Sneakers Casual Breathable Sport", "F. Pimentel Ave, Daet, Camarines Norte", "₱250", "Aron Labiano", "SHOES", "FOR TRADE", null, R.drawable.shoes));
            originalProductList.add(new Product("Canon EOS 1500D", "Dulongbayan Ilaod, Daet, Camarines Norte", "₱1,850", "Lucky Mansanas", "GADGETS", "FOR SALE", null, R.drawable.camera));
            originalProductList.add(new Product("Headband with nylon fastener", "Dulongbayan Ilaod, Daet, Camarines Norte", "₱50", "Lucky Mansanas", "HEADWEAR", "FOR SALE", null, R.drawable.headwewar));

            originalProductList.add(new Product("CLAUDE CASUAL DRESS FOR WOMENS", "Barangay II (Poblacion) Daet, Camarines Norte", "₱150", "Janica Salvador", "CLOTHES", "FOR SALE", null, R.drawable.clothes));
            originalProductList.add(new Product("Moira Shoulder Bag", "F. Pimentel Ave, Daet, Camarines Norte", "₱350", "Jharen Lastimosa", "BAGS", "FOR TRADE", null, R.drawable.bag2));
            originalProductList.add(new Product("Gravy Sauce Ladle With Long Handle", "Pamorangon   Daet, Camarines Norte", "₱80", "Lander Castrillo", "UTENSILS", "FOR SALE", null, R.drawable.utensil));
            originalProductList.add(new Product("Milana Heel Sandals", "Vivencio Street, Daet, Camarines Norte", "₱250", "Aron Labiano", "SHOES", "FOR TRADE", null, R.drawable.shoes2));
            originalProductList.add(new Product("USB Type C Earphones Magnetic In-Ear", "Magang Road, Daet, Camarines Norte", "₱100", "Lucky Mansanas", "GADGETS", "FOR SALE", null, R.drawable.gadget));
            originalProductList.add(new Product("Lace Headband", "San Vicente Road, Daet, Camarines Norte", "₱50", "Lucky Mansanas", "HEADWEAR", "FOR SALE", null, R.drawable.headwewar1));

            //NEW
            originalProductList.add(new Product("The Costello Pant", "San Isidro Road, Daet, Camarines Norte", "₱180", "Alexa Cruz", "CLOTHES", "FOR TRADE", null, R.drawable.clothes1));
            originalProductList.add(new Product("Nylon Male Shoulder Bag", "Mambalite Street, Daet, Camarines Norte", "₱320", "Mark Villanueva", "BAGS", "FOR SALE", null, R.drawable.bag3));
            originalProductList.add(new Product("Stainless Whisk", "Bactas Road, Basud, Camarines Norte", "₱90", "Trina Gomez", "UTENSILS", "FOR TRADE", null, R.drawable.utensil1));
            originalProductList.add(new Product("RRJ Men's Flip-flop", "Cobangbang-San Isidro NIA Road, Daet, Camarines Norte", "₱270", "Neil Soriano", "SHOES", "FOR SALE", null, R.drawable.shoes3));
            originalProductList.add(new Product("Nexode Charger 30W White", "NIA Road, Daet, Camarines Norte", "₱120", "Claire Abad", "GADGETS", "FOR TRADE", null, R.drawable.gadget1));
            originalProductList.add(new Product("CN Travel 550 Hat ", "Jose Rizal Street, Daet, Camarines Norte", "₱60", "Claire Abad", "HEADWEAR", "FOR SALE", null, R.drawable.headwear));

            originalProductList.add(new Product("Straight Fit Khaki Pants", "4X54+4G7, F. Pimentel Ave, Daet, Camarines Norte", "₱170", "Alexa Cruz", "CLOTHES", "FOR SALE", null, R.drawable.clothes2));
            originalProductList.add(new Product("Ransom Shoulder Bag Red", "F. Pimentel Ave, Daet, 4600 Camarines Norte", "₱330", "Mark Villanueva", "BAGS", "FOR SALE", null, R.drawable.bag4));
            originalProductList.add(new Product("Mercury - butter knife", "348-362 Dulongbayan Ilaod, Daet, Camarines Norte", "₱85", "Trina Gomez", "UTENSILS", "FOR TRADE", null, R.drawable.utensil2));
            originalProductList.add(new Product("Women's Shoes Halsey", "4X65+4CF, Daet, Camarines Norte", "₱240", "Neil Soriano", "SHOES", "FOR SALE", null, R.drawable.shoes4));
            originalProductList.add(new Product("Silicone Soft Phone Case For iphone 15", "370 Dulongbayan Ilaod, Daet, Camarines Norte", "₱110", "Claire Abad", "GADGETS", "FOR SALE", null, R.drawable.gadget2));
            originalProductList.add(new Product("Hut Hat", "370 Dulongbayan Ilaod, Daet, Camarines Norte", "₱55", "Claire Abad", "HEADWEAR", "FOR TRADE", null, R.drawable.headwear1));

            originalProductList.add(new Product("Floral Print Shirred Dress", "F. Pimentel Avenue, Daet, Camarines Norte", "₱160", "Alexa Cruz", "CLOTHES", "FOR TRADE", null, R.drawable.clothes3));
            originalProductList.add(new Product("Khadim Blue School Bag", "Governor Panotes Avenue, Daet, Camarines Norte", "₱340", "Mark Villanueva", "BAGS", "FOR TRADE", null, R.drawable.bag5));
            originalProductList.add(new Product("Rectangular Chopping Board 9.75 6 x 0.75", "Maharlika Highway, Daet, Camarines Norte", "₱95", "Trina Gomez", "UTENSILS", "FOR SALE", null, R.drawable.utensil3));
            originalProductList.add(new Product("Womens Shoes Huxford White Low-Cut", "Vivencio Street, Daet, Camarines Norte", "₱260", "Neil Soriano", "SHOES", "FOR TRADE", null, R.drawable.shoes5));
            originalProductList.add(new Product("Folding Bluetooth Headphones with Noise Cancelling", "J. Lukban Extension, Daet, Camarines Norte", "₱130", "Claire Abad", "GADGETS", "FOR SALE", null, R.drawable.gadget3));
            originalProductList.add(new Product("Houston Cowboy Natural Straw Hat", "Taft Ilaod, Daet, Camarines Norte", "₱65", "Claire Abad", "HEADWEAR", "FOR TRADE", null, R.drawable.headwear2));

            originalProductList.add(new Product("Men's Fitness Short Shorts", "Zaldua Street, Daet, Camarines Norte", "₱155", "Alexa Cruz", "CLOTHES", "FOR SALE", null, R.drawable.clothes4));
            originalProductList.add(new Product("CoCopeaunts Embroidered Messenger Bag", "Dulongbayan Ilaod, Daet, Camarines Norte", "₱360", "Mark Villanueva", "BAGS", "FOR SALE", null, R.drawable.bag6));
            originalProductList.add(new Product("7 inch Cleaver Knife", "348-362 Dulongbayan Ilaod, Daet, Camarines Norte", "₱100", "Trina Gomez", "UTENSILS", "FOR TRADE", null, R.drawable.utensil4));
            originalProductList.add(new Product("Looney Tunes Men Rubber Slip", "Remedios Street, Daet, Camarines Norte", "₱265", "Neil Soriano", "SHOES", "FOR SALE", null, R.drawable.shoes6));
            originalProductList.add(new Product("NOKIA 3310 Mobile Phone 2G", "Urbano Street, Daet, Camarines Norte", "₱115", "Claire Abad", "GADGETS", "FOR TRADE", null, R.drawable.gadget4));
            originalProductList.add(new Product("Girl Floral Crown Rose Flower", "Cobangbang-San Isidro NIA Road, Daet, Camarines Norte", "₱60", "Claire Abad", "HEADWEAR", "FOR SALE", null, R.drawable.headwear3));
        }

    // Copy original list into filtered list
        filteredProductList.addAll(originalProductList);

    // Pass context to adapter
        adapter = new ProductAdapter(this, filteredProductList);
        recyclerView.setAdapter(adapter);

        // Find the Spinner
        Spinner tradeSpinner = findViewById(R.id.sale_trade_spinner);

        // Define the options for the Spinner
        List<String> tradeOptions = Arrays.asList("ALL", "FOR SALE", "FOR TRADE");

        // Create an ArrayAdapter using the custom layout for selected item
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, tradeOptions);

        // Set custom dropdown layout
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Set adapter to the Spinner
        tradeSpinner.setAdapter(spinnerAdapter);

        // Set item selection listener for the Spinner
        tradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = tradeOptions.get(position);

                // Filter logic based on trade selection
                if ("ALL".equals(selectedItem)) {
                    // Show all products
                    applyyFilter("ALL");
                } else if ("FOR SALE".equals(selectedItem)) {
                    filterByTrade("FOR SALE");
                } else if ("FOR TRADE".equals(selectedItem)) {
                    filterByTrade("FOR TRADE");
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Filter buttons setup
        setupFilterButtons();

        //ADD
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddActivity();
            }
        });

        //SIDE BAR//
        setupNavigation();

        //NAVIGATING BOTTOM NAV MENU
        setupBottomNavigation(R.id.nav_home);

        // Initialize the search components
        searchIcon = findViewById(R.id.searchIcon);
        searchBackButton = findViewById(R.id.searchBackButton);
        searchContainer = findViewById(R.id.searchContainer);
        appName = findViewById(R.id.appName);
        EditText searchInput = findViewById(R.id.searchInput);
        // Show search bar when search icon is clicked
        searchIcon.setOnClickListener(v -> {
            searchContainer.setVisibility(View.VISIBLE);
            searchIcon.setVisibility(View.GONE);
            appName.setVisibility(View.GONE);
        });

        // Hide search bar when back button is clicked
        searchBackButton.setOnClickListener(v -> {
            searchContainer.setVisibility(View.GONE);
            searchIcon.setVisibility(View.VISIBLE);
            appName.setVisibility(View.VISIBLE);
        });

        // Filter products as the text changes in the EditText
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterProductList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

        //------- METHODS ---------//
    private void openAddActivity() {
        Intent intent = new Intent(HomeActivity.this, AddActivity.class);
        startActivity(intent);
    }

    // Method to filter products based on search input
    private void filterProductList(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : originalProductList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }

        // Update the filtered list and notify the adapter
        filteredProductList.clear();
        filteredProductList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    // Method to filter products based on trade type
    private void filterByTrade(String tradeType) {
        filteredProductList.clear();

        // Add products based on trade type (either "FOR SALE" or "FOR TRADE")
        for (Product product : originalProductList) {
            if (product.getTradeType().equalsIgnoreCase(tradeType)) {
                filteredProductList.add(product);
            }
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }

    // Method to apply the filter to the product list based on the category or trade type
    private void applyyFilter(String filter) {
        filteredProductList.clear();

        if ("ALL".equals(filter)) {
            // If "ALL" is selected, show all products
            filteredProductList.addAll(originalProductList);
        } else {
            // Filter the products based on the selected category or trade type
            for (Product product : originalProductList) {
                if (product.getTradeType().equalsIgnoreCase(filter) || product.getCategory().equalsIgnoreCase(filter)) {
                    filteredProductList.add(product);
                }
            }
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : originalProductList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }

        // Update the filtered list and notify the adapter
        filteredProductList.clear();
        filteredProductList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void setupFilterButtons() {
        Button clothesButton = findViewById(R.id.clothes);
        Button bagsButton = findViewById(R.id.bags);
        Button utensilsButton = findViewById(R.id.utensils);
        Button shoesButton = findViewById(R.id.shoes);
        Button gadgetsButton = findViewById(R.id.gadgets);
        Button headwearButton = findViewById(R.id.headwear);
        Button allButton = findViewById(R.id.all);

        // Set the default active button (ALL) to be highlighted
        allButton.setBackgroundColor(getResources().getColor(R.color.active_color));
        allButton.setTextColor(getResources().getColor(R.color.whitey)); // Set text color to whitey

        allButton.setOnClickListener(v -> applyFilter("ALL", allButton));

        clothesButton.setOnClickListener(v -> applyFilter("CLOTHES", clothesButton));
        bagsButton.setOnClickListener(v -> applyFilter("BAGS", bagsButton));
        utensilsButton.setOnClickListener(v -> applyFilter("UTENSILS", utensilsButton));
        shoesButton.setOnClickListener(v -> applyFilter("SHOES", shoesButton));
        gadgetsButton.setOnClickListener(v -> applyFilter("GADGETS", gadgetsButton));
        headwearButton.setOnClickListener(v -> applyFilter("HEADWEAR", headwearButton));
    }

    // Method to apply the filter and update the button styles
    private void applyFilter(String category, Button clickedButton) {
        // Reset all button backgrounds and text colors to default
        resetButtonStyles();

        // Highlight the clicked button with the active color and set text color to whitey
        clickedButton.setBackgroundColor(getResources().getColor(R.color.active_color));
        clickedButton.setTextColor(getResources().getColor(R.color.whitey));

        // Clear the filtered list and add the filtered items based on the category
        filteredProductList.clear();

        if (category.equals("ALL")) {
            // If the "ALL" button is clicked, show all products
            filteredProductList.addAll(originalProductList);
        } else {
            // Filter the products based on the selected category
            for (Product product : originalProductList) {
                if (product.getCategory().equalsIgnoreCase(category)) {
                    filteredProductList.add(product);
                }
            }
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }

    // Helper method to reset all button backgrounds and text colors
    private void resetButtonStyles() {
        Button clothesButton = findViewById(R.id.clothes);
        Button bagsButton = findViewById(R.id.bags);
        Button utensilsButton = findViewById(R.id.utensils);
        Button shoesButton = findViewById(R.id.shoes);
        Button gadgetsButton = findViewById(R.id.gadgets);
        Button headwearButton = findViewById(R.id.headwear);
        Button allButton = findViewById(R.id.all);

        // Reset all buttons to the default background color and text color
        int defaultColor = getResources().getColor(R.color.default_button_color);
        int defaultTextColor = getResources().getColor(R.color.blackie); // Default text color for buttons

        clothesButton.setBackgroundColor(defaultColor);
        clothesButton.setTextColor(defaultTextColor);

        bagsButton.setBackgroundColor(defaultColor);
        bagsButton.setTextColor(defaultTextColor);

        utensilsButton.setBackgroundColor(defaultColor);
        utensilsButton.setTextColor(defaultTextColor);

        shoesButton.setBackgroundColor(defaultColor);
        shoesButton.setTextColor(defaultTextColor);

        gadgetsButton.setBackgroundColor(defaultColor);
        gadgetsButton.setTextColor(defaultTextColor);

        headwearButton.setBackgroundColor(defaultColor);
        headwearButton.setTextColor(defaultTextColor);

        allButton.setBackgroundColor(defaultColor);
        allButton.setTextColor(defaultTextColor);
    }
}