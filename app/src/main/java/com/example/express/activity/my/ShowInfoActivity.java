package com.example.express.activity.my;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/23 14:52
 * 修改人：xutework
 * 修改时间：2015/7/23 14:52
 * 修改备注：
 */
public class ShowInfoActivity extends BaseActivity {

	private Intent intent;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_info);

        intent = getIntent();
        type = intent.getStringExtra("type");
        initTop();
        if (type.equals("protocol")) {
            setTitle("快递网用户协议");
        } else if (type.equals("information")) {
            setTitle("注册说明");
        }
    }
}
