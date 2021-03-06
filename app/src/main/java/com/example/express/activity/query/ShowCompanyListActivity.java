package com.example.express.activity.query;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.send.SortModel;
import com.example.express.bean.CompanyBean;
import com.example.express.bean.CompanyListBean;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.utils.Density;
import com.example.express.view.ClearEditText;
import com.example.express.view.ViewHolder;
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

    private View hotView;
    private GridView gv_hot_company;
    private TextView tv_hot_title;
    private HotCompanyAdapter hotCompanyAdapter;
    private ArrayList<PhoneModel> hotCompanyList;

    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = BaseApplication.getInstance();

//        Intent intent = getIntent();
//        exjson = intent.getStringExtra("json");
//        getJsonData(exjson);
        initJsonData();
        getJsonDataLocal(jsonObject);
        setContentView(R.layout.activity_company);
        initViews();
    }

    /**
     * 从网络数据获取公司信息
     *
     * @param str
     */
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
     * 读取本地json文件内容
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("ExpressCompany.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            jsonObject = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地获取公司信息
     *
     * @param obj
     */
    private void getJsonDataLocal(JSONObject obj) {
        companyListBean = new CompanyListBean();
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
//                if (name.indexOf(filterStr.toString()) != -1
//                        || filterStr.toString().toUpperCase()
//                        .startsWith(sortModel.getSortLetters())) {
                if (name.indexOf(filterStr.toString()) != -1
                        || filterStr.toString().toUpperCase().startsWith(sortModel.getSortLetters())
                        || characterParser.getSelling(name).startsWith(filterStr.toString())) {
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

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
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

        hotView = LayoutInflater.from(this).inflate(R.layout.hot_company, sortListView, false);
        gv_hot_company = (GridView) hotView.findViewById(R.id.gv_hot_city);
        tv_hot_title = (TextView) hotView.findViewById(R.id.tv_title);
        tv_hot_title.setText("常用快递");
        hotCompanyAdapter = new HotCompanyAdapter(this);
        hotCompanyList = new ArrayList<PhoneModel>();

        //手动添加热门城市
        setHotCompany();

        hotCompanyAdapter.setList(hotCompanyList);
        gv_hot_company.setAdapter(hotCompanyAdapter);
        sortListView.addHeaderView(hotView, null, false);
        //点击热门城市的事件监听
        gv_hot_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PhoneModel pm = ((PhoneModel) hotCompanyAdapter.getItem(i));
                Intent intent = new Intent();
                intent.putExtra("company", pm.getName());
                intent.putExtra("companytype", pm.getCompanytype());
                app.setCompany(pm.getName());
                app.setCom(pm.getCompanytype());
                setResult(1001, intent);
                finish();
            }
        });


        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneModel pm;
                TextView textview = (TextView) findViewById(R.id.company_name);
                if (sortListView.getHeaderViewsCount() != 0) {
                    pm = ((PhoneModel) adapter.getItem(position - 1));
                } else {
                    pm = ((PhoneModel) adapter.getItem(position));
                }
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
//                filterData(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (hotView != null) {
                    sortListView.removeHeaderView(hotView);
                }
                filterData(mClearEditText.getText().toString().trim());

                if (mClearEditText.getText().toString().trim().length() == 0) {
                    sortListView.addHeaderView(hotView, null, false);
                    Collections.sort(SourceDateList, pinyinComparator);
                    adapter = new SortAdapter(context, SourceDateList);
                    sortListView.setAdapter(adapter);
                }
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

    private void setHotCompany() {
        String[] hotcompanies = getResources().getStringArray(R.array.hot_companies);
        String[] hotcoms = getResources().getStringArray(R.array.hot_coms);
        String[] hotphones = getResources().getStringArray(R.array.hot_phones);
        int[] icons = new int[]{R.drawable.zhongtong_logo, R.drawable.shunfeng_logo, R.drawable.shentong_logo,
                R.drawable.yuantong_logo, R.drawable.huitongkuaidi_logo, R.drawable.yunda_logo,
                R.drawable.ems_logo, R.drawable.quanfengkuaidi_logo, R.drawable.tiantian_logo, R.drawable.zhaijisong_logo};
        for (int i = 0; i < hotcompanies.length; i++) {
            PhoneModel pm = new PhoneModel();
            pm.setName(hotcompanies[i]);
            pm.setCompanytype(hotcoms[i]);
            pm.setPhone(hotphones[i]);
            Drawable icon = getResources().getDrawable(icons[i]);
            pm.setComicon(icon);
            hotCompanyList.add(pm);
        }
    }

    class HotCompanyAdapter extends ArrayListAdapter<PhoneModel> {

        public AbsListView.LayoutParams params;

        public HotCompanyAdapter(Activity context) {
            super(context);
            params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Density.of(mContext, 40));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_company_item, null);
            }
            PhoneModel pm = getItem(position);
            ImageView iv_company_logo = ViewHolder.get(convertView, R.id.company_logo);
            TextView tv_company_name = ViewHolder.get(convertView, R.id.company_name);
            TextView tv_company_phone = ViewHolder.get(convertView, R.id.company_phone);
            View v_bottom_line = ViewHolder.get(convertView, R.id.v_bottom_line);
            if (position == mList.size() - 1) {
                v_bottom_line.setVisibility(View.GONE);
            } else {
                v_bottom_line.setVisibility(View.VISIBLE);
            }
            iv_company_logo.setImageDrawable(pm.getComicon());
            tv_company_name.setText(pm.getName());
            tv_company_phone.setText(pm.getPhone());
            return convertView;
        }
    }
}
