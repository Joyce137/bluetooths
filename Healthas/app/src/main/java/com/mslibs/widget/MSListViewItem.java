package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class MSListViewItem {

	private final String TAG = "MSListViewItem";
	private int listIndex;
	private Activity mActivity;
	private LayoutInflater mInflater;

	public ArrayList<MSListViewParam> mLVParamList;
	private int mListViewResource = 0;
	private OnClickListener itemOnClickListener = null;

	public MSListViewItem(int index, Activity activity, int res, OnClickListener ocl) {
		listIndex = index;
		mActivity = activity;
		mInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListViewResource = res;
		itemOnClickListener = ocl;
		mLVParamList = new ArrayList<MSListViewParam>();
	}

	public void add(MSListViewParam item) {
		mLVParamList.add(item);
	}

	public View getView(View convertView) {
		View newView = null;

//		 if (convertView != null) {
//			 newView = convertView;
//		 }

		//Log.e(TAG, "getView \n   mListViewResource:" + mListViewResource + "\n   listIndex:" + listIndex);

		if (mListViewResource != 0) {
			newView = mInflater.inflate(mListViewResource, null);
			newView.setTag(listIndex);
			if (itemOnClickListener != null) {
				newView.setOnClickListener(itemOnClickListener);
			}
			for (int i = 0; i < mLVParamList.size(); i++) {
				MSListViewParam lvp = mLVParamList.get(i);
				if (lvp != null) {
					lvp.bindItemData(newView);
				}else{
					Log.e(TAG,"getView \n MSListViewParam at "+i+"is null");
				}
			}
		}

		return newView;
	}
}
