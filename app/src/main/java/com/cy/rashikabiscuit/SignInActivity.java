package com.cy.rashikabiscuit;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class SignInActivity extends AppCompatActivity {

    Button cancelButton;

    private EditText nameEditText, phoneEditText, emailEditText, shopNameEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox agreeCheckBox;
    private Button signUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        emailEditText = findViewById(R.id.email_address);
        shopNameEditText = findViewById(R.id.shop_name);
        addressEditText = findViewById(R.id.address);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_pwd);
        agreeCheckBox = findViewById(R.id.agree_term);
        signUpButton = findViewById(R.id.sign_in_btn);


        // Find the TextView for already have account
        TextView loginTextView = findViewById(R.id.login_txt);

        // Set OnClickListener on the TextView
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        // Set OnClickListener on the sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String shopName = shopNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                boolean agreeTerms = agreeCheckBox.isChecked();

                // Validate input
                String validationMessage = isValidSignUpInput(name, phone, email, shopName, address, password, confirmPassword, agreeTerms);
                if (validationMessage != null) {
                    // Show error message to the user
                    Toast.makeText(SignInActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with sign up
                    View alertCustomDialog = LayoutInflater.from(SignInActivity.this).inflate(R.layout.activity_sign_up_success, null);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);

                    alertDialog.setView(alertCustomDialog);
                    cancelButton = (Button) alertCustomDialog.findViewById(R.id.back_to_home_btn);

                    final AlertDialog dialog = alertDialog.create();
                    findViewById(R.id.sign_in_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                }
            }
        });
    }

    // Method to validate sign-up input and return validation message
    private String isValidSignUpInput(String name, String phone, String email, String shopName, String address, String password, String confirmPassword, boolean agreeTerms) {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || shopName.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Please fill in all fields";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        if (!isValidEmail(email)) {
            return "Invalid email address";
        }
        if (!agreeTerms) {
            return "Please agree to terms and conditions";
        }
        return null; // All inputs are valid
    }

    // Method to validate email address format
    private boolean isValidEmail(String email) {
        // Implement your email validation logic
        // For simplicity, this example uses a basic regex pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return email.matches(emailPattern);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
