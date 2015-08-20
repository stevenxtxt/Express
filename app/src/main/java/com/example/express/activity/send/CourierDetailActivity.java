package com.example.express.activity.send;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.bean.CouriorDetailBean;
import com.example.express.bean.LoginUser;
import com.example.express.bean.ScopeBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;
import com.example.express.utils.ImageLoaderUtil;
import com.example.express.utils.StringUtils;
import com.example.express.view.CollapsibleTextView;
import com.example.express.view.RoundImageView;
import com.example.express.view.StarBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/31 17:16
 * 修改人：xutework
 * 修改时间：2015/7/31 17:16
 * 修改备注：
 */
public class CourierDetailActivity extends BaseActivity {

    private TextView tv_follow_flag;
    private LinearLayout ll_call;
    private TextView tv_phone_no;
    private RoundImageView riv_user_icon;
    private TextView tv_name;
    private TextView tv_company;
    private StarBarView stv_star;
    private TextView tv_service_count;
    private TextView tv_fav_count;
    private TextView tv_bad_count;
    private LinearLayout ll_rules;
    private TextView tv_service_time;
    private CollapsibleTextView ctv_scope;
    private TextView tv_comment_user_time;
    private TextView tv_comment_content;
    private StarBarView stv_comment_star;

    private String userId = "67";//用户id，如果未登录则默认为字母a
    private String couriorId = "16291";
    private LoginUser loginUser;
    private CouriorDetailBean couriorDetailBean;
    private ArrayList<ScopeBean> delivery = new ArrayList<ScopeBean>();
    private String flag;

    public static final String FOLLOW = "0";
    public static final String UNFOLLOW = "1";

    /*
    测试字符串
     */
    private String test_str = "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing " +
            "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing " +
            "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing " +
            "nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing nanjing ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courier_detail);
        initTop();

//        couriorId = getIntent().getStringExtra("couriorId");
//        loginUser = BaseApplication.getInstance().getLoginUser();
//        if (loginUser != null) {
//            userId = loginUser.getUserId();
//        }

        String response = getIntent().getStringExtra("json");
        getJsonData(response);

