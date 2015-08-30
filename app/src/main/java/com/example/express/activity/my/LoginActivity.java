package com.example.express.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.bean.LoginUser;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.MD5Util;
import com.example.express.utils.Preference;
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity {
    private ImageView iv_login_icon;
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
        iv_login_icon = (ImageView) findViewById(R.id.iv_login_icon);
        et_password = (EditText) findViewById(R.id.et_password);
        et_username = (EditText) findViewById(R.id.et_username);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);

        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_register.setOnClickListener(this);

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                iv_login_icon.setBackgroundResource(R.drawable.login_close_icon);
                if (et_password.getText().toString().trim().length() == 0) {
                    iv_login_icon.setBackgroundResource(R.drawable.login_icon);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                iv_login_icon.setBackgroundResource(R.drawable.login_icon);
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (StringUtils.isEmpty(username)) {
                    showToast("请输入用户名");
                    return;
                }
                if (StringUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                login(username, password);
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
    private void login(final String username, final String password) {
        showCustomDialog("正在登录...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", MD5Util.md5(password));
//        params.put("password", password);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.LOGIN + CommonConstants.HTML, params,
                new BDListener<String>() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回失败处理
                dismissCustomDialog();
            }

            @Override
            public void onResponse(String response) {
                dismissCustomDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean result = obj.optBoolean("result");
                    String reason = obj.optString("reason");
                    if (result) {
                        //获取userId
                        String userId = obj.optString("data");
                        //将用户名密码存入sp
                        Preference.putString("username", username);
                        Preference.putString("password", MD5Util.md5(password));
//                        Preference.putString("password", password);
                        //存储用户信息
                        LoginUser loginUser = new LoginUser();
                        loginUser.setUserId(userId);
                        loginUser.setUsername(username);
                        loginUser.setPassword(MD5Util.md5(password));
//                        loginUser.setPassword(password);
                        BaseApplication.getInstance().setLoginUser(loginUser);
                        Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
                        intent.putExtra("tab_type", "query");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {
                        showToast(reason);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("登录失败");
                    return;
                }


            }
        });
    }
}