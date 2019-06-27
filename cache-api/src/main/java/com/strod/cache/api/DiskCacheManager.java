package com.strod.cache.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.strod.cache.api.core.DiskLruCache;
import com.strod.cache.api.utils.CacheUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by laiying on 2017/7/17.
 */

public class DiskCacheManager {

    private static final String TAG = "DiskCacheManager";

    private static class SingletonHolder {
        private static final DiskCacheManager ourInstance = new DiskCacheManager();
    }

    public static DiskCacheManager getInstance() {
        return SingletonHolder.ourInstance;
    }

    private DiskCacheManager() {
    }

    private String mCacheDirName;
    /**
     * content cache dir name
     */
    // http://blog.csdn.net/guolin_blog/article/details/28863651
    private static final String DEFAULT_CACHE_DIR = "diskCache";

    private long mCacheSize;
    /**
     * 10MB
     */
    private static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;

    /**
     * is expire time mode
     */
    private boolean mHasExpireTime = false;
    /**
     * expire time
     */
    private long mExpireTime = DEFAULT_CACHE_TIME;

    /**
     * default cache expire time is 7 days
     */
    private static final long DEFAULT_CACHE_TIME = 7 * 24 * 60 * 60 * 1000;

    private DiskLruCache mDiskLruCache = null;


    public void init(Context context) {
        init(context, DEFAULT_CACHE_SIZE);
    }

    public void init(Context context, long cacheSize) {
        init(context, cacheSize, DEFAULT_CACHE_DIR);
    }

    public void init(Context context, long cacheSize, String cacheDirName) {
        init(context, cacheSize, cacheDirName, false);
    }

    public void init(Context context, long cacheSize, String cacheDirName, boolean hasExpireTime) {
        init(context, cacheSize, cacheDirName, hasExpireTime, DEFAULT_CACHE_TIME);
    }

    public void init(Context context, boolean hasExpireTime) {
        init(context, DEFAULT_CACHE_SIZE, DEFAULT_CACHE_DIR, hasExpireTime);
    }

    public void init(Context context, long cacheSize, String cacheDirName, boolean hasExpireTime, long expireTime) {
        mHasExpireTime = hasExpireTime;
        mExpireTime = expireTime;
        mCacheSize = cacheSize;
        mCacheDirName = cacheDirName;
        createDiskCache(context);
    }

    /**
     * get disk cache file
     *
     * @param context
     * @param uniqueName
     * @return
     */
    protected File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                cachePath = context.getExternalCacheDir().getPath();
            }
        }
        if (TextUtils.isEmpty(cachePath)) {
            cachePath = context.getCacheDir().getPath();
        }
