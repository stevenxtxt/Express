package com.example.express.activity.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.express.R;


import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class ShowExpressInfoActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		String exjson = intent.getStringExtra("json");
		SimpleAdapter adapter = new SimpleAdapter(this,getData(exjson),R.layout.express_info,
				new String[]{"title","info","img"},
				new int[]{R.id.title,R.id.info,R.id.img});
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData(String str) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Map<String, Object> map = new HashMap<String, Object>();
	JSONObject json_data=null;
	
	try {
		JSONObject  jsonObj = new JSONObject(str); 
		JSONArray jsonArray=jsonObj.getJSONArray("data");
		 for (int i = 0; i < jsonArray.length(); i++) {
               json_data = jsonArray.getJSONObject(i);
               map = new HashMap<String, Object>();
               map.put("title", json_data.getString("time"));
               map.put("info", json_data.get("context"));
               map.put("img", R.drawable.i2);
               list.add(map);
            }
		 

	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return list;
}

}
