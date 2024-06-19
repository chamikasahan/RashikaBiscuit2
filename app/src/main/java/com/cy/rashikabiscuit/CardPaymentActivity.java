package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class CardPaymentActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone;
    private Button buttonPay;

    private static final int PAYHERE_REQUEST = 11010;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonPay = findViewById(R.id.buttonPay);

        buttonPay.setOnClickListener(v -> initiatePayment());
    }

    private void initiatePayment() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        InitRequest req = new InitRequest();
        req.setMerchantId("YOUR_MERCHANT_ID"); // Replace with your Merchant ID
        req.setMerchantSecret("YOUR_MERCHANT_SECRET"); // Replace with your Merchant Secret
        req.setCurrency("LKR");
        req.setAmount(1000.0); // Replace with your amount
        req.setOrderId("12345"); // Unique Order ID
        req.setItemsDescription("Test Payment");
        req.setCustom1("Custom 1");
        req.setCustom2("Custom 2");

        // Customer Details
        req.getCustomer().setFirstName(name);
        req.getCustomer().setLastName(" ");
        req.getCustomer().setEmail(email);
        req.getCustomer().setPhone(phone);

        // Address Details
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        // Start PayHere activity
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL); // For sandbox mode
        Intent intent = new Intent(CardPaymentActivity.this, PHMainActivity.class);
        intent.putExtra(PHMainActivity.INTENT_EXTRA_DATA, req);
        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHMainActivity.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = data.getParcelableExtra(PHMainActivity.INTENT_EXTRA_RESULT);
            if (resultCode == PHMainActivity.RESULT_OK) {
                if (response != null) {
                    StatusResponse statusResponse = response.getData();
                    Toast.makeText(this, "Payment Success: " + statusResponse.toString(), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == PHMainActivity.RESULT_CANCELED) {
                if (response != null) {
                    String reason = response.toString();
                    Toast.makeText(this, "Payment Canceled: " + reason, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == PHMainActivity.RESULT_ERROR) {
                if (response != null) {
                    String error = response.toString();
                    Toast.makeText(this, "Payment Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}