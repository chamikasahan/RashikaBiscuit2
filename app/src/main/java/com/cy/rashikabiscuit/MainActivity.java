package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private BiscuitAdapter biscuitAdapter;
    private List<Biscuit> biscuitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        biscuitList = new ArrayList<>();
        biscuitAdapter = new BiscuitAdapter(this, biscuitList);
        recyclerView.setAdapter(biscuitAdapter);

        db = FirebaseFirestore.getInstance();
        loadBiscuitsFromFirestore();

        // Find snack button
        AppCompatButton snackBtn = findViewById(R.id.snacks_nav);

        // set onclick listener for snack nav button
        snackBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SnackActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // find other product button
        AppCompatButton otherBtn = findViewById(R.id.other_nav);

        // set onclick listener for other product btn
        otherBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OtherProductActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuid = menuItem.getItemId();

                if (menuid == R.id.navigation_home) {
                    // Do nothing, already in MainActivity
                    return true;
                } else if (menuid == R.id.navigation_payments) {
                    // Start OrderActivity
                    startActivity(new Intent(MainActivity.this, OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_profile) {
                    // Start ProfileActivity
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadBiscuitsFromFirestore() {
        CollectionReference biscuitsRef = db.collection("biscuit");
        biscuitsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "Error getting biscuits", error);
                return;
            }

            biscuitList.clear();
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
                int _350g = 0;
                int _500g = 0;

                // Retrieve price values if the map is not null
                if (priceMap != null) {
                    _100g = priceMap.containsKey("100g") ? ((Long) priceMap.get("100g")).intValue() : 0;
                    _350g = priceMap.containsKey("350g") ? ((Long) priceMap.get("350g")).intValue() : 0;
                    _500g = priceMap.containsKey("500g") ? ((Long) priceMap.get("500g")).intValue() : 0;
                }

                Biscuit.Price price = new Biscuit.Price(_100g, _350g, _500g);
                Biscuit biscuit = new Biscuit(imageUrl, name, price);
                biscuitList.add(biscuit);
            }
            biscuitAdapter.notifyDataSetChanged();
        });
    }

}