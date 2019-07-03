package com.strod.cache.compiler.utils;

/**
 * Created by laiying on 2019/3/25.
 */
public class CacheConsts {

    public static final String PROJECT = "Cache";

    // Log
    static final String PREFIX_OF_LOGGER = PROJECT + "::Compiler ";

    // Java type
    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";

    // System interface
    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "android.app.Fragment";
    public static final String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    public static final String SERVICE = "android.app.Service";
    public static final String PARCELABLE = "android.os.Parcelable";
    public static final String LIST = "java.util.List";


    //java write code
    public static final String INNER_LINK = "_";

    public static final String SUFFIX = "$$CACHE";

    //api pkg
    public static final String API_PKG = "com.strod.cache.api";
    public static final String API_CACHE_MANAGER = "CacheManager";
    public static final String API_GETINSTANCE = "getInstance";
    public static final String CACHE_BINDER = "CacheBinder";
    public static final String CACHE_BINDER_METHOD = "saveCache";
    public static final String CACHE_READ = "read";
    public static final String CACHE_WRITE = "write";

}
