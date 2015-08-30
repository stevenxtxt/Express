package com.example.express.activity.send.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.bean.CourierBean;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.utils.ImageLoaderUtil;
import com.example.express.view.RoundImageView;
import com.example.express.view.ViewHolder;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/30 20:13
 * 修改人：xutework
 * 修改时间：2015/7/30 20:13
 * 修改备注：
 */
public class SendCourierAdapter extends ArrayListAdapter<CourierBean> {

    private Activity context;
    private BaseApplication app;
    private String address;

    public SendCourierAdapter(Activity context) {
        super(context);
        this.context = context;
        app = BaseApplication.getInstance();
    }

    public SendCourierAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.send_courier_item, null);
        }
        CourierBean courierBean = getItem(position);
        RoundImageView riv_courier_icon = ViewHolder.get(convertView, R.id.riv_courier_icon);
        TextView tv_courier_name = ViewHolder.get(convertView, R.id.tv_courier_name);
        TextView tv_courier_company = ViewHolder.get(convertView, R.id.tv_courier_company);
        TextView tv_courier_scope = ViewHolder.get(convertView, R.id.tv_courier_scope);

//        ImageLoaderUtil.getInstance().displayImage(courierBean.getIcon(), riv_courier_icon);
        int resId = context.getResources().getIdentifier(courierBean.getExname() + "_logo", "drawable", context.getPackageName());
        riv_courier_icon.setBackgroundResource(resId);
        tv_courier_name.setText(courierBean.getName());
        tv_courier_company.setText(courierBean.getCompany());
        if (address == null || address.equals("")) {
            tv_courier_scope.setText(app.getExpressAddress() + "...");
        } else {
            tv_courier_scope.setText(address + "...");
        }
        return convertView;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
