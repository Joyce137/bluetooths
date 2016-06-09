package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CPagerAdapter extends PagerAdapter {
	private String TAG = "CPagerAdapter";

	public Activity mActivity;
	public Context mContext;
	public LayoutInflater mInflater;
	public ArrayList mDataList;
	
	public int pagerCount=0;

	public CPagerAdapter(Activity activity, Context context) {
		super();
		mActivity = activity;
		mContext = context;
		mInflater = mActivity.getLayoutInflater();
		mDataList = new ArrayList<Object>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(pagerCount>0){
			return pagerCount;
		}
		if (mDataList == null)
			return 0;
		return mDataList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == (View) obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Object item = getItemView(position);
		((ViewPager) container).addView((View) item, 0);
		return item;
		
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	public abstract void reload();

	public abstract Object getItemView(int position);

}
