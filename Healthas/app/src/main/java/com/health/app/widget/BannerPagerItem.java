package com.health.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.health.app.R;
import com.health.app.type.HomeBanner;
import com.health.app.util.ImageLoaderUtil;
import com.mslibs.widget.CPagerItem;

public class BannerPagerItem extends CPagerItem {

	private String TAG = "BannerPagerItem";
	private Object data;
	ImageView imgBanner;

	public BannerPagerItem(Activity activity, Context context) {
		super(activity, context);
		setContentView(R.layout.widget_banner_item);
		linkUiVar();
	}

	@Override
	public void linkUiVar() {
		imgBanner = (ImageView) findViewById(R.id.imgBanner);
	}

	@Override
	public void bindListener() {
		imgBanner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HomeBanner item = (HomeBanner) data;
				if (item.url != null && item.url.length() > 0) {
//					Intent intent = new Intent(mContext, WebviewActivity.class);
//					intent.putExtra("url", item.url);
//					mActivity.startActivity(intent);
				}
				//
				// Log.e(TAG, "item.picture"+item.picture);
			}
		});
	}

	public void reload(Object d) {
		data = d;
		bindListener();
		ensureUi();
	}

	@Override
	public void ensureUi() {

		// if (data instanceof String) {
		// String imgName = (String) data;
		// Resources resources = mContext.getResources();
		//
		// // Log.e(TAG, ""+resources.getIdentifier(item, "drawable",
		// "com.zj360.app"));
		// // imgBanner.setImageResource(resources.getIdentifier(item,
		// "drawable", "com.zj360.app"));
		// //
		//
		//
		// int imageResource = resources.getIdentifier(mContext.getPackageName()
		// + ":drawable/" + imgName, null, null);
		// imgBanner.setImageResource(imageResource);
		//
		//
		// }
		int width = 0;
		width = getWidth2();
		HomeBanner item = (HomeBanner) data;
		Log.e(TAG, "item.picture" + item.image + "--------" + width);
		
		if (item.picture != null && item.picture.length() > 0) {
			ImageLoaderUtil.setImage(imgBanner, item.picture,R.drawable.default120);
		} else {
			ImageLoaderUtil.setImage(imgBanner, item.image,R.drawable.default120);
		}

	}

	@Override
	public void reload() {

	}

	// 获取宽度
	public int getWidth2() {
		final WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

}
