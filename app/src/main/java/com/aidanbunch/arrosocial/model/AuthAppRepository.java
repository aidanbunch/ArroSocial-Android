package com.aidanbunch.arrosocial.model;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class AuthAppRepository {
    private Application application;

    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private int flag;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.flag = 0;

        if (mAuth.getCurrentUser() != null) {
            userLiveData.postValue(mAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void signUpUser(String email, String password, ProgressDialog progressDialog) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            System.out.println("registered");
                        } else {
                            progressDialog.dismiss();
                            flag = 1;
                            /* If sign in fails, display a message to the user.
                            setSignUpError(signUpError, "Authentication failed.");
                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();*/
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //Toast.makeText(SignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                //setSignUpError(signUpError, ""+e.getMessage());
            }
        });
    }

    public int getFlag() {
        return flag;
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
