package com.example.express;

import java.util.LinkedList;
import java.util.List;

import com.boredream.volley.BDVolley;
import com.db.MyDb;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Preference;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

public class BaseApplication extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();

    private static BaseApplication instance;

    private MyDb myDb;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BDVolley.init(this);
        instance = this;
        myDb = MyDb.create(this, CommonConstants.DB_NAME, true);

        // 初始化 异步加载图片
        initImageLoader(getApplicationContext());
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public void setExpressLat(Double latitude) {
        Preference.putDouble("ExpressLat", latitude);
    }

    public void setExpressLng(Double longitude) {
        Preference.putDouble("ExpressLng", longitude);
    }

    public Double getExpressLat() {

        return Preference.getDouble("ExpressLat");
    }

    public Double getExpressLng() {

        return Preference.getDouble("ExpressLng");
    }

    public void setExpressCity(String city) {
        Preference.putString("ExpressCity", city);
    }

    public String getExpressCity() {
        return Preference.getString("ExpressCity");
    }

    public void setExpressAddress(String address) {
        Preference.putString("ExpressAddress", address);
    }

    public String getExpressAddress() {
        return Preference.getString("ExpressAddress");
    }

    public MyDb getMyDb() {
        return myDb;
    }

    private void initImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这个。还有其他设置
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3)
                        // default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                        // .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (2 * 1024 * 1024))
                .memoryCacheSizePercentage(13)
                        // default
                        // .discCache(new UnlimitedDiscCache(cacheDir))
                        // default
                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
