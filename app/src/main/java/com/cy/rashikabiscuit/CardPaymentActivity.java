package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class CardPaymentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int menuid = menuItem.getItemId();

            if (menuid == R.id.navigation_home) {
                startActivity(new Intent(CardPaymentActivity.this, MainActivity.class));
                return true;
            } else if (menuid == R.id.navigation_payments) {
                // No need to start the same activity again, already in OrderActivity
                return true;
            } else if (menuid == R.id.navigation_profile) {
                startActivity(new Intent(CardPaymentActivity.this, ProfileActivity.class));
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