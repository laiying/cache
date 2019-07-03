package com.strod.cache.api;

import java.lang.reflect.Type;

/**
 * Created by laiying on 2019/7/2.
 */
public interface ICache {

    /**
     * Adds or replaces a String to the cache.
     * @param key Cache key
     * @param value Add or replaces String
     */
    public void putString(String key, String value);

    /**
     * Retrieves a String from the cache.
     *
     * @param key Cache key
     * @return A String or null in the event of a cache miss
     */
    public String getString(String key);

    /**
     * Retrieves a String from the cache.
     * @param key Cache key
     * @param defValue Default String if cache miss
     * @return A String in the event of a cache miss
     */
    public String getString(String key, String defValue);


    /**
     * Adds or replaces an Int to the cache.
     * @param key Cache key
     * @param value Add or replaces Int
     */
    public void putInt(String key, int value);

    /**
     * Retrieves an Int from the cache.
     *
     * @param key Cache key
     * @return A Int or 0 in the event of a cache miss
     */
    public int getInt(String key);

    /**
     * Retrieves an Int from the cache.
     * @param key Cache key
     * @param defValue Default Int if cache miss
     * @return A Int in the event of a cache miss
     */
    public int getInt(String key, int defValue);

    /**
     * Adds or replaces a Long to the cache.
     * @param key Cache key
     * @param value Add or replaces Long
     */
    public void putLong(String key, long value);

    /**
     * Retrieves a Long from the cache.
     *
     * @param key Cache key
     * @return A Long or 0L in the event of a cache miss
     */
    public long getLong(String key);

    /**
     * Retrieves a Long from the cache.
     * @param key Cache key
     * @param defValue Default Long if cache miss
     * @return A Long in the event of a cache miss
     */
    public long getLong(String key, long defValue);

    /**
     * Adds or replaces a Float to the cache.
     * @param key Cache key
     * @param value Add or replaces Float
     */
    public void putFloat(String key, float value);

    /**
     * Retrieves a Float from the cache.
     *
     * @param key Cache key
     * @return A Float or 0F in the event of a cache miss
     */
    public float getFloat(String key);

    /**
     * Retrieves a Float from the cache.
     * @param key Cache key
     * @param defValue Default Float if cache miss
     * @return A Float in the event of a cache miss
     */
    public float getFloat(String key, float defValue);

    /**
     * Adds or replaces a Double to the cache.
     * @param key Cache key
     * @param value Add or replaces Double
     */
    public void putDouble(String key, double value);

    /**
     * Retrieves a Double from the cache.
     *
     * @param key Cache key
     * @return A Double or 0D in the event of a cache miss
     */
    public double getDouble(String key);

    /**
     * Retrieves a Double from the cache.
     * @param key Cache key
     * @param defValue Default Double if cache miss
     * @return A Double in the event of a cache miss
     */
    public double getDouble(String key, double defValue);

    /**
     * Adds or replaces a Boolean to the cache.
     * @param key Cache key
     * @param value Add or replaces Boolean
     */
    public void putBoolean(String key, boolean value);

    /**
     * Retrieves a Boolean from the cache.
     *
     * @param key Cache key
     * @return A Boolean or false in the event of a cache miss
     */
    public boolean getBoolean(String key);

    /**
     * Retrieves a Boolean from the cache.
     * @param key Cache key
     * @param defValue Default Boolean if cache miss
     * @return A Boolean in the event of a cache miss
     */
    public boolean getBoolean(String key, boolean defValue);

    /**
     * Adds or replaces a T to the cache.
     * @param key Cache key
     * @param t Add or replaces T
     */
    public <T> void put(String key, T t);

    /**
     * Retrieves a T from the cache.
     * @param key Cache key
     * @param t Object class
     * @return A T or null in the event of a cache miss
     */
    public <T> T get(String key, Class<T> t);

    /**
     * Retrieves a T from the cache.
     * @param key Cache key
     * @param t Object type
     * @return A T or null in the event of a cache miss
     */
    public <T> T get(String key, Type t);

    /**
     * Removes from the cache.
     *
     * @param key Cache key
     */
    public void remove(String key);

    /**
     * Empties the cache.
     */
    public void clear();

    /**
     * Get Cache Size
     * @return bytes.length
     */
    public long getCacheSize();
}
