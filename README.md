#简介
使用编译时注解,简化 SharedPreference和文件缓存 的使用.
支持int,long,float,double,boolean,String,custom Object,Object<T>,List<T> or ArrayList<T>,List<Object<T>>,Map<K,V>,Map<K,List<V>>等
***变量的访问权限不可以是 Private or static***

#### 一、功能介绍
1. **支持多种类型数据缓存，并在目标页面中自动读取和缓存,让你更加专注业务开发**
2. **支持多模块工程使用**





#使用说明(请参考demo)
#### 二、基础功能
0. gradle环境
```
//root build.gradle
dependencies {
    classpath 'com.android.tools.build:gradle:3.3.1'

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
}
repositories {
    google()
    jcenter()
    maven { url "https://jitpack.io" }
}
// gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-4.10.1-all.zip
####其它gradle版本请自行配置

1. 添加依赖和配置
``` gradle
android {
    defaultConfig {
	...
}

dependencies {
    // 替换成最新版本, 需要注意的是api
    // 要与compiler匹配使用，均使用最新版可以保证兼容
    api 'com.github.laiying.cache:cache-api:1.0.1'
    annotationProcessor 'com.github.laiying.cache:cache-compiler:1.0.1'
    ...
}


2. 初始化SDK
``` java
if (BuildConfig.DEBUG){
   CacheInject.setDebug(true);// 打印日志
}

//init cache
new CacheManager.Builder()
        .with(this)
        .cacheDirName("Cache")
        .build();

```

3. 添加注解
``` java
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

    ...
}
```


4. 注入读取和保存操作
``` java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注入读取操作
        mCacheBinder = CacheInject.read(this);
        ...
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
```

5. 添加混淆规则(如果使用了Proguard)
```
-dontwarn com.strod.cache.api.**
-keep class com.strod.cache.annotation.**{*;}
-keep class com.strod.cache.api.**{*;}
```


#作者
laiying email:lai_ying@163.com


