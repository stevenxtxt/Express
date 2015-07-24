package com.example.express.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.main.MainTabActivity;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_register;
    private TextView tv_forget_password;

    private String username;
    private String password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initTop();
        setTitle("登录");

        initViews();

    }

    private void initViews() {
        et_password = (EditText) findViewById(R.id.et_password);
        et_username = (EditText) findViewById(R.id.et_username);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);

        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;

            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_forget_password:
                Intent intent1 = new Intent(LoginActivity.this, ForgetPasswordOneActivity.class);
                startActivity(intent1);
                break;

            default:
                break;
        }
    }

    /**
     * 登录方法
     */
    private void login() {
        Map<String, Object> params = new HashMap<String, Object>();
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        params.put("user", username);
        params.put("psw", password);
        BDVolleyHttp.postString("http://www.wshens.com/mobilelogin.html", params, new BDListener<String>() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回失败处理

            }

            @Override
            public void onResponse(String response) {

                //跳转界面
                Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}