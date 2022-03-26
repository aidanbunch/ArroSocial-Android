package com.aidanbunch.arrosocial.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.aidanbunch.arrosocial.model.AuthAppRepository;

public class RecoverPassViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    public static Activity resetAct;

    public RecoverPassViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public int getSignInFailFlag() {
        return authAppRepository.getSignInFailFlag();
    }

    public void recoverPass(String email) {
        authAppRepository.recoverPass(email);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}