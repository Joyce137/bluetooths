package com.mslibs.utils;

public abstract class CountDownType {
	
	public abstract String onTickString(long millisUntilFinished);	
	public String countDownFinishString = null;
	//public long millisInFuture = 0;	
	//秒
	public long countDownInterval = 1;
	
}
