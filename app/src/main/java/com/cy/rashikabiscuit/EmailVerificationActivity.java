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

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText emailEditText, otpEditText;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        // Initialize UI elements
        emailEditText = findViewById(R.id.email);
        otpEditText = findViewById(R.id.get_otp);
        confirmButton = findViewById(R.id.email_confirm_btn);

        // Set OnClickListener on the Confirm Button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email and OTP entered by the user
                String email = emailEditText.getText().toString().trim();
                String otp = otpEditText.getText().toString().trim();

                // Validate email format and OTP input
                if (isValidEmail(email) && !TextUtils.isEmpty(otp)) {
                    // If validation is successful, start the ResetPwdActivity
                    startActivity(new Intent(EmailVerificationActivity.this, ResetPwdActivity.class));
                } else {
                    // Show error message if validation fails
                    Toast.makeText(EmailVerificationActivity.this, "Please enter valid email and OTP", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(EmailVerificationActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_payments) {
                    startActivity(new Intent(EmailVerificationActivity.this, OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (menuid == R.id.navigation_profile) {
                    startActivity(new Intent(EmailVerificationActivity.this, ProfileActivity.class));
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
    // Method to validate email format
    private boolean isValidEmail(String email) {
        // Implement your email validation logic
        // For simplicity, this example uses a basic regex pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return email.matches(emailPattern);
    }
}
