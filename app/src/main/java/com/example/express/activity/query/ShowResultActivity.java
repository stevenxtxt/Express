package com.example.express.activity.query;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.db.MyDb;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.query.adapter.ResultAdapter;
import com.example.express.bean.QueryRecordBean;
import com.example.express.bean.QueryResultBean;
import com.example.express.bean.QueryResultItemBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.ImageLoaderUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private LinearLayout ll_content;
    private LinearLayout ll_no_content;
    private ImageView iv_message_icon;
    private TextView tv_company_name;
    private TextView tv_order_no;
    private ListView lv_query_result;
    private ResultAdapter adapter;
    private ArrayList<QueryResultItemBean> resultList = new ArrayList<QueryResultItemBean>();

    private String jsonStr;
    private QueryResultBean queryResultBean;

    private MyDb myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_result);
        initTop();
        setTitle("查询结果");

        myDb = BaseApplication.getInstance().getMyDb();
        if (null == myDb) {
            myDb = MyDb.create(this, CommonConstants.DB_NAME, true);
        }

        jsonStr = getIntent().getStringExtra("json");
        getJsonData(jsonStr);

        initViews();

        if (queryResultBean == null) {
            ll_content.setVisibility(View.GONE);
            ll_no_content.setVisibility(View.VISIBLE);
        } else {
            ll_no_content.setVisibility(View.GONE);
            ll_content.setVisibility(View.VISIBLE);

            tv_company_name.setText(queryResultBean.getCompany());
            tv_order_no.setText(queryResultBean.getNu());
            ImageLoaderUtil.getInstance().displayImage(CommonConstants.URLConstant + queryResultBean.getIco(), iv_message_icon);

            adapter = new ResultAdapter(ShowResultActivity.this);
            adapter.setList(queryResultBean.getData());
            lv_query_result.setAdapter(adapter);
        }

    }

    private void initViews() {
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_no_content = (LinearLayout) findViewById(R.id.ll_no_content);
        iv_message_icon = (ImageView) findViewById(R.id.iv_message_icon);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        lv_query_result = (ListView) findViewById(R.id.lv_query_result);
    }

    private void getJsonData(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            if (!obj.optBoolean("result")) {
                return;
            }
            queryResultBean = new QueryResultBean();
            queryResultBean.setResult(obj.optBoolean("result"));
            queryResultBean.setCompanytype(obj.optString("companytype"));
            queryResultBean.setNu(obj.optString("nu"));
            queryResultBean.setCompany(obj.optString("company"));
            queryResultBean.setReason(obj.optString("reason"));
            JSONArray arr = obj.optJSONArray("data");
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    QueryResultItemBean bean = new QueryResultItemBean();
                    JSONObject arrObj = arr.optJSONObject(i);
                    bean.setTime(arrObj.optString("time"));
                    bean.setContext(arrObj.optString("context"));
                    resultList.add(bean);
                }
                queryResultBean.setData(resultList);
            }
            queryResultBean.setIco(obj.optString("ico"));
            //相关内容存储到数据库
            saveRecord();
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     * 将查询过的记录存入数据库，如果存在相同的单号，则删除原来的数据，存入新的数据
     */
    private void saveRecord() {
        QueryRecordBean qrBean = new QueryRecordBean();
        qrBean.setCompany(queryResultBean.getCompany());
        qrBean.setCompanytype(queryResultBean.getCompanytype());
        qrBean.setNu(queryResultBean.getNu());
        qrBean.setIco(CommonConstants.URLConstant + queryResultBean.getIco());
        qrBean.setLatestContext(queryResultBean.getData().get(0).getContext());
        qrBean.setLatestTime(queryResultBean.getData().get(0).getTime());
        if (hasRepeatData(qrBean)) {
            myDb.deleteByWhereStr(QueryRecordBean.class, "nu='" + qrBean.getNu() + "'");
        }
        myDb.save(qrBean);
    }

    /**
     * 判断表中是否有重复数据
     * @param bean
     * @return
     */
    private boolean hasRepeatData(QueryRecordBean bean) {
        boolean flag = false;
        List<QueryRecordBean> list = myDb.findAll(QueryRecordBean.class);
        if (list == null || list.size() == 0) {
            return false;
        }
        for (QueryRecordBean qrBean : list) {
            if (qrBean.getNu().equals(bean.getNu())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
    }
}
