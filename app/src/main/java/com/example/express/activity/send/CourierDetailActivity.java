package com.example.express.activity.send;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.view.CollapsibleTextView;
import com.example.express.view.RoundImageView;
import com.example.express.view.StarBarView;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/31 17:16
 * 修改人：xutework
 * 修改时间：2015/7/31 17:16
 * 修改备注：
 */
public class CourierDetailActivity extends BaseActivity {

    private TextView tv_follow_flag;
    private LinearLayout ll_call;
    private TextView tv_phone_no;
    private RoundImageView riv_user_icon;
    private TextView tv_name;
    private TextView tv_company;
    private StarBarView stv_star;
    private TextView tv_service_count;
    private TextView tv_fav_count;
    private TextView tv_bad_count;
    private LinearLayout ll_rules;
    private TextView tv_service_time;
    private CollapsibleTextView ctv_scope;
    private TextView tv_comment_user_time;
    private TextView tv_comment_content;
    private StarBarView stv_comment_star;

    /*
    测试字符串
     */
    private String test_str = "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing " +
            "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing " +
            "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing " +
            "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courier_detail);
        initTop();

        initViews();
    }

    private void initViews() {
        tv_follow_flag = (TextView) findViewById(R.id.tv_follow_flag);
        ll_call = (LinearLayout) findViewById(R.id.ll_call);
        tv_phone_no = (TextView) findViewById(R.id.tv_phone_no);
        riv_user_icon = (RoundImageView) findViewById(R.id.riv_user_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_company = (TextView) findViewById(R.id.tv_company);
        stv_star = (StarBarView) findViewById(R.id.stv_star);
        tv_service_count = (TextView) findViewById(R.id.tv_service_count);
        tv_fav_count = (TextView) findViewById(R.id.tv_fav_count);
        tv_bad_count = (TextView) findViewById(R.id.tv_bad_count);
        ll_rules = (LinearLayout) findViewById(R.id.ll_rules);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        ctv_scope = (CollapsibleTextView) findViewById(R.id.ctv_scope);
        tv_comment_user_time = (TextView) findViewById(R.id.tv_comment_user_time);
        tv_comment_content = (TextView) findViewById(R.id.tv_comment_content);
        stv_comment_star = (StarBarView) findViewById(R.id.stv_comment_star);

        //设置收派范围文字
        ctv_scope.setDesc(test_str, TextView.BufferType.NORMAL);

        //设置星级图案
        stv_star.setBgImgResId(R.drawable.star, R.drawable.star_blank);
        stv_comment_star.setBgImgResId(R.drawable.star, R.drawable.star_blank);
        //根据分数设置显示几颗星
        try {
            stv_star.setImgCount(3, 2, false);
            stv_comment_star.setImgCount(5, 4, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stv_star.show(this, dip2px(this, 20), dip2px(this, 20));
        stv_comment_star.show(this, dip2px(this, 15), dip2px(this, 15));

        tv_follow_flag.setOnClickListener(this);
        ll_call.setOnClickListener(this);
        ll_rules.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_follow_flag:
                break;

            case R.id.ll_call:
                break;

            case R.id.ll_rules:
                break;

            default:
                break;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
