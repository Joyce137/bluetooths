package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.health.app.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class MSListView {

	private String TAG = "MSListView";
	// 基础变量
	public Activity mActivity; // 主窗体
	public Context mContext; // 上下文

	public ListView mListView; // 不带下啦刷新
	public PullToRefreshListView mListViewPTR; // 带下啦刷新
	public CListView_PullToRefresh mListViewPTR2; // 带下啦刷新

	public MSListViewAdapter mAdapter; // 适配器

	public ArrayList<MSListViewItem> mLVIsList; // 适配数据
	public ArrayList<Object> mDataList; // 原型数据
	private boolean isSaveItemViews = false;

	// 页面控制变量
	public int mOffset = 0;
	public int mPerpage = 20;

	//0=tbd 1=yes -1=no
	public int hasMorePage = 0;
	public Mode pullMode = Mode.DISABLED;
	public boolean shouldDisplayProgress = false;
	
	public int actionType = IDLE;
	static public final int IDLE = 0;
	static public final int INIT = 1;
	static public final int REFRESH = 2;
	static public final int GETMORE = 3;

	// UI效果变量
	private ProgressDialog dialog;
	private int dialogCount = 0;

	public MSListView(CListView_PullToRefresh lv, Activity activity) {
		if (lv == null) {
			Log.e(TAG, "CListView_PullToRefresh is null");
		}
		mListViewPTR2 = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		ensureUi();
	}

	public MSListView(PullToRefreshListView lv, Activity activity) {
		if (lv == null) {
			Log.e(TAG, "PullToRefreshListView is null");
		}
		mListViewPTR = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		
		pullMode = lv.getMode();
		
		ensureUi();
	}

	public MSListView(ListView lv, Activity activity) {
		if (lv == null) {
			Log.e(TAG, "ListView is null");
		}
		mListView = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		ensureUi();
	}

	public void ensureUi() {

		mDataList = new ArrayList<Object>();
		mLVIsList = new ArrayList<MSListViewItem>();
		mAdapter = new MSListViewAdapter(this);

		mAdapter.setLVPsList(mLVIsList);

		if (mListView != null) {
			mListView.setDividerHeight(0);
			mListView.setAdapter(mAdapter);
		}

		if (mListViewPTR != null) {
			mListViewPTR.setAdapter(mAdapter);
			if(Mode.PULL_FROM_START == pullMode) {			
				mListViewPTR.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						refreshListViewStart();
					}
				});
			}
			
			if(Mode.BOTH == pullMode) {			
				mListViewPTR.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						refreshListViewStart();
						
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						getmoreListViewStart();
					}

				});
			}
		}

		if (mListViewPTR2 != null) {
			mListViewPTR2.setDividerHeight(0);
			mListViewPTR2.setAdapter(mAdapter);
			mListViewPTR2.setOnRefreshListener(new CListView_PullToRefresh.OnRefreshListener() {
				@Override
				public void onRefresh() {
					refreshListViewStart();
				}
			});
		}
	}

	public void initListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = INIT;
		mOffset = 0;
		hasMorePage = 0;
		// 异步获取数据
		asyncData();
	}

	public void initListViewFinish() {

		for (int i = 0; i < mDataList.size(); i++) {
			mLVIsList.add(matchListItem(mDataList.get(i), i));
		}

		mAdapter.notifyDataSetChanged();
	}

	public void refreshListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = REFRESH;
		mOffset = 0;
		hasMorePage = 0;
		if(mListViewPTR != null && Mode.BOTH == pullMode) {
			mListViewPTR.setMode(Mode.BOTH);
		}
		// 异步获取数据
		asyncData();
	}

	public void refreshListViewFinish() {

		for (int i = 0; i < mDataList.size(); i++) {
			mLVIsList.add(matchListItem(mDataList.get(i), i));
		}

		mAdapter.notifyDataSetChanged();

		// 回到顶部
		if (mListView != null) {
			mListView.setSelection(0);
		}
		if (mListViewPTR != null) {
			mListViewPTR.onRefreshComplete();
		}
		if (mListViewPTR2 != null) {
			mListViewPTR2.onRefreshComplete();
			mListViewPTR2.setSelection(0);
		}
	}

	public void getmoreListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = GETMORE;
		mOffset += mPerpage;

		// 异步获取数据
		asyncData();
	}

	public void getmoreListViewFinish() {

		if(pullMode==Mode.BOTH) {
			
			int addDataCount = mDataList.size();
			
			for (int i = mLVIsList.size(); i < addDataCount; i++) {
				mLVIsList.add(matchListItem(mDataList.get(i), i));
			}

			
			mAdapter.notifyDataSetChanged();
			
			if (mListViewPTR != null && mListViewPTR.getMode()==Mode.BOTH) {
				mListViewPTR.onRefreshComplete();
				
				if(!isMorePage()) {
					mListViewPTR.setMode(Mode.PULL_FROM_START);
				}
			}
			
		} else {
			int addDataCount = mDataList.size();
			if (isMorePage()) {
				addDataCount = mDataList.size() - 1;
			}

			for (int i = mLVIsList.size() - 1; i < addDataCount; i++) {
				mLVIsList.add(i, matchListItem(mDataList.get(i), i));
			}

			if (!isMorePage()) {
				int deleteIndex = mLVIsList.size() - 1;
				mLVIsList.remove(deleteIndex);
			}

			mAdapter.notifyDataSetChanged();
		}
	}

	public void openEdit() {

	}

	public void closeEdit() {

	}

	public abstract MSListViewItem matchListItem(Object obj, int index);
	
	public void inGetView(View view ,int position){
		inGetView(view);
	}
	
	public void inGetView(View view){
		
	}
	
	public abstract void asyncData();

	public void showProgress() {
		
		if(true) return;
		
		dialogCount++;
		// Log.e(TAG, "------------------» showProgress["+dialogCount+"]");

		if (dialogCount > 1)
			return;

		if (dialog != null) {
			dialog.show();
		} else {
			try {
				dialog = ProgressDialog.show(mActivity, mContext.getString(R.string.dialog_title),
						mContext.getString(R.string.dialog_message), false, true, cg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	DialogInterface.OnCancelListener cg = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			Log.e(TAG, "------------------» cancel");
			dialogCount--;
		}
	};

	public void dismissProgress() {

		if(true) return;
		dialogCount--;
		// Log.e(TAG, "------------------» dismissProgress["+dialogCount+"]");

		if (dialogCount > 0)
			return;

		if (dialog == null) {
			return;
		} else {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog = null;
		}

	}

	public boolean isMorePage() {
		if(hasMorePage==1) {
			return true;
		} else if(hasMorePage==-1) {
			return false;
		}
		
		if (mDataList.size() < mOffset + mPerpage) {
			return false;
		} else {
			return true;
		}
	}

	public void setMorePage(boolean hasMore) {
		if(hasMore) {
			hasMorePage = 1;
		} else {
			hasMorePage = -1;
		}
	}

	public boolean isSaveItemViews() {
		return isSaveItemViews;
	}

	public void setSaveItemViews(boolean isSaveItemViews) {
		this.isSaveItemViews = isSaveItemViews;
		mAdapter.setSaveItemViews(isSaveItemViews);
	}
}
