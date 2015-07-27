package com.example.express.activity.more;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone);
        initTop();
        setTitle("修改手机号");
        initViews();
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
