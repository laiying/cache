package com.strod.cache;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.strod.cache.annotation.Cacheable;
import com.strod.library.BaseCompatActivity;
import com.strod.library.model.User;

import java.util.List;
import java.util.Random;

/**
 * Created by laiying on 2019/6/26.
 */
public class Test1Activity extends BaseCompatActivity implements View.OnClickListener {

    private static final String TAG = "Test1Activity";

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
        setContentView(R.layout.activity_test1);

        findViewById(R.id.update_value).setOnClickListener(this);

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
    public void onClick(View v) {
        //update_value
        switch (v.getId()){
            case R.id.update_value:
                updateValue();
                break;
        }
    }

    private void updateValue(){
        int randomInt = new Random().nextInt();
        long randomLong = new Random().nextLong();
        boolean randomBoolean = new Random().nextBoolean();
        float randomFloat = new Random().nextFloat();
        double randomDouble = new Random().nextDouble();

        if (mUserList != null){
            mUserList.add(new User("Test1 add", randomInt));
        }
        if (mDiskUserList != null){
            mDiskUserList.add(new User("Test1 add Disk", randomInt));
        }

        if (mUser != null){
            mUser.setName("Test1 udpate");
            mUser.setAge(randomInt);
        }
        if (mdiskUser != null){
            mdiskUser.setName("Test1 disk udpate");
            mdiskUser.setAge(randomInt);
        }

        name = "Test1 update"+ randomInt;
        diskName = "Test1 disk update"+ randomInt;

        mAge = randomInt;
        mdiskAge = randomInt;

        longData = randomLong;
        diskLongData = randomLong;

        booleanData = randomBoolean;
        diskBooleanData = randomBoolean;

        floatData = randomFloat;
        diskFloatData = randomFloat;

        doubleData = randomDouble;
        diskDoubleData = randomDouble;

    }
}
