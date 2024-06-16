package com.cy.rashikabiscuit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView shopNameTextView;
    private TextView shopAddressTextView, emailTextView;
    private Button editProfileButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.user_profile_image);
        nameTextView = findViewById(R.id.name_input);
        phoneTextView = findViewById(R.id.phone_input);
        shopNameTextView = findViewById(R.id.shop_name_input);
        shopAddressTextView = findViewById(R.id.shop_adrs_input);
        editProfileButton = findViewById(R.id.edit_profile_btn);
        emailTextView = findViewById(R.id.email_input);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        loadUserProfile();
    }

    private void loadUserProfile() {

        // Show ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading Profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String email = document.getString("email");
                                    String name = document.getString("name");
                                    String phone = document.getString("phone");
                                    String shopName = document.getString("shopName");
                                    String shopAddress = document.getString("address");
                                    String profileImageUrl = document.getString("profileImageUrl");


                                    emailTextView.setText(email);
                                    nameTextView.setText(name);
                                    phoneTextView.setText(phone);
                                    shopNameTextView.setText(shopName);
                                    shopAddressTextView.setText(shopAddress);



                                    // Load profile image using Glide
                                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                        Glide.with(ProfileActivity.this)
                                                .load(profileImageUrl)
                                                .placeholder(R.drawable.profile) // Placeholder image
                                                .error(R.drawable.profile) // Error image
                                                .into(profileImageView);
                                    } else {
                                        // Handle case where no profile image is available
                                        profileImageView.setImageResource(R.drawable.profile);
                                    }

                                } else {
                                    Toast.makeText(ProfileActivity.this, "User data does not exist.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                                Log.e("ProfileActivity", "Error getting documents: ", task.getException());
                            }
                            // Dismiss the progress dialog when data loading is complete
                            progressDialog.dismiss();
                        }
                    });

            // bottom navigation menu code starts
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            // Set listener for item selection
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int menuid = menuItem.getItemId();

                    if (menuid == R.id.navigation_home) {
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    } else if (menuid == R.id.navigation_payments) {
                        // Start OrderActivity
                        startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    } else if (menuid == R.id.navigation_profile) {
                        return true;
                    }
                    return false;
                }
            });

            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        }
    }
}
