package com.boredream.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class BDParseError extends VolleyError {

	public BDParseError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BDParseError(NetworkResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}

	public BDParseError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		// TODO Auto-generated constructor stub
	}

	public BDParseError(String exceptionMessage) {
		super(exceptionMessage);
		// TODO Auto-generated constructor stub
	}

	public BDParseError(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}
