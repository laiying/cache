package com.strod.cache.api.shareprefers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.strod.cache.api.ICache;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by laiying on 2019/7/2.
 */
public class SharePrefersManager implements ICache {
    private SharedPreferences mSharedPreferences;

    public void init(Context appContext, String appName){
        if (mSharedPreferences == null){
            mSharedPreferences = appContext.getSharedPreferences(appName, Context.MODE_PRIVATE);
        }
    }

    public SharedPreferences getSharedPreferences(){
        isSharedPreferencesInit();
        return mSharedPreferences;
    }

    private void isSharedPreferencesInit(){
        if (mSharedPreferences == null){
            throw new RuntimeException("must called SharePrefersManager.getInstance().init() method first");
        }
    }


    /**
     * Adds or replaces a String to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces String
     */
    @Override
    public void putString(String key, String value) {
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Retrieves a String from the cache.
     *
     * @param key Cache key
     * @return A String or null in the event of a cache miss
     */
    @Override
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * Retrieves a String from the cache.
     *
     * @param key      Cache key
     * @param defValue Default String if cache miss
     * @return A String in the event of a cache miss
     */
    @Override
    public String getString(String key, String defValue) {
        isSharedPreferencesInit();
        return mSharedPreferences.getString(key, defValue);
    }

    public void putStringSet(String key, Set<String> values){
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(key,values);
        editor.commit();
    }

    public Set<String> getStringSet(String key){
        return getStringSet(key, new HashSet<String>());
    }

    public Set<String> getStringSet(String key, Set<String> defValue){
        isSharedPreferencesInit();
        return mSharedPreferences.getStringSet(key, defValue);
    }

    /**
     * Adds or replaces an Int to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Int
     */
    @Override
    public void putInt(String key, int value) {
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Retrieves an Int from the cache.
     *
     * @param key Cache key
     * @return A Int or 0 in the event of a cache miss
     */
    @Override
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Retrieves an Int from the cache.
     *
     * @param key      Cache key
     * @param defValue Default Int if cache miss
     * @return A Int in the event of a cache miss
     */
    @Override
    public int getInt(String key, int defValue) {
        isSharedPreferencesInit();
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * Adds or replaces a Long to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Long
     */
    @Override
    public void putLong(String key, long value) {
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Retrieves a Long from the cache.
     *
     * @param key Cache key
     * @return A Long or 0L in the event of a cache miss
     */
    @Override
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    /**
     * Retrieves a Long from the cache.
     *
     * @param key      Cache key
     * @param defValue Default Long if cache miss
     * @return A Long in the event of a cache miss
     */
    @Override
    public long getLong(String key, long defValue) {
        isSharedPreferencesInit();
        return mSharedPreferences.getLong(key, defValue);
    }

    /**
     * Adds or replaces a Float to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Float
     */
    @Override
    public void putFloat(String key, float value) {
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Retrieves a Float from the cache.
     *
     * @param key Cache key
     * @return A Float or 0F in the event of a cache miss
     */
    @Override
    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    /**
     * Retrieves a Float from the cache.
     *
     * @param key      Cache key
     * @param defValue Default Float if cache miss
     * @return A Float in the event of a cache miss
     */
    @Override
    public float getFloat(String key, float defValue) {
        isSharedPreferencesInit();
        return mSharedPreferences.getFloat(key, defValue);
    }

    /**
     * Adds or replaces a Double to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Double
     */
    @Override
    public void putDouble(String key, double value) {
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    /**
     * Retrieves a Double from the cache.
     *
     * @param key Cache key
     * @return A Double or 0D in the event of a cache miss
     */
    @Override
    public double getDouble(String key) {
        return getDouble(key, 0d);
    }

    /**
     * Retrieves a Double from the cache.
     *
     * @param key      Cache key
     * @param defValue Default Double if cache miss
     * @return A Double in the event of a cache miss
     */
    @Override
    public double getDouble(String key, double defValue) {
        isSharedPreferencesInit();
        if (!mSharedPreferences.contains(key))
            return defValue;
        return Double.longBitsToDouble(mSharedPreferences.getLong(key, 0));
    }

    /**
     * Adds or replaces a Boolean to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Boolean
     */
    @Override
    public void putBoolean(String key, boolean value) {
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Retrieves a Boolean from the cache.
     *
     * @param key Cache key
     * @return A Boolean or false in the event of a cache miss
     */
    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Retrieves a Boolean from the cache.
     *
     * @param key      Cache key
     * @param defValue Default Boolean if cache miss
     * @return A Boolean in the event of a cache miss
     */
    @Override
    public boolean getBoolean(String key, boolean defValue) {
        isSharedPreferencesInit();
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Adds or replaces a T to the cache.
     *
     * @param key Cache key
     * @param t   Add or replaces T
     */
    @Override
    public <T> void put(String key, T t) {
        if (t == null){
            return;
        }
        String json = new Gson().toJson(t);
        putString(key, json);
    }

    /**
     * Retrieves a T from the cache.
     *
     * @param key Cache key
     * @param t   Object class
     * @return A T or null in the event of a cache miss
     */
    @Override
    public <T> T get(String key, Class<T> t) {
        String json = getString(key);
        if (TextUtils.isEmpty(json)){
            return null;
        }
        T tObj = null;
        try {
            tObj = new Gson().fromJson(json, t);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return tObj;
    }

    /**
     * Retrieves a T from the cache.
     *
     * @param key Cache key
     * @param t   Object type
     * @return A T or null in the event of a cache miss
     */
    @Override
    public <T> T get(String key, Type t) {
        String json = getString(key);
        if (TextUtils.isEmpty(json)){
            return null;
        }
        T tObj = null;
        try {
            tObj = new Gson().fromJson(json, t);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return tObj;
    }

    /**
     * Removes from the cache.
     *
     * @param key Cache key
     */
    @Override
    public void remove(String key) {
        if (mSharedPreferences == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * Empties the cache.
     */
    @Override
    public void clear() {
        if (mSharedPreferences == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Get Cache Size
     *
     * @return bytes.length
     */
    @Override
    public long getCacheSize() {
        return 0;
    }
}
