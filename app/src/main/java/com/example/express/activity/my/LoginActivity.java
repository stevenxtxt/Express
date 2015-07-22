package com.example.express.activity.my;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDVolleyHttp;
import com.example.express.R;
import com.example.express.activity.main.MainTabActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends Activity {
	private EditText loginname;
	private EditText loginpwd;
	private Button login_btn;
	private String content_name;
	private String content_pwd;
	private CheckBox rem_pw, auto_login; 
	private SharedPreferences sp;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_login); 
		loginname=(EditText) findViewById(R.id.login_name);
		loginpwd=(EditText) findViewById(R.id.login_pwd);
		login_btn=(Button) findViewById(R.id.loginbtn);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);
	    auto_login = (CheckBox) findViewById(R.id.cb_auto);  
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
		
		 //判断记住密码多选框的状态  
	      if(sp.getBoolean("ISCHECK", false))  
	        {  
	          //设置默认是记录密码状态  
	          rem_pw.setChecked(true);  
	          loginname.setText(sp.getString("USER_NAME", ""));  
	          loginpwd.setText(sp.getString("PASSWORD", ""));  
	          //判断自动登陆多选框状态  
	          if(sp.getBoolean("AUTO_ISCHECK", false))  
	          {  
	                 //设置默认是自动登录状态  
	                 auto_login.setChecked(true);  
	                //跳转界面  
	                Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);
	                LoginActivity.this.startActivity(intent);  
	                  
	          }  
	        }  
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Map<String, Object> params = new HashMap<String, Object>();
				content_name=loginname.getText().toString().trim();
				content_pwd=loginpwd.getText().toString().trim();
				params.put("user", content_name);
				params.put("psw", content_pwd);
				BDVolleyHttp.postString("http://www.wshens.com/mobilelogin.html",params,new BDListener<String>() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// 返回失败处理
								 Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show(); 
							}

							@Override
							public void onResponse(String response) {
								// 返回成功处理 response为json字符串
								 //登录成功和记住密码框为选中状态才保存用户信息  
			                    if(rem_pw.isChecked())  
			                    {  
			                     //记住用户名、密码、  
			                      Editor editor = sp.edit();  
			                      editor.putString("USER_NAME", content_name);  
			                      editor.putString("PASSWORD",content_pwd);  
			                      editor.commit();  
			                    }  
			                  //跳转界面  
			                    Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);
			                    startActivity(intent);  
			                    //finish();  

							}
						});

			}
		});
		//监听自动登录多选框事件  
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (auto_login.isChecked()) {  
                    System.out.println("自动登录已选中");  
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
  
                } else {  
                    System.out.println("自动登录没有选中");  
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
                }  
            }  
        });  
		
	}

}