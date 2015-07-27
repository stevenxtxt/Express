package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/27 10:01
 * 修改人：xutework
 * 修改时间：2015/7/27 10:01
 * 修改备注：
 */
public class PersonalInfoActivity extends BaseActivity {
    private TextView tv_change_password;
    private RelativeLayout rl_username;
    private TextView tv_username;
    private RelativeLayout rl_true_name;
    private TextView tv_true_name;
    private RelativeLayout rl_gender;
    private TextView tv_gender;
    private RelativeLayout rl_phone;
    private TextView tv_phone;
    private RelativeLayout rl_address;
    private TextView tv_address;
    private Button btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        initTop();
        setTitle("个人信息");
        initViews();
    }

    private void initViews() {
        tv_change_password = (TextView) findViewById(R.id.tv_change_password);
        rl_username = (RelativeLayout) findViewById(R.id.rl_username);
        tv_username = (TextView) findViewById(R.id.tv_username);
        rl_true_name = (RelativeLayout) findViewById(R.id.rl_true_name);
        tv_true_name = (TextView) findViewById(R.id.tv_true_name);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        tv_change_password.setOnClickListener(this);
        rl_username.setOnClickListener(this);
        rl_true_name.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_change_password:
                break;

            case R.id.rl_username:
                break;

            case R.id.rl_true_name:
                break;

            case R.id.rl_gender:
                break;

            case R.id.rl_phone:
                break;

            case R.id.rl_address:
                break;

            case R.id.btn_logout:
                break;

            default:
                break;
        }
    }
}
