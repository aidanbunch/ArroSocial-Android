package com.aidanbunch.arrosocial.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.aidanbunch.arrosocial.model.AuthAppRepository;

public class SplashViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}