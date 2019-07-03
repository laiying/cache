package com.strod.cache.api;

import android.content.Context;

import com.strod.cache.annotation.Cacheable;
import com.strod.cache.api.disk.DiskCacheManager;
import com.strod.cache.api.shareprefers.SharePrefersManager;

import java.lang.reflect.Type;

/**
 * Created by laiying on 2019/7/2.
 */
public class CacheManager {

    private DiskCacheManager mDiskCacheManager;
    private SharePrefersManager mSharePrefersManager;

    private static CacheManager sCacheManager;

    public static CacheManager getInstance() {
        if (sCacheManager == null){
            throw new RuntimeException("must call new CacheManager.Builder().with(this).build() in application");
        }
        return sCacheManager;
    }

    private CacheManager(Builder builder) {
        this.mCacheDirName = builder.cacheDirName;
        this.mCacheSize = builder.cacheSize;
        this.mExpireTimeMode = builder.expireTimeMode;
        this.mExpireTime = builder.expireTime;

        mDiskCacheManager = new DiskCacheManager();
        mDiskCacheManager.init(builder.appContext, builder.cacheDirName, builder.cacheSize, builder.expireTimeMode, builder.expireTime);

        mSharePrefersManager = new SharePrefersManager();
        mSharePrefersManager.init(builder.appContext, builder.cacheDirName);
    }


    public DiskCacheManager getDiskCacheManager() {
        return mDiskCacheManager;
    }

    public SharePrefersManager getSharePrefersManager() {
        return mSharePrefersManager;
    }

    /**
     * content cache dir name
     */
    private static final String DEFAULT_CACHE_DIR = "ContentCache";
    /**
     * 10MB
     */
    private static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
    /**
     * default cache expire time is 7 days
     */
    private static final long DEFAULT_CACHE_TIME = 7 * 24 * 60 * 60 * 1000;

    private String mCacheDirName = DEFAULT_CACHE_DIR;
    private long mCacheSize = DEFAULT_CACHE_SIZE;
    private boolean mExpireTimeMode = false;
    /**
     * expire time
     */
    private long mExpireTime = DEFAULT_CACHE_TIME;


    public static class Builder{

        private Context appContext;
        private String cacheDirName = DEFAULT_CACHE_DIR;
        private long cacheSize = DEFAULT_CACHE_SIZE;
        private boolean expireTimeMode = false;
        private long expireTime = DEFAULT_CACHE_TIME;

        public Builder with(Context appContext){
            this.appContext = appContext;
            return this;
        }

        public Builder cacheDirName(String cacheDirName){
            this.cacheDirName = cacheDirName;
            return this;
        }
        public Builder cacheSize(long cacheSize){
            this.cacheSize = cacheSize;
            return this;
        }
        public Builder expireTimeMode(boolean expireTimeMode){
            this.expireTimeMode = expireTimeMode;
            return this;
        }
        public Builder expireTime(long expireTime){
            this.expireTime = expireTime;
            return this;
        }

        public CacheManager build(){
            if (this.appContext == null){
                throw new RuntimeException("must call new CacheManager.Builder().with(this).build() in application");
            }
            return sCacheManager = new CacheManager(this);
        }
    }



