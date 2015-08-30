package com.example.express.activity.send;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.activity.BaseFragment;
import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.activity.send.adapter.SendCourierAdapter;
import com.example.express.bean.CourierBean;
import com.example.express.bean.LoginUser;
import com.example.express.constants.CommonConstants;
import com.example.express.view.CustomProgressDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendExpressFragment extends Fragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ListView> {

    private View view;
    private Activity context;
    private TextView tv_nearby;
    private TextView tv_record;
    private LinearLayout ll_start;
    private TextView tv_start_place;
    private LinearLayout ll_destination;
    private TextView tv_destination;
    private PullToRefreshListView ptrlv_courier;
    private LinearLayout ll_no_content;
    private SendCourierAdapter adapter;
    private ArrayList<CourierBean> courierList = new ArrayList<CourierBean>();

    private String address;
    private double latitude;
    private double longitude;

    private String userId;//用户id，如果未登录则默认为字母a
    private LoginUser loginUser;
    private BaseApplication app;

    protected CustomProgressDialog customProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        app = BaseApplication.getInstance();
        loginUser = app.getLoginUser();
        if (loginUser != null) {
            userId = loginUser.getUserId();
        } else {
            userId = "a";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.send_nearby_frag, container, false);
        initViews();
        adapter = new SendCourierAdapter(context);
        latitude = BaseApplication.getInstance().getExpressLat();
        longitude = BaseApplication.getInstance().getExpressLng();

        queryNearbyCourier(true);

        ptrlv_courier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String courierId = courierList.get(i - 1).getId();
                queryCouriorDetail(courierId);
            }
        });

        return view;
    }

    private void initViews() {
        ll_start = (LinearLayout) view.findViewById(R.id.ll_start);
        tv_start_place = (TextView) view.findViewById(R.id.tv_start_place);
        ll_destination = (LinearLayout) view.findViewById(R.id.ll_destination);
        ll_destination.setVisibility(View.GONE);
        tv_destination = (TextView) view.findViewById(R.id.tv_destination);
        ptrlv_courier = (PullToRefreshListView) view.findViewById(R.id.ptrlv_courier);
        ll_no_content = (LinearLayout) view.findViewById(R.id.ll_no_content);

        ptrlv_courier.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ptrlv_courier.setOnRefreshListener(this);

        ll_start.setOnClickListener(this);
        ll_destination.setOnClickListener(this);

        //将当前地址显示出来
        address = BaseApplication.getInstance().getExpressAddress();
        tv_start_place.setText(address);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_start:
                Intent intent = new Intent(context, StartPlaceActivity.class);
                startActivityForResult(intent, 100);

                break;

            case R.id.ll_destination:
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrlv_courier.onRefreshComplete();
//            }
//        }, 2000);
        queryNearbyCourier(false);
    }

    public void setData(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 查询附近的快递员
     *
     * @param isShowing 是否展示进度框true展示false不展示
     */
    private void queryNearbyCourier(final boolean isShowing) {
        if (isShowing) {
            showCustomDialog("正在查询中...");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        String lat = String.valueOf(latitude);
        String lng = String.valueOf(longitude);
        params.put("latitude", lat);
        params.put("longitude", lng);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.QUERY_NEARBY_COURIER + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ptrlv_courier.setVisibility(View.VISIBLE);
                        ll_no_content.setVisibility(View.GONE);
                        if (isShowing) {
                            dismissCustomDialog();
                        } else {
                            ptrlv_courier.onRefreshComplete();
                        }

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                JSONArray arr = obj.optJSONArray("data");
                                if (arr != null && arr.length() > 0) {
                                    if (courierList.size() > 0) {
                                        courierList.clear();
                                    }
                                    ptrlv_courier.setVisibility(View.VISIBLE);
                                    ll_no_content.setVisibility(View.GONE);
                                    for (int i = 0; i < arr.length(); i++) {
                                        CourierBean bean = new CourierBean();
                                        JSONObject arrObj = arr.optJSONObject(i);
                                        bean.setName(arrObj.optString("name"));
                                        bean.setPhone(arrObj.optString("phone"));
                                        bean.setId(arrObj.optString("id"));
                                        bean.setCompany(arrObj.optString("ename"));
                                        bean.setIcon(CommonConstants.URLConstant + arrObj.optString("smartico"));
                                        bean.setExname(arrObj.optString("exname"));
                                        courierList.add(bean);
                                    }
                                    adapter.setList(courierList);
                                    adapter.setAddress(address);
                                    ptrlv_courier.setAdapter(adapter);
                                } else {
                                    showToast("未查询到附近快递员");
                                    ptrlv_courier.setVisibility(View.GONE);
                                    ll_no_content.setVisibility(View.VISIBLE);
                                }
                            } else {
                                showToast("未查询到附近快递员");
                                ptrlv_courier.setVisibility(View.GONE);
                                ll_no_content.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("查询失败");
                            ptrlv_courier.setVisibility(View.GONE);
                            ll_no_content.setVisibility(View.VISIBLE);
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (isShowing) {
                            dismissCustomDialog();
                        } else {
                            ptrlv_courier.onRefreshComplete();
                        }
                        ptrlv_courier.setVisibility(View.GONE);
                        ll_no_content.setVisibility(View.VISIBLE);
                        showToast("网络异常，查询失败");
                    }
                });
    }

    /**
     * 查询快递员详情
     */
    private void queryCouriorDetail(final String courierId) {
        showCustomDialog("正在获取快递员详情...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_COURIOR_DETAIL +
                        courierId + "-" + userId + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        Intent intent = new Intent(context, CourierDetailActivity.class);
                        intent.putExtra("courierId", courierId);
                        intent.putExtra("json", response);
                        startActivity(intent);

                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("查询失败");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            address = data.getStringExtra("address");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            tv_start_place.setText(address);
            queryNearbyCourier(true);
        }
    }

    /**
     * 展示自定义对话框
     *
     * @param str
     */
    public void showCustomDialog(String str) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(context, str, R.anim.frame);
        }
        customProgressDialog.show();
    }

    /**
     * 隐藏自定义对话框
     */
    public void dismissCustomDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }

    protected void showToast(String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}