package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class OrderActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Find the confirm button
        AppCompatButton confirmPaymentButton = findViewById(R.id.confirm_pbtn);

        // Set OnClickListener on the confirm button
        confirmPaymentButton.setOnClickListener(v -> {
            // Start CardPaymentActivity
            startActivity(new Intent(OrderActivity.this, CardPaymentActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int menuid = menuItem.getItemId();

            if (menuid == R.id.navigation_home) {
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                return true;
            } else if (menuid == R.id.navigation_payments) {
                // No need to start the same activity again, already in OrderActivity
                return true;
            } else if (menuid == R.id.navigation_profile) {
                startActivity(new Intent(OrderActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Highlight the payments menu item
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_payments);
        }
    }
}