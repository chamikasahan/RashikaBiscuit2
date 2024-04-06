package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cy.rashikabiscuit.OrderActivity;
import com.cy.rashikabiscuit.ProfileActivity;
import com.cy.rashikabiscuit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private int quantity = 1; // Initial quantity
    private TextView quantityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find snack button

        AppCompatButton snackBtn = findViewById(R.id.snacks_nav);

        // set onclick listener for snack nav button
        snackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SnackActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // find other product button
        AppCompatButton otherBtn = findViewById(R.id.other_nav);

        // set onclick listener for other product btn
        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OtherProductActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        // Find the quantity TextView
        quantityTextView = findViewById(R.id.quantity_Text);

        // Find the increment button
        ImageView incrementButton = findViewById(R.id.plus_button);
        // Set OnClickListener on the increment button
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        // Find the decrement button
        ImageView decrementButton = findViewById(R.id.minus_button);
        // Set OnClickListener on the decrement button
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

        // Find the biscuit1_btn button
        Button addToCartButton = findViewById(R.id.add_to_cart_btn);
        // Set OnClickListener on the biscuit1_btn button
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start OrderActivity with quantity
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra("quantity", quantity);
                startActivity(intent);
            }
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

    // Method to increment quantity
    private void incrementQuantity() {
        quantity++;
        displayQuantity();
    }

    // Method to decrement quantity
    private void decrementQuantity() {
        if (quantity > 1) {
            quantity--;
            displayQuantity();
        } else {
            Toast.makeText(MainActivity.this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to display quantity
    private void displayQuantity() {
        quantityTextView.setText(String.valueOf(quantity));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
