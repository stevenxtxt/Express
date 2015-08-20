package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
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
 * 创建时间：2015/7/29 14:39
 * 修改人：xutework
 * 修改时间：2015/7/29 14:39
 * 修改备注：
 */
public class FeedbackActivity extends BaseActivity {

    private EditText et_feedback_content;
    private EditText et_feedback_contact;
    private Button btn_submit;

    private String content;
    private String phone;

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
            content = et_feedback_content.getText().toString().trim();
            phone = et_feedback_contact.getText().toString().trim();
            if (StringUtils.isEmpty(content)) {
                showToast("请填写反馈建议");
                return;
            }
            submitFeedback();
        }
    }

    /**
     * 提交反馈建议
     */
    private void submitFeedback() {
        showCustomDialog("正在提交...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("content", content);
        params.put("phone", phone);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.FEEDBACK + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("提交成功！感谢您的宝贵建议");
                                finish();
                            } else {
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("提交失败");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("提交失败");
                    }
                });
    }
}
