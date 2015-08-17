package com.example.express.activity.query;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.query.adapter.ResultAdapter;
import com.example.express.bean.QueryResultBean;
import com.example.express.bean.QueryResultItemBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.ImageLoaderUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<QueryResultItemBean> resultList = new ArrayList<QueryResultItemBean>();

    private String jsonStr;
    private QueryResultBean queryResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_result);
        initTop();
        setTitle("查询结果");

        jsonStr = getIntent().getStringExtra("json");
        getJsonData(jsonStr);

        initViews();

        tv_company_name.setText(queryResultBean.getCompany());
        tv_order_no.setText(queryResultBean.getNu());
        ImageLoaderUtil.getInstance().displayImage(CommonConstants.URLConstant + queryResultBean.getIco(), iv_message_icon);

        adapter = new ResultAdapter(ShowResultActivity.this);
        adapter.setList(queryResultBean.getData());
        lv_query_result.setAdapter(adapter);
    }

    private void initViews() {
        iv_message_icon = (ImageView) findViewById(R.id.iv_message_icon);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        lv_query_result = (ListView) findViewById(R.id.lv_query_result);
    }

    private void getJsonData(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            queryResultBean = new QueryResultBean();
            queryResultBean.setResult(obj.getBoolean("success"));
            queryResultBean.setCompanytype(obj.getString("companytype"));
            queryResultBean.setNu(obj.getString("nu"));
            queryResultBean.setCompany(obj.getString("company"));
            queryResultBean.setReason(obj.getString("reason"));
            JSONArray arr = obj.getJSONArray("data");
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    QueryResultItemBean bean = new QueryResultItemBean();
                    JSONObject arrObj = arr.getJSONObject(i);
                    bean.setTime(arrObj.getString("time"));
                    bean.setContext(arrObj.getString("context"));
                    resultList.add(bean);
                }
                queryResultBean.setData(resultList);
            }
            queryResultBean.setIco(obj.getString("ico"));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
    }
}
