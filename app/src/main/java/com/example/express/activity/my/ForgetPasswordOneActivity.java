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

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.more.ChangePhoneTwoActivity;
import com.example.express.constants.CommonConstants;
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
public class ForgetPasswordOneActivity extends BaseActivity {

    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private Button btn_next;

    private String phone;
    private String code;

    private TimeCount timer;

    private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
    private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间

    private SmsContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_one);

        content = new SmsContent(new Handler());
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);

        initTop();
        setTitle("忘记密码");
        initViews();

        timer = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(content);
    }

    private void initViews() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_send_code.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
                getVerifyCode();
                break;

            case R.id.btn_next:
                phone = et_phone.getText().toString().trim();
                code = et_code.getText().toString().trim();
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
                verifyPhone();
                break;

            default:
                break;
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
     * 验证手机号
     */
    private void verifyPhone() {
        showCustomDialog("正在验证中...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("phone", phone);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.VERIFY_PHONE + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                Intent intent = new Intent(ForgetPasswordOneActivity.this, ForgetPasswordTwoActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                            } else {
                                showToast(obj.optString("reason"));
                                cancelTimer();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("验证失败，请重试");
                            cancelTimer();
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("验证失败，请重试");
                        cancelTimer();
                    }
                });
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
            cursor = ForgetPasswordOneActivity.this.managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
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
