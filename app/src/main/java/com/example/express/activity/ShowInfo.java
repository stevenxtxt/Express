package com.example.express.activity;

import com.example.express.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class ShowInfo extends Activity{
	private TextView tv;
	String str;
	  public void onCreate(Bundle savedInstanceState) { 
	       super.onCreate(savedInstanceState); 
	       setContentView(R.layout.frag_user); 
	       tv = (TextView) findViewById(R.id.text_user);   
	       tv.setText("hello!"); 
	   } 
}
