package com.strod.cache.api.disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.strod.cache.api.ICache;
import com.strod.cache.api.core.DiskLruCache;
import com.strod.cache.api.utils.CacheUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Created by laiying on 2019/7/2.
 */
public class DiskCacheManager implements ICache {
    private static final String TAG = "DiskCacheManager";

    private String mCacheDirName;

    private long mCacheSize;

    /**
     * is expire time mode
     */
    private boolean mExpireTimeMode = false;
    /**
     * expire time
     */
    private long mExpireTime;

    private int mAppVersion;

    private DiskLruCache mDiskLruCache = null;

    public void init(Context context, String cacheDirName, long cacheSize, boolean expireTimeMode, long expireTime, int appVersion) {
        mCacheDirName = cacheDirName;
        mCacheSize = cacheSize;
        mExpireTimeMode = expireTimeMode;
        mExpireTime = expireTime;
        mAppVersion = appVersion;
        createDiskCache(context);
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
                mDiskLruCache = DiskLruCache.open(cacheDir, mAppVersion/*getAppVersion(context)*/, mExpireTimeMode ? 2 : 1, mCacheSize);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "create ContentCache error");
            }
        }
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

    /**
     * Adds or replaces a String to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces String
     */
    @Override
    public void putString(String key, String value) {
        writeCache(key, value);
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
        String value = readCache(key);
        if (TextUtils.isEmpty(value)){
            return defValue;
        }
        return value;
    }

    /**
     * Adds or replaces an Int to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Int
     */
    @Override
    public void putInt(String key, int value) {
        writeCache(key, String.valueOf(value));
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
        String intCacheStr = readCache(key);
        int intCache = defValue;
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
     * Adds or replaces a Long to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Long
     */
    @Override
    public void putLong(String key, long value) {
        writeCache(key, String.valueOf(value));
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
        String longCacheStr = readCache(key);
        long longCache = defValue;
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
     * Adds or replaces a Float to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Float
     */
    @Override
    public void putFloat(String key, float value) {
        writeCache(key, String.valueOf(value));
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
        float floatCache = defValue;
        String floatCacheStr = readCache(key);

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
     * Adds or replaces a Double to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Double
     */
    @Override
    public void putDouble(String key, double value) {
        writeCache(key, String.valueOf(value));
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
        double floatCache = defValue;
        String floatCacheStr = readCache(key);

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
     * Adds or replaces a Boolean to the cache.
     *
     * @param key   Cache key
     * @param value Add or replaces Boolean
     */
    @Override
    public void putBoolean(String key, boolean value) {
        writeCache(key, String.valueOf(value));
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
        boolean booleanCache = defValue;
        String booleanCacheStr = readCache(key);
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
     * Adds or replaces a T to the cache.
     *
     * @param key Cache key
     * @param t   Add or replaces T
     */
    @Override
    public <T> void put(String key, T t) {
        if (t == null) {
            return;
        }
        String json = new Gson().toJson(t);
        writeCache(key, json);
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
     * Retrieves a T from the cache.
     *
     * @param key Cache key
     * @param t   Object type
     * @return A T or null in the event of a cache miss
     */
    @Override
    public <T> T get(String key, Type t) {
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
     * Removes from the cache.
     *
     * @param key Cache key
     */
    @Override
    public void remove(String key) {
        if (mDiskLruCache == null) return;
        String hashKey = CacheUtils.hashKeyForDisk(key);
        try {
            mDiskLruCache.remove(hashKey);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, key + " remove() error");
        }
    }

    /**
     * Empties the cache.
     */
    @Override
    public void clear() {
        if (mDiskLruCache == null) return;
        try {
            mDiskLruCache.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "clearContentCache() error");
        }
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

    /**
     * write cache
     *
     * @param cacheKey Cache Key
     * @param value    Cache Value
     */
    public void writeCache(String cacheKey, String value, long expireTime) {
        if (mDiskLruCache == null) return;

        if (!mExpireTimeMode) {
            Log.e(TAG, "expireTime invalid, must call init() parameter hasExpireTime is true");
        }

        if (TextUtils.isEmpty(value)) {
            return;
        }
        writeCache(cacheKey, value.getBytes(), expireTime);
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

                if (mExpireTimeMode) {
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
                if (mExpireTimeMode) {
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
     * get cache size
     */
    @Override
    public long getCacheSize() {
        if (mDiskLruCache == null) return 0;
        return mDiskLruCache.size();
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
