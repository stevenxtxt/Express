package com.example.express.activity.more.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.express.R;
import com.example.express.bean.CourierBean;
import com.example.express.utils.ArrayListAdapter;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 10:42
 * 修改人：xutework
 * 修改时间：2015/7/29 10:42
 * 修改备注：
 */
public class FavCourierAdapter extends ArrayListAdapter<CourierBean> {

    private Activity context;

    public FavCourierAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public FavCourierAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favourite_courier_item, null);
        }
//        CourierBean courierBean = (CourierBean) getItem(position);
        return convertView;
    }
}
