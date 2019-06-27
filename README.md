#简介
使用编译时注解,简化 SharedPreference和文件缓存 的使用.
支持int,long,float,double,boolean,String,自定义对象,和List<T>类型,
***暂不支持其他的类型.变量的访问权限不可以是 Private or static***

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
    api 'com.github.laiying.cache:cache-api:1.0.0'
    annotationProcessor 'com.github.laiying.cache:cache-compiler:1.0.0'
    ...
}


2. 初始化SDK
``` java
if (BuildConfig.DEBUG){
   CacheInject.setDebug(true);// 打印日志
}
//init cache
DiskCacheManager.getInstance().init(this); 尽可能早，推荐在Application中初始化
SharePrefersManager.getInstance().init(this, "Cache");尽可能早，推荐在Application中初始化

```

3. 添加注解
``` java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

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
待补充
```


#作者
laiying email:lai_ying@163.com


