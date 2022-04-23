package com.aidanbunch.arrosocial.view.welcome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.aidanbunch.arrosocial.R;

public class WelcomeViewActivity extends AppCompatActivity {

    //views
    AppCompatButton signupBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpActionBar();

        TextView learnMore = (TextView) findViewById(R.id.learn_more);
        learnMore.setMovementMethod(LinkMovementMethod.getInstance());

        //initialize buttons
        signupBtn = findViewById(R.id.signup_btn);
        loginBtn = findViewById(R.id.login_btn);

        //sign up btn click
        signupBtn.setOnClickListener(view -> startActivity(new Intent(WelcomeViewActivity.this, SignupActivity.class)));

        loginBtn.setOnClickListener(view -> startActivity(new Intent(WelcomeViewActivity.this, LoginActivity.class)));

    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}