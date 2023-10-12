package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Shared {

    public static Context mContext = Config.appContext;

    public static void sharedPref(String key, String value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Shared.mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String obtainedPref(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Shared.mContext);
        String result = preferences.getString(key, "empty");
        return result;
    }

    public static void deletePref(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Shared.mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
