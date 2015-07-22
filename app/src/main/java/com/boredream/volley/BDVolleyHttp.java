package com.boredream.volley;

import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.boredream.volley.BDVolleyUtils.BDSmartParserable;
import com.google.gson.Gson;

public class BDVolleyHttp {

	/**
	 * get方式获取字符串数据(json或者其他内容)
	 * 
	 * 
	 * @param bean
	 *            get参数,与url拼装成完整地址(bean对象变量名为key,变量值为value)
	 * @param url
	 *            不包含参数的url部分(?以前的部分,不包含?)
	 * @param listener
	 *            响应回调
	 */
	public static void getString(String url, BDSmartParserable bean, final BDListener<String> listener) {
		String completeUrl = BDVolleyUtils.parseGetUrlWithParams(url, bean);
		doString(completeUrl, Request.Method.GET, null, listener);
	}

	/**
	 * get方式获取字符串数据(json或者其他内容)
	 * 
	 * 
	 * @param params
	 *            get参数,与url拼装成完整地址
	 * @param url
	 *            不包含参数的url部分(?以前的部分,不包含?)
	 * @param listener
	 *            响应回调
	 */
	public static void getString(String url, Map<String, Object> params, final BDListener<String> listener) {
		String completeUrl = BDVolleyUtils.parseGetUrlWithParams(url, params);
		doString(completeUrl, Request.Method.GET, null, listener);
	}

	/**
	 * get方式获取字符串数据(json或者其他内容)
	 * 
	 * 
	 * @param url
	 *            完整的url
	 * @param listener
	 *            响应回调
	 */
	public static void getString(String url, final BDListener<String> listener) {
		doString(url, Request.Method.GET, null, listener);
	}

	/**
	 * post方式获取字符串数据(json或者其他内容)
	 * 
	 * 
	 * @param url
	 *            不包含参数的url部分(?以前的部分,不包含?)
	 * @param params
	 *            post的数据<String, Object>
	 * @param listener
	 *            响应回调
	 */
	public static void postString(String url, Map<String, Object> params, final BDListener<String> listener) {
		doString(url, Request.Method.POST, params, listener);
	}

	public static void postString(Context context, String url, Map<String, Object> params, final BDListener<String> listener) {
		doString(url, Request.Method.POST, params, listener);
	}

	/**
	 * post方式获取字符串数据(json或者其他内容)
	 * 
	 * 
	 * @param url
	 *            不包含参数的url部分(?以前的部分,不包含?)
	 * @param bean
	 *            请求bean,会自动按照变量名-变量值的创建键值对封装为参数数组map
	 * @param listener
	 *            响应回调
	 */
	public static void postString(String url, BDSmartParserable bean, final BDListener<String> listener) {
		Map<String, Object> params = BDVolleyUtils.bean2params(bean);
		doString(url, Request.Method.POST, params, listener);
	}

