package com.mslibs.widget;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MSListViewAdapter extends BaseAdapter {
	
	private boolean isSaveItemViews = false;
	private ArrayList<View> mItemViewList = null;

	private final String TAG = "MSListViewAdapter";

	private ArrayList<MSListViewItem> mLVIsList;
	
	private Object Listview;

	public MSListViewAdapter(Object lv) {
		Listview = lv;
	}

	public void setLVPsList(ArrayList<MSListViewItem> LVIs) {
		mLVIsList = LVIs;
	}

	@Override
	public int getCount() {
		return mLVIsList.size();
	}

	@Override
	public Object getItem(int position) {
		return mLVIsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		Log.e("position", "position======="+position);
		
		if(isSaveItemViews && position<mItemViewList.size()){
			view = mItemViewList.get(position);
		}
		
		if(view == null){
			MSListViewItem lvitem = mLVIsList.get(position);
			view = lvitem.getView(convertView);
			if(isSaveItemViews){
				mItemViewList.add(view);
			}
		}
		
		if(Listview instanceof MSPullListView){
			
			MSPullListView mMSPullListView = (MSPullListView)Listview;
			mMSPullListView.inGetView(view,position);
			
		}else if(Listview instanceof MSListView){
			
			MSListView mMSListView = (MSListView)Listview;
			mMSListView.inGetView(view,position);
			
		}
		return view;
	}
	
	public boolean isSaveItemViews() {
		return isSaveItemViews;
	}

	public void setSaveItemViews(boolean isSaveItemViews) {
		this.isSaveItemViews = isSaveItemViews;
		if(isSaveItemViews && mItemViewList==null){
			mItemViewList = new ArrayList<View>();
		}
	}
}
