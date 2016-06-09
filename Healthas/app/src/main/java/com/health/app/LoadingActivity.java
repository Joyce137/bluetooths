package com.health.app;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.health.app.api.Api;
import com.health.app.type.Version;
import com.health.app.user.UserSignActivity;
import com.health.app.widget.FLActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mslibs.api.CallBack;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.PagerIndicator;

public class LoadingActivity extends FLActivity {

	private final String TAG = "LoadingActivity";
	// mActivity,mContext,mainApp 主窗体已经定义

	boolean needUpdate = false;
	boolean isCityed = false;
	ViewPager viewPager;
	PagerIndicator pagerIndicator;
	// LoadingPagerAdapter mLoadingPagerAdapter;

	Button btnCancel, btnSure;
	private LinearLayout llayoutDialog;
	private String current_version;
	Version version;

	// ImageView image;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.TAG = this.TAG;
		setContentView(R.layout.activity_loading);

		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		// image = findViewById(R.id.image)
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);
		pagerIndicator.update(mActivity);

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSure = (Button) findViewById(R.id.btnSure);
		llayoutDialog = (LinearLayout) findViewById(R.id.llayoutDialog);
	}

	@Override
	public void bindListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				pagerIndicator.setPagerIndex(arg0);
				// Log.e(TAG, "setPagerIndex:" + arg0);
			}

		});
		llayoutDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				llayoutDialog.setVisibility(View.GONE);
				if (version.force != 0) {
					finish();
				} else {
					gotoMian();
					// login();
				}
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				downloadApk(version.url);

				llayoutDialog.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void ensureUi() {
		current_version = getVersion();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1); // 给UI主线程发送消息
			}
		}, 3000); // 启动等待2秒钟

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 切换到主页面 或 检查更新
//				new Api(callback, mApp).version();
				// login();
				 gotoMian();
				break;
			}
		}
	};

	public void login() {
		// boolean ran =
		// mApp.getPref().getBoolean(Preferences.LOCAL.FIRSTRUN,false);
		// ran = false;
		// if (!ran) {
		// mApp.setPreference(Preferences.LOCAL.FIRSTRUN, true);

		// mLoadingPagerAdapter = new LoadingPagerAdapter(mActivity, mContext,
		// mApp);
		// ArrayList<String> mDataList = new ArrayList<String>();
		//
		// mDataList.add("1");
		// mDataList.add("2");
		// mDataList.add("3");
		//
		// mLoadingPagerAdapter.reload(mDataList);
		// viewPager.setAdapter(mLoadingPagerAdapter);

		// pagerIndicator.setVisibility(View.VISIBLE);
		// pagerIndicator.setPagerCount(mDataList.size());
		return;
	}

	public void gotoMian() {
		// isCityed = mApp.isCityed();
		// // 打开主窗体
		 if (mApp.isLogged()) {
			Intent intent = new Intent();
			intent.setClass(mActivity, MainActivity.class);
			mActivity.startActivity(intent);
			mActivity.finish();
		 } else {
			 Intent intent = new Intent();
			 intent.setClass(mActivity, UserSignActivity.class);
			 mActivity.startActivity(intent);
			 mActivity.finish();
		 }

	}

	CallBack callback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			Gson gson = new Gson();
			try {
				version = gson.fromJson(response, Version.class);
				// System.out.println(version.version);
				if (version.version.compareTo(current_version) > 0) {
					if (version.force != 0) {
						btnCancel.setText("退出");
					} else {

					}
					llayoutDialog.setVisibility(View.VISIBLE);
				} else {
					gotoMian();
					// login();
				}

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			showMessage(message);
			gotoMian();
		}
	};

	@SuppressLint("NewApi")
	private void downloadApk(String url) {

		try {
			Random ran = new Random(System.currentTimeMillis());

			String filename = "lohas.upgrade." + ran.nextInt(100000) + ".apk";

			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse(url));
			String appname = this.getResources().getString(R.string.app_name);
			request.setDescription(appname);
			request.setTitle("开始下载新版本");

			String downloaddir = Environment.DIRECTORY_DOWNLOADS;

			if (downloaddir.contains("://")) {
				downloaddir = "download";
			}

			// Log.e(TAG, "url=" + url + " dir:" + downloaddir + " filename:" +
			// filename);

			Environment.getExternalStoragePublicDirectory(downloaddir).mkdir();

			request.setDestinationInExternalPublicDir(downloaddir, filename);

			DownloadManager manager = (DownloadManager) mActivity
					.getSystemService(DOWNLOAD_SERVICE);

			long dwnid = manager.enqueue(request);

			String message = "开始下载新版本";
			NotificationsUtil.ToastLongMessage(this, message);

		} catch (Exception e) {
			e.printStackTrace();
			NotificationsUtil.ToastMessage(this, "下载文件出错");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}
}
