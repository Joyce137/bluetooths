package com.health.app;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.health.app.util.ImageLoaderUtil;
import com.health.app.util.Preferences;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


@SuppressLint("NewApi")
public class  MainApplication extends Application {
	private final String TAG = "MainApplication";
	private SharedPreferences mPrefs;

	private static MainApplication mInstance = null;

	private BroadcastReceiver OnDownloadDonereceiver;

	private static Context context;

	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}
		super.onCreate();

		Log.e(TAG, "MainApplication onCreate");

		context = getApplicationContext();

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mInstance = this;

		
		loadDownloadReceiver();

		ImageLoaderUtil.initImageLoader(getApplicationContext());
	}

	public static Context getContext() {

		return context;

	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static MainApplication getInstance() {
		return mInstance;
	}

	public SharedPreferences getPref() {
		return mPrefs;
	}

	public void setPreference(String PrefId, boolean value) {
		if (false == value) {
			mPrefs.edit().remove(PrefId).commit();
		} else {
			mPrefs.edit().putBoolean(PrefId, value).commit();
		}
	}

	public void setPreference(String PrefId, String value) {
		if (TextUtils.isEmpty(value)) {
			mPrefs.edit().remove(PrefId).commit();
		} else {
			mPrefs.edit().putString(PrefId, value).commit();
		}
	}

	public String getPreference(String PrefId) {

		String value = mPrefs.getString(PrefId, null);
		if (TextUtils.isEmpty(value)) {
			return null;
		}
		return value;
	}

	public String getImei() {
		String imei = getPreference(Preferences.LOCAL.IMEI);
		if (TextUtils.isEmpty(imei)) {
			WifiManager wifi = (WifiManager) getBaseContext().getSystemService(
					Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			imei = info.getMacAddress();
			if (TextUtils.isEmpty(imei)) {
				if (TextUtils.isEmpty(imei)) {
					Random ran = new Random(System.currentTimeMillis());
					imei = "random_imei_" + ran.nextInt(1000000);
				}
			}
			setPreference(Preferences.LOCAL.IMEI, imei);
		}
		return imei;
	}

	public String getToken() {
		String token = getPreference(Preferences.LOCAL.TOKEN);
		if (!TextUtils.isEmpty(token)) {
			return token;
		}
		return null;
	}

	
	public boolean isLogged() {
		String token = getPreference(Preferences.LOCAL.TOKEN);

		if (TextUtils.isEmpty(token)) {
			return false;
		}
		return true;
	}

	private void loadDownloadReceiver() {
		OnDownloadDonereceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
					// do something
					// Log.e(TAG, "ACTION_DOWNLOAD_COMPLETE");
					Long dwnId = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, 0);
					// Log.e(TAG, "got download id: " + dwnId);
					DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

					Query query = new Query();
					query.setFilterById(dwnId);
					Cursor cur = manager.query(query);

					if (cur.moveToFirst()) {
						int columnIndex = cur
								.getColumnIndex(DownloadManager.COLUMN_STATUS);
						int status = cur.getInt(columnIndex);

						// Log.e(TAG, "got cur.getInt(columnIndex):" + status);
						if (DownloadManager.STATUS_SUCCESSFUL == status) {
							String localUriString = cur
									.getString(cur
											.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
							// String uriString =
							// cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_URI));

							File mFile = new File(Uri.parse(localUriString)
									.getPath());

							if (!mFile.exists()) {
								return;
							}

							String filename = mFile.getName().toString();

							// sendBroadcast(new
							// Intent(Preferences.INTENT_ACTION.REFRESH));

							// Log.e(TAG, "got file:" + "/" +
							// mFile.getAbsolutePath());

							if (filename.endsWith(".apk")
									&& filename.startsWith("health.upgrade.")) {
								installPackage(mFile);
							}

						} else if (DownloadManager.STATUS_FAILED == status) {
							// NotificationsUtil.ToastMessage(getApplicationContext(),
							// "下载文件失败");
						}
					}
				}
			}
		};
		registerReceiver(OnDownloadDonereceiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public Boolean installPackage(File mFile) {

		// Log.e(TAG, "installPackage");

		Uri uri = Uri.fromFile(mFile);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		return true;
	}

	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
}
