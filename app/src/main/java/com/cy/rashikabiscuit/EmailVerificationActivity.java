package com.cy.rashikabiscuit;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;


    public class EmailVerificationActivity extends AppCompatActivity {

        private EditText emailEditText;
        private Button resetButton, backBtn;

        String strEmail;

        AlertDialog.Builder builder;

        FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_email_verification);

            backBtn = findViewById(R.id.back_button);
            resetButton = findViewById(R.id.reset_pwd_button);
            emailEditText = findViewById(R.id.email);
            builder = new AlertDialog.Builder(this);

            mAuth = FirebaseAuth.getInstance();


            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     strEmail = emailEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(strEmail)){
                        ResetPassword();
                    }
                }
            });

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EmailVerificationActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        }

        private void ResetPassword (){
            mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    builder.setTitle("Link Sent !!")
                            .setMessage("Password reset link sent to the registered Email")
                            .setCancelable(true)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                    ;


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EmailVerificationActivity.this, "failed to sent email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }