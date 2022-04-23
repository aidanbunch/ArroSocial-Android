package com.aidanbunch.arrosocial.view.onboardingUC;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aidanbunch.arrosocial.R;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aidanbunch.arrosocial.utils.Constants;
import com.aidanbunch.arrosocial.utils.SharedPrefs;
import com.aidanbunch.arrosocial.utils.UtilsMethods;
import com.aidanbunch.arrosocial.view.CentralActivity;
import com.aidanbunch.arrosocial.view.welcome.WelcomeViewActivity;
import com.aidanbunch.arrosocial.viewmodel.UCOViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserCreationOnboardingActivity extends AppCompatActivity {

    private UserCreationOnboardingAdapter onboardingAdapter;
    private LinearLayout onboardingLayout;
    private MaterialButton onboardingActionBtn;
    private AppCompatButton onboardingResetBtn;
    private FirebaseFirestore db;
    UCOViewModel ucoVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation_onboarding);

        db = FirebaseFirestore.getInstance();
        ucoVM = new ViewModelProvider(this).get(UCOViewModel.class);

        onboardingActionBtn = findViewById(R.id.buttonOnBoarding);
        onboardingResetBtn = findViewById(R.id.onboardingReset);
        onboardingLayout = findViewById(R.id.onboardingLinearLayout);

        setupUI(findViewById(R.id.UCParent));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setOnboardingItems();

        ViewPager2 onboardingViewPager = findViewById(R.id.viewPagerOnboarding);
        onboardingViewPager.setAdapter(onboardingAdapter);

        /*RecyclerView onRV = (RecyclerView) onboardingViewPager.getChildAt(0);
        RecyclerView.ViewHolder onboardingViewHolder = onRV.findViewHolderForAdapterPosition(0);*/

        setOnboardingIndicator();
        setCurrentOnboardingIndicators(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });

        onboardingActionBtn.setOnClickListener(view -> {
            if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                if (onboardingViewPager.getCurrentItem() == 0) {
                    UserCreationOnboardingItem.pageCount = 0;
                } else if (onboardingViewPager.getCurrentItem() == 1) {
                    UserCreationOnboardingItem.pageCount = 1;
                }
            } else {
                if(!(UserCreationOnboardingItem.userNameData == null || UserCreationOnboardingItem.firstNameData == null || UserCreationOnboardingItem.lastNameData == null)) {

                    SharedPrefs.instance().storeValueString(Constants.FSUserData.username, UserCreationOnboardingItem.userNameData.trim().toLowerCase());
                    SharedPrefs.instance().storeValueString(Constants.FSUserData.fName, UserCreationOnboardingItem.firstNameData.trim());
                    SharedPrefs.instance().storeValueString(Constants.FSUserData.lName, UserCreationOnboardingItem.lastNameData.trim());

                    Map<String, Object> user = UtilsMethods.createUserMap(SharedPrefs.instance().fetchValueString(Constants.FSUserData.fName),
                            SharedPrefs.instance().fetchValueString(Constants.FSUserData.lName),
                            SharedPrefs.instance().fetchValueString(Constants.FSUserData.username),
                            SharedPrefs.instance().fetchValueString(Constants.FSUserData.profilePicHex));

                    db.collection(Constants.FSCollections.users)
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("FS", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    startActivity(new Intent(getApplicationContext(), CentralActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("FS", "Error adding document", e);
                                    Toast.makeText(getApplicationContext(), "Error adding document.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "One or more fields were blank. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onboardingResetBtn.setOnClickListener(view -> {
            ucoVM.deleteUser();
            startActivity(new Intent(getApplicationContext(), WelcomeViewActivity.class));
            finish();
        });

    }

    private void setOnboardingIndicator() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            onboardingLayout.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentOnboardingIndicators(int index) {
        int childCount = onboardingLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) onboardingLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }
        if (index == onboardingAdapter.getItemCount() - 1) {
            onboardingActionBtn.setText("Start");
        } else {
            onboardingActionBtn.setText("Next");
        }
    }

    private void setOnboardingItems() {
        List<UserCreationOnboardingItem> onBoardingItems = new ArrayList<>();

        UserCreationOnboardingItem itemUsername = new UserCreationOnboardingItem();
        itemUsername.setOnboardingTitle("Pick a username");
        itemUsername.setOnboardingFirstName("Username");
        itemUsername.setLastNameVisibility(1);
        itemUsername.setButtonEnabled(false);
        itemUsername.setImageButtonEnabled(false);

        UserCreationOnboardingItem itemFullName = new UserCreationOnboardingItem();
        itemFullName.setOnboardingTitle("Enter your first and last name");
        itemFullName.setOnboardingFirstName("First Name");
        itemFullName.setOnboardingLastName("Last Name");
        itemFullName.setButtonEnabled(false);
        itemFullName.setImageButtonEnabled(false);

        UserCreationOnboardingItem itemProfilePic = new UserCreationOnboardingItem();
        itemProfilePic.setOnboardingImageTitle("Tap on the circle to import your own profile picture");
        itemProfilePic.setOnboardingImagePreview(getResources().getDrawable(R.drawable.image_preview_default));
        itemProfilePic.setButtonText("Shuffle");
        itemProfilePic.setButtonBackground(R.drawable.rounded_btn_purple);
        itemProfilePic.setButtonVisibility(0);
        itemProfilePic.setFirstNameVisibility(1);
        itemProfilePic.setLastNameVisibility(1);
        itemProfilePic.setButtonEnabled(true);
        itemProfilePic.setImageButtonEnabled(true);
        itemProfilePic.setImageButtonVisibility(0);

        onBoardingItems.add(itemUsername);
        onBoardingItems.add(itemFullName);
        onBoardingItems.add(itemProfilePic);

        onboardingAdapter = new UserCreationOnboardingAdapter(onBoardingItems);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    UtilsMethods.hideSoftKeyboard(UserCreationOnboardingActivity.this, view);
                    return false;
                }
            });
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                        UtilsMethods.hideSoftKeyboard(UserCreationOnboardingActivity.this, view);
                    }
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

}

