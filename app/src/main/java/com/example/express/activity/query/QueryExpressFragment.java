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

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.activity.BaseFragment;
import com.example.express.R;

public class QueryExpressFragment extends BaseFragment {

    private View view;
    private EditText danhao;
    private LinearLayout ll_gongsi;
    private TextView gongsi;
    private ImageView saoma;
    private TextView tv_query;
    private TextView tv_contacts;
    JSONArray Jarry;
    private String result;
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Map<String, Object> map = new HashMap<String, Object>();
    private Button button1;
    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_home, null);
        danhao = (EditText) view.findViewById(R.id.et_number);
        gongsi = (TextView) view.findViewById(R.id.tv_company);
        ll_gongsi = (LinearLayout) view.findViewById(R.id.ll_company);
        saoma = (ImageView) view.findViewById(R.id.iv_scan);
        button1 = (Button) view.findViewById(R.id.btn_express_query);
        mImageView = (ImageView) view.findViewById(R.id.iv_qrcode_bitmap);
        tv_contacts = (TextView) view.findViewById(R.id.tv_contacts);
        tv_query = (TextView) view.findViewById(R.id.tv_query);
//		String extras = savedInstanceState.getBundle("item").toString().trim();
//		gongsi.setText(extras);
        saoma.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1002);
            }
        });
        ll_gongsi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                BDVolleyHttp.getString(
                        "http://10.0.2.2/test.php",
                        new BDListener<String>() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onResponse(String response) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.putExtra("json", response);
                                intent.setClass(getActivity(), ShowCompanyListActivity.class);
                                startActivityForResult(intent, 1000);
                            }
                        });

            }
        });
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg2) {
                BDVolleyHttp.getString(
                        "http://www.kuaidi.com/index-ajaxselectcourierinfo-" + danhao.getText().toString().trim() + "-" + gongsi.getText().toString().trim() + ".html",
                        new BDListener<String>() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onResponse(String response) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.putExtra("json", response);
                                intent.setClass(getActivity(), ShowExpressInfoActivity.class);
                                startActivity(intent);
                            }
                        });


            }
        });
        //newsatat
        danhao.addTextChangedListener(new TextWatcher() {

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
                BDVolleyHttp.getString(
                        "http://www.kuaidi.com/index-ajaxselectinfo-" + danhao.getText().toString().trim() + ".html",
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
                                        result = json_data.getString("name");
                                        gongsi.setText(result);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                } else {
                                    try {
                                        JSONObject json = new JSONObject(response);
                                        JSONObject results = json.getJSONObject("0");
                                        String name = results.getString("name");
                                        gongsi.setText(name);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }

                            }
                        });


            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            String result_value = data.getStringExtra("item");
            gongsi.setText(result_value);
        }
        if (requestCode == 1002 && resultCode == 1003) {
            String result_value = data.getStringExtra("result");
            danhao.setText(result_value);
            mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
        }
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

}
