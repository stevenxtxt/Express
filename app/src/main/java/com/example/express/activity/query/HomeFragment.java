package com.example.express.activity.query;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseFragment;
import com.example.express.utils.Logger;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/14 16:45
 * 修改人：xutework
 * 修改时间：2015/8/14 16:45
 * 修改备注：
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private TextView tv_query;
    private TextView tv_contacts;
    private QueryExpressFragment expressFragment;
    private QueryContactsFragment contactsFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private BaseApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        app = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_home, null);
        initViews();

        return view;
    }

    private void initViews() {
        tv_contacts = (TextView) view.findViewById(R.id.tv_contacts);
        tv_query = (TextView) view.findViewById(R.id.tv_query);

        tv_query.setOnClickListener(this);
        tv_contacts.setOnClickListener(this);

        setDefaultFragment();
    }

    /**
     *设置默认的Fragment
     */
    private void setDefaultFragment() {
        expressFragment = new QueryExpressFragment();
        ft.replace(R.id.fl_content, expressFragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.tv_query:
                if (contactsFragment != null) {
                    ft.hide(contactsFragment);
                }
                if (expressFragment == null) {
                    expressFragment = new QueryExpressFragment();
                    ft.add(R.id.fl_content, expressFragment);
                }
                ft.show(expressFragment);
                tv_query.setBackgroundResource(R.drawable.top_left);
                tv_query.setTextColor(activity.getResources().getColor(R.color.blue_top));
                tv_contacts.setBackgroundResource(R.drawable.top_right_press);
                tv_contacts.setTextColor(activity.getResources().getColor(R.color.white));
                break;

            case R.id.tv_contacts:
                if (expressFragment != null) {
                    ft.hide(expressFragment);
                }
                if (contactsFragment == null) {
                    contactsFragment = new QueryContactsFragment();
                    ft.add(R.id.fl_content, contactsFragment);
                }
                ft.show(contactsFragment);
                tv_query.setBackgroundResource(R.drawable.top_left_press);
                tv_query.setTextColor(activity.getResources().getColor(R.color.white));
                tv_contacts.setBackgroundResource(R.drawable.top_right);
                tv_contacts.setTextColor(activity.getResources().getColor(R.color.blue_top));
                break;

            default:
                break;
        }
        ft.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 1001) {
            String result_value = data.getStringExtra("company");
            String com = data.getStringExtra("companytype");
//            expressFragment.setData(result_value, com, null, null);
            app.setCompany(result_value);
            app.setCom(com);
            Logger.show("---->>>>>>company11111", result_value);
            Logger.show("---->>>>>>com11111", com);
        }
        if (requestCode == 1002 && resultCode == 1003) {
            String result_value = data.getStringExtra("result");
            Bitmap bitmap = (Bitmap) data.getParcelableExtra("bitmap");
//            expressFragment.setData(null, null, result_value, bitmap);
            app.setResult(result_value);
            app.setBitmap(bitmap);
        }
    }

}
