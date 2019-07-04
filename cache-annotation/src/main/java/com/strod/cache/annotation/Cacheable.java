package com.strod.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by laiying on 2019/3/22.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Cacheable {
    /**
     * cache type
     */
    public static enum CACHETYPE{
        DISK,
        SHARE_PREFS;
//        @Deprecated
//        DATA_BASE;//unachieve

        private CACHETYPE() {
        }
    }

    /*public static enum OPTTYPE{
        GET,
        PUT,
        REMOVE,
        CLEAR;
        private OPTTYPE() {
        }
    }*/

    /**
     * read or write
     */
    public static enum RW{
        /**read only*/
        READ_ONLY,
        /**write only*/
        WRITE_ONLY,
        /**read and write*/
        ALL
    }

    /**cache type, default is disk*/
    CACHETYPE cacheType() default CACHETYPE.DISK;

    /**cache key*/
    String key();

    /**opt key, default is get*/
//    OPTTYPE optType() default OPTTYPE.GET;

    /**read or write*/
    RW rw() default RW.ALL;

}
