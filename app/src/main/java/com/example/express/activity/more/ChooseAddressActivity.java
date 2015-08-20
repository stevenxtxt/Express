package com.example.express.activity.more;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.BaseApplication;
import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.Logger;
import com.widget.OnWheelChangedListener;
import com.widget.WheelView;
import com.widget.adapters.ArrayWheelAdapter;

/**
 * @author zhy
 */
public class ChooseAddressActivity extends BaseActivity implements OnWheelChangedListener {

    private JSONObject mJsonObj;

    private WheelView mProvince;

    private WheelView mCity;

    private String[] mProvinceDatas;

    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    private String mCurrentProviceName;

    private String mCurrentCityName;

    private Button btn_confirm;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_address);

        initTop();
        setTitle("选择城市");

        initJsonData();

        mProvince = (WheelView) findViewById(R.id.id_province);
        mCity = (WheelView) findViewById(R.id.id_city);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        initDatas();

        mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);

        mProvince.setVisibleItems(5);
        mCity.setVisibleItems(5);
        updateCities();

    }

    private void updateCities() {
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mCity.setCurrentItem(0);
    }

    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                String province = jsonP.getString("p");

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");
                    mCitiesDatas[j] = city;

                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }


    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
        } else if (wheel == mCity) {
            int pCurrent = mCity.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_confirm:
                mCurrentProviceName = mProvinceDatas[mProvince.getCurrentItem()];
                mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[mCity.getCurrentItem()];
                changeArea();
                break;

            case R.id.btn_cancel:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 修改所在地
     */
    private void changeArea() {
        showCustomDialog("正在修改...");
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = BaseApplication.getInstance().getLoginUser().getUserId();
        params.put("userId", userId);
        params.put("newArea", mCurrentProviceName + "-" + mCurrentCityName);
        BDVolleyHttp.postString(CommonConstants.URLConstant + CommonConstants.CHANGE_AREA + CommonConstants.HTML,
                params, new BDListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissCustomDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optBoolean("result")) {
                                showToast("修改成功");
                                Logger.show("--------->>>>>>>>>>", mCurrentProviceName + "+" + mCurrentCityName);
                                Intent intent = new Intent();
                                intent.putExtra("province", mCurrentProviceName);
                                intent.putExtra("city", mCurrentCityName);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                dismissCustomDialog();
                                showToast(obj.optString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("修改失败，请重试");
                            return;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dismissCustomDialog();
                        showToast("修改失败，请重试");
                    }
                });
    }
}
