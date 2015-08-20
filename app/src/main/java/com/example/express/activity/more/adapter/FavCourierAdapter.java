package com.example.express.activity.more.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.bean.CourierBean;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.utils.ImageLoaderUtil;
import com.example.express.view.ViewHolder;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favourite_courier_item, null);
        }
        CourierBean courierBean = getItem(position);
        ImageView iv_icon = ViewHolder.get(convertView, R.id.item_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.item_name);
        TextView tv_company = ViewHolder.get(convertView, R.id.item_company);
        ImageLoaderUtil.getInstance().displayImage(courierBean.getIcon(), iv_icon);
        tv_name.setText(courierBean.getName());
        tv_company.setText(courierBean.getCompany());
        return convertView;
    }
}
