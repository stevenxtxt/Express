package com.example.express.activity.more;

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
import com.example.express.bean.LoginUser;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.MD5Util;
import com.example.express.utils.Preference;
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    private String oldPassword;
    private String newPassword;
    private String passwordConfirm;

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
            oldPassword = et_old_password.getText().toString().trim();
            newPassword = et_new_password.getText().toString().trim();
            passwordConfirm = et_confirm_password.getText().toString().trim();
            if (StringUtils.isEmpty(oldPassword)) {
                showToast("原密码不能为空");
                return;
            }
            if (StringUtils.isEmpty(newPassword)) {
                showToast("新密码不能为空");
                return;
            }
            if (StringUtils.isEmpty(passwordConfirm)) {
                showToast("确认新密码不能为空");
                return;
            }
            if (!newPassword.equals(passwordConfirm)) {
                showToast("两次输入密码不一致");
                return;
            }
            changePassword();
        }
    }

    private void changePassword() {
        showCustomDialog("正在提交修改...");
        final LoginUser loginUser = BaseApplication.getInstance().getLoginUser();
        String userId = loginUser.getUserId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("oldPassword", MD5Util.md5(oldPassword));
        params.put("newPassword", MD5Util.md5(newPassword));
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.CHANGE_PASSWORD + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("修改成功");
                                Preference.putString("password", MD5Util.md5(newPassword));
                                loginUser.setPassword(MD5Util.md5(newPassword));
                                BaseApplication.getInstance().setLoginUser(loginUser);
                            } else {
                                String reason = obj.optString("reason");
                                showToast(reason);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("修改失败");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("修改密码失败");
                    }
                });
    }
}
