package com.example.androidproject.profile;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String SWITCH_STATE_KEY = "switchState";

    private SharedPreferences preferences;

    public PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean getSwitchState() {
        return preferences.getBoolean(SWITCH_STATE_KEY, false);
    }

    public void setSwitchState(boolean state) {
        preferences.edit().putBoolean(SWITCH_STATE_KEY, state).apply();
    }
}
