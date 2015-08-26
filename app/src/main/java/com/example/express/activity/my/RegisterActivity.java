package com.example.express.activity.my;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.MD5Util;
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/23 14:50
 * 修改人：xutework
 * 修改时间：2015/7/23 14:50
 * 修改备注：
 */
public class RegisterActivity extends BaseActivity {
    private EditText et_username;
    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private EditText et_password;
    private EditText et_confirm_password;
    private Button btn_confirm;
    private TextView tv_protocol;

    private TimeCount timer;

    private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
    private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间

    private String username;
    private String phone;
    private String code;
    private String password;
    private String passwordConfirm;

    private SmsContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        content = new SmsContent(new Handler());
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
        
        initTop();
        setTitle("注册");
        initViews();

        timer = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(content);
    }

    private void initViews() {

        et_username = (EditText) findViewById(R.id.et_username);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);

        btn_top_right.setVisibility(View.VISIBLE);
        btn_top_right.setBackgroundResource(R.drawable.query_info_ico);

        btn_send_code.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_send_code:
                phone = et_phone.getText().toString().trim();
                if (StringUtils.isEmpty(phone)) {
                    showToast("请输入手机号");
                    return;
                }
                if (!StringUtils.checkString(phone, "^1[3-8]+\\d{9}$")) {
                    showToast("请输入正确的手机号");
                    return;
                }
                btn_send_code.setClickable(false);
                timer.start();
                verifyPhone();
                break;

            case R.id.btn_confirm:
                username = et_username.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                code = et_code.getText().toString().trim();
                password = et_password.getText().toString().trim();
                passwordConfirm = et_confirm_password.getText().toString().trim();
                if (StringUtils.isEmpty(username)) {
                    showToast("请输入用户名");
                    return;
                }
                if (!StringUtils.checkString(username, "[0-9a-zA-Z]{6,12}")) {
                    showToast("用户名由6-12位的字母或者数字组成");
                    return;
                }
                if (StringUtils.isEmpty(phone)) {
                    showToast("请输入手机号");
                    return;
                }
                if (!StringUtils.checkString(phone, "^1[3-8]+\\d{9}$")) {
                    showToast("请输入正确的手机号");
                    return;
                }
                if (StringUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                    return;
                }
                if (StringUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                if (StringUtils.isEmpty(passwordConfirm)) {
                    showToast("请输入确认密码");
                    return;
                }
                if (!passwordConfirm.equals(password)) {
                    showToast("两次输入密码不一致");
                    return;
                }
                register();
                break;

            case R.id.tv_protocol:
                intent.setClass(RegisterActivity.this, ShowInfoActivity.class);
                intent.putExtra("type", "protocol");
                startActivity(intent);
                break;

            case R.id.btn_top_right:
                intent.setClass(RegisterActivity.this, ShowInfoActivity.class);
                intent.putExtra("type", "information");
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 取消计时器
     */
    private void cancelTimer() {
        timer.cancel();
        btn_send_code.setText("免费获取验证码");
        btn_send_code.setClickable(true);
    }

    /**
     * 计时器类
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_send_code.setText(millisUntilFinished / COUNT_DOWN_INTERVAL + "秒后重新发送");
        }

        @Override
        public void onFinish() {
            btn_send_code.setText("免费获取验证码");
            btn_send_code.setClickable(true);
        }

    }

    /**
     * 获取短信验证码
     */
    private void getVerifyCode() {
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.GET_VERIFY_CODE + phone + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("短信验证码将发送至您的手机，请注意查收");
                            } else {
                                showToast("获取验证码失败");
                                cancelTimer();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("获取验证码失败");
                            cancelTimer();
                            return;
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("获取验证码失败");
                        cancelTimer();
                    }
                });
    }

    /**
     * 验证手机号是否被注册
     */
    private void verifyPhone() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("phone", phone);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.VERIFY_PHONE_REGISTER + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                getVerifyCode();
                            } else {
                                showToast("该手机号已被注册");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("网络异常");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("网络异常");
                    }
                });
    }

    /**
     * 注册
     */
    private void register() {
        showCustomDialog("正在注册，请稍后...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", MD5Util.md5(password));
        params.put("phone", phone);
        params.put("code", code);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.REGISTER + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("注册成功，请登录");
                                launch(LoginActivity.class);
                                finish();
                            } else {
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("注册失败");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("网络异常，注册失败");
                    }
                });
    }

    /**
     * 监听短信数据库
     */
    class SmsContent extends ContentObserver {

        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            super.onChange(selfChange);
            //读取收件箱中指定号码的短信
            cursor = RegisterActivity.this.managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
                    " address=? and read=?", new String[]{CommonConstants.CODE_PHONE, "0"}, "_id desc");
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1");        //修改短信为已读模式
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);

                et_code.setText(StringUtils.getDynamicPassword(smsBody));

            }

            //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
            if(Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
    }
}
