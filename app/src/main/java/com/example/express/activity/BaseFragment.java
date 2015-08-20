package com.example.express.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.view.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {

    protected MainTabActivity activity;
    protected CustomProgressDialog customProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainTabActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    protected void launch(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(activity, tarActivity);
        startActivity(intent);
    }

    /**
     * 展示自定义对话框
     *
     * @param str
     */
    public void showCustomDialog(String str) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(activity, str, R.anim.frame);
        }
        customProgressDialog.show();
    }

    /**
     * 隐藏自定义对话框
     */
    public void dismissCustomDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }

    protected void showToast(String msg) {

        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
