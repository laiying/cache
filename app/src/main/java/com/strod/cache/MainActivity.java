package com.strod.cache;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.strod.cache.annotation.Cacheable;
import com.strod.cache.api.CacheBinder;
import com.strod.cache.api.CacheInject;
import com.strod.library.ModelTest1Activity;
import com.strod.library.model.Response;
import com.strod.library.model.TestModel;
import com.strod.library.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private CacheBinder mCacheBinder;

    //int
    @Cacheable(key = "age", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    protected int mAge;

    @Cacheable(key = "diskAge", cacheType = Cacheable.CACHETYPE.DISK)
    public int mdiskAge;

    //boolean
    @Cacheable(key = "boolean", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public boolean booleanData;

    @Cacheable(key = "diskBoolean", cacheType = Cacheable.CACHETYPE.DISK)
    public boolean diskBooleanData;

    //long
    @Cacheable(key = "long", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public long longData;

    @Cacheable(key = "diskLong", cacheType = Cacheable.CACHETYPE.DISK)
    public long diskLongData;

    //float
    @Cacheable(key = "float", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public float floatData;

    @Cacheable(key = "diskFloat", cacheType = Cacheable.CACHETYPE.DISK)
    public float diskFloatData;

    //double
    @Cacheable(key = "double", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public double doubleData;

    @Cacheable(key = "diskDouble", cacheType = Cacheable.CACHETYPE.DISK)
    public double diskDoubleData;

    //string
    @Cacheable(key = "name", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    public String name;

    @Cacheable(key = "diskName", cacheType = Cacheable.CACHETYPE.DISK)
    public String diskName;

    //Object implements Parcelable
    @Cacheable(key = "user", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    User mUser;

    @Cacheable(key = "diskUser", cacheType = Cacheable.CACHETYPE.DISK)
    User mdiskUser;

    //Object
    @Cacheable(key = "testModel", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    TestModel testModel;

    @Cacheable(key = "diskTestModel", cacheType = Cacheable.CACHETYPE.DISK)
    TestModel diskTestModel;

    //Object<T>
    @Cacheable(key = "mResponseUser", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    Response<User> mResponseUser;

    @Cacheable(key = "mDiskResponseUser", cacheType = Cacheable.CACHETYPE.DISK)
    Response<User> mDiskResponseUser;

    //List<T>
    @Cacheable(key = "mUserList", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    List<User> mUserList;
    //ArrayList<T>
    @Cacheable(key = "mDiskUserList", cacheType = Cacheable.CACHETYPE.DISK)
    ArrayList<User> mDiskUserList;

    //List<Object<T>>
    @Cacheable(key = "mResponseUserList", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    List<Response<User>> mResponseUserList;

    @Cacheable(key = "mDiskResponseUserList", cacheType = Cacheable.CACHETYPE.DISK)
    List<Response<User>> mDiskResponseUserList;

    //Map
    @Cacheable(key = "mUserMap", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    Map<String, User> mUserMap;
    //HashMap
    @Cacheable(key = "mDiskUserMap", cacheType = Cacheable.CACHETYPE.DISK)
    HashMap<String, User> mDiskUserMap;

    //Map<K, List<T>>
    @Cacheable(key = "mUserMapList", cacheType = Cacheable.CACHETYPE.SHARE_PREFS)
    Map<String, List<User>> mUserMapList;

    //LinkedHashMap<K, List<T>>
    @Cacheable(key = "mDiskUserMapList", cacheType = Cacheable.CACHETYPE.DISK)
    LinkedHashMap<String, List<User>> mDiskUserMapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注入读取操作
        mCacheBinder = CacheInject.read(this);

        findViewById(R.id.extends_base).setOnClickListener(this);
        findViewById(R.id.extends_base_noimpl).setOnClickListener(this);
        findViewById(R.id.model_sample).setOnClickListener(this);

        Log.d(TAG, "mAge: "+ mAge);
        Log.d(TAG, "mdiskAge: "+ mdiskAge);

        Log.d(TAG, "booleanData: "+ booleanData);
        Log.d(TAG, "diskBooleanData: "+ diskBooleanData);

        Log.d(TAG, "longData: "+ longData);
        Log.d(TAG, "diskLongData: "+ diskLongData);

        Log.d(TAG, "floatData: "+ floatData);
        Log.d(TAG, "diskFloatData: "+ diskFloatData);

        Log.d(TAG, "doubleData: "+ doubleData);
        Log.d(TAG, "diskDoubleData: "+ diskDoubleData);

        Log.d(TAG, "name: "+ name);
        Log.d(TAG, "diskName: "+ diskName);


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

        if (testModel != null){
            Log.d(TAG, "testModel:"+testModel.toString());
        }else {
            Log.d(TAG, "testModel: null");
        }
        if (diskTestModel!= null){
            Log.d(TAG, "diskTestModel: "+ diskTestModel.toString());
        }else {
            Log.d(TAG, "diskTestModel: null");
        }

        if (mResponseUser != null){
            Log.d(TAG, "mResponseUser:"+mResponseUser.toString());
        }else {
            Log.d(TAG, "mResponseUser: null");
        }

        if (mDiskResponseUser != null){
            Log.d(TAG, "mDiskResponseUser:"+mDiskResponseUser.toString());
        }else {
            Log.d(TAG, "mDiskResponseUser: null");
        }
        if (mResponseUserList != null){
            Log.d(TAG, "mResponseUserList:"+mResponseUserList.toString());
        }else {
            Log.d(TAG, "mResponseUserList: null");
        }

        if(mUserList != null){
            Log.d(TAG, "mUserList:"+mUserList.toString());
        }else {
            Log.d(TAG, "mUserList: null");
        }
        if(mDiskUserList != null){
            Log.d(TAG, "mDiskUserList:"+mDiskUserList.toString());
        }else {
            Log.d(TAG, "mDiskUserList: null");
        }

        if (mDiskResponseUserList != null){
            Log.d(TAG, "mDiskResponseUserList:"+mDiskResponseUserList.toString());
        }else {
            Log.d(TAG, "mDiskResponseUserList: null");
        }
        if (mUserMap != null){
            Log.d(TAG, "mUserMap:"+mUserMap.toString());
        }else {
            Log.d(TAG, "mUserMap: null");
        }
        if (mDiskUserMap != null){
            Log.d(TAG, "mDiskUserMap:"+mDiskUserMap.toString());
        }else {
            Log.d(TAG, "mDiskUserMap: null");
        }
        if (mUserMapList != null){
            Log.d(TAG, "mUserMapList:"+mUserMapList.toString());
        }else {
            Log.d(TAG, "mUserMapList: null");
        }
        if (mDiskUserMapList != null){
            Log.d(TAG, "mDiskUserMapList:"+mDiskUserMapList.toString());
        }else {
            Log.d(TAG, "mDiskUserMapList: null");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.extends_base:
                startActivity(new Intent(this, Test1Activity.class));
                break;
            case R.id.extends_base_noimpl:
                startActivity(new Intent(this, Test2Activity.class));
                break;
            case R.id.model_sample:
                startActivity(new Intent(this, ModelTest1Activity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //注入保存操作
        if (mCacheBinder != null){
            mCacheBinder.saveCache(this);
        }
        super.onDestroy();
    }
}
