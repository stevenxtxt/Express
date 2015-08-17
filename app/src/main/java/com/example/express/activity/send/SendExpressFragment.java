package com.example.express.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.express.activity.BaseFragment;
import com.example.express.R;

public class SendExpressFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView tv_nearby;
    private TextView tv_record;
    private SendNearbyFragment nearbyFragment;
    private SendRecordFragment recordFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_send, null);
        initViews();

        return view;
    }

    private void initViews() {
        tv_nearby = (TextView) view.findViewById(R.id.tv_nearby);
        tv_record = (TextView) view.findViewById(R.id.tv_record);

        tv_nearby.setOnClickListener(this);
        tv_record.setOnClickListener(this);

        setDefaultFragment();
    }

    /**
     *设置默认的Fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        nearbyFragment = new SendNearbyFragment();
        ft.replace(R.id.fl_content, nearbyFragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.tv_nearby:
                if (recordFragment != null) {
                    ft.hide(recordFragment);
                }
                if (nearbyFragment == null) {
                    nearbyFragment = new SendNearbyFragment();
                    ft.add(R.id.fl_content, nearbyFragment);
                }
                ft.show(nearbyFragment);
                tv_nearby.setBackgroundResource(R.drawable.top_left);
                tv_nearby.setTextColor(activity.getResources().getColor(R.color.blue_top));
                tv_record.setBackgroundResource(R.drawable.top_right_press);
                tv_record.setTextColor(activity.getResources().getColor(R.color.white));
                break;

            case R.id.tv_record:
                if (nearbyFragment != null) {
                    ft.hide(nearbyFragment);
                }
                if (recordFragment == null) {
                    recordFragment = new SendRecordFragment();
                    ft.add(R.id.fl_content, recordFragment);
                }
                ft.show(recordFragment);
                ft.replace(R.id.fl_content, recordFragment);
                tv_nearby.setBackgroundResource(R.drawable.top_left_press);
                tv_nearby.setTextColor(activity.getResources().getColor(R.color.white));
                tv_record.setBackgroundResource(R.drawable.top_right);
                tv_record.setTextColor(activity.getResources().getColor(R.color.blue_top));
                break;

            default:
                break;
        }
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            String address = data.getStringExtra("address");
            Double latitude = data.getDoubleExtra("latitude", 0);
            Double longitude = data.getDoubleExtra("longitude", 0);
            nearbyFragment.setData(address, latitude, longitude);
        }
    }
}