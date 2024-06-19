package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private LinearLayout cartItemsLayout;
    private TextView cartItemsTextView;

    private TextView totalAmountTextView;
    private int totalAmount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        // Initialize total_amount TextView
        totalAmountTextView = findViewById(R.id.total_amount);

        cartItemsLayout = findViewById(R.id.cartItemsLayout); // Make sure this matches your layout id
        cartItemsTextView = findViewById(R.id.add_to_cart_item);

        displayCartItems();

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
    private void displayCartItems() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        StringBuilder cartDetails = new StringBuilder();

        for (CartItem item : cartItems) {
            String itemDetails = "Product: " + item.getName() + "\nQuantity: " + item.getQuantity() + "\nPrice: Rs " + item.getPrice() + "/-\n\n";
            cartDetails.append(itemDetails);

            // Add current item's price to total amount
            totalAmount += item.getPrice();
        }

        cartItemsTextView.setText(cartDetails.toString());

        // Set total amount to totalAmountTextView
        totalAmountTextView.setText("Total Amount: Rs " + totalAmount + "/-");

    }
}