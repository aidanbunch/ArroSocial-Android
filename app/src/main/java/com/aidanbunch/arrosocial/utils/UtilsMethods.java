package com.aidanbunch.arrosocial.utils;

import android.content.Context;

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

}
