package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private LinearLayout cartItemsLayout;
    private TextView cartItemsTextView;

    private TextView totalAmountTextView;
    private int totalAmount = 0;
    private RadioGroup paymentMethodGroup;
    private Button confirmButton;

    private ImageView order_histry_btn;

    ImageView deleteIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        // Initialize total_amount TextView
        totalAmountTextView = findViewById(R.id.total_amount);

        cartItemsLayout = findViewById(R.id.cartItemsLayout); // Make sure this matches your layout id
        cartItemsTextView = findViewById(R.id.add_to_cart_item);
        paymentMethodGroup = findViewById(R.id.pyment_grp);
        confirmButton = findViewById(R.id.confirm_btn);
        deleteIcon = findViewById(R.id.delete_icon);

        displayCartItems();


        confirmButton.setOnClickListener(v -> confirmOrder());
        deleteIcon.setOnClickListener(v -> clearCartItems());

        order_histry_btn = findViewById(R.id.history_btn);

        order_histry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderActivity.this, OrderHistryActivity.class));
            }
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

    private void confirmOrder() {
        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedPaymentMethodId == R.id.rdio_btn_cod) {
            // Save order to Firestore
            saveOrderToFirestore();

            // Show alert dialog
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Your Order is Placed")
                    .setPositiveButton("OK", null)
                    .show();
        } else if (selectedPaymentMethodId == R.id.rdio_btn_crd) {
            // Show alert dialog
            new AlertDialog.Builder(this)
                    .setTitle("Payment Method")
                    .setMessage("Card payment method coming soon")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void saveOrderToFirestore() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        // Create a map to store the order data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("userId", userId);
        orderData.put("timestamp", FieldValue.serverTimestamp());
        orderData.put("status", "pending");

        // Create a list to store the items
        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (CartItem item : cartItems) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("name", item.getName());
            itemData.put("quantity", item.getQuantity());
            itemData.put("price", item.getPrice());
            itemsList.add(itemData);
        }
        orderData.put("items", itemsList);

        // Save the order to the "orders" collection
        db.collection("orders").document(userId).collection("userOrders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    private void clearCartItems() {
        CartManager.getInstance().clearCart();
        displayCartItems();
        Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
    }

}