package com.example.express.activity.my.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.bean.QueryRecordBean;
import com.example.express.bean.RecordBean;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.utils.ImageLoaderUtil;
import com.example.express.view.ViewHolder;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/30 16:09
 * 修改人：xutework
 * 修改时间：2015/7/30 16:09
 * 修改备注：
 */
public class MyRecordAdapter extends ArrayListAdapter<QueryRecordBean> {

    private Activity context;

    private OnItemDeleteListener onItemDeleteListener;

    public MyRecordAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public MyRecordAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.record_item, null);
        }
        QueryRecordBean qrBean = getItem(position);
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_record_icon);
        ImageView iv_delete_icon = ViewHolder.get(convertView, R.id.iv_delete_icon);
        TextView tv_message_title = ViewHolder.get(convertView, R.id.tv_message_title);
        TextView tv_message_content = ViewHolder.get(convertView, R.id.tv_message_content);
        TextView tv_message_time = ViewHolder.get(convertView, R.id.tv_message_time);
        ImageLoaderUtil.getInstance().displayImage(qrBean.getIco(), iv_icon);
        tv_message_title.setText(qrBean.getCompany() + " " + qrBean.getNu());
        tv_message_time.setText(qrBean.getLatestTime());
        tv_message_content.setText(qrBean.getLatestContext());
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
