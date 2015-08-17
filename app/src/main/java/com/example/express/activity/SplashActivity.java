package com.example.express.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 17:06
 * 修改人：xutework
 * 修改时间：2015/7/29 17:06
 * 修改备注：
 */
public class SplashActivity extends BaseActivity {

    private ViewPager vp_content;
    private ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        initViews();

    }

    private void initViews() {
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        final ArrayList<View> views = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.splash_guide1, null, false);
        View view2 = inflater.inflate(R.layout.splash_guide2, null, false);
        iv_splash = (ImageView) view2.findViewById(R.id.iv_splash2);
        views.add(view1);
        views.add(view2);
        iv_splash.setOnClickListener(this);

        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = views.get(position);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View v = views.get(position);
                container.removeView(v);
            }
        };
        vp_content.setAdapter(adapter);
        vp_content.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_splash2:
                launch(MainTabActivity.class);
                finish();
                break;
            default:
                break;
        }
    }
}
