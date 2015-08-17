package com.example.express.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.view.CustomProgressDialog;

public class BaseFragment extends Fragment {

    protected MainTabActivity activity;
    protected CustomProgressDialog customProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainTabActivity) getActivity();
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
}
