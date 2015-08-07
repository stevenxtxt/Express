package com.example.express;

import java.util.LinkedList;
import java.util.List;

import com.boredream.volley.BDVolley;
import com.db.MyDb;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Preference;

import android.app.Activity;
import android.app.Application;

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
}
