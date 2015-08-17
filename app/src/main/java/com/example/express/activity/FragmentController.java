package com.example.express.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.express.activity.more.MoreFragment;
import com.example.express.activity.my.MyFragment;
import com.example.express.activity.query.HomeFragment;
import com.example.express.activity.query.QueryExpressFragment;
import com.example.express.activity.send.SendExpressFragment;

public class FragmentController {

	private int containerId;
	private android.support.v4.app.FragmentManager fm;
	
	private HomeFragment homeFragment;
	private static final String TAG_HOME = "home";
	private MyFragment shopFragment;
	private static final String TAG_SHOP = "shop";
	private MoreFragment userFragment;
	private static final String TAG_USER = "user";
	private SendExpressFragment loginFragment;
	private static final String TAG_LOGIN = "login";
	
	public FragmentController(FragmentActivity activity, int containerId) {
		this.containerId = containerId;
		fm = activity.getSupportFragmentManager();
	}
	
	public void showHomeFragment() {
		hideFragments();
		
		if(homeFragment == null) {
			homeFragment = new HomeFragment();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(containerId, homeFragment, TAG_HOME);
			ft.commit();
		} else {
			FragmentTransaction ft = fm.beginTransaction();
			ft.show(homeFragment);
			ft.commit();
		}
	}
	
	public void showShopFragment() {
		hideFragments();
		
		if(shopFragment == null) {
			shopFragment = new MyFragment();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(containerId, shopFragment, TAG_SHOP);
			ft.commit();
		} else {
			FragmentTransaction ft = fm.beginTransaction();
			ft.show(shopFragment);
			ft.commit();
		}
	}
	
	public void showUserFragment() {
		hideFragments();
		
		if(userFragment == null) {
			userFragment = new MoreFragment();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(containerId, userFragment, TAG_USER);
			ft.commit();
		} else {
			FragmentTransaction ft = fm.beginTransaction();
			ft.show(userFragment);
			ft.commit();
		}
	}
	public void showLoginFragment() {
		hideFragments();
		
		if(loginFragment == null) {
			loginFragment = new SendExpressFragment();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(containerId, loginFragment, TAG_LOGIN);
			ft.commit();
		} else {
			FragmentTransaction ft = fm.beginTransaction();
			ft.show(loginFragment);
			ft.commit();
		}
	}
	public void hideFragments() {
		FragmentTransaction ft = fm.beginTransaction();
		if(homeFragment != null) {
			ft.hide(homeFragment);
		}
		if(shopFragment != null) {
			ft.hide(shopFragment);
		}
		if(userFragment != null) {
			ft.hide(userFragment);
		}
		if(loginFragment != null) {
			ft.hide(loginFragment);
		}
		ft.commit();
	}

}