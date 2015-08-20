package com.example.express.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.express.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/27 14:18
 * 修改人：xutework
 * 修改时间：2015/7/27 14:18
 * 修改备注：
 */
public class ChangePhoneTwoActivity extends BaseActivity {
    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private Button btn_next;

    private TimeCount timer;

    private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
    private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间

    private String phone;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone);
        initTop();
        setTitle("修改手机号");
        initViews();
        timer = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
    }

    private void initViews() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_next = (Button) findViewById(R.id.btn_next);
        et_phone.setHint("新手机号");
        btn_next.setText("确认修改");

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
        showCustomDialog("正在修改中...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("phone", phone);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.VERIFY_PHONE + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                changePhone();
                            } else {
                                dismissCustomDialog();
                                showToast(obj.optString("reason"));
                                cancelTimer();
                            }
                        } catch (JSONException e) {
                            dismissCustomDialog();
                            e.printStackTrace();
                            showToast("修改失败，请重试");
                            cancelTimer();
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("修改失败，请重试");
                        cancelTimer();
                    }
                });
    }

    /**
     * 修改手机号
     */
    private void changePhone() {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = BaseApplication.getInstance().getLoginUser().getUserId();
        params.put("userId", userId);
        params.put("newPhone", phone);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.CHANGE_PHONE + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("修改成功");
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                dismissCustomDialog();
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
}
