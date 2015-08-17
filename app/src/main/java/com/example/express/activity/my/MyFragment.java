package com.example.express.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.activity.BaseFragment;
import com.example.express.R;
import com.example.express.activity.my.adapter.MyRecordAdapter;
import com.example.express.activity.query.ShowResultActivity;
import com.example.express.bean.RecordBean;
import com.example.express.constants.CommonConstants;

import java.util.ArrayList;

public class MyFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private TextView tv_received;
    private TextView tv_not_received;
    private LinearLayout ll_show_login;
    private ListView lv_record;
    private Button btn_login;
    private MyRecordAdapter adapter;
    private ArrayList<RecordBean> recordList = new ArrayList<RecordBean>();

    //测试数据
    private String nu = "132471708788";
    private String com = "shunfeng";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_my, null);
        initViews();

        adapter = new MyRecordAdapter(activity);
        adapter.setList(recordList);
        lv_record.setAdapter(adapter);

        return view;
}

    private void initViews() {
        tv_received = (TextView) view.findViewById(R.id.tv_received);
        tv_not_received = (TextView) view.findViewById(R.id.tv_not_received);
        ll_show_login = (LinearLayout) view.findViewById(R.id.ll_show_login);
        lv_record = (ListView) view.findViewById(R.id.lv_record);
        btn_login = (Button) view.findViewById(R.id.btn_login);

        ll_show_login.setVisibility(View.GONE);
        lv_record.setVisibility(View.VISIBLE);

        tv_received.setOnClickListener(this);
        tv_not_received.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                queryExpressResult();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_received:
                break;

            case R.id.tv_not_received:
                break;

            case R.id.btn_login:
                break;

            default:
                break;
        }
    }

    /**
     * 查询快递
     */
    private void queryExpressResult() {
        showCustomDialog("正在努力查询中...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_EXPRESS_RESULT + nu + "-" + com + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        Intent intent = new Intent();
                        intent.putExtra("json", response);
                        intent.setClass(getActivity(), ShowResultActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        Toast.makeText(activity, "查询失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}