package com.aidanbunch.arrosocial.view.onboardingUC;

import android.graphics.drawable.Drawable;

public class UserCreationOnboardingItem {
    public static int pageCount;
    public static String userNameData;
    public static String firstNameData;
    public static String lastNameData;
    private String onboardingTitle;
    private String onboardingFirstName;
    private String onboardingLastName;
    private Drawable onboardingImagePreview;
    private String onboardingImageTitle;
    private String buttonText;
    private int buttonBackground;
    private int buttonVisibility;
    private int firstNameVisibility;
    private int lastNameVisibility;
    private boolean buttonEnabled;
    private boolean imageButtonEnabled;
    private int imageButtonVisibility;
    private int imageButtonColor;

    public int getImageButtonColor() {
        return imageButtonColor;
    }

    public void setImageButtonColor(int imageButtonColor) {
        this.imageButtonColor = imageButtonColor;
    }

    public boolean isImageButtonEnabled() {
        return imageButtonEnabled;
    }

    public void setImageButtonEnabled(boolean imageButtonEnabled) {
        this.imageButtonEnabled = imageButtonEnabled;
    }

    public int getImageButtonVisibility() {
        return imageButtonVisibility;
    }

    public void setImageButtonVisibility(int imageButtonVisibility) {
        this.imageButtonVisibility = imageButtonVisibility;
    }

    public Drawable getOnboardingImagePreview() {
        return onboardingImagePreview;
    }

    public void setOnboardingImagePreview(Drawable onboardingImagePreview) {
        this.onboardingImagePreview = onboardingImagePreview;
    }

    public boolean isButtonEnabled() {
        return buttonEnabled;
    }

    public void setButtonEnabled(boolean buttonEnabled) {
        this.buttonEnabled = buttonEnabled;
    }

    public int getFirstNameVisibility() {
        return firstNameVisibility;
    }

    public void setFirstNameVisibility(int firstNameVisibility) {
        this.firstNameVisibility = firstNameVisibility;
    }

    public int getLastNameVisibility() {
        return lastNameVisibility;
    }

    public void setLastNameVisibility(int lastNameVisibility) {
        this.lastNameVisibility = lastNameVisibility;
    }

    public int getButtonVisibility() {
        return buttonVisibility;
    }

    public void setButtonVisibility(int buttonVisibility) {
        this.buttonVisibility = buttonVisibility;
    }


    public String getOnboardingTitle() {
        return onboardingTitle;
    }

    public void setOnboardingTitle(String onboardingTitle) {
        this.onboardingTitle = onboardingTitle;
    }

    public String getOnboardingFirstName() {
        return onboardingFirstName;
    }

    public void setOnboardingFirstName(String onboardingFirstName) {
        this.onboardingFirstName = onboardingFirstName;
    }

    public String getOnboardingLastName() {
        return onboardingLastName;
    }

    public void setOnboardingLastName(String onboardingLastName) {
        this.onboardingLastName = onboardingLastName;
    }

    public String getOnboardingImageTitle() {
        return onboardingImageTitle;
    }

    public void setOnboardingImageTitle(String onboardingImageTitle) {
        this.onboardingImageTitle = onboardingImageTitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getButtonBackground() {
        return buttonBackground;
    }

    public void setButtonBackground(int buttonBackground) {
        this.buttonBackground = buttonBackground;
    }

}

