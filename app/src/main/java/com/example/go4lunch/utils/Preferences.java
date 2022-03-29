package com.example.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String MY_PREF = "goLunch";
    public static final String HAS_LIKED_RESTAURANT = "hasLikedRestaurant";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public Preferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void setString(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public String getString(String key) {
        return this.sharedPreferences.getString(key, null);
    }

    public void setBoolean(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public boolean getBoolean(String key) {
        return this.sharedPreferences.getBoolean(key, false);
    }


    public void clear(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }
}
