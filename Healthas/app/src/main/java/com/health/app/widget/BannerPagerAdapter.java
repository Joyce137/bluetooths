package com.health.app.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.mslibs.widget.CPagerAdapter;

public class BannerPagerAdapter extends CPagerAdapter {

	private String TAG = "BannerPagerAdapter";

	public BannerPagerAdapter(Activity activity, Context context) {
		super(activity, context);
	}

	public void reload(ArrayList dateList) {
		mDataList = dateList;
	}

	@Override
	public Object getItemView(int position) {
		// TODO Auto-generated method stub
		BannerPagerItem item = new BannerPagerItem(mActivity, mContext);
		item.reload(mDataList.get(position));
		return item;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

}
