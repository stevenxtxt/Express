package com.example.express.activity.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.activity.BaseFragment;
import com.example.express.R;
import com.example.express.constants.CommonConstants;
import com.example.express.utils.TestKey;

public class QueryExpressFragment extends BaseFragment implements OnClickListener {

    private View view;
    private EditText et_number;
    private LinearLayout ll_company;
    private TextView tv_company;
    private ImageView iv_scan;
    JSONArray Jarry;
    private String json_result;
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Map<String, Object> map = new HashMap<String, Object>();
    private Button btn_express_query;
    private ImageView iv_qrcode_bitmap;

    //快递公司简码
    private String com;
    //快递公司名称
    private String company;
    //运单号
    private String nu;

    private String result;
    private Bitmap bitmap;

    @Override
    public void onResume() {
        super.onResume();
        if (company != null) {
            tv_company.setText(company);
        }
        if (result != null) {
            et_number.setText(result);
        }
        if (bitmap != null) {
            iv_qrcode_bitmap.setImageBitmap(bitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_query, null);
        et_number = (EditText) view.findViewById(R.id.et_number);
        tv_company = (TextView) view.findViewById(R.id.tv_company);
        ll_company = (LinearLayout) view.findViewById(R.id.ll_company);
        iv_scan = (ImageView) view.findViewById(R.id.iv_scan);
        btn_express_query = (Button) view.findViewById(R.id.btn_express_query);
        iv_qrcode_bitmap = (ImageView) view.findViewById(R.id.iv_qrcode_bitmap);

        ll_company.setOnClickListener(this);
        iv_scan.setOnClickListener(this);
        btn_express_query.setOnClickListener(this);

        et_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                Log.d("TAG", "afterTextChanged--------------->");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                Log.d("TAG", "beforeTextChanged--------------->");
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.MATCH_COMPANY
                                + et_number.getText().toString().trim() + CommonConstants.HTML,
                        new BDListener<String>() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onResponse(String response) {
                                // TODO Auto-generated method stub
                                //
                                if (isJarray(response) == true) {
                                    try {
                                        Jarry = new JSONArray(response);
                                        JSONObject json_data = null;
                                        json_data = Jarry.getJSONObject(0);
                                        json_result = json_data.getString("name");
                                        com = json_data.getString("exname");
                                        tv_company.setText(result);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                        return;
                                    }

                                } else {
                                    try {
                                        JSONObject json = new JSONObject(response);
                                        JSONObject results = json.getJSONObject("0");
                                        String name = results.getString("name");
                                        com = results.getString("exname");
                                        tv_company.setText(name);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                        return;
                                    }

                                }

                            }
                        });


            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_company:
                queryCompany();
                break;

            case R.id.iv_scan:
                intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                getParentFragment().startActivityForResult(intent, 1002);
                break;

            case R.id.btn_express_query:
                nu = et_number.getText().toString().trim();
                queryExpressResult();
                break;

            default:
                break;
        }
    }

    /**
     * 查询快递
     */
    private void queryExpressResult() {
        showCustomDialog("正在努力查询中...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_EXPRESS_RESULT + nu + "-" + com + CommonConstants.HTML,
                new BDListener<String>() {
            @Override
            public void onResponse(String response) {
                dismissCustomDialog();
                Intent intent = new Intent();
                intent.putExtra("json", response);
                intent.setClass(getActivity(), ShowResultActivity.class);
                startActivity(intent);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissCustomDialog();
                Toast.makeText(activity, "查询失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询快递公司
     */
    private void queryCompany() {
        showCustomDialog("正在努力查询中...");
        BDVolleyHttp.getString(CommonConstants.URLConstant + CommonConstants.QUERY_EXPRESS_COMPANY + CommonConstants.HTML,
                new BDListener<String>() {
            @Override
            public void onResponse(String response) {
                dismissCustomDialog();
                Intent intent = new Intent();
                intent.putExtra("json", response);
                intent.setClass(getActivity(), ShowCompanyListActivity.class);
                getParentFragment().startActivityForResult(intent, 1000);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissCustomDialog();
                Toast.makeText(activity, "查询失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isJarray(String str) {
        JSONArray Jarray;
        //JSONObject json_data = null;
        try {
            Jarray = new JSONArray(str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return false;

        }
        return true;
    }

    public void setData(String company, String com, String result, Bitmap bitmap) {
        this.company = company;
        this.com = com;
        this.result = result;
        this.bitmap = bitmap;
    }
}
