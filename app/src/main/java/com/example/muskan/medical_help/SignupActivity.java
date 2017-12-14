package com.example.muskan.medical_help;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muskan.medical_help.Data.signupDbHelper;
import com.example.muskan.medical_help.Helpers.inputValidation;
import com.example.muskan.medical_help.Models.user_model;

public class SignupActivity extends AppCompatActivity {

    private final AppCompatActivity activity = SignupActivity.this;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private Button buttonSignup;
    private TextView textviewLogin;

    private inputValidation input_Validation;
    private signupDbHelper dbHelper;
    private user_model user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        initViews();
        initObjects();

//      To handle already have account
        textviewLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

//        To handle sign up
        buttonSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                postDataToSQLite();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void initViews() {

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.email_input);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.pwd_input);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.pwd_confirm);

        buttonSignup = (Button) findViewById(R.id.Signup_Button);
        textviewLogin = (TextView) findViewById(R.id.login_tv);
    }

    private void initObjects() {
        input_Validation = new inputValidation(activity);
        dbHelper = new signupDbHelper(activity);
        user = new user_model();
    }

    private void postDataToSQLite() {
        Boolean flag = true;

        if (!input_Validation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!input_Validation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!input_Validation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!input_Validation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!dbHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {
            user.setUserId(textInputEditTextEmail.getText().toString().trim());
            user.setPwd(textInputEditTextPassword.getText().toString().trim());
            dbHelper.addUser(user);

        } else {
            flag = false;
            emptyInputEditText();
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (flag) {
            Intent dashboardIntent = new Intent(SignupActivity.this, DashboardActivity.class);
            startActivity(dashboardIntent);
        }

    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}