	/**
	 * get方式获取数据对象
	 * 
	 * <p>
	 * 要获取单纯的json字符串,用BDVolleyHttp.getString方法
	 * 
	 * 
	 * @param bean
	 *            get参数,与url拼装成完整地址(bean对象变量名为key,变量值为value)
	 * @param url
	 *            完整的url(可以用BDVolleyUtils.parseGetUrlWithParams方法智能拼装参数)
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void getJsonObject(String url, BDSmartParserable bean, final Class<T> clazz, final BDListener<T> listener) {
		String completeUrl = BDVolleyUtils.parseGetUrlWithParams(url, bean);
		doJsonObject(completeUrl, Request.Method.GET, null, clazz, listener);
	}

	/**
	 * get方式获取数据对象
	 * 
	 * <p>
	 * 要获取单纯的json字符串,用BDVolleyHttp.getString方法
	 * 
	 * 
	 * @param params
	 *            get参数,与url拼装成完整地址
	 * @param url
	 *            完整的url(可以用BDVolleyUtils.parseGetUrlWithParams方法智能拼装参数)
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void getJsonObject(String url, Map<String, Object> map, final Class<T> clazz, final BDListener<T> listener) {
		String completeUrl = BDVolleyUtils.parseGetUrlWithParams(url, map);
		doJsonObject(completeUrl, Request.Method.GET, null, clazz, listener);
	}

	/**
	 * get方式获取数据对象
	 * 
	 * <p>
	 * 要获取单纯的json字符串,用BDVolleyHttp.getString方法
	 * 
	 * 
	 * @param url
	 *            完整的url(可以用BDVolleyUtils.parseGetUrlWithParams方法智能拼装参数)
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void getJsonObject(String url, final Class<T> clazz, final BDListener<T> listener) {
		doJsonObject(url, Request.Method.GET, null, clazz, listener);
	}

	/**
	 * post方式获取json数据对象
	 * 
	 * <p>
	 * 要获取单纯的json字符串,用BDVolleyHttp.getString方法
	 * 
	 * 
	 * @param url
	 * @param params
	 *            post的数据<String, Object>
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void postJsonObject(String url, final Map<String, Object> params, final Class<T> clazz, final BDListener<T> mListener) {
		doJsonObject(url, Request.Method.POST, params, clazz, mListener);
	}

	/**
	 * post方式获取json数据对象
	 * 
	 * <p>
	 * 要获取单纯的json字符串,用<b>postString</b>方法
	 * 
	 * 
	 * @param url
	 * @param bean
	 *            请求bean,会自动按照变量名-变量值的创建键值对封装为参数数组map
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void postJsonObject(String url, final BDSmartParserable bean, final Class<T> clazz, final BDListener<T> mListener) {
		Map<String, Object> params = BDVolleyUtils.bean2params(bean);
		doJsonObject(url, Request.Method.POST, params, clazz, mListener);
	}

	/**
	 * post方式获取json数据对象(post数据提交内容限定为content-type=application/json)
	 * 
	 * 
	 * @param url
	 * @param bean
	 *            请求bean,会自动按照变量名-变量值的创建键值对封装为参数数组map
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void postJsonObjectWithJsonContent(String url, final BDSmartParserable bean, final Class<T> clazz, final BDListener<T> mListener) {
		doJsonObjectWithJsonContent(url, Request.Method.POST, new Gson().toJson(bean), clazz, mListener);
	}

	/**
	 * 
	 * post方式获取json数据对象(post数据提交内容限定为content-type=application/json)
	 * 
	 * 
	 * @param url
	 * @param jsonStr
	 *            请求json数据,当post的content-type为a/json时使用
	 * @param listener
	 *            响应回调,直接从json解析成Object对象
	 * @param clazz
	 *            想要将json封装成对象的数据类型
	 */
	public static <T> void postJsonObjectWithJsonContent(String url, final String jsonStr, final Class<T> clazz, final BDListener<T> mListener) {
		doJsonObjectWithJsonContent(url, Request.Method.POST, jsonStr, clazz, mListener);
	}

	/**
	 * 利用Volley异步加载图片
	 * 
	 * 
	 * @param imageUrl
	 * @param iv
	 */
	public static void loadImageByVolley(String imageUrl, ImageView iv) {
		loadImageByVolley(imageUrl, iv, null);
	}

	/**
	 * 利用Volley异步加载图片,有回调型
	 * 
	 * 
	 * @param imageUrl
	 * @param iv
	 * @param listener
	 *            图片回调接口,bitmap为空时即为获取失败
	 */
	public static void loadImageByVolley(String imageUrl, final ImageView iv, final BDImageListener listener) {
		Log.i("DDD", "load bitmap " + imageUrl);
		ImageLoader imageLoader = BDVolley.getImageLoader();
		imageLoader.get(imageUrl, new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (BDVolleyConfig.ERROR_IMAGE_RESID != 0) {
					iv.setImageResource(BDVolleyConfig.ERROR_IMAGE_RESID);
				}
				if (listener != null) {
					listener.onErrorResponse(error);
				}
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				if (response.getBitmap() != null) {
					iv.setImageBitmap(response.getBitmap());
					if (listener != null) {
						listener.onComplete(response.getBitmap());
					}
				} else {
					if (BDVolleyConfig.DEFAULT_IMAGE_RESID != 0) {
						iv.setImageResource(BDVolleyConfig.DEFAULT_IMAGE_RESID);
					}
					if (listener != null) {
						listener.onComplete(null);
					}
				}
			}
		}, 200, 200);
	}

	private static <T> void doJsonObject(String url, int method, final Map<String, Object> params, final Class<T> mClazz, final BDListener<T> mListener) {
		Log.i("DDD", "get url = " + url);
		RequestQueue requestQueue = BDVolley.getRequestQueue();
		BDJsonRequest<T> jsonObjectRequest = new BDJsonRequest<T>(method, url, params, mClazz, mListener);
		requestQueue.add(jsonObjectRequest);
	}

	private static <T> void doJsonObjectWithJsonContent(String url, int method, final String postJson, final Class<T> mClazz, final BDListener<T> mListener) {
		Log.i("Volley", "post json = " + postJson);
		RequestQueue requestQueue = BDVolley.getRequestQueue();
		BDJsonWithJsonContentRequest<T> jsonObjectRequest = new BDJsonWithJsonContentRequest<T>(method, url, postJson, mClazz, mListener);
		requestQueue.add(jsonObjectRequest);
	}

	private static void doString(String url, int method, final Map<String, Object> params, final BDListener<String> mListener) {
		RequestQueue requestQueue = BDVolley.getRequestQueue();
		BDStringRequest sRequest = new BDStringRequest(method, url, params, mListener);
		requestQueue.add(sRequest);
	}

}
