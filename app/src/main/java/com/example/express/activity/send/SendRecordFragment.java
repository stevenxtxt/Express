package com.example.express.activity.send;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/31 13:50
 * 修改人：xutework
 * 修改时间：2015/7/31 13:50
 * 修改备注：
 */
public class SendRecordFragment extends Fragment {

    private View view;
    private Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (MainTabActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.send_record_frag, container, false);
        return view;
    }
}
