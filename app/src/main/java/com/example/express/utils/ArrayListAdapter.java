package com.example.express.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 10:34
 * 修改人：xutework
 * 修改时间：2015/7/29 10:34
 * 修改备注：
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {
    protected ArrayList<T> mList;
    protected Activity mContext;
    protected AbsListView mListView;
    protected int selectedItem=-1;

    private final Object mLock = new Object();

    public ArrayListAdapter(Activity context){
        this.mContext = context;
    }

    public ArrayListAdapter(Activity context,ListView listView){
        this.mContext = context;
        this.mListView = listView;
    }

    @Override
    public int getCount() {
        if(mList != null)
            return mList.size();
        else
            return 0;
    }

    @Override
    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

    public void setList(ArrayList<T> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public ArrayList<T> getList(){
        return mList;
    }

    public void setList(T[] list){
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public void setListWithSort(ArrayList<T> list,Comparator<? super T> comparator) {
        Collections.sort(list, comparator);
        setList(list);
    }

    public AbsListView getListView(){
        return mListView;
    }

    public void setListView(AbsListView listView){
        mListView = listView;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void remove(T bean){
        synchronized (mLock) {
            mList.remove(bean);
        }
        notifyDataSetChanged();
    }
}
