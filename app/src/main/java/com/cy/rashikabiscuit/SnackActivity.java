package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class SnackActivity extends AppCompatActivity {
    private static final String TAG = "SnackActivity";

    private RecyclerView recyclerView;
    private List<Snack> snackList;
    private SnackAdapter snackAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        snackList = new ArrayList<>();
        snackAdapter = new SnackAdapter(this, snackList);
        recyclerView.setAdapter(snackAdapter);

        db = FirebaseFirestore.getInstance();
        loadSnackFromFirestore();

        // Find biscuit button
        AppCompatButton biscuitBtn = findViewById(R.id.biscuits_nav);

        // set onclick listener for biscuit nav button
        biscuitBtn.setOnClickListener(v -> {
            startActivity(new Intent(SnackActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // find other product button
        AppCompatButton otherBtn = findViewById(R.id.other_nav);

        // set onclick listener for other product btn
        otherBtn.setOnClickListener(v -> {
            startActivity(new Intent(SnackActivity.this, OtherProductActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuid = menuItem.getItemId();

                if (menuid == R.id.navigation_home) {
                    // Do nothing, already in SnackActivity
                    return true;
                } else if (menuid == R.id.navigation_payments) {
                    // Start OrderActivity
                    startActivity(new Intent(SnackActivity.this, OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_profile) {
                    // Start ProfileActivity
                    startActivity(new Intent(SnackActivity.this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadSnackFromFirestore() {
        CollectionReference snacksRef = db.collection("snack");
        snacksRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "Error getting snacks", error);
                return;
            }

            snackList.clear();
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
                int _6_packs = 0;
                int _12_packs = 0;
                int _24_packs = 0;

                // Retrieve price values if the map is not null
                if (priceMap != null) {
                    _6_packs = priceMap.containsKey("6_packs") ? ((Long) priceMap.get("6_packs")).intValue() : 0;
                    _12_packs = priceMap.containsKey("12_packs") ? ((Long) priceMap.get("12_packs")).intValue() : 0;
                    _24_packs = priceMap.containsKey("24_packs") ? ((Long) priceMap.get("24_packs")).intValue() : 0;
                }

                Snack.Price price = new Snack.Price(_6_packs, _12_packs, _24_packs);
                Snack snack = new Snack(imageUrl, name, price);
                snackList.add(snack);
            }
            snackAdapter.notifyDataSetChanged();
        });
    }
}