package com.example.express.activity.send;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.BaseFragment;
import com.example.express.activity.main.MainTabActivity;
import com.example.express.activity.send.adapter.SendCourierAdapter;
import com.example.express.bean.CourierBean;
import com.example.express.bean.CouriorDetailBean;
import com.example.express.bean.ScopeBean;
import com.example.express.constants.CommonConstants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/31 10:15
 * 修改人：xutework
 * 修改时间：2015/7/31 10:15
 * 修改备注：
 */
public class SendNearbyFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ListView> {

    private View view;
    private Activity context;
    private TextView tv_nearby;
    private TextView tv_record;
    private LinearLayout ll_start;
    private TextView tv_start_place;
    private LinearLayout ll_destination;
    private TextView tv_destination;
    private PullToRefreshListView ptrlv_courier;
    private SendCourierAdapter adapter;
    private ArrayList<CourierBean> courierList = new ArrayList<CourierBean>();

    private String address;
    private double latitude;
    private double longitude;

    private String userId = "67";//用户id，如果未登录则默认为字母a
    private String couriorId = "16291";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (MainTabActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_start_place.setText(address);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.send_nearby_frag, container, false);
        initViews();
        adapter = new SendCourierAdapter(context);
        adapter.setList(courierList);
        ptrlv_courier.setAdapter(adapter);

        ptrlv_courier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                queryCouriorDetail();
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
                getParentFragment().startActivityForResult(intent, 100);

                break;

            case R.id.ll_destination:
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrlv_courier.onRefreshComplete();
            }
        }, 2000);
    }

    public void setData(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 查询快递员详情
     */
    private void queryCouriorDetail() {
        showCustomDialog("正在获取快递员详情...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_COURIOR_DETAIL +
                        couriorId + "-" + userId + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        Intent intent = new Intent(context, CourierDetailActivity.class);
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
}
