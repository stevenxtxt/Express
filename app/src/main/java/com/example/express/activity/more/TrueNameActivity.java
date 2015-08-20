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
import com.example.express.constants.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/27 11:40
 * 修改人：xutework
 * 修改时间：2015/7/27 11:40
 * 修改备注：
 */
public class TrueNameActivity extends BaseActivity{
    private EditText et_true_name;
    private Button btn_confirm;
    private String truename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.true_name);
        initTop();
        setTitle("真实姓名");
        initViews();
    }

    private void initViews() {
        et_true_name = (EditText) findViewById(R.id.et_true_name);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_confirm) {
            truename = et_true_name.getText().toString().trim();
            String userId = BaseApplication.getInstance().getLoginUser().getUserId();
            showCustomDialog("正在提交修改...");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            params.put("trueName", truename);
            BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.CHANGE_TRUENAME + CommonConstants.HTML,
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
                            showToast("修改失败");
                        }
                    });
        }
    }
}
