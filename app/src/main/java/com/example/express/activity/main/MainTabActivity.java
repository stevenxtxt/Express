package com.example.express.activity.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.FragmentController;

public class MainTabActivity extends BaseActivity implements
        OnCheckedChangeListener {

    private RadioGroup rg_tab;
    private RadioButton rb_query;
    private RadioButton rb_send;
    private RadioButton rb_my;
    private RadioButton rb_more;

    private FragmentController fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_tab);

        initMap(savedInstanceState);

        initView();

        initData();

        showDialog("正在获取定位信息...");

    }

    private void initView() {
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        rb_query = (RadioButton) findViewById(R.id.rb_query);
        rb_send = (RadioButton) findViewById(R.id.rb_send);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        rb_more = (RadioButton) findViewById(R.id.rb_more);
        rg_tab.setOnCheckedChangeListener(this);
    }

    private void initData() {
        fc = new FragmentController(this, R.id.fl_content);
        rb_query.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_query:
                fc.showHomeFragment();
                break;
            case R.id.rb_send:
                fc.showLoginFragment();
                break;
            case R.id.rb_my:
                fc.showShopFragment();
                break;
            case R.id.rb_more:
                fc.showUserFragment();
                break;

            default:
                break;
        }
    }

}
