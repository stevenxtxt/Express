package com.example.express.activity.more;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.db.MyDb;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.more.adapter.RecycleAdapter;
import com.example.express.activity.query.ShowResultActivity;
import com.example.express.bean.RecycleItemBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/19 10:32
 * 修改人：xutework
 * 修改时间：2015/8/19 10:32
 * 修改备注：
 */
public class RecyclerActivity extends BaseActivity {
    private ListView lv_record;
    private LinearLayout ll_no_content;
    private RecycleAdapter adapter;
    private List<RecycleItemBean> recycleList = new ArrayList<RecycleItemBean>();

    private BaseApplication app;
    private MyDb myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);
        initTop();
        setTitle("回收箱");
        app = BaseApplication.getInstance();
        myDb = app.getMyDb();
        if (null == myDb) {
            myDb = MyDb.create(this, CommonConstants.DB_NAME, true);
        }
        recycleList = myDb.findAll(RecycleItemBean.class);
        initViews();
    }

    private void initViews() {
        lv_record = (ListView) findViewById(R.id.lv_record);
        ll_no_content = (LinearLayout) findViewById(R.id.ll_no_content);
        if (recycleList.size() == 0 || recycleList == null) {
            lv_record.setVisibility(View.GONE);
            ll_no_content.setVisibility(View.VISIBLE);
        } else {
            ll_no_content.setVisibility(View.GONE);
            lv_record.setVisibility(View.VISIBLE);
            adapter = new RecycleAdapter(this);

            //监听删除按钮事件
            adapter.setOnItemDeleteListener(new RecycleAdapter.OnItemDeleteListener() {
                @Override
                public void delete(int position) {
                    showDeleteDialog(position);
                }
            });

            adapter.setList((ArrayList<RecycleItemBean>) recycleList);
            lv_record.setAdapter(adapter);

            lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    queryExpressResult(i);
                }
            });
        }
    }

    /**
     * 查询快递
     */
    private void queryExpressResult(int position) {
        RecycleItemBean qrBean = recycleList.get(position);
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
                        intent.setClass(RecyclerActivity.this, ShowResultActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("查询失败");
                    }
                });
    }

    /**
     * 显示删除对话框
     * @param position
     */
    private void showDeleteDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(RecyclerActivity.this);
        View view = inflater.inflate(R.layout.delete_dialog_layout, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);

        final Dialog delete_dialog = new Dialog(RecyclerActivity.this, R.style.call_dialog);
        delete_dialog.setContentView(view);
        delete_dialog.setCanceledOnTouchOutside(true);
        delete_dialog.show();
        WindowManager.LayoutParams lp = delete_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(RecyclerActivity.this)); // 设置宽度
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
                myDb.deleteByWhereStr(RecycleItemBean.class, "nu='" + recycleList.get(position).getNu() + "'");
                recycleList.remove(position);
                adapter.setList((ArrayList<RecycleItemBean>) recycleList);
                lv_record.setAdapter(adapter);
                if (recycleList.size() == 0) {
                    ll_no_content.setVisibility(View.VISIBLE);
                    lv_record.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
