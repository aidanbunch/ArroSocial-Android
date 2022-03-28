package com.aidanbunch.arrosocial.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    static SharedPrefs _instance;

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    public static SharedPrefs instance(Context context) {
        if (_instance == null) {
            _instance = new SharedPrefs();
            _instance.configSessionUtils(context);
        }
        return _instance;
    }

    public static SharedPrefs instance() {
        return _instance;
    }

    public void configSessionUtils(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("AppPreferences", Activity.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();
    }

    public void storeValueString(String key, String value) {
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    public String fetchValueString(String key) {
        return sharedPref.getString(key, null);
    }

    public void resetSharedPrefs() {
        sharedPrefEditor.clear();
        sharedPrefEditor.commit();
    }
}
