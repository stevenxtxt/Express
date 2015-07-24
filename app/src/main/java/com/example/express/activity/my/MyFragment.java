package com.example.express.activity.my;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.activity.BaseFragment;
import com.example.express.R;

public class MyFragment extends BaseFragment {

	private View view;
	private EditText loginname;
	private EditText loginpwd;
	private Button login_btn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_shop, null);
		loginname=(EditText) view.findViewById(R.id.login_name);
		loginpwd=(EditText) view.findViewById(R.id.login_pwd);
		login_btn=(Button) view.findViewById(R.id.loginbtn);
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Map<String, Object> params = new HashMap<String, Object>();
				String content_name=loginname.getText().toString().trim();
				//String content_pwd=loginpwd.getText().toString().trim();
				params.put("user", content_name);
				//params.put("psw", content_pwd);
				BDVolleyHttp.postString("10.0.2.2/select.php",params,new BDListener<String>() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// 杩斿洖澶辫触澶勭悊
								
							}

							@Override
							public void onResponse(String response) {
								// 杩斿洖鎴愬姛澶勭悊 response涓簀son瀛楃涓�								  
								  Intent intent = new Intent(); 
								  intent.setClass(getActivity(), ShowInfoActivity.class);
								  intent.putExtra("json", response);
								  startActivity(intent);

							}
						});

			}
		});
		
//		et1 = (EditText) view.findViewById(R.id.et);
//		et1.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				if(arg0.length() >= 7) {
//					
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
		
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		return view;
	}

}