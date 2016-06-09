package com.mslibs.widget;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public abstract class CPagerItem extends RelativeLayout {

	private String TAG = "PagerItem";

	public Activity mActivity;
	public Context mContext;
	private DisplayMetrics dm;
	public LayoutInflater mInflater;
	

	public int displayWidth = 0;
	public int displayHeight = 0;
	public ProgressBar mProgressBar;

	
	public CPagerItem(Activity activity, Context context) {
		super(context);
		
		mActivity = activity;
		mContext = context;

		dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.setLayoutParams(params);
	}
	
	public void setContentView(int resource){
		View view = mInflater.inflate(resource, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		view.setLayoutParams(params);
		this.addView(view);
	}
	public void setFrame(int width, int height) {
		displayWidth = width;
		displayHeight = height;
	}

	public void showProgressLoading() {

		// 添加滚动条
		if(mProgressBar == null){
			mProgressBar = new ProgressBar(mContext);
			LayoutParams params = new LayoutParams((int)(30*dm.density), (int)(30*dm.density));
			params.leftMargin = (displayWidth - (int)(30*dm.density)) / 2;
			params.topMargin = (displayHeight - (int)(30*dm.density)) / 2;
			mProgressBar.setLayoutParams(params);
			addView(mProgressBar);
			//Log.e(TAG, "new ProgressBar");
		}
		bringChildToFront(mProgressBar);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void hiddenProgressLoading() {
		if(mProgressBar != null){
			mProgressBar.setVisibility(View.GONE);
		}
	}
	

	public abstract void linkUiVar();
	public abstract void bindListener();
	public abstract void ensureUi();
	public abstract void reload();

}
