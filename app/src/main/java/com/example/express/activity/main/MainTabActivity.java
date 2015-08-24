package com.example.express.activity.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.FragmentController;
import com.example.express.activity.more.MoreFragment;
import com.example.express.activity.my.MyFragment;
import com.example.express.activity.query.HomeFragment;
import com.example.express.activity.send.SendExpressFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.Timer;
import java.util.TimerTask;

public class MainTabActivity extends BaseActivity implements
        OnCheckedChangeListener {

    private RadioGroup rg_tab;
    private RadioButton rb_query;
    private RadioButton rb_send;
    private RadioButton rb_my;
    private RadioButton rb_more;

    private Drawable query_drawable;
    private Drawable query_drawble_press;
    private Drawable deliever_drawable;
    private Drawable deliever_drawable_press;
    private Drawable my_drawable;
    private Drawable my_drawable_press;
    private Drawable more_drawable;
    private Drawable more_drawable_press;

    private FragmentController fc;
    private HomeFragment homeFragment;
    private MyFragment myFragment;
    private MoreFragment moreFragment;
    private SendExpressFragment sendExpressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main_tab);

        MobclickAgent.updateOnlineConfig(getApplicationContext());

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

        query_drawable = getResources().getDrawable(R.drawable.check_express_icon);
        query_drawble_press = getResources().getDrawable(R.drawable.check_express_press_icon);
        deliever_drawable = getResources().getDrawable(R.drawable.deliver_express_icon);
        deliever_drawable_press = getResources().getDrawable(R.drawable.deliver_express_press_icon);
        my_drawable = getResources().getDrawable(R.drawable.my_express_icon);
        my_drawable_press = getResources().getDrawable(R.drawable.my_express_press_icon);
        more_drawable = getResources().getDrawable(R.drawable.more_information_icon);
        more_drawable_press = getResources().getDrawable(R.drawable.more_information_press_icon);
        query_drawable.setBounds(0, 0, query_drawable.getMinimumWidth(), query_drawable.getMinimumHeight());
        query_drawble_press.setBounds(0, 0, query_drawble_press.getMinimumWidth(), query_drawble_press.getMinimumHeight());
        deliever_drawable.setBounds(0, 0, deliever_drawable.getMinimumWidth(), deliever_drawable.getMinimumHeight());
        deliever_drawable_press.setBounds(0, 0, deliever_drawable_press.getMinimumWidth(), deliever_drawable_press.getMinimumHeight());
        my_drawable.setBounds(0, 0, my_drawable.getMinimumWidth(), my_drawable.getMinimumHeight());
        my_drawable_press.setBounds(0, 0, my_drawable_press.getMinimumWidth(), my_drawable_press.getMinimumHeight());
        more_drawable.setBounds(0, 0, more_drawable.getMinimumWidth(), more_drawable.getMinimumHeight());
        more_drawable_press.setBounds(0, 0, more_drawable_press.getMinimumWidth(), more_drawable_press.getMinimumHeight());
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
                rb_query.setCompoundDrawables(null, query_drawble_press, null, null);
                rb_send.setCompoundDrawables(null, deliever_drawable, null, null);
                rb_more.setCompoundDrawables(null, more_drawable, null, null);
                rb_my.setCompoundDrawables(null, my_drawable, null, null);
                rb_query.setTextColor(getResources().getColor(R.color.blue_top));
                rb_send.setTextColor(Color.parseColor("#666666"));
                rb_more.setTextColor(Color.parseColor("#666666"));
                rb_my.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.rb_send:
                fc.showLoginFragment();
                rb_query.setCompoundDrawables(null, query_drawable, null, null);
                rb_send.setCompoundDrawables(null, deliever_drawable_press, null, null);
                rb_more.setCompoundDrawables(null, more_drawable, null, null);
                rb_my.setCompoundDrawables(null, my_drawable, null, null);
                rb_query.setTextColor(Color.parseColor("#666666"));
                rb_send.setTextColor(getResources().getColor(R.color.blue_top));
                rb_more.setTextColor(Color.parseColor("#666666"));
                rb_my.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.rb_my:
                fc.showShopFragment();
                rb_query.setCompoundDrawables(null, query_drawable, null, null);
                rb_send.setCompoundDrawables(null, deliever_drawable, null, null);
                rb_more.setCompoundDrawables(null, more_drawable, null, null);
                rb_my.setCompoundDrawables(null, my_drawable_press, null, null);
                rb_query.setTextColor(Color.parseColor("#666666"));
                rb_send.setTextColor(Color.parseColor("#666666"));
                rb_more.setTextColor(Color.parseColor("#666666"));
                rb_my.setTextColor(getResources().getColor(R.color.blue_top));
                break;
            case R.id.rb_more:
                fc.showUserFragment();
                rb_query.setCompoundDrawables(null, query_drawable, null, null);
                rb_send.setCompoundDrawables(null, deliever_drawable, null, null);
                rb_more.setCompoundDrawables(null, more_drawable_press, null, null);
                rb_my.setCompoundDrawables(null, my_drawable, null, null);
                rb_query.setTextColor(Color.parseColor("#666666"));
                rb_send.setTextColor(Color.parseColor("#666666"));
                rb_more.setTextColor(getResources().getColor(R.color.blue_top));
                rb_my.setTextColor(Color.parseColor("#666666"));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            String tab_type = intent.getStringExtra("tab_type");
            if (tab_type == null) {
                return;
            }
            if (tab_type.equals("query")) {
                rb_query.setChecked(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
