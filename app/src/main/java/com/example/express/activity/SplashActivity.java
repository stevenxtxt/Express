package com.example.express.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 17:06
 * 修改人：xutework
 * 修改时间：2015/7/29 17:06
 * 修改备注：
 */
public class SplashActivity extends BaseActivity {

    private ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        iv_splash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_splash) {
            launch(MainTabActivity.class);
            finish();
        }
    }
}
