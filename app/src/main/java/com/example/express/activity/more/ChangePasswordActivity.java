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
 * 创建时间：2015/7/27 13:27
 * 修改人：xutework
 * 修改时间：2015/7/27 13:27
 * 修改备注：
 */
public class ChangePasswordActivity extends BaseActivity {
    private EditText et_old_password;
    private EditText et_new_password;
    private EditText et_confirm_password;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        initTop();
        setTitle("修改密码");
        initViews();
    }

    private void initViews() {
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
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
