package com.example.express.activity.my.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.express.R;
import com.example.express.bean.RecordBean;
import com.example.express.utils.ArrayListAdapter;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/30 16:09
 * 修改人：xutework
 * 修改时间：2015/7/30 16:09
 * 修改备注：
 */
public class MyRecordAdapter extends ArrayListAdapter<RecordBean> {

    private Activity context;

    public MyRecordAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public MyRecordAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.record_item, null);
        }
        return convertView;
    }
}