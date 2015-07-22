package com.boredream.volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;

public class BDJsonWithJsonContentRequest<T> extends BDJsonRequest<T> {

	private static final String JSON_CONTENT_CHARSET = "UTF-8";
	private static final String JSON_CONTENT_TYPE = "application/json; charset=" + JSON_CONTENT_CHARSET;

	private final String postJson;

	public BDJsonWithJsonContentRequest(int method, String url, String postJson, Class<T> mClazz, BDListener<T> mListener) {
		super(method, url, null, mClazz, mListener);
		this.postJson = postJson;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();
		if (method == Method.POST) {
			if (headers == null || headers.size() == 0) {
				headers = new HashMap<String, String>();
			}
			headers.put("Content-Length", String.valueOf(getBody().length));
		}
		return headers;
	}

	@Override
	public byte[] getBody() {
		try {
			return postJson == null ? null : postJson.getBytes(JSON_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException uee) {
			System.out.println("get body exception");
			return null;
		}
	}

	@Override
	public String getBodyContentType() {
		return JSON_CONTENT_TYPE;
	}

}