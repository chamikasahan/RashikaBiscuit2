package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PhoneVerificationActivity extends AppCompatActivity {

    private EditText phoneEditText, otpEditText;
    private Button confirmButton;

    // Constants for minimum phone number length and OTP length
    private static final int MIN_PHONE_NUMBER_LENGTH = 10;
    private static final int OTP_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        // Initialize UI elements
        phoneEditText = findViewById(R.id.phone);
        otpEditText = findViewById(R.id.get_otp);
        confirmButton = findViewById(R.id.phone_confirm_btn);

        // Set OnClickListener on the Confirm Button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email and OTP entered by the user
                String phone = phoneEditText.getText().toString().trim();
                String otp = otpEditText.getText().toString().trim();

                // Validate phone number format and OTP input
                if (isValidPhoneNumber(phone) && isValidOTP(otp)) {
                    // If validation is successful, start the ResetPwdActivity
                    startActivity(new Intent(PhoneVerificationActivity.this, ResetPwdActivity.class));
                } else {
                    // Show error message if validation fails
                    Toast.makeText(PhoneVerificationActivity.this, "Please enter valid phone number and OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

// Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuid = menuItem.getItemId();

                if (menuid == R.id.navigation_home) {
                    startActivity(new Intent(PhoneVerificationActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_payments) {
                    startActivity(new Intent(PhoneVerificationActivity.this, OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_profile) {
                    startActivity(new Intent(PhoneVerificationActivity.this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Implement phone number validation logic
        // You can use a regex pattern or other validation methods
        // For simplicity, let's check if it's not empty and has the required length
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.length() >= MIN_PHONE_NUMBER_LENGTH;
    }

    // Method to validate OTP format
    private boolean isValidOTP(String otp) {
        // Implement OTP validation logic
        // You can check if it's not empty and has the required length
        // For example, if the OTP should be 6 digits:
        return otp.length() == OTP_LENGTH;
    }
}
