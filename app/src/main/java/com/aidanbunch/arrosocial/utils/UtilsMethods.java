package com.aidanbunch.arrosocial.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UtilsMethods {

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

    public static String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }

    public static String generateRandomColor() {
        Random randObj = new Random();
        int randNum = randObj.nextInt(0xffffff + 1);
        return String.format("#%06x", randNum);
    }

    public static Map<String, Object> addUserFS(String first, String last, String username, String hex) {
        Map<String, Object> user = new HashMap<>();
        user.put("first_name", first);
        user.put("last_name", last);
        user.put("username", username);
        user.put("generated_profile_picture_background_in_hex", hex);
        return user;
    }

}
