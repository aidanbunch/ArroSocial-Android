package com.aidanbunch.arrosocial.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.aidanbunch.arrosocial.view.CentralActivity;
import com.aidanbunch.arrosocial.view.welcome.LoginActivity;
import com.aidanbunch.arrosocial.view.welcome.WelcomeViewActivity;
import com.aidanbunch.arrosocial.view.onboardingUC.UserCreationOnboardingActivity;
import com.aidanbunch.arrosocial.viewmodel.LogInViewModel;
import com.aidanbunch.arrosocial.viewmodel.RecoverPassViewModel;
import com.aidanbunch.arrosocial.viewmodel.SignUpViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppRepository {
    private Application application;

    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private static int signUpFailFlag;
    private static int signInFailFlag;
    private static Activity logInAct = LogInViewModel.logInAct;
    private static Activity signUpAct = SignUpViewModel.signUpAct;
    private static Activity resetAct = RecoverPassViewModel.resetAct;

    private ProgressDialog progDialog;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if (mAuth.getCurrentUser() != null) {
            userLiveData.postValue(mAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void signUpUser(String email, String password) {
        progDialog = new ProgressDialog(signUpAct);
        progDialog.setMessage("Signing up...");
        progDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progDialog.dismiss();
                            userLiveData.postValue(mAuth.getCurrentUser());
                            signUpAct.startActivity(new Intent(application.getApplicationContext(), UserCreationOnboardingActivity.class));
                            signUpAct.finish();
                        } else {
                            //signUpFailFlag = 1;
                            progDialog.dismiss();
                            Toast.makeText(application.getApplicationContext(), "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void loginUser(String email, String password) {
        progDialog = new ProgressDialog(logInAct);
        progDialog.setMessage("Logging in...");
        progDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progDialog.dismiss();
                if (task.isSuccessful()) {
                    userLiveData.postValue(mAuth.getCurrentUser());
                    logInAct.startActivity(new Intent(application.getApplicationContext(), CentralActivity.class));
                    logInAct.finish();
                } else {
                    //signInFailFlag = 1;
                    Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Auth", "User account deleted.");
                }
            }
        });
    }

    public void recoverPass(String email) {
        progDialog = new ProgressDialog(logInAct);
        progDialog.setMessage("Sending reset email...");
        progDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progDialog.dismiss();
                if(task.isSuccessful()) {
                    resetAct.startActivity(new Intent(application.getApplicationContext(), LoginActivity.class));
                    resetAct.finish();
                }
                else {
                    Toast.makeText(application.getApplicationContext(), "Reset Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logOut() {
        mAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public int getSignUpFailFlag() {
        return signUpFailFlag;
    }

    public int getSignInFailFlag() {
        return signInFailFlag;
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
