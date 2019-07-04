package com.strod.library;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.strod.cache.annotation.Cacheable;
import com.strod.cache.api.CacheInject;
import com.strod.cache.api.CacheManager;
import com.strod.library.model.Response;
import com.strod.library.model.TestModel;
import com.strod.library.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laiying on 2019/3/18.
 */
public class BaseApplication extends Application {
    protected static Application sInstance;



    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        if (BuildConfig.DEBUG){
            CacheInject.setDebug(true);
        }
        //init cache
        new CacheManager.Builder()
                .with(this)//必须设置
                .diskCacheDirName("diskCache")//可选,默认为"ContentCache";设置硬盘缓存文件夹的名字
                .sharePrefsName("Cache")//可选,默认为"ContentCache";设置sharePrefs文件的名字
                .cacheSize(20 * 1024 * 1024)//可选,默认为10MB;设置硬盘缓存大小
                .expireTimeMode(true)//可选，默认为false;设置过期模式
                .expireTime(7 * 24 * 60 * 60 * 1000)//可选，默认为7天;设置自动过期时间，必须expireTimeMode(true)生效
                .appVersion(getAppVersion(this))//可选，默认为1;如果设置当前app versionCode，则升级版本时，硬盘缓存会清空
                .build();

        initTestData();
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
     * 初始化一些数据，供测试
     */
    private void initTestData(){
        int age = 10;
        String name = "cache demo";
        String diskName = "Disk cache demo";

        //int
        CacheManager.getInstance().putInt(Cacheable.CACHETYPE.SHARE_PREFS,"age", age);
        CacheManager.getInstance().putInt(Cacheable.CACHETYPE.DISK,"diskAge", age);

        //boolean
        CacheManager.getInstance().putBoolean(Cacheable.CACHETYPE.SHARE_PREFS,"boolean", true);
        CacheManager.getInstance().putBoolean(Cacheable.CACHETYPE.DISK,"diskBoolean", true);

        //long
        CacheManager.getInstance().putLong(Cacheable.CACHETYPE.SHARE_PREFS,"long", 99999L);
        CacheManager.getInstance().putLong(Cacheable.CACHETYPE.DISK,"diskLong", 100000L);

        //float
        CacheManager.getInstance().putFloat(Cacheable.CACHETYPE.SHARE_PREFS,"float", 1.0f);
        CacheManager.getInstance().putFloat(Cacheable.CACHETYPE.DISK,"diskFloat", 1.5f);

        //double
        CacheManager.getInstance().putDouble(Cacheable.CACHETYPE.SHARE_PREFS,"double", 2.0d);
        CacheManager.getInstance().putDouble(Cacheable.CACHETYPE.DISK,"diskDouble", 3.0d);

        //String
        CacheManager.getInstance().putString(Cacheable.CACHETYPE.SHARE_PREFS,"name", name);
        CacheManager.getInstance().putString(Cacheable.CACHETYPE.DISK,"diskName", diskName);

        //Object implements Parcelable
        User user = new User();
        user.setAge(age);
        user.setName(name);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"user", user);

        user.setName(diskName);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"diskUser", user);

        //Object
        TestModel testModel = new TestModel();
        testModel.setName("TestModel");
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"testModel", testModel);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"diskTestModel", testModel);

        //Object<T>
        Response<User> response1 = new Response<User>(1, "sucess", user);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"mResponseUser", response1);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"mDiskResponseUser", response1);

        //List
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(new User("hello", 18));
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"mUserList", userList);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"mDiskUserList", userList);

        //List<Object<T>>
        List<Response<User>> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response1);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"mResponseUserList", responseList);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"mDiskResponseUserList", responseList);

        //Map<K,V>
        Map<String, User> mUserMap = new HashMap<>();
        mUserMap.put("user1", user);
        mUserMap.put("user2", new User("map", 18));
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"mUserMap", mUserMap);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"mDiskUserMap", mUserMap);

        //Map<K, List<V>>
        List<User> userList2 = new ArrayList<>();
        userList2.add(user);
        userList2.add(new User("userList2", 18));
        Map<String, List<User>> mUserMapList = new HashMap<>();
        mUserMapList.put("userList1", userList);
        mUserMapList.put("userList2", userList2);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.SHARE_PREFS,"mUserMapList", mUserMapList);
        CacheManager.getInstance().put(Cacheable.CACHETYPE.DISK,"mDiskUserMapList", mUserMapList);

    }

    public static Application getApplication(){
        return sInstance;
    }

}
