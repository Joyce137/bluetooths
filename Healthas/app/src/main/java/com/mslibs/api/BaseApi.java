package com.mslibs.api;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.json.JSONException;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class BaseApi {

	protected String TAG = "BaseApi";
	protected CallBack mCallBack = null;
	protected Object extra = null;	
	protected boolean is_log = true;	
	public BaseApi() {
		this.mCallBack = null;
	}

	public BaseApi(CallBack callback) {
		this.mCallBack = callback;
	}

	public void setCallBack(CallBack callback) {
		this.mCallBack = callback;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
	public void setLog(Boolean is_log) {
		this.is_log = is_log;
	}
	
	public abstract void onStart();
	public abstract void onSuccess();
	//type=1 errorFromServer 2=networkError
	public abstract void onFailure(String message, int  type);
	public abstract void start();
	public abstract void retry();

	
	protected AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		public void onSuccess(String response) {
			if(is_log){
				Log.e(TAG, "\nonSuccess:" + response + "\n");
			}
			
			if (mCallBack == null)
				return;
			if (extra != null)
				mCallBack.setExtra(extra);
			try {
				String obj = parse(response);
				BaseApi.this.onSuccess();
				mCallBack.onSuccess(obj);
			} catch (ResponseException e) {
				BaseApi.this.onFailure(e.getMessage(), 1);
				mCallBack.error = e.error;
				mCallBack.onFailure(e.getMessage());
			} catch (JSONException e) {
				String message = "收到了错误的格式";
				BaseApi.this.onFailure(message, 1);
				mCallBack.onFailure(message);				
			}
			// mCallBack.onSuccess(response);
		}

		
		public void onFailure(Throwable e) {
			if(is_log){
				Log.e(TAG, "\nonFailure:" + e.toString() + "\n");
			}
			if (mCallBack == null) {
				return;
			}
			if (e instanceof UnknownHostException) {
				String message = "请检查您的网络是否可用";
				BaseApi.this.onFailure(message, 2);
				mCallBack.onFailure(message);
			} else if(e instanceof SocketTimeoutException) {
				String message = "服务器正忙，请稍后再试";
				BaseApi.this.onFailure(message, 2);
				mCallBack.onFailure(message);
			}else{
				String message = "网络异常，请稍后再试";
				BaseApi.this.onFailure(message, 2);
				mCallBack.onFailure(message);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable e) {
			onFailure(e);			
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] response) {
			onSuccess(new String(response));			
		}
	};

	protected String parse(String response) throws ResponseException, JSONException {
		return response;
	}

}
