package com.aidanbunch.arrosocial.view.welcome;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidanbunch.arrosocial.R;
import com.aidanbunch.arrosocial.utils.Constants;
import com.aidanbunch.arrosocial.utils.UtilsMethods;
import com.aidanbunch.arrosocial.viewmodel.LogInViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView signInError, forgotPass;
    TextInputEditText emailForm, passForm;
    AppCompatButton logInBtn;
    LogInViewModel logInViewModel;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Activity logInAct = LoginActivity.this;

        setupUI(findViewById(R.id.logInParent));
        setUpActionBar();

        // init
        emailForm = findViewById(R.id.LIemForm);
        passForm = findViewById(R.id.LIpassForm);
        signInError = findViewById(R.id.signInError);
        logInBtn = findViewById(R.id.login_btn_2);
        forgotPass = findViewById(R.id.LIforgotPassword);

        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecoverPassActivity.class));
            }
        });

        passForm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66) {
                    UtilsMethods.hideSoftKeyboard(LoginActivity.this, view);
                }
                return false;
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                String email = emailForm.getText().toString().trim();
                String password = passForm.getText().toString().trim();

                if (email.equals("") || password.equals("")) {
                    UtilsMethods.hideSoftKeyboard(LoginActivity.this, view);
                    setSignInError(signInError, "All fields must be entered.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    UtilsMethods.hideSoftKeyboard(LoginActivity.this, view);
                    setSignInError(signInError, "Invalid email");
                } else if (!(UtilsMethods.checkPass(password))) {
                    UtilsMethods.hideSoftKeyboard(LoginActivity.this, view);
                    setSignInError(signInError, "Invalid password.");
                } else {
                    signInError.setTextColor(getResources().getColor(Constants.AppColors.off_white));
                    logInViewModel.signIn(email, password, logInAct);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    UtilsMethods.hideSoftKeyboard(LoginActivity.this, view);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }

    }

    public void setUpActionBar() {
        // Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.chevron_left);
        actionBar.setBackgroundDrawable(getResources().getDrawable(Constants.AppColors.off_white));
        actionBar.setElevation(0);

        // back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public void setSignInError(TextView signUpError, String errorMsg) {
        signUpError.setTextColor(getResources().getColor(Constants.AppColors.red));
        signUpError.setText(errorMsg);
    }
}