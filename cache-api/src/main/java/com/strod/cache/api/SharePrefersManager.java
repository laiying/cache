package com.strod.cache.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lying
 */
public class SharePrefersManager {

    private static SharePrefersManager sSharePreferencesMgr;

//    private Context mAppContext;

    private SharedPreferences mSharedPreferences;

    private static final String DEFAULT_NAME = "sp_content";

    public static SharePrefersManager getInstance(){
        if(sSharePreferencesMgr == null){
            synchronized (SharePrefersManager.class){
                if(sSharePreferencesMgr == null){
                    sSharePreferencesMgr = new SharePrefersManager();
                }
            }
        }

        return sSharePreferencesMgr;
    }

    private SharePrefersManager(){
    }

    public void init(Context appContext){
        init(appContext, DEFAULT_NAME);
    }

    public void init(Context appContext, String appName){
//        mAppContext = appContext;
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


    public void putString(String key, String value){
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key){
        return getString(key, "");
    }
    public String getString(String key, String defValue){
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

    public void putInt(String key, int value){
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key){
        return getInt(key, 0);
    }
    public int getInt(String key, int defValue){
        isSharedPreferencesInit();
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, long value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key){
        return getLong(key, 0l);
    }

    public long getLong(String key, long defValue){
        isSharedPreferencesInit();
        return mSharedPreferences.getLong(key, defValue);
    }

    public void putFloat(String key, float value){
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key){
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defValue){
        isSharedPreferencesInit();
        return mSharedPreferences.getFloat(key, defValue);
    }

    public void putDouble(String key, double value){
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public double getDouble(String key){
        return getDouble(key, 0d);
    }

    public double getDouble(String key, double defValue){
        isSharedPreferencesInit();
        if (!mSharedPreferences.contains(key))
            return defValue;
        return Double.longBitsToDouble(mSharedPreferences.getLong(key, 0));
    }

    public void putBoolean(String key, boolean value){
        isSharedPreferencesInit();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * get boolean
     * @param key
     * @return
     */
    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    /**
     * get boolean
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue){
        isSharedPreferencesInit();
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * remove SharedPreferences by key
     * @param key the SharedPreferences key
     */
    public void remove(String key) {
        if (mSharedPreferences == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * clear SharedPreferences
     */
    public void clear() {
        if (mSharedPreferences == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * put obj to SharedPreferences,inner obj convert to json
     * @param key the key
     * @param t obj class
     * @param <T> obj class
     */
    public <T> void put(String key, T t){
        if (t == null){
            return;
        }
        String json = new Gson().toJson(t);
        putString(key, json);
    }

    /**
     * get obj by SharedPreferences,inner json convert to obj
     * @param key the key
     * @param t obj class
     * @param <T> obj class
     * @return return obj
     */
    public <T> T get(String key, Class<T> t){
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
     * get obj by SharedPreferences,inner json convert to obj
     * @param key the key
     * @param t obj type
     * @param <T> obj class
     * @return return obj
     */
    public <T> T get(String key, Type t){
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

    public <T> void putList(String key, List<T> lists){
        if (lists == null){
            return;
        }
//        TypeToken typeToken = new TypeToken();
        String json = new Gson().toJson(lists);
        putString(key, json);
    }

    public <T> List<T> getList(String key, Class<T> t){
        String json = getString(key);
        if (TextUtils.isEmpty(json)){
            return null;
        }
        List<T> tObj = null;
        Type type = new ParameterizedTypeImpl(t);
        try {
            tObj = new Gson().fromJson(json,  type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return tObj;
    }

}
