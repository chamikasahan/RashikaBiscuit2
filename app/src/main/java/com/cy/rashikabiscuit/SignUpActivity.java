package com.cy.rashikabiscuit;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;






public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, emailEditText, shopNameEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox agreeCheckBox;
    private Button signUpButton, cancelButton;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

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
        signUpButton = findViewById(R.id.sign_up_btn);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find the TextView for log in
        TextView loginTextView = findViewById(R.id.login_txt);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start login activity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
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
                    Toast.makeText(SignUpActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with sign up
                    signUpUser(name, phone, email, shopName, address, password);
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

    // Method to sign up user with Firebase
    private void signUpUser(String name, String phone, String email, String shopName, String address, String password) {

        // Show ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Setting up your account...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                Map<String, Object> userDetails = new HashMap<>();
                                userDetails.put("name", name);
                                userDetails.put("phone", phone);
                                userDetails.put("shopName", shopName);
                                userDetails.put("address", address);
                                userDetails.put("email" , email );

                                db.collection("users").document(userId)
                                        .set(userDetails)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            // Dismiss the ProgressDialog
                                                            progressDialog.dismiss();

                                                            if (task.isSuccessful()) {

                                                                // Navigate to main activity and clear back stack
                                                                Intent intent = new Intent(SignUpActivity.this, SignUpSuccessActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                            } else {

                                                                // Dismiss the ProgressDialog
                                                                progressDialog.dismiss();
                                                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {

                                                    // Dismiss the ProgressDialog
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SignUpActivity.this, "Failed to store additional details", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {

                            // Dismiss the ProgressDialog
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();

    }
}


