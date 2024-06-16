package com.cy.rashikabiscuit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.AuthCredential;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailToNewActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextInputEditText currentEmailEditText, newEmailEditText, confirmNewEmailEditText, passwordEditText;
    private Button resetEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email_to_new);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        currentEmailEditText = findViewById(R.id.current_email);
        newEmailEditText = findViewById(R.id.new_email);
        confirmNewEmailEditText = findViewById(R.id.confirm_new_email);
        passwordEditText = findViewById(R.id.password);
        resetEmailButton = findViewById(R.id.reset_email_button);

        if (user != null) {
            currentEmailEditText.setText(user.getEmail());
        }

        resetEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserEmail();
            }
        });
    }

    private void changeUserEmail() {
        String currentEmail = currentEmailEditText.getText().toString().trim();
        String newEmail = newEmailEditText.getText().toString().trim();
        String confirmNewEmail = confirmNewEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(confirmNewEmail) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newEmail.equals(confirmNewEmail)) {
            Toast.makeText(this, "New email addresses do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user != null && user.isEmailVerified()) {

            // Show ProgressDialog
            ProgressDialog progressDialog = new ProgressDialog(ChangeEmailToNewActivity.this);
            progressDialog.setMessage("Changing" +
                    "" +
                    " Email...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {



                            if (task.isSuccessful()) {
                                user.verifyBeforeUpdateEmail(newEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // Dismiss the ProgressDialog
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    showVerificationEmailSentDialog(newEmail);
                                                } else {
                                                    Toast.makeText(ChangeEmailToNewActivity.this, "Failed to send verification email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(ChangeEmailToNewActivity.this, "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please verify your current email address before updating.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showVerificationEmailSentDialog(String newEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Link Sent !!")
                .setMessage("Verification email sent to " + newEmail + ". Please sign out and sign in again using the new email.")
                .setCancelable(true)
                .setNegativeButton("Sign Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        dialogInterface.cancel();
                        // Redirect to login activity or close the current activity
                        Intent intent = new Intent(ChangeEmailToNewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}