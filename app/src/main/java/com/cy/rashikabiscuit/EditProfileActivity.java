package com.cy.rashikabiscuit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView userImageView;
    private EditText nameEditText, phoneEditText, shopNameEditText, shopAddressEditText;
    private Button uploadImageButton;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userImageView = findViewById(R.id.user_pro_image);
        nameEditText = findViewById(R.id.edit_profile_name);
        phoneEditText = findViewById(R.id.edit_profile_phone);
        shopNameEditText = findViewById(R.id.edit_profile_shop_name);
        shopAddressEditText = findViewById(R.id.edit_profile_address);
        uploadImageButton = findViewById(R.id.add_photo_btn);
        Button saveButton = findViewById(R.id.save_btn);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        loadUserProfile();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Highlight the profile menu item

        AppCompatButton changeEmail = findViewById(R.id.change_email_btn);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ChangeEmailToNewActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        AppCompatButton passwordResetBtn = findViewById(R.id.reset_pwd_btn);
        passwordResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EmailVerificationActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                userImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            String userId = mAuth.getCurrentUser().getUid();
            StorageReference ref = storageReference.child("images/" + userId + ".jpeg");

            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveProfileWithImage(imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadUserProfile() {

        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Updating Profile...");
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
                                progressDialog.dismiss();
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String name = document.getString("name");
                                    String phone = document.getString("phone");
                                    String shopName = document.getString("shopName");
                                    String shopAddress = document.getString("address");
                                    String profileImageUrl = document.getString("profileImageUrl");

                                    nameEditText.setText(name);
                                    phoneEditText.setText(phone);
                                    shopNameEditText.setText(shopName);
                                    shopAddressEditText.setText(shopAddress);

                                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                        Glide.with(EditProfileActivity.this).load(profileImageUrl).into(userImageView);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "User data does not exist.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                                Log.e("EditProfileActivity", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void saveProfile() {
        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Updating Profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String shopName = shopNameEditText.getText().toString().trim();
        String shopAddress = shopAddressEditText.getText().toString().trim();

        Map<String, Object> updates = new HashMap<>();
        if (!TextUtils.isEmpty(name)) {
            updates.put("name", name);
        }
        if (!TextUtils.isEmpty(phone)) {
            updates.put("phone", phone);
        }
        if (!TextUtils.isEmpty(shopName)) {
            updates.put("shopName", shopName);
        }
        if (!TextUtils.isEmpty(shopAddress)) {
            updates.put("address", shopAddress);
        }

        if (updates.isEmpty() && imageUri == null) {
            progressDialog.dismiss();
            Toast.makeText(EditProfileActivity.this, "No changes to update.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.dismiss();
            if (imageUri != null) {
                uploadImage();
            } else {
                saveProfileWithImage(null);
            }
        }
    }

    private void saveProfileWithImage(String imageUrl) {
        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Updating Profile...");
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
                                    // Fetch existing data
                                    String existingEmail = document.getString("email");

                                    Map<String, Object> userProfile = new HashMap<>();
                                    userProfile.put("name", nameEditText.getText().toString().trim());
                                    userProfile.put("phone", phoneEditText.getText().toString().trim());
                                    userProfile.put("shopName", shopNameEditText.getText().toString().trim());
                                    userProfile.put("address", shopAddressEditText.getText().toString().trim());
                                    userProfile.put("email", existingEmail); // Include the existing email

                                    if (imageUrl != null) {
                                        userProfile.put("profileImageUrl", imageUrl);
                                    }

                                    db.collection("users").document(userId).set(userProfile)
                                            .addOnSuccessListener(aVoid -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("EditProfileActivity", "Error updating profile", e);
                                            });
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Failed to load existing user data.", Toast.LENGTH_SHORT).show();
                                Log.e("EditProfileActivity", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}