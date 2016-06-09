package com.mslibs.api;

public abstract class CallBack {
	public abstract void onSuccess(String response);	
	public void onFailure(String message) {}
	public int error = 0;
	private int code = 0;
	
	private Object extra = null;
	
	public void setExtra(Object extra) {
		this.extra = extra;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}	
	
	public Object getExtra() {
		return extra;
	}	
}
