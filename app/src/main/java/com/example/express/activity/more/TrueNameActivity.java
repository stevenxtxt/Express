package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/27 11:40
 * 修改人：xutework
 * 修改时间：2015/7/27 11:40
 * 修改备注：
 */
public class TrueNameActivity extends BaseActivity{
    private EditText et_true_name;
    private Button btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.true_name);
        initTop();
        setTitle("真实姓名");
        initViews();
    }

    private void initViews() {
        et_true_name = (EditText) findViewById(R.id.et_true_name);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_confirm) {

        }
    }
}
