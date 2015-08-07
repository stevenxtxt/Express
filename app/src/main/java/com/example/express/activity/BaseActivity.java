package com.example.express.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Logger;
import com.example.express.utils.StringUtils;

import java.util.ArrayList;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener, LocationSource, AMapLocationListener {

    protected String TAG;

    protected BaseApplication application;
    protected SharedPreferences sp;
    protected Intent intent;
    protected ProgressDialog progDialog;
    protected Button btn_top_right;
    protected Button btn_top_right_first;

    protected MapView mapView;
    protected AMap aMap;
    protected AMapLocation aMapLocation;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private Marker marker;// 定位雷达小图标
    protected Marker geoMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        TAG = this.getClass().getSimpleName();
        showLog("onCreate()");

        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
        intent = getIntent();
        application.addActivity(this);

    }

    protected void launch(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    protected void initTop() {
        RelativeLayout btn_back = (RelativeLayout) this.findViewById(R.id.btn_title_btn_back_layout);
        if (null != btn_back) {
            btn_back.setOnClickListener(this);
        }
        btn_top_right = (Button) findViewById(R.id.btn_top_right);
        if (null != btn_top_right) {
            btn_top_right.setOnClickListener(this);
        }
        btn_top_right_first = (Button) findViewById(R.id.btn_top_right_first);
        if (null != btn_top_right_first) {
            btn_top_right_first.setOnClickListener(this);
        }
    }

    public void setTitle(String title) {
        super.setTitle(title);
        ((TextView) this.findViewById(R.id.tv_title_name)).setText(title);
    }

    public void setTitle(int titleId) {
        super.setTitle(titleId);
        ((TextView) this.findViewById(R.id.tv_title_name)).setText(titleId);
    }

    protected void initMap(Bundle savedInstanceState) {
        if (null != (mapView = (MapView) findViewById(R.id.mv_map))) {
            mapView.onCreate(savedInstanceState);
            if (null == aMap) {
                aMap = mapView.getMap();
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // 设置定位资源
        aMap.setLocationSource(this);
        // 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
        aMap.setMyLocationEnabled(true);
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
        marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icons(giflist).period(50));
        geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.parseColor("#00000000"));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_title_btn_back_layout) {
            onBackPressed();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLog("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();

        }
        showLog("onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();

        }
        showLog("onDestroy()");

        application.removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        showLog("onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {

            mapView.onPause();
            deactivate();
        }
        showLog("onPause()");
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    protected void finishActivity() {

        this.finish();
    }

    protected void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String msg) {

        Logger.show(TAG, msg);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (progDialog.isShowing()) {
            dismissDialog();
        }
        mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
        marker.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation
                .getLongitude()));// 定位雷达小图标
        float bearing = aMap.getCameraPosition().bearing;
        aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
        String desc = aMapLocation.getExtras().getString("desc");
        String address = StringUtils.splitAddress(desc);
        BaseApplication.getInstance().setExpressLat(aMapLocation.getLatitude());
        BaseApplication.getInstance().setExpressLng(aMapLocation.getLongitude());
        BaseApplication.getInstance().setExpressCity(aMapLocation.getCity());
        BaseApplication.getInstance().setExpressAddress(address);

        showLog("当前位置：" + aMapLocation.getLatitude() + ","
                + aMapLocation.getLongitude() +
                "当前城市：" + aMapLocation.getCity()
                + "当前地址：" + aMapLocation.getAddress()
                + "显示地址：" + address);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            /*
             * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog(String str) {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage(str);
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
}
