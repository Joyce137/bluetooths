package com.health.app.widget;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.health.app.R;
import com.health.app.type.HomeBanner;
import com.mslibs.widget.CRelativeLayout;

public class BannerLayout extends CRelativeLayout {

	private String TAG = "BannerLayout";

	private ViewPager viewPager;
	private BannerPagerAdapter bannerPagerAdapter;
	private PagerIndicator pagerIndicator;
	ArrayList<HomeBanner> mDataList;
	// ArrayList<String> mDataList;
	Timer mTimer = null;
	TimerTask task = null;

	public BannerLayout(Context context) {
		super(context);
		this.setContentView(R.layout.widget_banner_layout);
		Log.e(TAG, "BannerLayout(Context context)");
	}

	public BannerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setContentView(R.layout.widget_banner_layout);
		// Log.e(TAG, "BannerLayout(Context context, AttributeSet attrs)");
	}

	@Override
	public void linkUiVar() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);
		pagerIndicator.setVisibility(GONE);
		pagerIndicator.update(mActivity);
		setProgressBarResourceID(R.id.mProgressBarLayout);
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				pagerIndicator.setPagerIndex(arg0);
				// Log.e(TAG, "setPagerIndex:" + arg0);
			}

		});

		viewPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				viewPager.getParent().requestDisallowInterceptTouchEvent(true);

				if (event.ACTION_UP == event.getAction()) {
					// Log.e(TAG, "ACTION_UP");
					reload();
				}
				if (event.ACTION_MOVE == event.getAction()) {
					// Log.e(TAG, "ACTION_MOVE");
					pause();
				}

				return false;
			}
		});
	}

	@Override
	public void ensureUi() {

		// refresh();

		// 自动播放banner 每3秒一次
		// reload();
	}

	// public void refresh() {
	//
	// bannerPagerAdapter = new BannerPagerAdapter(mActivity, mContext);
	// mDataList = new ArrayList<String>();
	// mDataList.add("banner1");
	// mDataList.add("banner2");
	// mDataList.add("banner3");
	// mDataList.add("banner4");
	// bannerPagerAdapter.mDataList = mDataList;
	//
	// viewPager.setAdapter(bannerPagerAdapter);
	// pagerIndicator.setPagerCount(bannerPagerAdapter.getCount());
	//
	// Log.e(TAG, ""+bannerPagerAdapter.getCount());
	// if (bannerPagerAdapter.getCount() > 1) {
	// reload();
	// }
	//
	// hiddenProgressLoading();
	// }
	public void refresh(ArrayList<HomeBanner> banner) {
		// new
		// Api(callback).test("{\"success\":1,\"data\":[{\"title\":\"banner_img01\"},{\"title\":\"banner_img02\"},{\"title\":\"banner_img03\"},{\"title\":\"banner_img04\"},{\"title\":\"banner_img05\"}]}");

		// new Api(callback).recommendedevent(10, 1);

		bannerPagerAdapter = new BannerPagerAdapter(mActivity, mContext);
		mDataList = new ArrayList<HomeBanner>();

		if (banner != null) {
			for (int i = 0; i < banner.size(); i++) {
				mDataList.add(banner.get(i));
			}
		}
		bannerPagerAdapter.reload(mDataList);
		viewPager.setAdapter(bannerPagerAdapter);

		// viewPager
		// pagerIndicator.setPagerCountForGray(bannerPagerAdapter.getCount());//黑色
		pagerIndicator.setVisibility(VISIBLE);
		pagerIndicator.setPagerCount(bannerPagerAdapter.getCount());
		if (bannerPagerAdapter.getCount() > 1) {
			reload();
		}

		hiddenProgressLoading();
	}

	@Override
	public void reload() {

		pause();

		// Log.e(TAG, "banner reload");
		if (mTimer == null && task == null) {
			// Log.e(TAG, "banner reload mTimer==null");
			mTimer = new Timer();

			task = new TimerTask() {
				public void run() {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
			};
			mTimer.schedule(task, 4500, 4500);
		}
	}

	public void pause() {

		// Log.e(TAG, "banner pause");
		if (mTimer != null && task != null) {
			// Log.e(TAG, "banner pause if(mTimer!=null) ");
			task.cancel();
			mTimer.cancel();
			task = null;
			mTimer = null;
		}
	}

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (mDataList == null || mDataList.size() == 0) {
					return;
				}
				int index = viewPager.getCurrentItem();
				// Log.e(TAG, "getCurrentItem:" + index);
				index++;
				if (index >= mDataList.size()) {
					index = 0;
				}
				viewPager.setCurrentItem(index, true);
				break;
			}
			super.handleMessage(msg);
		}
	};

}
