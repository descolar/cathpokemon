package com.descolar.catchpokemon;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREFERENCES_FILE = "user_preferences";
    private static final String KEY_LANGUAGE = "language";

    private final SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public void setLanguage(String language) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, "es");
    }
}
