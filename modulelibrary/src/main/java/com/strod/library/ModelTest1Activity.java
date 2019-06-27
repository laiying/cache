package com.strod.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.strod.cache.annotation.Cacheable;
import com.strod.cache.api.CacheBinder;
import com.strod.cache.api.CacheInject;
import com.strod.library.model.User;

import java.util.List;

/**
 * Created by laiying on 2019/6/26.
 */
public class ModelTest1Activity extends AppCompatActivity {

    private static final String TAG = "ModelTest1Activity";

    private CacheBinder mCacheBinder;

    @Cacheable(key = "mUserList", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    List<User> mUserList;

    @Cacheable(key = "mDiskUserList", cacheType = Cacheable.CACHETYPE.DISK)
    List<User> mDiskUserList;

    @Cacheable(key = "user", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    User mUser;

    @Cacheable(key = "diskUser", cacheType = Cacheable.CACHETYPE.DISK)
    User mdiskUser;

    @Cacheable(key = "age", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    protected int mAge;

    @Cacheable(key = "diskAge", cacheType = Cacheable.CACHETYPE.DISK)
    public int mdiskAge;

    @Cacheable(key = "name", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public String name;

    @Cacheable(key = "diskName", cacheType = Cacheable.CACHETYPE.DISK)
    public String diskName;


    @Cacheable(key = "long", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public long longData;

    @Cacheable(key = "diskLong", cacheType = Cacheable.CACHETYPE.DISK)
    public long diskLongData;

    @Cacheable(key = "boolean", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public boolean booleanData;

    @Cacheable(key = "diskBoolean", cacheType = Cacheable.CACHETYPE.DISK)
    public boolean diskBooleanData;

    @Cacheable(key = "float", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public float floatData;

    @Cacheable(key = "diskFloat", cacheType = Cacheable.CACHETYPE.DISK)
    public float diskFloatData;

    @Cacheable(key = "double", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public double doubleData;

    @Cacheable(key = "diskDouble", cacheType = Cacheable.CACHETYPE.DISK)
    public double diskDoubleData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_test1);
        mCacheBinder = CacheInject.read(this);


        if(mUserList != null){
            Log.d(TAG, "mUserList:"+new Gson().toJson(mUserList));
        }else {
            Log.d(TAG, "mUserList: null");
        }
        if(mDiskUserList != null){
            Log.d(TAG, "mDiskUserList:"+new Gson().toJson(mDiskUserList));
        }else {
            Log.d(TAG, "mDiskUserList: null");
        }

        if (mUser != null){
            Log.d(TAG, "mUser:"+mUser.toString());
        }else {
            Log.d(TAG, "mUser: null");
        }
        if (mdiskUser != null){
            Log.d(TAG, "mdiskUser: "+ mdiskUser.toString());
        }else {
            Log.d(TAG, "mdiskUser: null");
        }

        Log.d(TAG, "mAge: "+ mAge);
        Log.d(TAG, "mdiskAge: "+ mdiskAge);

        Log.d(TAG, "name: "+ name);
        Log.d(TAG, "diskName: "+ diskName);

        Log.d(TAG, "longData: "+ longData);
        Log.d(TAG, "diskLongData: "+ diskLongData);

        Log.d(TAG, "booleanData: "+ booleanData);
        Log.d(TAG, "diskBooleanData: "+ diskBooleanData);

        Log.d(TAG, "floatData: "+ floatData);
        Log.d(TAG, "diskFloatData: "+ diskFloatData);

        Log.d(TAG, "doubleData: "+ doubleData);
        Log.d(TAG, "diskDoubleData: "+ diskDoubleData);
    }

    @Override
    protected void onDestroy() {
        if (mCacheBinder != null){
            mCacheBinder.saveCache(this);
        }
        super.onDestroy();
    }
}
