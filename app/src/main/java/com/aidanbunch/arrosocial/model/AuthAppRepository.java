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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.aidanbunch.arrosocial.utils.Constants;
import com.aidanbunch.arrosocial.utils.SharedPrefs;
import com.aidanbunch.arrosocial.view.CentralActivity;
import com.aidanbunch.arrosocial.view.welcome.LoginActivity;
import com.aidanbunch.arrosocial.view.welcome.RecoverPassActivity;
import com.aidanbunch.arrosocial.view.welcome.WelcomeViewActivity;
import com.aidanbunch.arrosocial.view.onboardingUC.UserCreationOnboardingActivity;
import com.aidanbunch.arrosocial.viewmodel.LogInViewModel;
import com.aidanbunch.arrosocial.viewmodel.RecoverPassViewModel;
import com.aidanbunch.arrosocial.viewmodel.SignUpViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class AuthAppRepository {
    private Application application;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        SharedPrefs.instance(application.getApplicationContext());

        if (mAuth.getCurrentUser() != null) {
            userLiveData.postValue(mAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
            setUserProperties();
        }
    }

    public void signUpUser(String email, String password, Activity act) {
        ProgressDialog progDialog = new ProgressDialog(act);
        progDialog.setMessage("Signing up...");
        progDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    progDialog.dismiss();
                    if (task.isSuccessful()) {
                        userLiveData.postValue(mAuth.getCurrentUser());
                        setUserProperties();
                        act.startActivity(new Intent(application.getApplicationContext(), UserCreationOnboardingActivity.class));
                        act.finish();
                    } else {
                        //signUpFailFlag = 1;
                        Toast.makeText(application.getApplicationContext(), "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void loginUser(String email, String password, Activity act) {
        ProgressDialog progDialog = new ProgressDialog(act);
        progDialog.setMessage("Logging in...");
        progDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progDialog.dismiss();
            if (task.isSuccessful()) {
                userLiveData.postValue(mAuth.getCurrentUser());
                setUserProperties();
                act.startActivity(new Intent(application.getApplicationContext(), CentralActivity.class));
                act.finish();
            } else {
                Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Auth", "User account deleted.");
            }
        });
        SharedPrefs.instance().resetSharedPrefs();
    }

    public void recoverPass(String email, Activity act) {
        ProgressDialog progDialog = new ProgressDialog(act);
        progDialog.setMessage("Sending reset email...");
        progDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progDialog.dismiss();
            if(task.isSuccessful()) {
                act.startActivity(new Intent(application.getApplicationContext(), LoginActivity.class));
                act.finish();
            }
            else {
                Toast.makeText(application.getApplicationContext(), "Reset Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserToDb(Map<String, Object> user, Activity act) {
        db.collection(Constants.FSCollections.users)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FS", "DocumentSnapshot added with ID: " + documentReference.getId());
                    act.startActivity(new Intent(application.getApplicationContext(), CentralActivity.class));
                    act.finish();
                })
                .addOnFailureListener(e -> {
                    Log.d("FS", "Error adding document", e);
                    Toast.makeText(application.getApplicationContext(), "Error adding document.", Toast.LENGTH_SHORT).show();
                });
    }

    public void checkUserInDb(String user) {
        String cleanedUser = user.trim().toLowerCase();

        Query dbRef = db.collection(Constants.FSCollections.users).whereEqualTo(Constants.FSUserData.username, cleanedUser).limit(1);

        dbRef.addSnapshotListener((documentSnapshots, e) -> {
            for (DocumentSnapshot ds: documentSnapshots){
                if (ds!=null){
                    Log.d("checkUser", "checkingIfUsernameExist: FOUND A MATCH");
                    Toast.makeText(application.getApplicationContext(), "That username already exists.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("checkUser", "checkingIfUsernameExist: FOUND NO MATCH");
                }
            }
        });
    }

    public void setUserProperties() {

        if (userLiveData.getValue() != null) {
            String uid = userLiveData.getValue().getUid();

            db.collection(Constants.FSCollections.users).document(uid).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Map<String, Object> user = task.getResult().getData();
                    if (user != null) {
                        SharedPrefs.instance().storeValueString(Constants.FSUserData.username, (String) user.get(Constants.FSUserData.username));
                        SharedPrefs.instance().storeValueString(Constants.FSUserData.fName, (String) user.get(Constants.FSUserData.fName));
                        SharedPrefs.instance().storeValueString(Constants.FSUserData.lName, (String) user.get(Constants.FSUserData.lName));
                        SharedPrefs.instance().storeValueString(Constants.FSUserData.profilePicHex, (String) user.get(Constants.FSUserData.profilePicHex));
                    }
                }
                else {
                    Toast.makeText(application.getApplicationContext(), "User data could not be retrieved, please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void logOut() {
        mAuth.signOut();
        loggedOutLiveData.postValue(true);
        SharedPrefs.instance().resetSharedPrefs();
    }


    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
