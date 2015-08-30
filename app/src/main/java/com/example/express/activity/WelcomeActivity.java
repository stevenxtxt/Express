package com.example.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolley;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.activity.my.LoginActivity;
import com.example.express.bean.LoginUser;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Logger;
import com.example.express.utils.MD5Util;
import com.example.express.utils.Preference;
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 16:33
 * 修改人：xutework
 * 修改时间：2015/7/29 16:33
 * 修改备注：
 */
public class WelcomeActivity extends BaseActivity {

    private static final long LOAD_DELAY_TIME = 2000;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        username = Preference.getString("username");
        password = Preference.getString("password");

        //判断是否有网络
        if (StringUtils.isNetworkAvailable(this)) {
            //用户名密码为空，表示从未登录
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpToActivity();
                    }
                }, LOAD_DELAY_TIME);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autoLogin();
                    }
                }, 1000);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jumpToActivity();
                    showToast("没有可用的网络连接，请打开蜂窝数据或者wifi");
                }
            }, LOAD_DELAY_TIME);
        }


    }

    private void autoLogin() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.LOGIN + CommonConstants.HTML, params,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean result = obj.optBoolean("result");
                            String reason = obj.optString("reason");
                            Logger.show("Login Reason------->>>>", reason);
                            if (result) {
                                String userId = obj.optString("data");
                                //存储用户信息
                                LoginUser loginUser = new LoginUser();
                                loginUser.setUserId(userId);
                                loginUser.setUsername(username);
                                loginUser.setPassword(MD5Util.md5(password));
//                                loginUser.setPassword(password);
                                BaseApplication.getInstance().setLoginUser(loginUser);
                                jumpToActivity();

                            } else {
                                jumpToActivity();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            jumpToActivity();

                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        jumpToActivity();
                    }
                });
    }

    private void jumpToActivity() {
        if (Preference.getBoolean("isFirstLoad", true)) {
            Preference.putBoolean("isFirstLoad", false);
            launch(SplashActivity.class);
            finish();
        } else {
            Preference.putBoolean("isFirstLoad", false);
            launch(MainTabActivity.class);
            finish();
        }
    }
}
