package com.example.express.activity.query;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.R;
import com.silent.adapter.SortAdapter;
import com.silent.handle.CharacterParser;
import com.silent.handle.PinyinComparator;
import com.silent.handle.SideBar;
import com.silent.handle.SideBar.OnTouchingLetterChangedListener;
import com.silent.model.PhoneModel;

/**
 * 
 * @author Mr.Z
 */
public class ShowCompanyListActivity extends Activity {
	private Context				context	= ShowCompanyListActivity.this;
	private ListView			sortListView;
	private SideBar				sideBar;
	private TextView			dialog;
	private SortAdapter			adapter;
	private String item_name;
	private String[] data_name;
	private String[] data_phone;
	private String[] data_img;
	/**
	 * 汉字转换成拼音的�?
	 */
	private CharacterParser		characterParser;
	private List<PhoneModel>	SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator	pinyinComparator;
	Intent intent;
	String exjson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent() ;
		exjson = intent.getStringExtra("json");
		JSONObject json_data=null;
		
		try {
			JSONArray jsonArray=new JSONArray(exjson);
			 data_name = new String[jsonArray.length()];
			 data_phone = new String[jsonArray.length()];
			 data_img = new String[jsonArray.length()];
			 for (int i = 0; i < jsonArray.length(); i++) {
	               json_data = jsonArray.getJSONObject(i);
	               data_name[i]=json_data.getString("name");
	               data_phone[i]=json_data.getString("phone");
	               data_img[i]="http://www.kuaidi.com"+json_data.getString("ico");
	            }
			 

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.activity_company);
		initViews();
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<PhoneModel> filledData(String[] date, String[] phoneData, String[] imgData) {
		List<PhoneModel> mSortList = new ArrayList<PhoneModel>();

		for (int i = 0; i < date.length; i++) {
			PhoneModel sortModel = new PhoneModel();
			sortModel.setImgSrc(imgData[i]);
			sortModel.setName(date[i]);
			sortModel.setPhone(phoneData[i]);
			// 汉字转换成拼�?
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private void initViews() {
		// 实例化汉字转拼音�?
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position�?��应的对象
				//Toast.makeText(context, ((PhoneModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                TextView textview = (TextView) findViewById(R.id.company_name);
                Object obj = ((PhoneModel) adapter.getItem(position)).getName();
                String s = obj.toString();
			    //String str =textview.getText().toString().trim();
				Intent intent = new Intent();
				intent.putExtra("item", s);
				setResult(1001, intent);
				finish();

			}
		});
//		data_name=getData("name");
//		data_phone=getData("phone");
//		data_img=getData("ico");
		SourceDateList = filledData(data_name, data_phone,data_img);

		// 根据a-z进行排序源数�?
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(context, SourceDateList);
		sortListView.setAdapter(adapter);
	}
}
