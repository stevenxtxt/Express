package com.example.express.activity.my;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.db.MyDb;
import com.example.express.BaseApplication;
import com.example.express.activity.BaseFragment;
import com.example.express.R;
import com.example.express.activity.my.adapter.MyRecordAdapter;
import com.example.express.activity.query.ShowResultActivity;
import com.example.express.bean.LoginUser;
import com.example.express.bean.QueryRecordBean;
import com.example.express.bean.RecordBean;
import com.example.express.bean.RecycleItemBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView tv_received;
    private TextView tv_not_received;
    private LinearLayout ll_show_login;
    private ListView lv_record;
    private LinearLayout ll_no_content;
    private Button btn_login;
    private MyRecordAdapter adapter;
    private List<QueryRecordBean> recordList = new ArrayList<QueryRecordBean>();


    private BaseApplication app;
    private MyDb myDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = BaseApplication.getInstance();
        myDb = app.getMyDb();
        if (null == myDb) {
            myDb = MyDb.create(activity, CommonConstants.DB_NAME, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_my, null);
        initViews();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recordList = myDb.findAll(QueryRecordBean.class);
        //判断是否已经登录
        LoginUser loginUser = BaseApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            ll_show_login.setVisibility(View.VISIBLE);
            lv_record.setVisibility(View.GONE);
            ll_no_content.setVisibility(View.GONE);
        } else {
            ll_show_login.setVisibility(View.GONE);
            if (recordList.size() == 0 || recordList == null) {
                lv_record.setVisibility(View.GONE);
                ll_no_content.setVisibility(View.VISIBLE);
            } else {
                ll_no_content.setVisibility(View.GONE);
                lv_record.setVisibility(View.VISIBLE);
                adapter = new MyRecordAdapter(activity);

                //监听删除按钮事件
                adapter.setOnItemDeleteListener(new MyRecordAdapter.OnItemDeleteListener() {
                    @Override
                    public void delete(int position) {
                        showDeleteDialog(position);
                    }
                });

                adapter.setList((ArrayList<QueryRecordBean>) recordList);
                lv_record.setAdapter(adapter);
            }
        }
    }

    private void initViews() {
        tv_received = (TextView) view.findViewById(R.id.tv_received);
        tv_not_received = (TextView) view.findViewById(R.id.tv_not_received);
        ll_show_login = (LinearLayout) view.findViewById(R.id.ll_show_login);
        lv_record = (ListView) view.findViewById(R.id.lv_record);
        ll_no_content = (LinearLayout) view.findViewById(R.id.ll_no_content);
        btn_login = (Button) view.findViewById(R.id.btn_login);


        tv_received.setOnClickListener(this);
        tv_not_received.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                queryExpressResult(i);
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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 查询快递
     */
    private void queryExpressResult(int position) {
        QueryRecordBean qrBean = recordList.get(position);
        String nu = qrBean.getNu();
        String com = qrBean.getCompanytype();
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

    /**
     * 显示删除对话框
     *
     * @param position
     */
    private void showDeleteDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.delete_dialog_layout, null);
        TextView tv_zhuyi = (TextView) view.findViewById(R.id.tv_zhuyi);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        tv_zhuyi.setText("删除后的记录可在回收箱中查看");

        final Dialog delete_dialog = new Dialog(activity, R.style.call_dialog);
        delete_dialog.setContentView(view);
        delete_dialog.setCanceledOnTouchOutside(true);
        delete_dialog.show();
        WindowManager.LayoutParams lp = delete_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(getActivity())); // 设置宽度
        delete_dialog.getWindow().setAttributes(lp);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_dialog.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_dialog.dismiss();
                //将删除的条目放进回收箱
                RecycleItemBean riBean = new RecycleItemBean();
                QueryRecordBean queryRecordBean = recordList.get(position);
                riBean.setNu(queryRecordBean.getNu());
                riBean.setCompany(queryRecordBean.getCompany());
                riBean.setCompanytype(queryRecordBean.getCompanytype());
                riBean.setIco(queryRecordBean.getIco());
                riBean.setLatestTime(queryRecordBean.getLatestTime());
                riBean.setLatestContext(queryRecordBean.getLatestContext());
                myDb.save(riBean);

                myDb.deleteByWhereStr(QueryRecordBean.class, "nu='" + queryRecordBean.getNu() + "'");
                recordList.remove(position);
                adapter.setList((ArrayList<QueryRecordBean>) recordList);
                lv_record.setAdapter(adapter);
                if (recordList.size() == 0) {
                    ll_no_content.setVisibility(View.VISIBLE);
                    lv_record.setVisibility(View.GONE);
                    ll_show_login.setVisibility(View.GONE);
                }
            }
        });
    }
}