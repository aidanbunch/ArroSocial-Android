package com.aidanbunch.arrosocial.view.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aidanbunch.arrosocial.R;
import com.aidanbunch.arrosocial.utils.Constants;
import com.aidanbunch.arrosocial.utils.UtilsMethods;
import com.aidanbunch.arrosocial.viewmodel.RecoverPassViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPassActivity extends AppCompatActivity {

    private TextView resetError;
    private AppCompatButton resetBtn;
    private TextInputEditText emForm;
    RecoverPassViewModel recoverPassVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pass);

        recoverPassVM = new ViewModelProvider(this).get(RecoverPassViewModel.class);
        Activity recoverPassAct = RecoverPassActivity.this;

        setupUI(findViewById(R.id.forgotPassParent));
        setUpActionBar();

        resetError = findViewById(R.id.forgotError);
        resetBtn = findViewById(R.id.forgot_btn);
        emForm = findViewById(R.id.forgotEmForm);

        emForm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66) {
                    UtilsMethods.hideSoftKeyboard(RecoverPassActivity.this, view);
                }
                return false;
            }
        });

        resetBtn.setOnClickListener(view -> {
            String email = emForm.getText().toString().trim().toLowerCase();
            if (email.equals("")) {
                UtilsMethods.hideSoftKeyboard(RecoverPassActivity.this, view);
                setResetError(resetError, "All fields must be entered.");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                UtilsMethods.hideSoftKeyboard(RecoverPassActivity.this, view);
                setResetError(resetError, "Invalid email");
            }
            else {
                resetError.setTextColor(getResources().getColor(Constants.AppColors.off_white));
                recoverPassVM.recoverPass(email, recoverPassAct);
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
                    UtilsMethods.hideSoftKeyboard(RecoverPassActivity.this, view);
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

    public void setResetError(TextView signUpError, String errorMsg) {
        signUpError.setTextColor(getResources().getColor(Constants.AppColors.red));
        signUpError.setText(errorMsg);
    }

}

