package com.boredream.volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;

public abstract class BDRequest<T> extends Request<T> {
	/** 超时时间 */
	private static int REQUEST_TIMEOUT_MS = 45 * 1000;

	/** Retry最大次数 */
	private static final int REQUEST_MAX_RETRIES = 0;

	/** Default backoff multiplier for image requests */
	private static final float REQUEST_BACKOFF_MULT = 1f;

	private BDListener<T> mListener;
	private Map<String, Object> postParams;
	protected int method;

	public BDRequest(int method, String url, BDListener<T> mListener) {
		this(method, url, null, mListener);
	}

	public BDRequest(int method, String url, Map<String, Object> params, BDListener<T> mListener) {
		super(method, url, null);
		this.method = method;
		init(method, url, params, mListener);
	}

	// 所有继承自BDRequest的都会运行这个初始化方法
	private void init(int method, String url, Map<String, Object> params, BDListener<T> mListener) {
		this.mListener = mListener;
		if (method == Method.POST) {
			if (params != null && params.size() > 0) {
				this.postParams = params;
			}
		}
		url = BDVolleyUtils.encodeUrl(url);
		setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, REQUEST_MAX_RETRIES, REQUEST_BACKOFF_MULT));
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> params = super.getParams();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		if (postParams == null) {
			return params;
		}
		for (Entry<String, Object> entry : postParams.entrySet()) {
			params.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return params;
	}

	@Override
	public void deliverError(VolleyError error) {
		mListener.onErrorResponse(error);
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
}