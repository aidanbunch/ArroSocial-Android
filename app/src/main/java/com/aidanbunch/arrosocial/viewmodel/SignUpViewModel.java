package com.aidanbunch.arrosocial.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.aidanbunch.arrosocial.model.AuthAppRepository;

public class SignUpViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public int getFlag() {
        return(authAppRepository.getFlag());
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void signUp(String email, String password) {
        authAppRepository.signUpUser(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}