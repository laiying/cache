package com.strod.cache.compiler.utils;

/**
 * Created by laiying on 2019/3/25.
 */
public class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
}
