package com.silent.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.ImageLoaderUtil;
import com.silent.handle.ImageLoader;
import com.silent.model.PhoneModel;

/**
 * @author Mr.Z
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<PhoneModel> list = null;
    private Context mContext;
    public ImageLoader imageLoader;

    public SortAdapter(Context mContext, List<PhoneModel> list) {
        this.mContext = mContext;
        this.list = list;
        imageLoader = new ImageLoader(mContext);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<PhoneModel> list) {
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
        final PhoneModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.company_list, null);
            viewHolder.tvHead = (ImageView) view.findViewById(R.id.company_logo);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.company_name);
            viewHolder.tvPhone = (TextView) view.findViewById(R.id.company_phone);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        PhoneModel pm = this.list.get(position);
//		ImageLoaderUtil.getInstance().displayImage(CommonConstants.URLConstant + this.list.get(position).getImgSrc() , viewHolder.tvHead);
		int resId = mContext.getResources().getIdentifier(pm.getImgSrc(), "drawable", mContext.getPackageName());
        viewHolder.tvHead.setBackgroundResource(resId);
        viewHolder.tvTitle.setText(pm.getName());
        viewHolder.tvPhone.setText(pm.getPhone());
        // viewHolder.tvTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        return view;

    }

    final static class ViewHolder {
        ImageView tvHead;
        TextView tvLetter;
        TextView tvTitle;
        TextView tvPhone;
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
