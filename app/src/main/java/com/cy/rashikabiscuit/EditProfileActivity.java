package com.cy.rashikabiscuit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    AppCompatButton cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        AppCompatButton confirmButton = findViewById(R.id.confirm_btn);

        confirmButton.setOnClickListener(v -> {

            View alertCustomDialog = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.activity_edit_profile_success, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);

            alertDialog.setView(alertCustomDialog);
            cancelButton = (AppCompatButton) alertCustomDialog.findViewById(R.id.back_to_home_btn);

            final AlertDialog dialog = alertDialog.create();
            findViewById(R.id.confirm_btn).setOnClickListener(v1 -> {
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            });

            cancelButton.setOnClickListener(v12 -> {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });


        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

// Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int menuid = menuItem.getItemId();

            if (menuid == R.id.navigation_home) {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (menuid == R.id.navigation_payments) {
                startActivity(new Intent(EditProfileActivity.this, OrderActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (menuid == R.id.navigation_profile) {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}