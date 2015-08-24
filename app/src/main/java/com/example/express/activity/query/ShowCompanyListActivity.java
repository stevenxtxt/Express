package com.example.express.activity.query;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.bean.CompanyBean;
import com.example.express.bean.CompanyListBean;
import com.example.express.view.ClearEditText;
import com.google.gson.Gson;
import com.silent.adapter.SortAdapter;
import com.silent.handle.CharacterParser;
import com.silent.handle.PinyinComparator;
import com.silent.handle.SideBar;
import com.silent.handle.SideBar.OnTouchingLetterChangedListener;
import com.silent.model.PhoneModel;

/**
 * @author Mr.Z
 */
public class ShowCompanyListActivity extends BaseActivity {
    private Context context = ShowCompanyListActivity.this;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;

    private CharacterParser characterParser;
    private List<PhoneModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private String exjson;

    private CompanyListBean companyListBean;

    private BaseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = BaseApplication.getInstance();

        Intent intent = getIntent();
        exjson = intent.getStringExtra("json");
        getJsonData(exjson);
        setContentView(R.layout.activity_company);
        initViews();
    }

	private void getJsonData(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            companyListBean = new CompanyListBean();
            companyListBean.setResult(obj.optBoolean("result"));
            companyListBean.setReason(obj.optString("reason"));
            JSONArray arr = obj.optJSONArray("data");
            if (arr != null && arr.length() > 0) {
                ArrayList<CompanyBean> list = new ArrayList<CompanyBean>();
                for (int i = 0; i < arr.length(); i++) {
                    CompanyBean companyBean = new CompanyBean();
                    JSONObject arrObj = arr.optJSONObject(i);
                    companyBean.setCompanytype(arrObj.optString("companytype"));
                    companyBean.setCompany(arrObj.optString("company"));
                    companyBean.setPhone(arrObj.optString("phone"));
                    companyBean.setIco(arrObj.optString("ico"));
                    list.add(companyBean);
                }
                companyListBean.setData(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 为ListView填充数据
     *
     * @param listBean
     * @return
     */
    private List<PhoneModel> filledData(CompanyListBean listBean) {
        List<PhoneModel> mSortList = new ArrayList<PhoneModel>();
        ArrayList<CompanyBean> companyBeans = listBean.getData();
        int size = companyBeans.size();
        for (int i = 0; i < size; i++) {
            PhoneModel sortModel = new PhoneModel();
            CompanyBean bean = companyBeans.get(i);
            sortModel.setImgSrc(bean.getIco());
            sortModel.setName(bean.getCompany());
            sortModel.setPhone(bean.getPhone());
            sortModel.setCompanytype(bean.getCompanytype());
            String pinyin = characterParser.getSelling(bean.getCompany());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        if (null == SourceDateList) {
            return;
        }
        List<PhoneModel> filterDateList = new ArrayList<PhoneModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (PhoneModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || filterStr.toString().toUpperCase()
                        .startsWith(sortModel.getSortLetters())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    private void initViews() {

        initTop();
        setTitle("选择快递公司");

        mClearEditText = (ClearEditText) findViewById(R.id.cet_filter);
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textview = (TextView) findViewById(R.id.company_name);
                PhoneModel pm = ((PhoneModel) adapter.getItem(position));
                Intent intent = new Intent();
                intent.putExtra("company", pm.getName());
                intent.putExtra("companytype", pm.getCompanytype());
                app.setCompany(pm.getName());
                app.setCom(pm.getCompanytype());
                setResult(1001, intent);
                finish();

            }
        });
//		data_name=getData("name");
//		data_phone=getData("phone");
//		data_img=getData("ico");
        SourceDateList = filledData(companyListBean);

        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(context, SourceDateList);
        sortListView.setAdapter(adapter);

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //点击列表隐藏输入法
        sortListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (null != getCurrentFocus())
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ShowCompanyListActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }
}
