package com.mslibs.widget;

import java.util.ArrayList;

import com.health.app.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.GridView;


public abstract class CGridView {

	private String TAG = "CGridView";
	// 基础变量
	public GridView mGridView;
	public CListViewAdapter mAdapter; // 适配器
	public ArrayList<ArrayList<CListViewParam>> mDateList; // 适配数据
	public ArrayList<Object> mListItems; // 原型数据
	public Activity mActivity; // 主窗体
	public Context mContext; // 上下文

	// 页面控制变量
	private int mOffset = 0;
	protected int mPerpage = 20;

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

	public CGridView(GridView gv, Activity activity) {
		mGridView = gv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		mListItems = new ArrayList<Object>();
		initListItemResource();
		ensureUi();
	}

	public abstract void initListItemResource();

	public void setListItemResource(int mListItemResource) {
		this.mListItemResource = mListItemResource;
	}

	public void setGetMoreResource(int mGetMoreResource,int titleRID,String title) {
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
		mAdapter = new CListViewAdapter(mActivity, mListItemResource);

		mAdapter.setGetMoreResource(mGetMoreResource);
		mAdapter.setHeaderResource(mHeaderResource);
		mAdapter.setFooterResource(mFooterResource);
		mAdapter.setSingleResource(mSingleResource);

		mAdapter.setData(mDateList);

		mAdapter.ItemViewEmptyInvisible = true; // 隐藏空值的控件

		mGridView.setAdapter(mAdapter);
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

		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}
		setMoreLVP();
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

		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}
		setMoreLVP();
		mAdapter.notifyDataSetChanged();
		mGridView.setSelection(0);
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
		for (int i = mDateList.size() - 1, m = mListItems.size(); i < m; i++) {
			mDateList.add(i, matchListItem(mListItems.get(i), i));
		}
		mAdapter.notifyDataSetChanged();
	}

	public void setMoreLVP() {
		if (mGetMoreResource == 0) {
			return;
		}

		if (mListItems.size() >= mPerpage) {
			ArrayList<CListViewParam> getMoreLVP = new ArrayList<CListViewParam>();
			getMoreLVP.add(new CListViewParam(mGetMoreTitleRID,mGetMoreTitle));
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
		//Log.e(TAG, "------------------» showProgress[" + dialogCount + "]");

		if (dialogCount > 1)
			return;

		if (dialog != null){
			dialog.show();
		}else{
			dialog = ProgressDialog.show(mActivity, mContext.getString(R.string.dialog_title), mContext.getString(R.string.dialog_message));
		}
		
		
		
	}

	public void dismissProgress() {

		dialogCount--;
		//Log.e(TAG, "------------------» dismissProgress[" + dialogCount + "]");

		if (dialogCount > 0)
			return;

		if (dialog == null)
			return;

		dialog.dismiss();

	}

}
