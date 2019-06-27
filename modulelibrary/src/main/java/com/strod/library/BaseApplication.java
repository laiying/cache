package com.strod.library;

import android.app.Application;

import com.strod.cache.api.CacheInject;
import com.strod.cache.api.DiskCacheManager;
import com.strod.cache.api.SharePrefersManager;
import com.strod.library.model.User;

import java.util.ArrayList;
import java.util.List;

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
        //init cache module
        DiskCacheManager.getInstance().init(this);
        SharePrefersManager.getInstance().init(this, "Cache");

        initTestData();
    }

    /**
     * 初始化一些数据，供测试
     */
    private void initTestData(){
        int age = 10;
        String name = "cache demo";
        String diskName = "Disk cache demo";

        //int
        SharePrefersManager.getInstance().putInt("age", age);
        DiskCacheManager.getInstance().writeCache("diskAge", age);

        //String
        SharePrefersManager.getInstance().putString("name", name);
        DiskCacheManager.getInstance().writeCache("diskName", diskName);

        //long
        SharePrefersManager.getInstance().putLong("long", 99999L);
        DiskCacheManager.getInstance().writeCache("diskLong", 100000L);

        //boolean
        SharePrefersManager.getInstance().putBoolean("boolean", true);
        DiskCacheManager.getInstance().writeCache("diskBoolean", true);

        //float
        SharePrefersManager.getInstance().putFloat("float", 1.0f);
        DiskCacheManager.getInstance().writeCache("diskFloat", 1.5f);

        //double
        SharePrefersManager.getInstance().putDouble("double", 2.0d);
        DiskCacheManager.getInstance().writeCache("diskDouble", 2.0d);

        User user = new User();
        user.setAge(age);
        user.setName(name);
        SharePrefersManager.getInstance().put("user", user);


        user.setName(diskName);
        DiskCacheManager.getInstance().writeCache("diskUser", user);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(new User("hello", 18));
        SharePrefersManager.getInstance().putList("mUserList", userList);
        DiskCacheManager.getInstance().writeCache("mDiskUserList", userList);
    }

    public static Application getApplication(){
        return sInstance;
    }

}