//        cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * create disk cache
     *
     * @param context
     */
    public void createDiskCache(Context context) {
        if (mDiskLruCache == null) {
            try {
                File cacheDir = getDiskCacheDir(context, mCacheDirName);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir, 1/*getAppVersion(context)*/, mHasExpireTime ? 2 : 1, mCacheSize);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "create ContentCache error");
            }
        }
    }

    /**
     * get disk cache
     *
     * @param context
     * @return
     */
    @Deprecated
    public DiskLruCache getDiskCache(Context context) {
        if (mDiskLruCache == null) {
            createDiskCache(context);
        }
        return mDiskLruCache;
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * write int value
     * @param cacheKey
     * @param value
     */
    public void writeCache(String cacheKey, int value) {
        writeCache(cacheKey, String.valueOf(value));
    }

    /**
     * read int value
     * @param cacheKey
     * @return
     */
    public int readIntCache(String cacheKey){
        return readIntCache(cacheKey, 0);
    }

    /**
     * read int value
     * @param cacheKey
     * @param def
     * @return
     */
    public int readIntCache(String cacheKey, int def){
        String intCacheStr = readCache(cacheKey);
        int intCache = def;
        if (TextUtils.isEmpty(intCacheStr)){
            return intCache;
        }
        try {
            intCache = Integer.parseInt(intCacheStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return intCache;
    }

    /**
     * write long value
     * @param cacheKey
     * @param value
     */
    public void writeCache(String cacheKey, long value) {
        writeCache(cacheKey, String.valueOf(value));
    }

    /**
     * read long
     * @param cacheKey
     * @return
     */
    public long readLongCache(String cacheKey){
        return readLongCache(cacheKey, 0L);
    }

    /**
     * read long
     * @param cacheKey
     * @param def
     * @return
     */
    public long readLongCache(String cacheKey, long def){
        String longCacheStr = readCache(cacheKey);
        long longCache = def;
        if (TextUtils.isEmpty(longCacheStr)){
            return longCache;
        }
        try {
            longCache = Long.parseLong(longCacheStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return longCache;
    }

    /**
     * write boolean
     * @param cacheKey
     * @param value
     */
    public void writeCache(String cacheKey, boolean value) {
        writeCache(cacheKey, String.valueOf(value));
    }

    /**
     * read boolean
     * @param cacheKey
     * @return
     */
    public boolean readBooleanCache(String cacheKey){
        return readBooleanCache(cacheKey, false);
    }

    /**
     * read boolean
     * @param cacheKey
     * @param def
     * @return
     */
    public boolean readBooleanCache(String cacheKey, boolean def){
        boolean booleanCache = def;
        String booleanCacheStr = readCache(cacheKey);
        if (TextUtils.isEmpty(booleanCacheStr)){
            return booleanCache;
        }
        try {
            booleanCache = Boolean.parseBoolean(booleanCacheStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return booleanCache;
    }


    /**
     * write float
     * @param cacheKey
     * @param value
     */
    public void writeCache(String cacheKey, float value) {
        writeCache(cacheKey, String.valueOf(value));
    }

    /**
     * read float
     * @param cacheKey
     * @return
     */
    public float readFloatCache(String cacheKey){
        return readFloatCache(cacheKey, 0.0f);
    }

    /**
     * read float
     * @param cacheKey
     * @param def
     * @return
     */
    public float readFloatCache(String cacheKey, float def){
        float floatCache = def;
        String floatCacheStr = readCache(cacheKey);

        if (TextUtils.isEmpty(floatCacheStr)){
            return floatCache;
        }
        try {
            floatCache = Float.parseFloat(floatCacheStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return floatCache;
    }

    /**
     * write double
     * @param cacheKey
     * @param value
     */
    public void writeCache(String cacheKey, double value) {
        writeCache(cacheKey, String.valueOf(value));
    }

    /**
     * read double
     * @param cacheKey
     * @return
     */
    public double readDoubleCache(String cacheKey){
        return readDoubleCache(cacheKey, 0.0d);
    }

    /**
     * read double
     * @param cacheKey
     * @param def
     * @return
     */
    public double readDoubleCache(String cacheKey, double def){
        double floatCache = def;
        String floatCacheStr = readCache(cacheKey);

        if (TextUtils.isEmpty(floatCacheStr)){
            return floatCache;
        }
        try {
            floatCache = Double.parseDouble(floatCacheStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return floatCache;
    }


    /**
     * write cache
     *
     * @param cacheKey Cache Key
     * @param value    Cache Value
     */
    public void writeCache(String cacheKey, String value) {
        if (mDiskLruCache == null) return;

        if (TextUtils.isEmpty(value)) {
            return;
        }
        writeCache(cacheKey, value.getBytes());
    }

    public void writeCache(String cacheKey, byte b[]) {
        writeCache(cacheKey, b, mExpireTime);
    }

    public void writeCache(String cacheKey, byte b[], long expireTime) {
        if (mDiskLruCache == null) return;

        if (b == null) {
            return;
        }
        try {
            String key = CacheUtils.hashKeyForDisk(cacheKey);
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                outputStream.write(b);

                if (mHasExpireTime) {
                    OutputStream expireOS = editor.newOutputStream(1);
                    if (expireTime < 0) {
                        expireTime = mExpireTime;
                    }
                    expireOS.write(String.valueOf(System.currentTimeMillis() + expireTime).getBytes());
                }
                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, cacheKey + " writeContentCache() error");
        }
    }

    /**
     * write cache
     *
     * @param cacheKey Cache Key
     * @param value    Cache Value
     */
    public void writeCache(String cacheKey, String value, long expireTime) {
        if (mDiskLruCache == null) return;

        if (!mHasExpireTime) {
            Log.e(TAG, "expireTime invalid, must call init() parameter hasExpireTime is true");
        }

        if (TextUtils.isEmpty(value)) {
            return;
        }
        writeCache(cacheKey, value.getBytes(), expireTime);
    }

    public <T> void writeCache(String key, List<T> lists){
        if (lists == null){
            return;
        }
        String json = new Gson().toJson(lists);
        writeCache(key, json);
    }

    public <T> List<T> readListCache(String key, Class<T> t){
        String json = readCache(key);
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

    /**
     * write cache,inner obj convert json then write json cache
     *
     * @param key cacheKey
     * @param t   obj class
     * @param <T> obj class
     */
    public <T> void writeCache(String key, T t) {
        if (t == null) {
            return;
        }
        String json = new Gson().toJson(t);
        writeCache(key, json);
    }

    public <T> void writeCache(String key, T t, long expireTime) {
        if (t == null) {
            return;
        }
        String json = new Gson().toJson(t);
        writeCache(key, json, expireTime);
    }


    /**
     * read cache
     *
     * @param cacheKey cacheKey
     * @return return string
     */
    public String readCache(String cacheKey) {
        if (mDiskLruCache == null) return null;
        String value = null;
        try {
            String key = CacheUtils.hashKeyForDisk(cacheKey);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                value = snapShot.getString(0);
                if (mHasExpireTime) {
                    String expireTimeStr = snapShot.getString(1);
                    if (!TextUtils.isEmpty(expireTimeStr)) {
                        try {
                            long expireTime = Long.parseLong(expireTimeStr);
                            if (expireTime != 0 && System.currentTimeMillis() > expireTime) {
                                value = null;
                                mDiskLruCache.remove(key);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, cacheKey + " readCache() error");
        }
        return value;

    }

    /**
     * read cache, inner read json cache then json convert obj
     *
     * @param key cacheKey
     * @param t   json to convert obj,the obj class
     * @param <T> json to convert obj
     * @return return obj
     */
    public <T> T readCache(String key, Class<T> t) {
        String json = readCache(key);
        if (TextUtils.isEmpty(json)) {
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
     * read cache, inner read json cache then json convert obj
     *
     * @param key cacheKey
     * @param t   json to convert obj,the obj type
     * @param <T> json to convert obj
     * @return return obj
     */
    public <T> T readCache(String key, Type t) {
        String json = readCache(key);
        if (TextUtils.isEmpty(json)) {
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
     * remove cache
     *
     * @param cacheKey cacheKey
     */
    public void removeCache(String cacheKey) {
        if (mDiskLruCache == null) return;
        String key = CacheUtils.hashKeyForDisk(cacheKey);
        try {
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, cacheKey + " remove() error");
        }
    }

    /**
     * clear all cache
     */
    public void clearCache() {
        if (mDiskLruCache == null) return;
        try {
            mDiskLruCache.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "clearContentCache() error");
        }
    }

    /**
     * get cache last modified time
     *
     * @param cacheKey cacheKey
     * @return
     */
    public long getCacheLastModifiedTime(String cacheKey) {
        if (mDiskLruCache == null) return 0;

//        File cacheDir = getDiskCacheDir(context, mCacheDirName);
        File cacheDir = mDiskLruCache.getDirectory();
        if (!cacheDir.exists()) {
            return 0;
        }
        String key = CacheUtils.hashKeyForDisk(cacheKey);
        File file = new File(cacheDir, key + ".0");
//        Log.d(TAG, uniqu + " path:" + file.getAbsolutePath());
        if (!file.exists()) {
            return 0;
        }
        return file.lastModified();
    }

    /**
     * get cache size
     */
    public long getCacheSize() {
        if (mDiskLruCache == null) return 0;
        return mDiskLruCache.size();
    }

    /**
     * close cache in app destroy
     */
    public void closeCache() {
        if (mDiskLruCache == null) return;
        try {
            mDiskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "closeContentCache() error");
        }

    }

}