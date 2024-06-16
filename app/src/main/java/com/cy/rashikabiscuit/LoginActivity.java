package com.cy.rashikabiscuit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView; // Add this import statement

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;


    private FirebaseAuth fAuth;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_REMEMBER_ME = "rememberMe";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        loginButton = findViewById(R.id.loginbtn);

        fAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)) {
            navigateToMainActivity();
            return;
        }

        // Set OnClickListener on the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform Firebase Authentication
                signInUser(username, password);
            }
        });


        AppCompatButton testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChangeEmailToNewActivity.class));
            }
        });



        // Find the TextView for "Forgot Password"
        TextView forgotPasswordTextView = findViewById(R.id.forgot_pwd);

        // Set OnClickListener on the TextView
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ForgetPasswordActivity
                startActivity(new Intent(LoginActivity.this, EmailVerificationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Find the TextView for sign up
        TextView signUpTextView = findViewById(R.id.singbtn);

        // Set OnClickListener on the TextView
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignUpActivity
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    // Method to authenticate user with Firebase
    private void signInUser(String username, String password) {


        // Show ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.setCancelable(false);
        progressDialog.show();



        fAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {

                            // Dismiss the ProgressDialog
                            progressDialog.dismiss();

                            // Save login state if "Remember Me" is checked
                            if (Objects.requireNonNull(fAuth.getCurrentUser()).isEmailVerified() && rememberMeCheckBox.isChecked()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(KEY_REMEMBER_ME, true);
                                editor.putString(KEY_EMAIL, username); // Optional: Save the email
                                editor.apply();

                                navigateToMainActivity();
                            } else if (fAuth.getCurrentUser().isEmailVerified()) {

                                // Clear SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear(); // Clear all stored data
                                editor.apply();
                                navigateToMainActivity();

                            } else {

                               startActivity(new Intent(LoginActivity.this, EmailVerifiedFailedActivity.class));
                            }
                        } else {
                            // Dismiss the ProgressDialog
                            progressDialog.dismiss();
                            // Sign in failed, display error message
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish(); // Optional: Close the LoginActivity to prevent going back
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}


