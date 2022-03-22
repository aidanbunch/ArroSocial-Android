package com.aidanbunch.arrosocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    //views
    TextView signUpError;
    TextInputEditText emailForm, passForm, rePassForm;
    AppCompatButton signUpBtn;
    ProgressDialog progressDialog;

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

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");

        rePassForm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66) {
                    hideSoftKeyboard(view);
                    signUpBtn.performClick();
                }
                return false;
            }
        });

        // reg click
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailForm.getText().toString().trim();
                String password = passForm.getText().toString().trim();
                String rePassword = rePassForm.getText().toString().trim();

                if (email.equals("") || password.equals("") || rePassword.equals("")) {
                    hideSoftKeyboard(view);
                    setSignUpError(signUpError, "All fields must be entered.");
                }
                else if (!(password.compareTo(rePassword) == 0)) {
                    hideSoftKeyboard(view);
                    setSignUpError(signUpError, "Both passwords must match.");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    hideSoftKeyboard(view);
                    setSignUpError(signUpError, "Invalid email");
                }
                else if (!(checkPass(password))) {
                    hideSoftKeyboard(view);
                    setSignUpError(signUpError, "Password needs at least 6 characters, a number, a capital letter and a lowercase letter.");
                }
                else {
                    signUpError.setTextColor(getResources().getColor(R.color.off_white));
                    signUpUser(email, password);
                }
            }
        });

    }

    private void signUpUser(String email, String password) {
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
                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private static boolean checkPass(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean lengthFlag = str.length() >= 7;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag && lengthFlag)
                return true;
        }
        return false;
    }

    private void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(view);
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
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.off_white));
        actionBar.setElevation(0);

        // back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setSignUpError(TextView signUpError, String errorMsg) {
        signUpError.setTextColor(getResources().getColor(R.color.red));
        signUpError.setText(errorMsg);
    }
}