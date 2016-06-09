package com.health.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.app.MainActivity;
import com.health.app.MainApplication;
import com.health.app.R;
import com.mslibs.widget.CPagerItem;

public class LoadingPagerItem extends CPagerItem {

	private String TAG = "BannerPagerItem";
	private Object title;
	TextView textTitle;
	ImageView Image_bg;
	Button btnSignIn;
	int i;
	boolean isCityed;
	public MainApplication mApp;

	public LoadingPagerItem(Activity activity, Context context,
			MainApplication mApp) {
		super(activity, context);
		setContentView(R.layout.widget_loading_item);

		this.mApp = mApp;
		linkUiVar();
	}

	@Override
	public void linkUiVar() {
		textTitle = (TextView) findViewById(R.id.textTitle);
		Image_bg = (ImageView) findViewById(R.id.Image_bg);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);
	}

	@Override
	public void bindListener() {
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goTomain();
			}

		});
	}

	public void reload(Object t) {
		title = t;
		bindListener();
		ensureUi();
	}

	@Override
	public void ensureUi() {

		switch (Integer.valueOf(title.toString())) {
//
//		case 1:
//			i = R.drawable.image_1;
//			break;
//
//		case 2:
//			i = R.drawable.image_2;
//			break;
//		case 3:
//			i = R.drawable.image_3;
//			break;
//		case 4:
//			i = R.drawable.image_4;
//			btnSignIn.setVisibility(View.VISIBLE);
//			break;
//
//		default:
//			i = R.drawable.loading;
//			break;
		}

		Image_bg.setImageResource(i);
	}

	@Override
	public void reload() {

	}

	public void goTomain() {
		// 打开主窗体
		if (isCityed) {
			Intent intent = new Intent();
			intent.setClass(mActivity, MainActivity.class);
			mActivity.startActivity(intent);
			mActivity.finish();
		} else {
//			Intent intent = new Intent();
//			intent.setClass(mActivity, SelectCityActivity.class);
//			intent.putExtra("first", 1);
//			mActivity.startActivity(intent);
//			mActivity.finish();
		}
	}
}
