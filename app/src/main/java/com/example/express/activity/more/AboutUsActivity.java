package com.example.express.activity.more;

import android.os.Bundle;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 14:34
 * 修改人：xutework
 * 修改时间：2015/7/29 14:34
 * 修改备注：
 */
public class AboutUsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        initTop();
        setTitle("关于我们");
    }
}
