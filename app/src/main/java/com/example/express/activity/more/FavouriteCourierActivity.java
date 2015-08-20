package com.example.express.activity.more;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.more.adapter.FavCourierAdapter;
import com.example.express.bean.CourierBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/28 16:29
 * 修改人：xutework
 * 修改时间：2015/7/28 16:29
 * 修改备注：
 */
public class FavouriteCourierActivity extends BaseActivity {

    private RelativeLayout rl_official;
    private LinearLayout ll_no_data;
    private ListView lv_fav_courier;
    private FavCourierAdapter adapter;
    private ArrayList<CourierBean> courierList = new ArrayList<CourierBean>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_courier);
        initTop();
        setTitle("收藏的快递员");

        userId = BaseApplication.getInstance().getLoginUser().getUserId();

        adapter = new FavCourierAdapter(FavouriteCourierActivity.this);

        initViews();
        queryFavCourier();
    }

    private void initViews() {
        rl_official = (RelativeLayout) findViewById(R.id.rl_official);
        ll_no_data = (LinearLayout) findViewById(R.id.ll_no_data);
        lv_fav_courier = (ListView) findViewById(R.id.lv_fav_courier);

        rl_official.setOnClickListener(this);

        lv_fav_courier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String phone = courierList.get(i).getPhone();
                showCallDialog(phone);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.rl_official) {
            showCallDialog(CommonConstants.HOT_LINE);
        }
    }

    /**
     * 查询收藏的快递员列表
     */
    private void queryFavCourier() {
        showCustomDialog("正在查询...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_FAV_COURIER + userId + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                JSONArray arr = obj.optJSONArray("data");
                                if (arr != null && arr.length() > 0) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        CourierBean bean = new CourierBean();
                                        JSONObject arrObj = arr.optJSONObject(i);
                                        bean.setName(arrObj.optString("courierName"));
                                        bean.setCompany(arrObj.optString("courierCompany"));
                                        bean.setIcon(CommonConstants.URLConstant + arrObj.optString("courierIcon"));
                                        bean.setPhone(arrObj.optString("phone"));
                                        courierList.add(bean);
                                    }
                                    showData();
                                } else {
                                    ll_no_data.setVisibility(View.VISIBLE);
                                    lv_fav_courier.setVisibility(View.GONE);
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
                        showToast("网络异常，查询失败");
                        finish();
                    }
                });
    }

    private void showData() {
        ll_no_data.setVisibility(View.GONE);
        lv_fav_courier.setVisibility(View.VISIBLE);
        adapter.setList(courierList);
        lv_fav_courier.setAdapter(adapter);
    }

    /**
     * 打电话对话框
     * @param phoneNo
     */
    private void showCallDialog(final String phoneNo) {
        LayoutInflater inflater = LayoutInflater.from(FavouriteCourierActivity.this);
        View view = inflater.inflate(R.layout.call_dialog_layout, null);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone_no);
        TextView tv_zhuyi = (TextView) view.findViewById(R.id.tv_zhuyi);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_call = (Button) view.findViewById(R.id.btn_call);
        tv_phone.setText(phoneNo);
        tv_zhuyi.setVisibility(View.GONE);

        final Dialog call_dialog = new Dialog(FavouriteCourierActivity.this, R.style.call_dialog);
        call_dialog.setCanceledOnTouchOutside(true);
        call_dialog.setContentView(view);
        call_dialog.show();
        WindowManager.LayoutParams lp = call_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(FavouriteCourierActivity.this)); // 设置宽度
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
}
