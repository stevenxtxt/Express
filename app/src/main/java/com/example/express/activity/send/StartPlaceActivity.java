package com.example.express.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.activity.send.adapter.PoiAdapter;
import com.example.express.utils.Logger;

import java.util.ArrayList;


/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/31 18:18
 * 修改人：xutework
 * 修改时间：2015/7/31 18:18
 * 修改备注：
 */
public class StartPlaceActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener {

    private TextView tv_confirm;
    private LinearLayout ll_search_place;
    private ListView lv_places;
    private PoiAdapter adapter;
    private ArrayList<PoiItem> poiItemList;

    private PoiResult poiResult;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private int page = 0;
    private int size = 10;
    private String city;
    private LatLonPoint center_point;
    private BaseApplication app;
    private String address;
    private double latitude;
    private double longitude;

    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_place);
        app = BaseApplication.getInstance();
        city = app.getExpressCity();
        center_point = new LatLonPoint(app.getExpressLat(), app.getExpressLng());
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        address = app.getExpressAddress();
        latitude = app.getExpressLat();
        longitude = app.getExpressLng();

        initTop();
        setTitle("出发地");
        initMap(savedInstanceState);
        initViews();

        lv_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem item = poiItemList.get(i);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertToLatLng(item.getLatLonPoint()), 15));
                geoMarker.setPosition(convertToLatLng(item.getLatLonPoint()));
                address = item.getSnippet();
                latitude = item.getLatLonPoint().getLatitude();
                longitude = item.getLatLonPoint().getLongitude();
            }
        });

        doSearchQuery(center_point);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initViews() {
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        ll_search_place = (LinearLayout) findViewById(R.id.ll_search_place);
        lv_places = (ListView) findViewById(R.id.lv_places);

        adapter = new PoiAdapter(this);

        ll_search_place.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    /**
     * 开始进行poi搜索
     */
    private void doSearchQuery(LatLonPoint lp) {
        showDialog("正在搜索附近建筑...");
        query = new PoiSearch.Query("", "120000", city);
        query.setPageNum(page);
        query.setPageSize(size);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 1000));
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_search_place:
                Intent intent = new Intent(StartPlaceActivity.this, SearchPlaceActivity.class);
                startActivityForResult(intent, 200);
                break;

            case R.id.tv_confirm:

                Intent intent1 = new Intent();
                intent1.putExtra("address", address);
                app.setExpressAddress(address);
                intent1.putExtra("latitude", latitude);
                app.setExpressLat(latitude);
                intent1.putExtra("longitude", longitude);
                app.setExpressLng(longitude);
                Logger.show("------->>>>>latlng", "latitude:" + latitude + ", " + "longitude:" + longitude);
                setResult(RESULT_OK, intent1);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {
        dismissDialog();
        if (i == 0) {
            if (result != null && result.getQuery() != null) {
                poiResult = result;
                poiItemList = poiResult.getPois();
                adapter.setList(poiItemList);
                lv_places.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            address = data.getStringExtra("name");
            String district = data.getStringExtra("district");
            city = app.getExpressCity();
            getLatlon(district + address);
        }
    }

    /**
     * 将地址转换为经纬度显示在地图上
     *
     * @param name
     */
    private void getLatlon(String name) {
        showDialog("正在地图上查找地址...");
        GeocodeQuery query = new GeocodeQuery(name, city);
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 0) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertToLatLng(address.getLatLonPoint()), 15));
                geoMarker.setPosition(convertToLatLng(address
                        .getLatLonPoint()));
                latitude = address.getLatLonPoint().getLatitude();
                longitude = address.getLatLonPoint().getLongitude();

                //查询附件建筑物
                doSearchQuery(address.getLatLonPoint());
            } else {
                showToast(getResources().getString(R.string.no_result));
            }
        }
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }
}
