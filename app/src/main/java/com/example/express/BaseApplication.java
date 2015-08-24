package com.example.express;

import java.util.LinkedList;
import java.util.List;

import com.boredream.volley.BDVolley;
import com.db.MyDb;
import com.example.express.bean.LoginUser;
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

    private LoginUser loginUser;

    private String company;
    private String com;
    private String result;
    private Bitmap bitmap;

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

    public void setExpressProvince(String province) {
        Preference.putString("ExpressProvince", province);
    }

    public String getExpressProvince() {
        return Preference.getString("ExpressProvince");
    }

    public void setExpressCity(String city) {
        Preference.putString("ExpressCity", city);
    }

    public String getExpressCity() {
        return Preference.getString("ExpressCity");
    }

    public void setExpressDistrict(String district) {
        Preference.putString("ExpressDistrict", district);
    }

    public String getExpressDistrict() {
        return Preference.getString("ExpressDistrict");
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

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
