package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.express.R;
import com.example.express.activity.BaseActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 14:39
 * 修改人：xutework
 * 修改时间：2015/7/29 14:39
 * 修改备注：
 */
public class FeedbackActivity extends BaseActivity {

    private EditText et_feedback_content;
    private EditText et_feedback_contact;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        initTop();
        setTitle("意见反馈");

        initViews();
    }

    private void initViews() {
        et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);
        et_feedback_contact = (EditText) findViewById(R.id.et_feedback_contact);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_submit) {

        }
    }
}
