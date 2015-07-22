package com.example.express;

import java.util.LinkedList;
import java.util.List;

import com.boredream.volley.BDVolley;

import android.app.Activity;
import android.app.Application;

public class BaseApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();

	private static BaseApplication instance;
	public static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		BDVolley.init(this);
		
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
}
