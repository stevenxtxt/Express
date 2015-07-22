package com.example.express.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.express.activity.BaseFragment;
import com.example.express.R;

public class SendExpressFragment extends BaseFragment {

	private View view;
	private EditText loginname;
	private EditText loginpwd;
	private Button login_btn;
	private String str;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_login, null);
//		loginname=(EditText) view.findViewById(R.id.login_name);
//		loginpwd=(EditText) view.findViewById(R.id.login_pwd);
//		login_btn=(Button) view.findViewById(R.id.loginbtn);
//		login_btn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Map<String, Object> params = new HashMap<String, Object>();
//				String content_name=loginname.getText().toString().trim();
//				String content_pwd=loginpwd.getText().toString().trim();
//				params.put("user", content_name);
//				params.put("psw", content_pwd);
//				BDVolleyHttp.postString("http://www.wshens.com/mobilelogin.html",params,new BDListener<String>() {
//
//							@Override
//							public void onErrorResponse(VolleyError error) {
//								// 返回失败处理
//							}

	//						@Override
	//						public void onResponse(String response) {
								// 返回成功处理 response为json字符串
								  Intent intent = new Intent(); 
								  str=loginname.getText().toString().trim();
								 // intent.setClass(getActivity(), LoginInfoFragment.class);
								  intent.putExtra("json", str);
								 
//								  startActivity(intent);
//
//							}
//						});
//
//			}
//		});
		
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