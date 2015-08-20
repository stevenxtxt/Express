package com.example.express.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Logger;
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    private String password;
    private String passwordConfirm;

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
            password = et_password.getText().toString().trim();
            passwordConfirm = et_confirm_password.getText().toString().trim();
            if (StringUtils.isEmpty(password)) {
                showToast("密码不能为空");
                return;
            }
            if (StringUtils.isEmpty(passwordConfirm)) {
                showToast("密码不能为空");
                return;
            }
            if (!password.equals(passwordConfirm)) {
                showToast("两次输入密码不一致，请重新填写");
                return;
            }
            forgetPassword();
        }
    }

    /**
     * 重置密码
     */
    private void forgetPassword() {
        showCustomDialog("正在重置密码...");
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = BaseApplication.getInstance().getLoginUser().getUserId();
        params.put("userId", userId);
        params.put("newpassword", password);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.FORGET_PASSWORD + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("重置密码成功");
                                Intent intent = new Intent(ForgetPasswordTwoActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dismissCustomDialog();
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("重置密码失败，请重试");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("重置密码失败，请重试");
                    }
                });
    }
}
