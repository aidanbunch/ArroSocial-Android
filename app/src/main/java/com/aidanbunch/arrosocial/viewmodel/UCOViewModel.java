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

import java.util.Map;

public class UCOViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public UCOViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public void addUserToDb(Map<String, Object> user, Activity act) {
        authAppRepository.addUserToDb(user, act);
    }

    public void deleteUser() {
        authAppRepository.deleteUser();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}