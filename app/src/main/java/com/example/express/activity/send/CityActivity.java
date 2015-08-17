package com.example.express.activity.send;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.db.CityDBManager;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.send.adapter.CitySortAdapter;
import com.example.express.bean.CityInfo;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.utils.Density;
import com.example.express.view.ClearEditText;
import com.silent.handle.CharacterParser;
import com.silent.handle.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/7 16:19
 * 修改人：xutework
 * 修改时间：2015/8/7 16:19
 * 修改备注：
 */
public class CityActivity extends BaseActivity {

    private ClearEditText mClearEditText;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private CitySortAdapter adapter;
    private TextView tv_current_city;

    private MyPinyinComparator pinyinComparator;
    /**
     * 汉字转换成拼音的类
     */
    private List<SortModel> sourceDateList;
    private CharacterParser characterParser;

    /**
     * 热门城市相关
     */
    private GridView gv_hot_city;
    private HotCityAdapter hotCityAdapter;
    private ArrayList<SortModel> hotCityModel;

    /**
     * 查询本地数据库获取城市相关
     */
    private CityDBManager cityDBManager;
    private List<CityInfo> cityList;
    private String[] cityNames;
    private String[] cityCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_main);
        initTop();
        setTitle("选择城市");

        queryLocalCity();

        initViews();
    }

    /**
     * 查询本地数据库，获取所有城市
     */
    private void queryLocalCity() {
        cityDBManager = new CityDBManager(this);
        cityList = cityDBManager.queryAllinfo();
        int size = cityList.size();
        cityNames = new String[size];
        cityCodes = new String[size];
        CityInfo ci;
        for (int i = 0; i < size; i++) {
            ci = cityList.get(i);
            cityNames[i] = ci.getName();
            cityCodes[i] = ci.getCode();
        }
    }

    private void initViews() {
        characterParser = CharacterParser.getInstance();
        mClearEditText = (ClearEditText) findViewById(R.id.cet_filter);
        sortListView = (ListView) findViewById(R.id.lv_country);
        sideBar = (SideBar) findViewById(R.id.sb_sidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        tv_current_city = (TextView) findViewById(R.id.tv_current_city);
        tv_current_city.setText("当前城市：" + BaseApplication.getInstance().getExpressCity());

        pinyinComparator = new MyPinyinComparator();
        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (CityActivity.this.getCurrentFocus() != null) { // 是否存在焦点
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(CityActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                // 该字母首次出现的位置
                if (null == adapter) {
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != AbsListView.INVALID_POSITION) {
                    sortListView.setSelection(position);
                }

            }
        });

        View hotView = LayoutInflater.from(this).inflate(R.layout.hot_city, sortListView, false);
        gv_hot_city= (GridView) hotView.findViewById(R.id.gv_hot_city);
        hotCityAdapter=new HotCityAdapter(this);
        hotCityModel=new ArrayList<SortModel>();

        //手动添加热门城市
        setHotCity();

        hotCityAdapter.setList(hotCityModel);
        gv_hot_city.setAdapter(hotCityAdapter);
        sortListView.addHeaderView(hotView, null, false);
        //点击热门城市的事件监听
        gv_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent hot_intent = new Intent();
                hot_intent.putExtra("code", hotCityModel.get(i).getCode());
                BaseApplication.getInstance().setExpressCity(hotCityModel.get(i).getName());
                setResult(RESULT_OK, hot_intent);
                finish();
            }
        });

        //点击列表的事件监听
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SortModel model = (SortModel) sortListView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.putExtra("code", model.getCode());
                BaseApplication.getInstance().setExpressCity(model.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        sourceDateList = filledData(cityNames, cityCodes);
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);
        adapter = new CitySortAdapter(CityActivity.this, sourceDateList);
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
                            .hideSoftInputFromWindow(CityActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
    }

    private List<SortModel> filledData(String[] data, String[] codes) {
        List<SortModel> sortList = new ArrayList<SortModel>();
        for (int i = 0; i < data.length; i++) {
            SortModel sort = new SortModel();
            sort.setName(data[i]);
            sort.setCode(codes[i]);
            String pinyin = characterParser.getSelling(data[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")) {
                sort.setSortLetters(sortString.toUpperCase());
            } else {
                sort.setSortLetters("#");
            }
            sortList.add(sort);
        }
        return sortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        if (null == sourceDateList) {
            return;
        }
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : sourceDateList) {
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

    private void setHotCity() {
        SortModel sm1 = new SortModel();
        sm1.setName("北京市");
        sm1.setCode("110100");
        hotCityModel.add(sm1);

        SortModel sm2 = new SortModel();
        sm2.setName("南京市");
        sm2.setCode("320100");
        hotCityModel.add(sm2);
    }

    class HotCityAdapter extends ArrayListAdapter<SortModel> {

        public AbsListView.LayoutParams  params;
        public HotCityAdapter(Activity context) {
            super(context);
            params=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Density.of(mContext, 40));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view;
            if(null==convertView){
                view=new TextView(mContext);
                view.setLayoutParams(params);
                view.setGravity(Gravity.CENTER);
                view.setTextColor(Color.parseColor("#4c4c4c"));
                view.setBackgroundResource(R.drawable.shape_white_rec);
            }else{
                view= (TextView) convertView;
            }
            view.setText(mList.get(position).getName());
            return view;
        }
    }
}
