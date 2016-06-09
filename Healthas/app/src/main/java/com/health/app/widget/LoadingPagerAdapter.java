package com.health.app.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.health.app.MainApplication;
import com.mslibs.widget.CPagerAdapter;

public class LoadingPagerAdapter extends CPagerAdapter {

	private String TAG = "LoadingPagerAdapter";
	public MainApplication mApp;

	public LoadingPagerAdapter(Activity activity, Context context,
			MainApplication mApp) {
		super(activity, context);
		this.mApp = mApp;
	}

	public void reload(ArrayList dateList) {
		mDataList = dateList;
	}

	@Override
	public Object getItemView(int position) {
		// TODO Auto-generated method stub
		LoadingPagerItem item = new LoadingPagerItem(mActivity, mContext, mApp);
		item.reload(mDataList.get(position));
		return item;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

}
