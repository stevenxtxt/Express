package com.example.express.activity.query;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.query.adapter.ResultAdapter;
import com.example.express.bean.ResultBean;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/30 16:02
 * 修改人：xutework
 * 修改时间：2015/7/30 16:02
 * 修改备注：
 */
public class ShowResultActivity extends BaseActivity {

    private ImageView iv_message_icon;
    private TextView tv_company_name;
    private TextView tv_order_no;
    private ListView lv_query_result;
    private ResultAdapter adapter;
    private ArrayList<ResultBean> resultList = new ArrayList<ResultBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_result);
        initTop();
        setTitle("查询结果");
        initViews();

        adapter = new ResultAdapter(ShowResultActivity.this);
        adapter.setList(resultList);
        lv_query_result.setAdapter(adapter);
    }

    private void initViews() {
        iv_message_icon = (ImageView) findViewById(R.id.iv_message_icon);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        lv_query_result = (ListView) findViewById(R.id.lv_query_result);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
