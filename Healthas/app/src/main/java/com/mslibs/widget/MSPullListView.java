package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;

public abstract class MSPullListView {

	private String TAG = "MSPullListView";
	// 基础变量
	public Activity mActivity; // 主窗体
	public Context mContext; // 上下文
	
	public PullToRefreshListView mListViewPTR; // 带下啦刷新	

	public MSListViewAdapter mAdapter; // 适配器

	public ArrayList<MSListViewItem> mLVIsList; // 适配数据
	public ArrayList<Object> mDataList; // 原型数据
	
	private boolean isSaveItemViews = false;

	// 页面控制变量
	public int mOffset = 0;
	public int mPerpage = 20;
	public int page = 1;
	
	//0=tbd 1=yes -1=no
	public int hasMorePage = 0;
	private Mode pullMode = Mode.DISABLED;
	
	//0=no 1=pulldown 2=pulldown+autoload 3=pulldown+pull up
	private int moreType = 0;
	
	public int actionType = IDLE;
	static public final int IDLE = 0;
	static public final int INIT = 1;
	static public final int REFRESH = 2;
	static public final int GETMORE = 3;

	public MSPullListView(PullToRefreshListView lv, Activity activity) {
		this(lv, 1, activity);
	}

	public MSPullListView(PullToRefreshListView lv, int moreType, Activity activity) {
		if (lv == null) {
			Log.e(TAG, "PullToRefreshListView is null");
		}
		mListViewPTR = lv;
		mActivity = activity;
		mContext = activity.getBaseContext();
		
		this.moreType = 0;
		
		if(moreType==0) {
			this.moreType = moreType;
			pullMode = Mode.DISABLED;
			lv.setMode(pullMode);
		} else if(moreType==1) {
			this.moreType = moreType;
			pullMode = Mode.PULL_FROM_START;
			lv.setMode(pullMode);
		} else if(moreType==2) {
			this.moreType = moreType;
			pullMode = Mode.PULL_FROM_START;
			lv.setMode(pullMode);
		} else if(moreType==3) {
			this.moreType = moreType;
			pullMode = Mode.BOTH;
			lv.setMode(pullMode);			
		}
		//pullMode = Mode.BOTH;
		
		ensureUi();
	}
	

	public void ensureUi() {

		mDataList = new ArrayList<Object>();
		mLVIsList = new ArrayList<MSListViewItem>();
		mAdapter = new MSListViewAdapter(this);

		mAdapter.setLVPsList(mLVIsList);

		if (mListViewPTR != null) {
			mListViewPTR.setAdapter(mAdapter);
			if(Mode.PULL_FROM_START == pullMode) {			
				mListViewPTR.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						refreshStart();
					}
				});
			}
			
			if(Mode.BOTH == pullMode) {			
				mListViewPTR.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						refreshStart();
						
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if(isMorePage()) {
							moreStart();
						}
					}

				});
			}
			
			if(moreType==2) {
				mListViewPTR.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub
					     switch (scrollState) {
				            // 当不滚动时
				            case OnScrollListener.SCROLL_STATE_IDLE:
				                // 判断滚动到底部
				                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
									if(isMorePage()) {
										moreStart();
									}
				              }
				              break;
				        }
					}			
				});
			}
		}

	}

	public void initStart() {
		if (actionType != IDLE)
			return;
		actionType = INIT;
		mOffset = 0;
		hasMorePage = 0;
		page = 1;
		// 异步获取数据
		asyncData();
	}

	public void initFinish() {

		for (int i = 0; i < mDataList.size(); i++) {
			mLVIsList.add(matchListItem(mDataList.get(i), i));
		}

		mAdapter.notifyDataSetChanged();
		
		if(pullMode==Mode.BOTH) {

			if (mListViewPTR != null && mListViewPTR.getMode()==Mode.BOTH) {
				if(!isMorePage()) {
					mListViewPTR.setMode(Mode.PULL_FROM_START);
				}
			}			
		}		
	}

	public void refreshStart() {
		if (actionType != IDLE)
			return;
		actionType = REFRESH;
		mOffset = 0;
		hasMorePage = 0;
		page = 1;
		if(mListViewPTR != null && Mode.BOTH == pullMode) {
			mListViewPTR.setMode(Mode.BOTH);
		}
		// 异步获取数据
		asyncData();
	}


	
	public void refreshFinish() {

		for (int i = 0; i < mDataList.size(); i++) {
			mLVIsList.add(matchListItem(mDataList.get(i), i));
		}

		mAdapter.notifyDataSetChanged();

		// 回到顶部
		if (mListViewPTR != null) {
			mListViewPTR.onRefreshComplete();
		}
		
		if(pullMode==Mode.BOTH) {

			if (mListViewPTR != null && mListViewPTR.getMode()==Mode.BOTH) {
				if(!isMorePage()) {
					mListViewPTR.setMode(Mode.PULL_FROM_START);
				}
			}			
		}

	}

	public void moreStart() {
		if (actionType != IDLE)
			return;
		actionType = GETMORE;
		mOffset += mPerpage;
		page++;
		// 异步获取数据
		asyncData();
	}

	public void moreFinish() {

		
		int addDataCount = mDataList.size();
		
		for (int i = mLVIsList.size(); i < addDataCount; i++) {
			mLVIsList.add(matchListItem(mDataList.get(i), i));
		}

		mAdapter.notifyDataSetChanged();
		
		if(pullMode==Mode.BOTH) {

			if (mListViewPTR != null && mListViewPTR.getMode()==Mode.BOTH) {
				mListViewPTR.onRefreshComplete();			
				if(!isMorePage()) {
					mListViewPTR.setMode(Mode.PULL_FROM_START);
				}
			}			
		}
	}

	public abstract MSListViewItem matchListItem(Object obj, int index);
	
	public void inGetView(View view ,int position){
		inGetView(view);
	}
	
	public void inGetView(View view){
		
	}
	
	public abstract void asyncData();

	private boolean isMorePage() {
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

	public void setFinish() {
		setFinish(true);
	}
	
	public void setFinish(boolean success) {

		if(success) {
			if(actionType == INIT) {
				initFinish();
			} else if(actionType == REFRESH) {
				refreshFinish();
			} else if(actionType == GETMORE) {
				moreFinish();
			}	
		} else {
			if(actionType == INIT) {
			} else if(actionType == REFRESH) {
				if (mListViewPTR != null) {
					mListViewPTR.onRefreshComplete();
				}
			} else if(actionType == GETMORE) {
				if (mListViewPTR != null && mListViewPTR.getMode()==Mode.BOTH) {
					mListViewPTR.onRefreshComplete();
				}
			}	
		}
		actionType = IDLE;
	}

	
	public void smoothScrollToHeader() {
		mListViewPTR.smoothScrollToHeader();
	}
	
	public boolean isInitStatus() {
		return (actionType == INIT);
	}
	
	public void showMessage(String message) {
		NotificationsUtil.ToastMessage(mContext, message);
	}
	
	public void setSaveItemViews(boolean isSaveItemViews) {
		this.isSaveItemViews = isSaveItemViews;
		mAdapter.setSaveItemViews(isSaveItemViews);
	}
}
