package com.boredream.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

public class BDStringRequest extends BDRequest<String> {
	public BDStringRequest(int method, String url, BDListener<String> mListener) {
		super(method, url, mListener);
		this.setShouldCache(false);
	}

	public BDStringRequest(int method, String url, Map<String, Object> params, BDListener<String> mListener) {
		super(method, url, params, mListener);
		this.setShouldCache(false);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, BDVolleyUtils.getCharsetFromHeaders(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		// log it
		if (!TextUtils.isEmpty(parsed)) {
			StringBuffer sb = new StringBuffer();
			sb.append("url = " + getUrl());
			if (method == Method.POST) {
				try {
					sb.append(" ... post params = " + getParams());
				} catch (AuthFailureError e) {
					e.printStackTrace();
				}
			}
			sb.append(" ... response = " + parsed);
			Log.i(BDVolleyConfig.TAG, sb.toString());
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}
}
