package com.example.express.activity.more;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 13:52
 * 修改人：xutework
 * 修改时间：2015/7/29 13:52
 * 修改备注：
 */
public class AboutActivity extends BaseActivity {

    private RelativeLayout rl_about_us;
    private TextView tv_version_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        initTop();
        setTitle("关于");

        initViews();
    }

    private void initViews() {
        rl_about_us = (RelativeLayout) findViewById(R.id.rl_about_us);
        tv_version_no = (TextView) findViewById(R.id.tv_version_no);
        rl_about_us.setOnClickListener(this);

        tv_version_no.setText("V" + getVersionName());
    }

    /*
    获取版本号
     */
    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "exception";
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.rl_about_us) {
            Intent intent = new Intent(AboutActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
    }
}
