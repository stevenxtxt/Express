package com.example.express.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.db.MyDb;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.send.adapter.SearchRecordAdapter;
import com.example.express.bean.SearchRecordBean;
import com.example.express.constants.CommonConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/4 17:53
 * 修改人：xutework
 * 修改时间：2015/8/4 17:53
 * 修改备注：
 */
public class SearchPlaceActivity extends BaseActivity implements TextWatcher{

    private TextView tv_city;
    private AutoCompleteTextView actv_keyword;
    private TextView tv_cancel;
    private TextView tv_record_hint;
    private ListView lv_search_record;
    private String city;
    private BaseApplication app;
    private MyDb myDb;
    private SearchRecordAdapter adapter;
    private List<SearchRecordBean> recordList;
    private View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_place);
        app = BaseApplication.getInstance();
        city = app.getExpressCity();
        //从数据库中查找搜索记录
        myDb = app.getMyDb();
        if (null == myDb) {
            myDb = MyDb.create(this, CommonConstants.DB_NAME, true);
        }
        recordList = myDb.findAll(SearchRecordBean.class);

        initViews();
        initListView();
    }

    private void initViews() {
        tv_city = (TextView) findViewById(R.id.tv_city);
        actv_keyword = (AutoCompleteTextView) findViewById(R.id.actv_keyword);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_record_hint = (TextView) findViewById(R.id.tv_record_hint);
        lv_search_record = (ListView) findViewById(R.id.lv_search_record);
        footer = LayoutInflater.from(this).inflate(R.layout.footer_view, null);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除所有搜索记录
                myDb.dropTableIfTableExist(SearchRecordBean.class);
                lv_search_record.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);
                tv_record_hint.setText("暂无搜索历史");
            }
        });

        tv_city.setText(city);

        tv_city.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        //搜索框增加搜索提示
        actv_keyword.addTextChangedListener(this);
    }

    private void initListView() {
        if (recordList != null && recordList.size() > 0) {
            lv_search_record.addFooterView(footer);
            adapter = new SearchRecordAdapter(this);
            adapter.setList((ArrayList<SearchRecordBean>) recordList);
            lv_search_record.setAdapter(adapter);
            tv_record_hint.setText("以下是您的搜索历史");

            lv_search_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //返回至上一个界面
                    Intent intent = new Intent();
                    intent.putExtra("name", recordList.get(i).getName());
                    intent.putExtra("district", recordList.get(i).getDistrict());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String newText = charSequence.toString().trim();
        Inputtips inputTips = new Inputtips(SearchPlaceActivity.this,
                new Inputtips.InputtipsListener() {

                    @Override
                    public void onGetInputtips(List<Tip> tipList, int rCode) {
                        if (rCode == 0) {// 正确返回
                            final List<String> nameString = new ArrayList<String>();
                            final List<String> districtString = new ArrayList<String>();
                            for (int i = 0; i < tipList.size(); i++) {
                                nameString.add(tipList.get(i).getName());
                                districtString.add(tipList.get(i).getDistrict());
                            }
                            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.route_inputs, nameString);
                            actv_keyword.setAdapter(aAdapter);
                            actv_keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //将点击的条目存进数据库
                                    SearchRecordBean bean = new SearchRecordBean();
                                    String name = nameString.get(i);
                                    String district = districtString.get(i);
                                    bean.setName(name);
                                    bean.setDistrict(district);

                                    //判断是否有相同的数据，若有，则不再进行存储操作
                                    if (!hasRepeatData(bean)) {
                                        myDb.save(bean);

                                    }

                                    //返回至上一个界面
                                    Intent intent = new Intent();
                                    intent.putExtra("name", name);
                                    intent.putExtra("district", district);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                            aAdapter.notifyDataSetChanged();
                        }
                    }
                });
        try {
            inputTips.requestInputtips(newText,  city);// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 查询数据库中内容相同的数据，若有，则返回true
     * @param bean
     * @return
     */
    private boolean hasRepeatData(SearchRecordBean bean) {
        boolean flag = false;
        List<SearchRecordBean> list = myDb.findAll(SearchRecordBean.class);
        if (list == null || list.size() == 0) {
            return false;
        }
        for (SearchRecordBean srBean : list) {
            if (srBean.getName().equals(bean.getName()) && srBean.getDistrict().equals(bean.getDistrict())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
