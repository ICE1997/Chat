package com.chzu.ice.chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;

public class SPHelper {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    /**
     * @param ctx     Context
     * @param address 配置文件存取地址
     */
    @SuppressLint("CommitPrefEdits")
    public SPHelper(Context ctx, String address) {
        sp = ctx.getSharedPreferences(address, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public String getString(String key, @Nullable String defValue) {
        return sp.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return sp.getStringSet(key, defValues);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public boolean contains(String key) {
        return sp.contains(key);
    }


    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sp.registerOnSharedPreferenceChangeListener(listener);
    }


    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        unregisterOnSharedPreferenceChangeListener(listener);
    }

    public SharedPreferences.Editor putString(String key, @Nullable String value) {
        return editor.putString(key, value);
    }

    public SharedPreferences.Editor putStringSet(String key, @Nullable Set<String> values) {
        return editor.putStringSet(key, values);
    }

    public SharedPreferences.Editor putInt(String key, int value) {
        return editor.putInt(key, value);
    }

    public SharedPreferences.Editor putLong(String key, long value) {
        return editor.putLong(key, value);
    }

    public SharedPreferences.Editor putFloat(String key, float value) {
        return editor.putFloat(key, value);
    }

    public SharedPreferences.Editor putBoolean(String key, boolean value) {
        return editor.putBoolean(key, value);
    }

    public SharedPreferences.Editor remove(String key) {
        return editor.remove(key);
    }

    public SharedPreferences.Editor clear() {
        return editor.clear();
    }

    public boolean commit() {
        return editor.commit();
    }

    public void apply() {
        editor.apply();
    }
}