package com.mslibs.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public abstract class CSpannedTextViewBase extends LinearLayout {

	public Activity mActivity;
	public Context mContext;
	public LayoutInflater mInflater;
	
	public CSpannedTextViewBase(Context context) {
		super(context);
		mActivity = ((Activity) context);
		mContext = mActivity.getBaseContext();
		mInflater = mActivity.getLayoutInflater();
	}

	public CSpannedTextViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = ((Activity) context);
		mContext = mActivity.getBaseContext();
		mInflater = mActivity.getLayoutInflater();
	}
	
	public abstract void setData(Object obj);
	
}
