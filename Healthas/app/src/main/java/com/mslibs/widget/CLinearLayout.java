package com.mslibs.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public abstract class CLinearLayout extends LinearLayout {

	private static String TAG = "CLinearLayout";

	public Activity mActivity;
	public Context mContext;
	public LayoutInflater mInflater;

	private LinearLayout mProgressBarLayout;
	private ProgressBar mProgressBar;

	private int mResourceID = 0;

	public CLinearLayout(Context context) {
		super(context);
	}

	public CLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setContentView(int id) {
		if (mResourceID == 0) {
			mResourceID = id;
			View.inflate(this.getContext(), mResourceID, this);
			Log.e(TAG, "setContentView,View.inflate by mResourceID:" + mResourceID);
		}
	}

	public void addContentView(int id) {
		if (mActivity == null) {
			Log.e(TAG, "mInflater is null,请通过对控件update(mActivity)进行初始化");
			return;
		}
		addView(mInflater.inflate(id, null));
	}

	public void setProgressBarResourceID(int id) {
		if (mActivity == null) {
			Log.e(TAG, "mInflater is null,请通过对控件update(mActivity)进行初始化");
			return;
		}
		if (id > 0) {
			mProgressBarLayout = (LinearLayout) this.findViewById(id);
			if (mProgressBarLayout != null) {
				mProgressBar = new ProgressBar(mContext);
				LinearLayout.LayoutParams pParams = new LinearLayout.LayoutParams(30, 30);
				mProgressBar.setLayoutParams(pParams);
				mProgressBarLayout.addView(mProgressBar);
			}
		} else {
			return;
		}
	}

	public void showProgressLoading() {
		if (mActivity == null) {
			Log.e(TAG, "mInflater is null,请通过对控件update(mActivity)进行初始化");
			return;
		}
		if (mProgressBarLayout != null) {
			bringChildToFront(mProgressBarLayout);
			mProgressBarLayout.setVisibility(View.VISIBLE);
		}
	}

	public void hiddenProgressLoading() {
		if (mActivity == null) {
			Log.e(TAG, "mInflater is null,请通过对控件update(mActivity)进行初始化");
			return;
		}
		if (mProgressBarLayout != null) {
			mProgressBarLayout.setVisibility(View.GONE);
		}
	}

	public void update(Activity activity) {
		mActivity = activity;
		mContext = activity.getBaseContext();
		mInflater = activity.getLayoutInflater();
		linkUiVar();
		bindListener();
		ensureUi();
	}

	public abstract void linkUiVar();

	public abstract void bindListener();

	public abstract void ensureUi();

	public abstract void reload();
}
