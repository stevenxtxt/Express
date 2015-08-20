package com.example.express.activity.more.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.bean.MessageBean;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.view.ViewHolder;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 16:15
 * 修改人：xutework
 * 修改时间：2015/7/29 16:15
 * 修改备注：
 */
public class MessageAdapter extends ArrayListAdapter<MessageBean> {

    private Activity context;

    private OnItemDeleteListener onItemDeleteListener;

    public MessageAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public MessageAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, null);
        }
        MessageBean mBean = getItem(position);
        ImageView iv_message_icon = ViewHolder.get(convertView, R.id.iv_message_icon);
        ImageView iv_delete_icon = ViewHolder.get(convertView, R.id.iv_delete_icon);
        TextView tv_message_title = ViewHolder.get(convertView, R.id.tv_message_title);
        TextView tv_message_time = ViewHolder.get(convertView, R.id.tv_message_time);
        if (mBean.getIsRead().equals("0")) {
            iv_message_icon.setBackgroundResource(R.drawable.yidu);
        } else if (mBean.getIsRead().equals("1")) {
            iv_message_icon.setBackgroundResource(R.drawable.weidu);
        }
        tv_message_title.setText(mBean.getTitle());
        tv_message_time.setText(mBean.getTime());
        iv_delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemDeleteListener.delete(position);
            }
        });

        return convertView;
    }

    public interface OnItemDeleteListener {
        void delete(int position);
    }
}
