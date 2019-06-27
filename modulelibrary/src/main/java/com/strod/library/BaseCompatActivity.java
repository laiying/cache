package com.strod.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.strod.cache.api.CacheBinder;
import com.strod.cache.api.CacheInject;

/**
 * Created by laiying on 2019/6/26.
 */
public class BaseCompatActivity extends AppCompatActivity {

    protected CacheBinder mCacheBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCacheBinder = CacheInject.read(this);
    }

    @Override
    protected void onDestroy() {
        if (mCacheBinder != null){
            mCacheBinder.saveCache(this);
        }
        super.onDestroy();
    }
}
