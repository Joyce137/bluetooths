package com.health.app.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Client {

	private static final String TAG = "client";
	// 测试
	public static final String BASE_URL = "http://shop.cohcreate.com/";
	// 正式
	// public static final String BASE_URL="http://api.51awt.com/";
	
	protected static boolean is_log = true; //true=有log输出 ； false=关闭log
	
	private static String DEVICE = "Android";
	private static String APIVERSION = "1.0";
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String url, String method, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		// params.put("act", method);
		params.put("api", APIVERSION);
		params.put("device", DEVICE);

		client.setTimeout(10000);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);

		String turl = url + method;
		if(is_log){
			Log.e(TAG, turl + "?" + params.toString());
		}
		client.get(turl, params, responseHandler);


	}

	public static void post(String url, String method, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		RequestParams mods = new RequestParams();

		// mods.put("act", method);
		mods.put("api", APIVERSION);
		mods.put("device", DEVICE);

		client.setTimeout(10000);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);

		String turl = url + method;
		if(is_log){
			Log.e(TAG, turl + "?" + mods.toString() + "&" + params.toString());
		}
		client.post(turl + "?" + mods.toString(), params, responseHandler);

	}

	public static void get(String method, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		// params.put("act", method);
		// params.put("api", APIVERSION);
		params.put("device", DEVICE);

		client.setTimeout(10000);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);

		String turl = getAbsoluteUrl(method);
		if(is_log){
			Log.e(TAG, turl + "?" + params.toString());
		}
		client.get(turl, params, responseHandler);
	}

	public static void post(String method, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		// RequestParams mods = new RequestParams();

		// mods.put("act", method);
		// mods.put("api", APIVERSION);
		// mods.put("device", DEVICE);

		client.setTimeout(10000);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);

		String turl = getAbsoluteUrl(method);
		if(is_log){
			Log.e(TAG, turl + "?" + params.toString());
		}
		client.post(turl, params, responseHandler);

	}

	private static String getAbsoluteUrl(String method) {
		return BASE_URL + method;


	}

}
