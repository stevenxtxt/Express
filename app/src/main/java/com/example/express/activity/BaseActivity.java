package com.example.express.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Logger;

public abstract class BaseActivity extends Activity implements View.OnClickListener{

    protected String TAG;

    protected BaseApplication application;
    protected SharedPreferences sp;
    protected Intent intent;

    protected Button btn_top_right;
    protected Button btn_top_right_first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        TAG = this.getClass().getSimpleName();
        showLog("onCreate()");

        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
        intent = getIntent();
        application.addActivity(this);

    }

    protected void launch(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    protected void initTop() {
        RelativeLayout btn_back = (RelativeLayout) this.findViewById(R.id.btn_title_btn_back_layout);
        if (null != btn_back) {
            btn_back.setOnClickListener(this);
        }
        btn_top_right = (Button) findViewById(R.id.btn_top_right);
        if (null != btn_top_right) {
            btn_top_right.setOnClickListener(this);
        }
        btn_top_right_first = (Button) findViewById(R.id.btn_top_right_first);
        if (null != btn_top_right_first) {
            btn_top_right_first.setOnClickListener(this);
        }
    }

    public void setTitle(String title) {
        super.setTitle(title);
        ((TextView) this.findViewById(R.id.tv_title_name)).setText(title);
    }

    public void setTitle(int titleId) {
        super.setTitle(titleId);
        ((TextView) this.findViewById(R.id.tv_title_name)).setText(titleId);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_title_btn_back_layout) {
            onBackPressed();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLog("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLog("onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showLog("onDestroy()");

        application.removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        showLog("onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLog("onPause()");
    }

    protected void finishActivity() {

        this.finish();
    }

    protected void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String msg) {

        Logger.show(TAG, msg);
    }
}
