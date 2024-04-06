package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetPassword extends AppCompatActivity {

    private EditText usernameEditText;
    private Button nextButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        nextButton = findViewById(R.id.next_btn);
        backButton = findViewById(R.id.back_btn);

        // Set OnClickListener on the "Next" button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email address entered by the user
                String emailAddress = usernameEditText.getText().toString().trim();

                // Check if the email address is empty
                if (TextUtils.isEmpty(emailAddress)) {
                    // Show error message if the email address is empty
                    Toast.makeText(ForgetPassword.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(emailAddress)) {
                    // Show error message if the email address is not valid
                    Toast.makeText(ForgetPassword.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    // Start ForgetPwd2Activity if the email address is valid
                    startActivity(new Intent(ForgetPassword.this, ForgetPwd2Activity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        // Set OnClickListener on the "Back" button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to MainActivity
                startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish(); // Optional: Close ForgetPasswordActivity to prevent going back
            }
        });
    }

    // Method to validate email address
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
