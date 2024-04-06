package com.cy.rashikabiscuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPwd2Activity extends AppCompatActivity {

    private RadioButton radioButtonPhone;
    private RadioButton radioButtonEmail;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd2);

        // Find the radio buttons and the next button
        radioButtonPhone = findViewById(R.id.radioButtonPhone);
        radioButtonEmail = findViewById(R.id.radioButtonEmail);
        nextButton = findViewById(R.id.nxt_button);

        // Set OnClickListener on the next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check which radio button is selected
                if (radioButtonPhone.isChecked()) {
                    // Start PhoneVerificationActivity
                    startActivity(new Intent(ForgetPwd2Activity.this, PhoneVerificationActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (radioButtonEmail.isChecked()) {
                    // Start EmailVerificationActivity
                    startActivity(new Intent(ForgetPwd2Activity.this, EmailVerificationActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
