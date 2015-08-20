package com.example.express.activity.more;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    private String province;
    private String city;
    private String gender;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        initTop();
        setTitle("个人信息");

        userId = BaseApplication.getInstance().getLoginUser().getUserId();

        initViews();
        queryUserInfo("正在获取您的信息...");
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
                launch(ChangePasswordActivity.class);
                break;

            case R.id.rl_username:
                break;

            case R.id.rl_true_name:
                Intent truename_intent = new Intent(PersonalInfoActivity.this, TrueNameActivity.class);
                startActivityForResult(truename_intent, 1002);
                break;

            case R.id.rl_gender:
                showGenderDialog();
                break;

            case R.id.rl_phone:
                Intent phone_intent = new Intent(PersonalInfoActivity.this, ChangePhoneOneActivity.class);
                startActivityForResult(phone_intent, 1003);
                break;

            case R.id.rl_address:
                Intent address_intent = new Intent(PersonalInfoActivity.this, ChooseAddressActivity.class);
                startActivityForResult(address_intent, 1001);
                break;

            case R.id.btn_logout:
                BaseApplication.getInstance().setLoginUser(null);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");
//            tv_address.setText(province + "-" + city);
            queryUserInfo("正在更新您的信息...");
        }
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            queryUserInfo("正在更新您的信息...");
        }
        if (requestCode == 1003 && resultCode == RESULT_OK) {
            queryUserInfo("正在更新您的信息...");
        }
    }

    private void showGenderDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.choose_gender_dialog, null);
        final TextView tv_privacy = (TextView) view.findViewById(R.id.tv_privacy);
        final TextView tv_male = (TextView) view.findViewById(R.id.tv_male);
        final TextView tv_female = (TextView) view.findViewById(R.id.tv_female);

        final Dialog call_dialog = new Dialog(this, R.style.call_dialog);
        call_dialog.setCanceledOnTouchOutside(true);
        call_dialog.setContentView(view);
        call_dialog.show();
        WindowManager.LayoutParams lp = call_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(this)); // 设置宽度
        call_dialog.getWindow().setAttributes(lp);

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
                gender = tv_privacy.getText().toString();
                changeGender();
            }
        });
        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
                gender = tv_male.getText().toString();
                changeGender();
            }
        });
        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
                gender = tv_female.getText().toString();
                changeGender();
            }
        });
    }

    /**
     * 修改性别
     */
    private void changeGender() {
        showCustomDialog("正在修改...");
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = BaseApplication.getInstance().getLoginUser().getUserId();
        params.put("userId", userId);
        params.put("sex", gender);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.CHANGE_GENDER + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("修改成功");
                                tv_gender.setText(gender);
                            } else {
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("修改失败，请重试");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("修改失败，请重试");
                    }
                });
    }

    /**
     * 查询用户信息
     */
    private void queryUserInfo(String str) {
        showCustomDialog(str);
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_USER + userId + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                tv_username.setText(obj.optString("username"));
                                String truename = obj.optString("truename");
                                if (StringUtils.isEmpty(truename)) {
                                    tv_true_name.setText("未编辑");
                                } else {
                                    tv_true_name.setText(truename);
                                }
                                String sex = obj.optString("sex");
                                if (StringUtils.isEmpty(sex)) {
                                    tv_gender.setText("保密");
                                } else {
                                    tv_gender.setText(sex);
                                }
                                tv_phone.setText(obj.optString("phone"));
                                String area = obj.optString("area");
                                if (StringUtils.isEmpty(area)) {
                                    tv_address.setText("未编辑");
                                } else {
                                    tv_address.setText(area);
                                }
                            } else {
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("查询失败");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("查询失败");
                    }
                });
    }
}
