package com.example.express.activity.query.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.bean.QueryResultItemBean;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.view.ViewHolder;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/30 16:17
 * 修改人：xutework
 * 修改时间：2015/7/30 16:17
 * 修改备注：
 */
public class ResultAdapter extends ArrayListAdapter<QueryResultItemBean> {

    private Activity context;

    public ResultAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public ResultAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.query_result_item, null);
        }
        QueryResultItemBean bean = getItem(position);
        TextView tv_result_date = ViewHolder.get(convertView, R.id.tv_result_date);
        TextView tv_result_time = ViewHolder.get(convertView, R.id.tv_result_time);
        TextView tv_result_content = ViewHolder.get(convertView, R.id.tv_result_content);
        ImageView iv_result_divider = ViewHolder.get(convertView, R.id.iv_result_divider);
        String[] time = bean.getTime().split(" ");
        tv_result_date.setText(time[0]);
        tv_result_time.setText(time[1].substring(0, time[1].length() - 3));
        tv_result_content.setText(bean.getContext());
        if (position == 0) {

            tv_result_content.setTextColor(context.getResources().getColor(R.color.blue_top));
            tv_result_date.setTextColor(context.getResources().getColor(R.color.blue_top));
            tv_result_time.setTextColor(context.getResources().getColor(R.color.blue_top));
            iv_result_divider.setBackgroundResource(R.drawable.gou);
        } else {
            tv_result_content.setTextColor(Color.parseColor("#666666"));
            tv_result_date.setTextColor(Color.parseColor("#666666"));
            tv_result_time.setTextColor(Color.parseColor("#666666"));
            iv_result_divider.setBackgroundResource(R.drawable.shang);
        }
        return convertView;
    }
}
