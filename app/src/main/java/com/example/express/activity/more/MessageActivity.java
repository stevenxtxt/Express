package com.example.express.activity.more;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.more.adapter.MessageAdapter;
import com.example.express.bean.MessageBean;

import java.util.ArrayList;

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
    private MessageAdapter adapter;
    private ArrayList<MessageBean> messageList = new ArrayList<MessageBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        initTop();
        setTitle("消息");

        lv_message = (ListView) findViewById(R.id.lv_message);
        adapter = new MessageAdapter(MessageActivity.this);
        adapter.setList(messageList);
        lv_message.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
