package com.example.express.activity.my;

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
 * 创建时间：2015/7/23 14:52
 * 修改人：xutework
 * 修改时间：2015/7/23 14:52
 * 修改备注：
 */
public class ForgetPasswordTwoActivity extends BaseActivity {
    private EditText et_password;
    private EditText et_confirm_password;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_two);

        initTop();
        setTitle("忘记密码");
        initViews();
    }

    private void initViews() {
        et_password = (EditText) findViewById(R.id.et_password);
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
