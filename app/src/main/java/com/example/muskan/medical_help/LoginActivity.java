package com.example.muskan.medical_help;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.muskan.medical_help.Helpers.InputValidation;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private final AppCompatActivity activity = LoginActivity.this;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private Button ButtonLogin;

    private InputValidation input_Validation;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    SignInButton googleSignInButton;
    private final static int RC_SIGN_IN = 2;
    GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initObjects();

        ButtonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user_email = textInputEditTextEmail.getText().toString().trim();
                String pwd = textInputEditTextPassword.getText().toString().trim();

                if(!TextUtils.isEmpty(user_email) || !TextUtils.isEmpty(pwd)){
                    progressDialog.setTitle("Logging In");
                    progressDialog.setMessage("Please wait while your credentials are verified!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    login_user(user_email, pwd);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please fill the desired fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //======================================================================================
        //Google sign in

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // To handle the google sign in button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {

                    signIn();
                }else
                {
                    Toast.makeText(getApplicationContext(), "No Connection!\nCheck your Internet Connection", Toast.LENGTH_LONG).show();
                }
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
        googleSignInButton = (SignInButton) findViewById(R.id.googleLoginBtn);

    }

    private void initObjects() {

        input_Validation = new InputValidation(activity);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void login_user(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(dashboardIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Authentication failed. Password is too short!",
                                    Toast.LENGTH_SHORT).show();
                            emptyInputEditText();

                        }
                    }
                });

    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void signIn() {

        progressDialog.setTitle("Logging into your account");
        progressDialog.setMessage("Please wait while your account is being authenticated!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);

            } else {
                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Intent mainIntent = new Intent(LoginActivity.this,DashboardActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this,"Authentication failed!", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

}