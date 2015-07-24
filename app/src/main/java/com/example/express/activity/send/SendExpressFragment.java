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
		view = View.inflate(getActivity(), R.layout.login, null);

		return view;
	}

}