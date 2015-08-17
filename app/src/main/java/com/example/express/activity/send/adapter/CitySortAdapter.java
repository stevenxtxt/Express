package com.example.express.activity.send.adapter;

import java.util.List;

import com.example.express.R;
import com.example.express.activity.send.SortModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CitySortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private Context mContext;

    public CitySortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.city_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.v_top_line = view.findViewById(R.id.v_top_line);
            viewHolder.v_bottom_line = view.findViewById(R.id.v_bottom_line);
            viewHolder.v_group_line = view.findViewById(R.id.v_group_line);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        boolean isFirtPostion = position == getPositionForSection(section);
        if (isFirtPostion) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.v_top_line.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
            viewHolder.v_top_line.setVisibility(View.GONE);
        }

        boolean nextIsFistPostion = (getCount() > position + 1) ? ((position + 1) == getPositionForSection(getSectionForPosition(position + 1)))
                : false;
        if (nextIsFistPostion) {
            viewHolder.v_group_line.setVisibility(View.GONE);
            viewHolder.v_bottom_line.setVisibility(View.VISIBLE);
        } else {
            viewHolder.v_group_line.setVisibility(View.VISIBLE);
            viewHolder.v_bottom_line.setVisibility(View.GONE);
        }
        
        viewHolder.tvTitle.setText(this.list.get(position).getName());

        return view;

    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        View v_top_line;
        View v_bottom_line;
        View v_group_line;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     * 
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}