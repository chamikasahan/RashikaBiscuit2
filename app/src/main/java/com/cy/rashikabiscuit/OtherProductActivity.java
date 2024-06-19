package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OtherProductActivity extends AppCompatActivity {

    private static final String TAG = "OtherProductActivity";

    private RecyclerView recyclerView;
    private List<Other> otherProductList;
    private OtherAdapter otherAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_product);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        otherProductList = new ArrayList<>();
        otherAdapter = new OtherAdapter(this, otherProductList);
        recyclerView.setAdapter(otherAdapter);

        db = FirebaseFirestore.getInstance();
        loadOtherFromFirestore();

        // Find biscuit button
        AppCompatButton biscuitBtn = findViewById(R.id.biscuits_nav);

        // set onclick listener for biscuit nav button
        biscuitBtn.setOnClickListener(v -> {
            startActivity(new Intent(OtherProductActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // find snack button
        AppCompatButton snackBtn = findViewById(R.id.snacks_nav);

        // set onclick listener for snack btn
        snackBtn.setOnClickListener(v -> {
            startActivity(new Intent(OtherProductActivity.this, SnackActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuid = menuItem.getItemId();

                if (menuid == R.id.navigation_home) {
                    // Start MainActivity
                    startActivity(new Intent(OtherProductActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_payments) {
                    // Start OrderActivity
                    startActivity(new Intent(OtherProductActivity.this, OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_profile) {
                    // Start ProfileActivity
                    startActivity(new Intent(OtherProductActivity.this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadOtherFromFirestore() {
        CollectionReference otherProductsRef = db.collection("other");
        otherProductsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "Error getting other products", error);
                return;
            }

            otherProductList.clear();
            for (QueryDocumentSnapshot document : value) {
                String imageUrl = document.getString("imageUrl");
                String name = document.getString("name");

                // Handle nullable Map
                Object priceObj = document.get("price");
                Map<String, Object> priceMap = null;
                if (priceObj instanceof Map) {
                    priceMap = (Map<String, Object>) priceObj;
                }

                // Initialize price values
                int _100g = 0;
                int _250g = 0;
                int _1kg = 0;

                // Retrieve price values if the map is not null
                if (priceMap != null) {
                    _100g = priceMap.containsKey("100g") ? ((Long) priceMap.get("100g")).intValue() : 0;
                    _250g = priceMap.containsKey("250g") ? ((Long) priceMap.get("250g")).intValue() : 0;
                    _1kg = priceMap.containsKey("1kg") ? ((Long) priceMap.get("1kg")).intValue() : 0;
                }

                Other.Price price = new Other.Price(_100g, _250g, _1kg);
                Other otherProduct = new Other(imageUrl, name, price);
                otherProductList.add(otherProduct);
            }
            otherAdapter.notifyDataSetChanged();
        });
    }
}