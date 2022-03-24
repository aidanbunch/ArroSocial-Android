package com.aidanbunch.arrosocial.view.onboardingUC;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aidanbunch.arrosocial.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aidanbunch.arrosocial.view.WelcomeViewActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UserCreationOnboardingActivity extends AppCompatActivity {

    private UserCreationOnboardingAdapter onboardingAdapter;
    private LinearLayout onboardingLayout;
    private MaterialButton onboardingActionBtn;
    private AppCompatButton onboardingResetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation_onboarding);

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

        onboardingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                    if (onboardingViewPager.getCurrentItem() == 0) {
                        UserCreationOnboardingViewModel.pageCount = 0;
                    } else if (onboardingViewPager.getCurrentItem() == 1) {
                        UserCreationOnboardingViewModel.pageCount = 1;
                    }
                } else {
                    Log.v("username", UserCreationOnboardingViewModel.userNameData);
                    Log.v("firstName", UserCreationOnboardingViewModel.firstNameData);
                    Log.v("lastName", UserCreationOnboardingViewModel.lastNameData);
                    startActivity(new Intent(getApplicationContext(), WelcomeViewActivity.class));
                    finish();
                }
            }
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
        List<UserCreationOnboardingViewModel> onBoardingItems = new ArrayList<>();

        UserCreationOnboardingViewModel itemUsername = new UserCreationOnboardingViewModel();
        itemUsername.setOnboardingTitle("Pick a username");
        itemUsername.setOnboardingFirstName("Username");
        itemUsername.setLastNameVisibility(1);
        itemUsername.setButtonEnabled(false);
        itemUsername.setImageButtonEnabled(false);

        UserCreationOnboardingViewModel itemFullName = new UserCreationOnboardingViewModel();
        itemFullName.setOnboardingTitle("Enter your first and last name");
        itemFullName.setOnboardingFirstName("First Name");
        itemFullName.setOnboardingLastName("Last Name");
        itemFullName.setButtonEnabled(false);
        itemFullName.setImageButtonEnabled(false);

        UserCreationOnboardingViewModel itemProfilePic = new UserCreationOnboardingViewModel();
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
                    hideSoftKeyboard(UserCreationOnboardingActivity.this, view);
                    return false;
                }
            });
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                        hideSoftKeyboard(UserCreationOnboardingActivity.this, view);
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

    public static void hideSoftKeyboard(AppCompatActivity userCreationActivity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) userCreationActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

