package com.mslibs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.health.app.R;


public class PagerIndicator extends CLinearLayout {

	private static String TAG = "PagerIndicator";

	private int mPagerCount = 0;
	private int mPagerIndex = 0;
	private LinearLayout currentPoint = null;

	boolean isGray = false;

	public PagerIndicator(Context context) {
		super(context);
		this.setContentView(R.layout.widget_pager_indicator);
	}

	public PagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setContentView(R.layout.widget_pager_indicator);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.setContentView(R.layout.widget_pager_indicator);
	}
	
	public void setPagerCountForGray(int count) {
		isGray = true;
		setPagerCount(count);
	}

	public void setPagerCount(int count) {
		if (mActivity == null) {
			Log.e(TAG, "mInflater is null,请通过对控件update(mActivity)进行初始化");
			return;
		}
		mPagerCount = count;
		mPagerIndex = 0;
		if (this.getChildCount() > 0) {
			this.removeAllViews();
		}

		for (int i = 0; i < mPagerCount; i++) {
			LinearLayout item = new LinearLayout(mContext);
			final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics metrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(metrics);
			
			LayoutParams params = new LayoutParams((int) (8*metrics.density), (int) (8*metrics.density));
			params.setMargins((int) (4*metrics.density), (int) (6*metrics.density),(int) (4*metrics.density), (int) (6*metrics.density));
			item.setLayoutParams(params);
			
			if (i == 0) {
				if (isGray) {
					item.setBackgroundResource(R.drawable.rounded8_gray_o);
				} else {
					item.setBackgroundResource(R.drawable.rounded8_o);
				}
				currentPoint = item;
			} else {
				if (isGray) {
					item.setBackgroundResource(R.drawable.rounded8_gray_n);
				} else {
					item.setBackgroundResource(R.drawable.rounded8_n);
				}
			}
			this.addView(item);
		}
	}

	public void setPagerIndex(int index) {
		if (mActivity == null) {
			Log.e(TAG, "mInflater is null,请通过对控件update(mActivity)进行初始化");
			return;
		}
		if (index < getChildCount()) {
			mPagerIndex = index;
			LinearLayout item = (LinearLayout) this.getChildAt(index);
			if (isGray) {
				item.setBackgroundResource(R.drawable.rounded8_gray_o);
			} else {
				item.setBackgroundResource(R.drawable.rounded8_o);
			}
			//Log.e(TAG, "setPagerIndex:" + index + "item:" + item);
			if (currentPoint != null) {
				if (isGray) {
					currentPoint.setBackgroundResource(R.drawable.rounded8_gray_n);
				} else {
					currentPoint.setBackgroundResource(R.drawable.rounded8_n);
				}
				//Log.e(TAG, "currentPoint:" + currentPoint);
			}
			currentPoint = item;
		}
	}

	@Override
	public void linkUiVar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ensureUi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

}
