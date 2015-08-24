package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/24 13:21
 * 修改人：xutework
 * 修改时间：2015/8/24 13:21
 * 修改备注：
 */
public class ShowWeiboPicActivity extends BaseActivity {
    private ImageView iv_weibo_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_weibo_pic);
        initTop();
        setTitle("关注微博");
        showToast("请在新浪微博中搜索关注追影网络科技有限公司");
        iv_weibo_bg = (ImageView) findViewById(R.id.iv_weibo_bg);
        iv_weibo_bg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_weibo_bg) {
            showToast("请在新浪微博中搜索关注追影网络科技有限公司");
        }
    }
}
