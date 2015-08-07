package com.example.express.activity.send.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.example.express.R;
import com.example.express.utils.ArrayListAdapter;
import com.example.express.view.ViewHolder;

import java.util.List;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/4 15:58
 * 修改人：xutework
 * 修改时间：2015/8/4 15:58
 * 修改备注：
 */
public class PoiAdapter extends ArrayListAdapter<PoiItem> {

    private Activity context;

    private List<PoiItem> poiList;

    public PoiAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public PoiAdapter(Activity context, ListView listView) {
        super(context, listView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nearby_poi_item, parent, false);
        }
        TextView tv_item_name = ViewHolder.get(convertView, R.id.tv_poi_item_name);
        TextView tv_item_district = ViewHolder.get(convertView, R.id.tv_poi_item_district);
        ImageView iv_item_icon = ViewHolder.get(convertView, R.id.iv_poi_item_icon);

        PoiItem poiItem = getItem(position);

        tv_item_name.setText(poiItem.getSnippet());
        tv_item_district.setText(poiItem.getAdName());

        return convertView;
    }
}