    /**
     * Adds or replaces a String to the cache.
     * @param key Cache key
     * @param value Add or replaces String
     */
    public void putString(Cacheable.CACHETYPE type, String key, String value){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.putString(key, value);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.putString(key, value);
            }
        }
    }

    /**
     * Retrieves a String from the cache.
     *
     * @param key Cache key
     * @return A String or null in the event of a cache miss
     */
    public String getString(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getString(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getString(key);
            }
        }
        return "";
    }

    /**
     * Retrieves a String from the cache.
     * @param key Cache key
     * @param defValue Default String if cache miss
     * @return A String in the event of a cache miss
     */
    public String getString(Cacheable.CACHETYPE type, String key, String defValue){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getString(key, defValue);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getString(key, defValue);
            }
        }
        return "";
    }


    /**
     * Adds or replaces an Int to the cache.
     * @param key Cache key
     * @param value Add or replaces Int
     */
    public void putInt(Cacheable.CACHETYPE type, String key, int value){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.putInt(key, value);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.putInt(key, value);
            }
        }
    }

    /**
     * Retrieves an Int from the cache.
     *
     * @param key Cache key
     * @return A Int or 0 in the event of a cache miss
     */
    public int getInt(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getInt(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getInt(key);
            }
        }
        return 0;
    }

    /**
     * Retrieves an Int from the cache.
     * @param key Cache key
     * @param defValue Default Int if cache miss
     * @return A Int in the event of a cache miss
     */
    public int getInt(Cacheable.CACHETYPE type, String key, int defValue){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getInt(key, defValue);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getInt(key, defValue);
            }
        }
        return 0;
    }

    /**
     * Adds or replaces a Long to the cache.
     * @param key Cache key
     * @param value Add or replaces Long
     */
    public void putLong(Cacheable.CACHETYPE type, String key, long value){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.putLong(key, value);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.putLong(key, value);
            }
        }
    }

    /**
     * Retrieves a Long from the cache.
     *
     * @param key Cache key
     * @return A Long or 0L in the event of a cache miss
     */
    public long getLong(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getLong(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getLong(key);
            }
        }
        return 0L;
    }

    /**
     * Retrieves a Long from the cache.
     * @param key Cache key
     * @param defValue Default Long if cache miss
     * @return A Long in the event of a cache miss
     */
    public long getLong(Cacheable.CACHETYPE type, String key, long defValue){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getLong(key, defValue);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getLong(key, defValue);
            }
        }
        return 0L;
    }

    /**
     * Adds or replaces a Float to the cache.
     * @param key Cache key
     * @param value Add or replaces Float
     */
    public void putFloat(Cacheable.CACHETYPE type, String key, float value){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.putFloat(key, value);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.putFloat(key, value);
            }
        }
    }

    /**
     * Retrieves a Float from the cache.
     *
     * @param key Cache key
     * @return A Float or 0F in the event of a cache miss
     */
    public float getFloat(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getFloat(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getFloat(key);
            }
        }
        return 0f;
    }

    /**
     * Retrieves a Float from the cache.
     * @param key Cache key
     * @param defValue Default Float if cache miss
     * @return A Float in the event of a cache miss
     */
    public float getFloat(Cacheable.CACHETYPE type, String key, float defValue){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getFloat(key, defValue);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getFloat(key, defValue);
            }
        }
        return 0f;
    }

    /**
     * Adds or replaces a Double to the cache.
     * @param key Cache key
     * @param value Add or replaces Double
     */
    public void putDouble(Cacheable.CACHETYPE type, String key, double value){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.putDouble(key, value);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.putDouble(key, value);
            }
        }
    }

    /**
     * Retrieves a Double from the cache.
     *
     * @param key Cache key
     * @return A Double or 0D in the event of a cache miss
     */
    public double getDouble(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getDouble(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getDouble(key);
            }
        }
        return 0d;
    }

    /**
     * Retrieves a Double from the cache.
     * @param key Cache key
     * @param defValue Default Double if cache miss
     * @return A Double in the event of a cache miss
     */
    public double getDouble(Cacheable.CACHETYPE type, String key, double defValue){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getDouble(key, defValue);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getDouble(key, defValue);
            }
        }
        return 0d;
    }

    /**
     * Adds or replaces a Boolean to the cache.
     * @param key Cache key
     * @param value Add or replaces Boolean
     */
    public void putBoolean(Cacheable.CACHETYPE type, String key, boolean value){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.putBoolean(key, value);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.putBoolean(key, value);
            }
        }
    }

    /**
     * Retrieves a Boolean from the cache.
     *
     * @param key Cache key
     * @return A Boolean or false in the event of a cache miss
     */
    public boolean getBoolean(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getBoolean(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getBoolean(key);
            }
        }
        return false;
    }

    /**
     * Retrieves a Boolean from the cache.
     * @param key Cache key
     * @param defValue Default Boolean if cache miss
     * @return A Boolean in the event of a cache miss
     */
    public boolean getBoolean(Cacheable.CACHETYPE type, String key, boolean defValue){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getBoolean(key, defValue);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getBoolean(key, defValue);
            }
        }
        return false;
    }

    /**
     * Adds or replaces a T to the cache.
     * @param key Cache key
     * @param t Add or replaces T
     */
    public <T> void put(Cacheable.CACHETYPE type, String key, T t){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.put(key, t);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.put(key, t);
            }
        }
    }

    /**
     * Retrieves a T from the cache.
     * @param key Cache key
     * @param t Object class
     * @return A T or null in the event of a cache miss
     */
    public <T> T get(Cacheable.CACHETYPE type, String key, Class<T> t){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
               return mDiskCacheManager.get(key, t);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.get(key, t);
            }
        }
        return null;
    }

    /**
     * Retrieves a T from the cache.
     * @param key Cache key
     * @param t Object type
     * @return A T or null in the event of a cache miss
     */
    public <T> T get(Cacheable.CACHETYPE type, String key, Type t){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.get(key, t);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.get(key, t);
            }
        }
        return null;
    }

    /**
     * Removes from the cache.
     *
     * @param key Cache key
     */
    public void remove(Cacheable.CACHETYPE type, String key){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.remove(key);
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.remove(key);
            }
        }
    }

    /**
     * Empties the cache.
     */
    public void clear(Cacheable.CACHETYPE type){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                mDiskCacheManager.clear();
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                mSharePrefersManager.clear();
            }
        }
    }

    /**
     * Get Cache Size
     * @return bytes.length
     */
    public long getCacheSize(Cacheable.CACHETYPE type){
        if (type == Cacheable.CACHETYPE.DISK){
            if (mDiskCacheManager != null){
                return mDiskCacheManager.getCacheSize();
            }
        }else if (type == Cacheable.CACHETYPE.SHARE_PREFS){
            if (mSharePrefersManager != null){
                return mSharePrefersManager.getCacheSize();
            }
        }
        return 0;
    }
}