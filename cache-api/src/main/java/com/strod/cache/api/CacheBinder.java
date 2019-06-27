package com.strod.cache.api;

/**
 * Created by laiying on 2019/6/21.
 */
public interface CacheBinder<T> {
    void saveCache(T t);

    CacheBinder EMPTY = new CacheBinder() {
        @Override
        public void saveCache(Object o) {

        }
    };
//    CacheBinder EMPTY = (object) -> { };
}
