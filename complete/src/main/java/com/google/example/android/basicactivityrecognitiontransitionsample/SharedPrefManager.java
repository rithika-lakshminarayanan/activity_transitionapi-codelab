package com.google.example.android.basicactivityrecognitiontransitionsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefManager {
    private SharedPreferences sharedPreferences;
    private static String LAST_ACTIVITY = "last_activity";

    public SharedPrefManager(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLastActivity(String lastActivity){
        sharedPreferences.edit().putString(LAST_ACTIVITY, lastActivity).apply();
    }

    public String getLastActivity(){
        return sharedPreferences.getString(LAST_ACTIVITY, "");
    }
}
