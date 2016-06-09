package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.health.app.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class CListView {

	private String TAG = "CListView";
	// 基础变量
	public ListView mListView; // 不带下啦刷新
	public PullToRefreshListView mListViewPTR; // 带下啦刷新
	public CListView_PullToRefresh mListViewPTR2; // 带下啦刷新
	public CListViewAdapter mAdapter; // 适配器
	public ArrayList<ArrayList<CListViewParam>> mDateList; // 适配数据
	public ArrayList<Object> mListItems; // 原型数据
	public Activity mActivity; // 主窗体
	public Context mContext; // 上下文

	// 页面控制变量
	public int mOffset = 0;
	public int mPerpage = 20;

	public int actionType = IDLE; // 1=init;2=refresh;3=getmore
	static public final int IDLE = 0;
	static public final int INIT = 1;
	static public final int REFRESH = 2;
	static public final int GETMORE = 3;

	// UI效果变量
	private ProgressDialog dialog;
	private int dialogCount = 0;

	// 资源变量
	private int mListItemResource = 0;
	private int mGetMoreResource = 0;
	private int mGetMoreTitleRID = 0;
	private String mGetMoreTitle = "";
	private int mHeaderResource = 0;
	private int mFooterResource = 0;
	private int mSingleResource = 0;

	public CListView(CListView_PullToRefresh lv, Activity activity) {
		mListViewPTR2 = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		mListItems = new ArrayList<Object>();
		initListItemResource();
		ensureUi();
	}

	public CListView(PullToRefreshListView lv, Activity activity) {
		mListViewPTR = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		mListItems = new ArrayList<Object>();
		initListItemResource();
		ensureUi();
	}

	public CListView(ListView lv, Activity activity) {
		mListView = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		mListItems = new ArrayList<Object>();
		initListItemResource();
		ensureUi();
	}

	public abstract void initListItemResource();

	public void setSelectedResource(int res) {
		mAdapter.setSelectedResource(res);
	}

	public void setSelectedIndex(int i) {
		mAdapter.setSelectedIndex(i);
	}

	public void setSelectedIndex(int i, View v) {
		mAdapter.setSelectedIndex(i, v);
	}

	public void setListItemResource(int mListItemResource) {
		this.mListItemResource = mListItemResource;
	}

	public void setGetMoreResource(int mGetMoreResource, int titleRID, String title) {
		this.mGetMoreResource = mGetMoreResource;
		mGetMoreTitleRID = titleRID;
		mGetMoreTitle = title;
	}

	public void setHeaderResource(int mHeaderResource) {
		this.mHeaderResource = mHeaderResource;
	}

	public void setFooterResource(int mFooterResource) {
		this.mFooterResource = mFooterResource;
	}

	public void setSingleResource(int mSingleResource) {
		this.mSingleResource = mSingleResource;
	}

	public void setItemOnclickLinstener(OnClickListener onClickListener) {
		mAdapter.setItemOnclickLinstener(onClickListener);
	}

	public void setGetMoreClickListener(OnClickListener onClickListener) {
		mAdapter.setGetMoreClickListener(onClickListener);
	}

	public void ensureUi() {

		mDateList = new ArrayList<ArrayList<CListViewParam>>();
		mAdapter = new CListViewAdapter(mContext, mListItemResource);

		mAdapter.setGetMoreResource(mGetMoreResource);
		mAdapter.setHeaderResource(mHeaderResource);
		mAdapter.setFooterResource(mFooterResource);
		mAdapter.setSingleResource(mSingleResource);

		mAdapter.setData(mDateList);

		mAdapter.ItemViewEmptyInvisible = true; // 隐藏空值的控件

		if (mListView != null) {

			mListView.setDividerHeight(0);
			mListView.setAdapter(mAdapter);
		}
		if (mListViewPTR != null) {
			mListViewPTR.setAdapter(mAdapter);
			mListViewPTR.setOnRefreshListener(new OnRefreshListener<ListView>() {
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					refreshListViewStart();
				}
			});
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
		// 异步获取数据
		asyncData();
	}

	public void initListViewFinish() {
		// 控制是否需要显示更多
		isMorePage();

		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}

		if (mAdapter.isNotMore == false) {
			setMoreLVP();
		}

		mAdapter.notifyDataSetChanged();
	}

	public void refreshListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = REFRESH;
		mOffset = 0;
		// 异步获取数据
		asyncData();
	}

	public void refreshListViewFinish() {
		// 控制是否需要显示更多
		isMorePage();

		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}

		if (mAdapter.isNotMore == false) {
			setMoreLVP();
		}

		mAdapter.notifyDataSetChanged();

		// 回到顶部
		if (mListView != null) {
			mListView.setSelection(0);
		} 
		if(mListViewPTR != null){
			mListViewPTR.onRefreshComplete();
		}
		if(mListViewPTR2 != null){
			mListViewPTR2.onRefreshComplete();
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
		// 控制是否需要显示更多
		isMorePage();

		for (int i = mDateList.size() - 1, m = mListItems.size(); i < m; i++) {
			mDateList.add(i, matchListItem(mListItems.get(i), i));
		}
		if (!mDateList.isEmpty() && mAdapter.isNotMore) {
			mDateList.remove(mDateList.size() - 1);
		}
		mAdapter.notifyDataSetChanged();
	}

	public void setMoreLVP() {
		if (mGetMoreResource == 0) {
			return;
		}
		if (mListItems.size() >= mPerpage) {
			ArrayList<CListViewParam> getMoreLVP = new ArrayList<CListViewParam>();
			getMoreLVP.add(new CListViewParam(mGetMoreTitleRID, mGetMoreTitle));
			mDateList.add(getMoreLVP);
		}
	}

	public void openEdit() {

	}

	public void closeEdit() {

	}

	public abstract ArrayList<CListViewParam> matchListItem(Object obj, int index);

	public abstract void asyncData();

	public void showProgress() {

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

	private void isMorePage() {
		// Log.e(TAG, "------------------» count[" + mListItems.size() +
		// "]    mOffset[" + mOffset + "]    mPerpage[" + mPerpage + "]");
		if (mListItems.size() < mOffset + mPerpage) {
			mAdapter.isNotMore = true;
		} else {
			mAdapter.isNotMore = false;
		}
	}
}
