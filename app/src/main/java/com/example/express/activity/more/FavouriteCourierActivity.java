package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/28 16:29
 * 修改人：xutework
 * 修改时间：2015/7/28 16:29
 * 修改备注：
 */
public class FavouriteCourierActivity extends BaseActivity {

    private RelativeLayout rl_official;
    private LinearLayout ll_no_data;
    private ListView lv_fav_courier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_courier);
        initTop();
        setTitle("收藏的快递员");

        initViews();
    }

    private void initViews() {
        rl_official = (RelativeLayout) findViewById(R.id.rl_official);
        ll_no_data = (LinearLayout) findViewById(R.id.ll_no_data);
        lv_fav_courier = (ListView) findViewById(R.id.lv_fav_courier);
        rl_official.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.rl_official) {

        }
    }
}
