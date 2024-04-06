package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class OtherProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_product);

        // Find biscuit button
        AppCompatButton biscuitBtn = findViewById(R.id.biscuits_nav);

// Find snack button
        AppCompatButton snackBtn = findViewById(R.id.snacks_nav);

// Set OnClickListener using lambda expression for biscuit button
        biscuitBtn.setOnClickListener(v -> startActivity(new Intent(OtherProductActivity.this, MainActivity.class)));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

// Set OnClickListener using lambda expression for snack button
        snackBtn.setOnClickListener(v -> startActivity(new Intent(OtherProductActivity.this, SnackActivity.class)));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


// nav button
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

    // transition

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}