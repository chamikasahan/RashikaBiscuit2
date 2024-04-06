package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView; // Add this import statement
import androidx.appcompat.app.AppCompatActivity;

import com.cy.rashikabiscuit.MainActivity;
import com.cy.rashikabiscuit.SignInActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        loginButton = findViewById(R.id.loginbtn);

        // Find the TextView for "Forgot Password"
        TextView forgotPasswordTextView = findViewById(R.id.forgot_pwd);

        // Set OnClickListener on the TextView
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ForgetPasswordActivity
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
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
                startActivity(new Intent(LoginActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform validation
                if (isValidCredentials(username, password)) {
                    // Credentials are valid, navigate to the main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                    finish(); // Optional: Close the LoginActivity to prevent going back
                } else {
                    // Credentials are invalid, show error message
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Method to validate credentials
    private boolean isValidCredentials(String username, String password) {
        // In this example, let's assume a simple validation
        // You can replace this with your actual validation logic (e.g., checking against a database)
        return username.equals("codex") && password.equals("2024");
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
