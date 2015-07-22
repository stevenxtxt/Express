package com.boredream.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class BDJsonRequest<T> extends BDRequest<T> {

	private Gson mGson;
	private Class<T> mClazz;

	public BDJsonRequest(int method, String url, Class<T> mClazz, BDListener<T> mListener) {
		this(method, url, null, mClazz, mListener);
	}

	public BDJsonRequest(int method, String url, Map<String, Object> params, Class<T> mClazz, BDListener<T> mListener) {
		super(method, url, params, mListener);
		init(mClazz);
	}

	private void init(Class<T> mClazz) {
		this.mGson = new Gson();
		this.mClazz = mClazz;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		String json = null;
		try {
			json = new String(response.data, BDVolleyUtils.getCharsetFromHeaders(response.headers));
			// log it
			if(!TextUtils.isEmpty(json)) {
				StringBuffer sb = new StringBuffer();
				sb.append("url = " + getUrl());
				if(method == Method.POST) {
					try {
						sb.append(" ... post params = " + getParams());
					} catch (AuthFailureError e) {
						e.printStackTrace();
					}
				}
				sb.append(" ... response = " + json);
				Log.i(BDVolleyConfig.TAG, sb.toString());
			}
			return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
		// json瑙ｆ瀽寮傚父,浣嗚繕鏄兂浼犻�鍏朵腑閮ㄥ垎鍙傛暟鍊�		
			} catch (UnsupportedEncodingException e) {
			return Response.error(new BDParseError(json));
		} catch (JsonParseException e) {
			return Response.error(new BDParseError(json));
		}
	}
}