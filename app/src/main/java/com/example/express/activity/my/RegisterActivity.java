package com.example.express.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

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
    private EditText et_code;
    private Button btn_send_code;
    private EditText et_password;
    private EditText et_confirm_password;
    private Button btn_confirm;
    private TextView tv_protocol;

    private TimeCount timer;

    private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
    private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        initTop();
        setTitle("注册");
        initViews();

        timer = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
    }

    private void initViews() {

        et_username = (EditText) findViewById(R.id.et_username);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);

        btn_top_right.setVisibility(View.VISIBLE);
        btn_top_right.setBackgroundResource(R.drawable.ic_launcher);

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
                btn_send_code.setClickable(false);
                timer.start();
                break;

            case R.id.btn_confirm:
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
}
