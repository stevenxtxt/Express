package com.example.express.activity.query;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.R;
import com.example.express.activity.BaseFragment;
import com.example.express.bean.CompanyBean;
import com.example.express.bean.CompanyListBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;
import com.example.express.view.ClearEditText;
import com.silent.adapter.SortAdapter;
import com.silent.handle.CharacterParser;
import com.silent.handle.PinyinComparator;
import com.silent.handle.SideBar;
import com.silent.model.PhoneModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/14 16:51
 * 修改人：xutework
 * 修改时间：2015/8/14 16:51
 * 修改备注：
 */
public class QueryContactsFragment extends BaseFragment implements View.OnClickListener{
    private View view;
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

    private CompanyListBean companyListBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = View.inflate(getActivity(), R.layout.frag_contacts, null);
        queryCompany();
        return view;
    }

    @Override
    public void onClick(View view) {

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

        mClearEditText = (ClearEditText) view.findViewById(R.id.cet_filter);
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneModel pm = ((PhoneModel) adapter.getItem(position));
                showCallDialog(pm.getPhone());
            }
        });
//		data_name=getData("name");
//		data_phone=getData("phone");
//		data_img=getData("ico");
        SourceDateList = filledData(companyListBean);

        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(getActivity(), SourceDateList);
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
                if (null != getActivity().getCurrentFocus())
                    ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity()
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    /**
     * 查询快递公司
     */
    private void queryCompany() {
        showCustomDialog("正在努力查询中...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_EXPRESS_COMPANY + CommonConstants.HTML,
                new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        getJsonData(response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        Toast.makeText(activity, "查询失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getJsonData(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            companyListBean = new CompanyListBean();
            companyListBean.setResult(obj.getBoolean("success"));
            companyListBean.setReason(obj.getString("reason"));
            JSONArray arr = obj.getJSONArray("data");
            if (arr != null && arr.length() > 0) {
                ArrayList<CompanyBean> list = new ArrayList<CompanyBean>();
                for (int i = 0; i < arr.length(); i++) {
                    CompanyBean companyBean = new CompanyBean();
                    JSONObject arrObj = arr.getJSONObject(i);
                    companyBean.setCompanytype(arrObj.getString("companytype"));
                    companyBean.setCompany(arrObj.getString("company"));
                    companyBean.setPhone(arrObj.getString("phone"));
                    companyBean.setIco(arrObj.getString("ico"));
                    list.add(companyBean);
                }
                companyListBean.setData(list);
                initViews();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 打电话对话框
     * @param phoneNo
     */
    private void showCallDialog(final String phoneNo) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.call_dialog_layout, null);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone_no);
        TextView tv_zhuyi = (TextView) view.findViewById(R.id.tv_zhuyi);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_call = (Button) view.findViewById(R.id.btn_call);
        tv_phone.setText(phoneNo);
        tv_zhuyi.setVisibility(View.GONE);

        final Dialog call_dialog = new Dialog(getActivity(), R.style.call_dialog);
        call_dialog.setCanceledOnTouchOutside(true);
        call_dialog.setContentView(view);
        call_dialog.show();
        WindowManager.LayoutParams lp = call_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(getActivity())); // 设置宽度
        call_dialog.getWindow().setAttributes(lp);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
            }
        });
    }
}
