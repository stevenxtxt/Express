package com.example.express.activity.more;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.db.MyDb;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.more.adapter.MessageAdapter;
import com.example.express.bean.MessageBean;
import com.example.express.bean.QueryRecordBean;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Density;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 15:26
 * 修改人：xutework
 * 修改时间：2015/7/29 15:26
 * 修改备注：
 */
public class MessageActivity extends BaseActivity {

    private ListView lv_message;
    private LinearLayout ll_no_content;
    private MessageAdapter adapter;
    private ArrayList<MessageBean> messageList = new ArrayList<MessageBean>();

    private BaseApplication app;
    private MyDb myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        initTop();
        setTitle("消息");

        lv_message = (ListView) findViewById(R.id.lv_message);
        ll_no_content = (LinearLayout) findViewById(R.id.ll_no_content);

        /****测试数据，将查询的快递记录假设为消息，便于展示****/
        app = BaseApplication.getInstance();
        myDb = app.getMyDb();
        if (null == myDb) {
            myDb = MyDb.create(this, CommonConstants.DB_NAME, true);
        }
        List<QueryRecordBean> recordList = myDb.findAll(QueryRecordBean.class);
        if (recordList == null || recordList.size() == 0) {
            ll_no_content.setVisibility(View.VISIBLE);
            lv_message.setVisibility(View.GONE);
        } else {
            for (QueryRecordBean qrBean : recordList) {
                MessageBean mBean = new MessageBean();
                mBean.setTitle(qrBean.getCompany() + " " + qrBean.getNu());
                mBean.setTime(qrBean.getLatestTime());
                mBean.setIsRead("0");
                messageList.add(mBean);
            }
            ll_no_content.setVisibility(View.GONE);
            lv_message.setVisibility(View.VISIBLE);
            adapter = new MessageAdapter(MessageActivity.this);
            adapter.setList(messageList);
            adapter.setOnItemDeleteListener(new MessageAdapter.OnItemDeleteListener() {
                @Override
                public void delete(int position) {
                    showDeleteDialog(position);
                }
            });
            lv_message.setAdapter(adapter);
        }

    }

    /**
     * 显示删除对话框
     * @param position
     */
    private void showDeleteDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(MessageActivity.this);
        View view = inflater.inflate(R.layout.delete_dialog_layout, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);

        final Dialog delete_dialog = new Dialog(MessageActivity.this, R.style.call_dialog);
        delete_dialog.setContentView(view);
        delete_dialog.setCanceledOnTouchOutside(true);
        delete_dialog.show();
        WindowManager.LayoutParams lp = delete_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(MessageActivity.this)); // 设置宽度
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

                messageList.remove(position);
                adapter.setList((ArrayList<MessageBean>) messageList);
                lv_message.setAdapter(adapter);
                if (messageList.size() == 0) {
                    ll_no_content.setVisibility(View.VISIBLE);
                    lv_message.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
