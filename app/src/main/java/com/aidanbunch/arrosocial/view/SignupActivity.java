package com.aidanbunch.arrosocial.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.aidanbunch.arrosocial.R;
import com.aidanbunch.arrosocial.utils.Constants;
import com.aidanbunch.arrosocial.utils.UtilsMethods;
import com.aidanbunch.arrosocial.viewmodel.SignUpViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    //views
    TextView signUpError;
    TextInputEditText emailForm, passForm, rePassForm;
    AppCompatButton signUpBtn;
    ProgressDialog progressDialog;
    SignUpViewModel signUpViewModel;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupUI(findViewById(R.id.signUpParent));

        // init
        emailForm = findViewById(R.id.emForm);
        passForm = findViewById(R.id.passForm);
        rePassForm = findViewById(R.id.rePassForm);
        signUpError = findViewById(R.id.signUpError);
        signUpBtn = findViewById(R.id.signup_btn_2);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");

        rePassForm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66) {
                    hideSoftKeyboard(SignupActivity.this, view);
                    signUpBtn.performClick();
                }
                return false;
            }
        });

        // reg click
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                String email = emailForm.getText().toString().trim();
                String password = passForm.getText().toString().trim();
                String rePassword = rePassForm.getText().toString().trim();

                if (email.equals("") || password.equals("") || rePassword.equals("")) {
                    hideSoftKeyboard(SignupActivity.this, view);
                    setSignUpError(signUpError, "All fields must be entered.");
                } else if (!(password.compareTo(rePassword) == 0)) {
                    hideSoftKeyboard(SignupActivity.this, view);
                    setSignUpError(signUpError, "Both passwords must match.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    hideSoftKeyboard(SignupActivity.this, view);
                    setSignUpError(signUpError, "Invalid email");
                } else if (!(UtilsMethods.checkPass(password))) {
                    hideSoftKeyboard(SignupActivity.this, view);
                    setSignUpError(signUpError, "Password needs at least 6 characters, a number, a capital letter and a lowercase letter.");
                } else {
                    signUpError.setTextColor(getResources().getColor(Constants.AppColors.off_white));
                    signUpViewModel.signUp(email, password, progressDialog);
                    if(signUpViewModel.getFlag() == 1) {
                        setSignUpError(signUpError, "Authentication failed.");
                    }
                }
            }
        });

    }

    /*private void signUpUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            System.out.println("registered");
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            setSignUpError(signUpError, "Authentication failed.");
                            //Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //Toast.makeText(SignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                setSignUpError(signUpError, "" + e.getMessage());
            }
        });
    } */

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
                    hideSoftKeyboard(SignupActivity.this, view);
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

    public void setSignUpError(TextView signUpError, String errorMsg) {
        signUpError.setTextColor(getResources().getColor(Constants.AppColors.red));
        signUpError.setText(errorMsg);
    }

    private static void hideSoftKeyboard(SignupActivity signupActivity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) signupActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}