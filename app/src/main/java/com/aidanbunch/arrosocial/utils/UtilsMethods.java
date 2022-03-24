package com.aidanbunch.arrosocial.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

public class UtilsMethods {
    static Context context;

    UtilsMethods(Context c) {
        context = c;
    }

    public static boolean checkPass(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean lengthFlag = str.length() >= 7;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowerCaseFlag && lengthFlag)
                return true;
        }
        return false;
    }

    public static void hideSoftKeyboard(AppCompatActivity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
