package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PreferenceHelper {
    private final SharedPreferences sharedPreferences;

    public PreferenceHelper(@NonNull Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveBoolean(@NonNull String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void saveString(@NonNull String key, @Nullable String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void saveInt(@NonNull String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void saveFloat(@NonNull String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public void saveLong(@NonNull String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public void savePreferences(@NonNull String key, @Nullable Object value) {
        if (value instanceof Boolean) {
            saveBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            saveString(key, (String) value);
        } else if (value instanceof Integer) {
            saveInt(key, (Integer) value);
        } else if (value instanceof Float) {
            saveFloat(key, (Float) value);
        } else if (value instanceof Long) {
            saveLong(key, (Long) value);
        }
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    @Nullable
    public String getString(@NonNull String key, @Nullable String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Nullable
    public String getString(@NonNull String key) {
        return getString(key, "");
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public int getInt(@NonNull String key) {
        return getInt(key, 0);
    }

    public float getFloat(@NonNull String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public float getFloat(@NonNull String key) {
        return getFloat(key, 0.0f);
    }

    public long getLong(@NonNull String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public long getLong(@NonNull String key) {
        return getLong(key, 0L);
    }
}
