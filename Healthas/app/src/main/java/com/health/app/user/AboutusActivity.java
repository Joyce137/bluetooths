package com.health.app.user;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.health.app.R;
import com.health.app.api.Api;
import com.health.app.type.Version;
import com.health.app.widget.FLActivity;
import com.mslibs.api.CallBack;
import com.mslibs.utils.NotificationsUtil;

import java.util.Random;

@SuppressLint("NewApi")
public class AboutusActivity extends FLActivity {
	private Button btnSub, btnCancel, btnSure;
	private LinearLayout llayoutDialog;
	private String current_version;
	private TextView textVersion;
	Version version;
	TextView textDesc;
	ScrollView mScrollView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.TAG = this.TAG;
		// setContentView(R.layout.activity_user_about);
		navSetContentView(R.layout.activity_user_about);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		btnSub = (Button) findViewById(R.id.btnSub);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSure = (Button) findViewById(R.id.btnSure);
		llayoutDialog = (LinearLayout) findViewById(R.id.llayoutDialog);
		textVersion = (TextView) findViewById(R.id.textVersion);
		textDesc = (TextView) findViewById(R.id.textDesc);
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
	}

	@Override
	public void bindListener() {
		llayoutDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				llayoutDialog.setVisibility(View.GONE);
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				llayoutDialog.setVisibility(View.GONE);
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				downloadApk(version.url);

				llayoutDialog.setVisibility(View.GONE);
			}
		});
		btnSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Api(callback, mApp).version();

			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("关于我们");

		current_version = getVersion();
		textVersion.setText("当前版本：v " + current_version);

	}

	CallBack callback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			Gson gson = new Gson();
			try {
				version = gson.fromJson(response, Version.class);
				// System.out.println(version.version);
				if (version.version.compareTo(current_version) > 0) {
					llayoutDialog.setVisibility(View.VISIBLE);
				} else {
					showMessage("当前应用已是最新版本");
				}

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			showMessage(message);
		}
	};

	private void downloadApk(String url) {

		try {
			Random ran = new Random(System.currentTimeMillis());

			String filename = "health.upgrade." + ran.nextInt(100000) + ".apk";

			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse(url));
			String appname = mContext.getResources().getString(
					R.string.app_name);
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
			NotificationsUtil.ToastLongMessage(mContext, message);

		} catch (Exception e) {
			e.printStackTrace();
			showMessage("下载文件出错");
		}

	}
}
