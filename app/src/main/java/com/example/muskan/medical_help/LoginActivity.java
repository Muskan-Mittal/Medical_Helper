package com.example.muskan.medical_help;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.muskan.medical_help.Data.signupDbHelper;
import com.example.muskan.medical_help.Helpers.inputValidation;

public class LoginActivity extends AppCompatActivity {

    private final AppCompatActivity activity = LoginActivity.this;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private Button ButtonLogin;

    private inputValidation input_Validation;
    private signupDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initObjects();

        ButtonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                verifyFromSQLite();
            }
        });


    }

    @SuppressLint("WrongViewCast")
    private void initViews() {

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.email_input);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.pwd_input);
        ButtonLogin = (Button) findViewById(R.id.buttonLogin);

    }

    private void initObjects() {
        dbHelper = new signupDbHelper(activity);
        input_Validation = new inputValidation(activity);

    }


    private void verifyFromSQLite() {
        if (!input_Validation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        if (!input_Validation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        if(dbHelper.checkUser(textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())){

            Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(dashboardIntent);
        }
        else {
            emptyInputEditText();
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}