        initViews();
    }

    private void initViews() {
        tv_follow_flag = (TextView) findViewById(R.id.tv_follow_flag);
        ll_call = (LinearLayout) findViewById(R.id.ll_call);
        tv_phone_no = (TextView) findViewById(R.id.tv_phone_no);
        riv_user_icon = (RoundImageView) findViewById(R.id.riv_user_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_company = (TextView) findViewById(R.id.tv_company);
        stv_star = (StarBarView) findViewById(R.id.stv_star);
        tv_service_count = (TextView) findViewById(R.id.tv_service_count);
        tv_fav_count = (TextView) findViewById(R.id.tv_fav_count);
        tv_bad_count = (TextView) findViewById(R.id.tv_bad_count);
        ll_rules = (LinearLayout) findViewById(R.id.ll_rules);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        ctv_scope = (CollapsibleTextView) findViewById(R.id.ctv_scope);
        tv_comment_user_time = (TextView) findViewById(R.id.tv_comment_user_time);
        tv_comment_content = (TextView) findViewById(R.id.tv_comment_content);
        stv_comment_star = (StarBarView) findViewById(R.id.stv_comment_star);

//        ctv_scope.setDesc(test_str, TextView.BufferType.NORMAL);
        showData();

        tv_follow_flag.setOnClickListener(this);
        ll_call.setOnClickListener(this);
        ll_rules.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_follow_flag:
                if (loginUser == null) {
                    showToast("您尚未登录，登录后即可关注快递员");
                    return;
                }
                if (flag.equals(FOLLOW)) {
                    followOrUnfollow(UNFOLLOW);
                } else if (flag.equals(UNFOLLOW)) {
                    followOrUnfollow(FOLLOW);
                }
                break;

            case R.id.ll_call:
                showCallDialog(couriorDetailBean.getPhone());
                break;

            case R.id.ll_rules:
                break;

            default:
                break;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void showData() {
        ImageLoaderUtil.getInstance().displayImage(couriorDetailBean.getCourierIcon(), riv_user_icon);
        tv_name.setText(couriorDetailBean.getName());
        tv_company.setText(couriorDetailBean.getEname());
        tv_service_time.setText(couriorDetailBean.getServertime());
        tv_phone_no.setText(couriorDetailBean.getPhone());

        //设置收派范围文字
        StringBuffer sb = new StringBuffer();
        for (ScopeBean bean : delivery) {
            sb.append(bean.getName()).append("    ");
        }
        String desc = sb.toString();
        ctv_scope.setDesc(desc, TextView.BufferType.NORMAL);

        //根据isFavourite字段设置是否关注文字
        flag = couriorDetailBean.getIsFavourite();
        if (StringUtils.isEmpty(flag)) {
            tv_follow_flag.setText("关注");
        } else if (flag.equals(UNFOLLOW)) {
            tv_follow_flag.setText("关注");
        } else if (flag.equals(FOLLOW)) {
            tv_follow_flag.setText("取消关注");
        }

        //设置星级图案
        stv_star.setBgImgResId(R.drawable.star, R.drawable.star_blank);
        stv_comment_star.setBgImgResId(R.drawable.star, R.drawable.star_blank);
        //根据分数设置显示几颗星
        try {
            stv_star.setImgCount(3, 2, false);
            stv_comment_star.setImgCount(5, 4, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stv_star.show(this, dip2px(this, 20), dip2px(this, 20));
        stv_comment_star.show(this, dip2px(this, 15), dip2px(this, 15));
    }

    /**
     * 根据标识关注或者取消关注快递员
     *
     * @param isFavourite 0表示关注，1表示取消关注
     */
    private void followOrUnfollow(final String isFavourite) {
        showCustomDialog("正在处理，请稍后...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courierId", couriorId);
        params.put("userId", userId);
        params.put("isFavourite", isFavourite);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.FOLLOW_OR_UNFOlLOW + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("处理成功");
                                if (isFavourite.equals(FOLLOW)) {
                                    tv_follow_flag.setText("取消关注");
                                } else if (isFavourite.equals(UNFOLLOW)) {
                                    tv_follow_flag.setText("关注");
                                }
                            } else {
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("处理失败");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("网络异常，处理失败");
                    }
                });
    }

    /**
     * 打电话对话框
     *
     * @param phoneNo
     */
    private void showCallDialog(final String phoneNo) {
        LayoutInflater inflater = LayoutInflater.from(CourierDetailActivity.this);
        View view = inflater.inflate(R.layout.call_dialog_layout, null);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone_no);
        TextView tv_zhuyi = (TextView) view.findViewById(R.id.tv_zhuyi);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_call = (Button) view.findViewById(R.id.btn_call);
        tv_phone.setText(phoneNo);
        tv_zhuyi.setVisibility(View.GONE);

        final Dialog call_dialog = new Dialog(CourierDetailActivity.this, R.style.call_dialog);
        call_dialog.setCanceledOnTouchOutside(true);
        call_dialog.setContentView(view);
        call_dialog.show();
        WindowManager.LayoutParams lp = call_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(CourierDetailActivity.this)); // 设置宽度
        call_dialog.getWindow().setAttributes(lp);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
            }
        });
    }

    private void getJsonData(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            if (obj.optBoolean("result")) {
                couriorDetailBean = new CouriorDetailBean();
                couriorDetailBean.setName(obj.optString("name"));
                couriorDetailBean.setPhone(obj.optString("phone"));
                couriorDetailBean.setServertime(obj.optString("servertime"));
                couriorDetailBean.setEname(obj.optString("ename"));
                couriorDetailBean.setIsFavourite(obj.optString("isFavourite"));
                couriorDetailBean.setCourierIcon(CommonConstants.URLConstant + obj.getString("courierIcon"));
                JSONArray arr = obj.optJSONArray("delivery");
                if (arr != null && arr.length() > 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        ScopeBean scopeBean = new ScopeBean();
                        JSONObject arrObj = arr.optJSONObject(i);
                        scopeBean.setName(arrObj.optString("name"));
                        delivery.add(scopeBean);
                    }
                    couriorDetailBean.setDelivery(delivery);
                }
            } else {
                dismissCustomDialog();
                showToast("查询失败");
                finish();
            }
        } catch (JSONException e) {
            dismissCustomDialog();
            e.printStackTrace();
            showToast("查询失败");
            finish();
            return;
        }
    }

}
