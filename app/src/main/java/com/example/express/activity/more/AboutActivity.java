package com.example.express.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

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
        rl_about_us.setOnClickListener(this);
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
