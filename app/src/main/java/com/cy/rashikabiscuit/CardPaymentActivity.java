package com.cy.rashikabiscuit;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardPaymentActivity extends AppCompatActivity {

    Button cancelButton;

    private EditText cardPaymentEmailEditText, cardNumberEditText, dateYearEditText, cvsNumberEditText, zipCodeEditText;
    private Spinner spinnerCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);


        // initialize UI elements
        cardPaymentEmailEditText = findViewById(R.id.card_payment_email);
        cardNumberEditText = findViewById(R.id.card_number);
        dateYearEditText = findViewById(R.id.date_year);
        cvsNumberEditText = findViewById(R.id.cvc_number);
        spinnerCountry = findViewById(R.id.spinner_country);
        AppCompatButton confirmPaymentBtn = findViewById(R.id.confirm_payment);
        zipCodeEditText = findViewById(R.id.zip_code);

        confirmPaymentBtn.setOnClickListener(v -> {
            String email = cardPaymentEmailEditText.getText().toString().trim();
            String number = cardNumberEditText.getText().toString().trim();
            String date = dateYearEditText.getText().toString().trim();
            String cvNumber = cvsNumberEditText.getText().toString().trim();
            String country = spinnerCountry.toString().trim();
            String zipCode = zipCodeEditText.getText().toString().trim();

            // validate payment
            String validationMessage = isValidPaymentInput(email, number, date, cvNumber, country, zipCode);
            if (validationMessage != null) {
                Toast.makeText(CardPaymentActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
            } else {

                View alertCustomDialog = LayoutInflater.from(CardPaymentActivity.this).inflate(R.layout.activity_payment_success, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CardPaymentActivity.this);

                alertDialog.setView(alertCustomDialog);
                cancelButton = (Button) alertCustomDialog.findViewById(R.id.back_to_home_btn);

                final AlertDialog dialog = alertDialog.create();
                findViewById(R.id.confirm_payment).setOnClickListener(v1 -> {
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                });

                cancelButton.setOnClickListener(v12 -> {
                    startActivity(new Intent(CardPaymentActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });
            }
        });

        // Find the Spinner
        Spinner spinnerCountry = findViewById(R.id.spinner_country);

        // Create a list of countries
        ArrayAdapter<String> adapter = getStringArrayAdapter();

        // Apply the adapter to the spinner
        spinnerCountry.setAdapter(adapter);

        // Set up a listener to capture the selected country
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected country
                String selectedCountry = parentView.getItemAtPosition(position).toString();

                // Perform any necessary actions based on the selected country
                Log.d("SelectedCountry", "Selected country: " + selectedCountry);
                // For example, you can log the selected country to see it in the logcat
                // You can replace this with any action you want to perform with the selected country
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

// Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int menuid = menuItem.getItemId();

            if (menuid == R.id.navigation_home) {
                startActivity(new Intent(CardPaymentActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (menuid == R.id.navigation_payments) {
                startActivity(new Intent(CardPaymentActivity.this, OrderActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (menuid == R.id.navigation_profile) {
                startActivity(new Intent(CardPaymentActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });


    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        List<String> countries = new ArrayList<>();
        countries.add("Country 1");
        countries.add("Country 2");
        // Add more countries as needed

        // Create an ArrayAdapter using the country list and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private String isValidPaymentInput(String email, String number, String date, String cvNumber, String country, String zipCode) {

        if (email.isEmpty() || number.isEmpty() || date.isEmpty() || cvNumber.isEmpty() || country.isEmpty() || zipCode.isEmpty()) {
            return "Please Fill In All fields";

        }
        if (!isValidEmail((email))) {
            return "Invalid Email Address";
        }
        return null;
    }

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