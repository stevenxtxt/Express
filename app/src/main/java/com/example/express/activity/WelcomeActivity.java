package com.example.express.activity;

import android.os.Bundle;
import android.os.Handler;

import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.utils.Preference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, LOAD_DELAY_TIME);
    }
}
