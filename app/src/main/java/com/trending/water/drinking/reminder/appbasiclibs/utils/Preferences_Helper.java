package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences_Helper {
    Activity act;
    Context mContext;
    SharedPreferences sharedPreferences;

    public Preferences_Helper(Context mContext2, Activity act2) {
        this.mContext = mContext2;
        this.act = act2;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext2);
    }

    public Preferences_Helper(Context mContext2) {
        this.mContext = mContext2;
        this.act = null;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext2);
    }

    public void savePreferences(String key, boolean value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void savePreferences(String key, float value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void savePreferences(String key, long value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void clearAll() {
        this.sharedPreferences.edit().clear().apply();
    }

    public boolean getBoolean(String name) {
        return this.sharedPreferences.getBoolean(name, false);
    }

    public String getString(String name) {
        return this.sharedPreferences.getString(name, "");
    }

    public int getInt(String name) {
        return this.sharedPreferences.getInt(name, 0);
    }

    public float getFloat(String name) {
        return this.sharedPreferences.getFloat(name, 0.0f);
    }

    public long getLong(String name) {
        return this.sharedPreferences.getLong(name, 0);
    }
}
