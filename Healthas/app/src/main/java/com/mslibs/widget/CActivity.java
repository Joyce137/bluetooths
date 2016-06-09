package com.mslibs.widget;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.ImageUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.Preferences;

public abstract class CActivity extends Activity {

	public String TAG = "CActivity";
	private final boolean DEBUG = Preferences.DEBUG;

	public Activity mActivity;
	public Context mContext;
	public Application mainApp;
	private LayoutInflater mInflater;

	// 覆盖层
	private LinearLayout llayoutOverView = null;
	private int llayoutOverViewRsID = 0;
	// 加载界面
	private LinearLayout llayoutLoading = null;
	private int loadingLayoutRsID = 0;
	private int loadingShowCount = 0;
	private String loadingTips = null;
	// 提示界面
	private LinearLayout llayoutTips = null;
	private int tipsLayoutRsID = 0;
	private String refreshTitle = null;
	private String refreshTips = null;
	private String btnTitle = null;
	private OnClickListener refreshOCL = null;

	// UI效果变量
	private ProgressDialog dialog;
	private int dialogCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = this;
		mainApp = (Application) mActivity.getApplication();
		mContext = mActivity.getBaseContext();
		mInflater = (LayoutInflater) mActivity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void log(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}

	public abstract void linkUiVar();

	public abstract void bindListener();

	public abstract void ensureUi();

	// 设置overview r.id.llayoutOverView
	public void setLlayoutOverViewRsID(int rsID) {
		this.llayoutOverViewRsID = rsID;
	}
	// 设置loadinglayout R.layout.over_view_loading
	public void setLoadingLayoutRsID(int rsID) {
		this.loadingLayoutRsID = rsID;
	}
	// 设置tipsayout R.layout.over_view_tips及刷新事件
	public void setTipsLayoutRsID(int rsID) {
		this.tipsLayoutRsID = rsID;
	}

	public void showProgressDialog(String title, String message) {
		dialogCount++;
		Log.e(TAG, "------------------» showProgress[" + dialogCount + "]");

		if (dialogCount > 1)
			return;

		if (dialog != null)
			return;

		try {
			dialog = ProgressDialog.show(mActivity, title, message, false, true, cg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void dismissProgressDialog() {
		dialogCount--;
		Log.e(TAG, "------------------» dismissProgress[" + dialogCount + "]");

		if (dialogCount > 0)
			return;

		if (dialog == null)
			return;

		try {
			dialog.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean showOverView() {
		if (llayoutOverView != null) {
			llayoutOverView.setVisibility(View.VISIBLE);
		} else {
			if (llayoutOverViewRsID != 0) {
				llayoutOverView = (LinearLayout) this.findViewById(llayoutOverViewRsID);
				if (llayoutOverView != null) {
					llayoutOverView.setVisibility(View.VISIBLE);
				} else {
					Log.e(TAG, "------------------» showLoadingLayout[llayoutOverView not found]");
					return false;
				}
			} else {
				Log.e(TAG, "------------------» showLoadingLayout[llayoutOverViewRsID is 0]");
				return false;
			}
		}
		return true;
	}
	public void showLoadingLayout() {
		showLoadingLayout(null);
	}
	public void showLoadingLayout(String tips) {

		loadingTips = tips;
		if (!showOverView()) {
			return;
		}

		llayoutOverView.removeAllViews();
		if (llayoutLoading == null) {
			if (loadingLayoutRsID != 0) {
				llayoutLoading = (LinearLayout) mInflater.inflate(loadingLayoutRsID, null);
				if (llayoutLoading != null) {
					llayoutLoading.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 捕捉点击时间

						}

					});
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					llayoutLoading.setLayoutParams(params);

				} else {
					Log.e(TAG, "------------------» showLoadingLayout[llayoutLoading cannt inflate]");
					return;
				}
			} else {
				Log.e(TAG, "------------------» showLoadingLayout[loadingLayoutRsID is 0]");
				return;
			}
		}

		// 设置提示文字

		TextView textTips = (TextView) llayoutLoading.findViewWithTag("textTips");
		if (textTips != null) {
			if (!TextUtils.isEmpty(loadingTips)) {
				textTips.setText(loadingTips);
				textTips.setVisibility(View.VISIBLE);
			} else {
				textTips.setVisibility(View.GONE);
			}
		}
		llayoutOverView.addView(llayoutLoading);

		loadingShowCount++; // 对显示次数计数
		Log.e(TAG, "------------------» showLoadingLayout[" + loadingShowCount + "]");
	}

	public void dismissLoadingLayout() {

		loadingShowCount--;
		Log.e(TAG, "------------------» dismissLoadingLayout[" + dialogCount + "]");

		if (loadingShowCount <= 0) { // 显示次数小于等于0时关闭OverView
			if (llayoutOverView != null) {
				llayoutOverView.setVisibility(View.GONE);
			}
		}
	}
	public void showTipsLayout(OnClickListener ocl) {
		showTipsLayout(null, null, null, ocl);
	}
	public void showTipsLayout(String title, String tips, String btntitle, OnClickListener ocl) {
		refreshTitle = title;
		refreshTips = tips;
		btnTitle = btntitle;
		refreshOCL = ocl;

		if (!showOverView()) {
			return;
		}

		// private LinearLayout llayoutTips = null;
		// private int tipsLayoutRsID = 0;
		// private String refreshTitle = null;
		// private OnClickListener refreshOCL = null;

		llayoutOverView.removeAllViews();
		if (llayoutTips == null) {
			if (tipsLayoutRsID != 0) {
				llayoutTips = (LinearLayout) mInflater.inflate(tipsLayoutRsID, null);
				if (llayoutTips != null) {
					llayoutTips.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 捕捉点击时间

						}

					});
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					llayoutTips.setLayoutParams(params);

					// 设置提示标题
					if (refreshTitle != null) {
						TextView textTitle = (TextView) llayoutTips.findViewWithTag("textTitle");
						if (textTitle != null) {
							textTitle.setText(refreshTitle);
						}
					} else {
						TextView textTitle = (TextView) llayoutTips.findViewWithTag("textTitle");
						if (textTitle != null) {
							textTitle.setVisibility(View.GONE);
						}
					}
					// 设置提示文字
					if (refreshTips != null) {
						TextView textTips = (TextView) llayoutTips.findViewWithTag("textTips");
						if (textTips != null) {
							textTips.setText(refreshTips);
						}
					} else {
						TextView textTips = (TextView) llayoutTips.findViewWithTag("textTips");
						if (textTips != null) {
							textTips.setVisibility(View.GONE);
						}
					}
					// 添加按钮事件
					if (refreshOCL != null) {
						Button btnRefresh = (Button) llayoutTips.findViewWithTag("btnRefresh");
						if (btnRefresh != null) {
							btnRefresh.setOnClickListener(refreshOCL);
							if (btnTitle != null) {
								btnRefresh.setText(btnTitle);
							}
						}
					}

				} else {
					Log.e(TAG, "------------------» showTipsLayout[llayoutTips cannt inflate]");
					return;
				}
			} else {
				Log.e(TAG, "------------------» showTipsLayout[tipsLayoutRsID is 0]");
				return;
			}
		}
		llayoutOverView.addView(llayoutTips);
	}
	public void dismissTipsLayout() {

		loadingShowCount--;
		Log.e(TAG, "------------------» dismissProgress[" + dialogCount + "]");

		if (loadingShowCount <= 0) { // 显示次数小于等于0时关闭OverView
			if (llayoutOverView != null) {
				llayoutOverView.setVisibility(View.GONE);
			}
		}
	}

	public void showProgress() {
		showProgressDialog("正在获取内容", "请稍候...");
	}
	public void dismissProgress() {
		dismissProgressDialog();
	}

	DialogInterface.OnCancelListener cg = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			Log.e(TAG, "------------------» cancel");
			dialogCount--;
		}
	};

	public void startCameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_upload.jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

		startActivityForResult(intent, Preferences.REQUEST_CODE.TAKE_PHOTO);
	}
	public void startGalleryIntent() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, Preferences.REQUEST_CODE.GET_PHOTO);
		} catch (Exception ex) {

		}
	}

	public void BuildImageDialog(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("什么方式上传图片呢?");
		builder.setPositiveButton("拍照上传", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startCameraIntent();
			}
		});
		builder.setNegativeButton("相册选择", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startGalleryIntent();

			}
		});
		builder.show();

	}

	public void hideSoftInput(View v) {
		Log.e(TAG, "hideSoftInput");
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 打开软键盘
	public void openKeyboard() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			}
		}, 500);
	}

	// 关闭软键盘
	public void closeKeyboard(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	// 获取密度
	public float getMetricsDensity() {
		final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.density;
	}

	// 获取宽度
	public int getWidth() {
		final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public String getVersion() {
		String v = "";
		try {
			v = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	public int getVersionCode() {
		int v = 0;
		try {
			v = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	public void showAlert(String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();
	}

	public void showAlert(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("友情提醒");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();
	}

	public void showMessage(String message) {
		NotificationsUtil.ToastMessage(mContext, message);
	}

	public void setImage(ImageView imageview, String url, int defaultresource) {

		ImageUtils.setImage(mContext, imageview, url, defaultresource);
		// Ion.with(imageview).placeholder(defaultresource).error(defaultresource).load(url);
		// UrlImageViewHelper.setUrlDrawable(imageview, url, defaultresource);
	}

	public void sendBroadcast(String action) {
		Intent intent = new Intent().setAction(action);
		mContext.sendBroadcast(intent);
	}

	public void showNewAlert(String title, String message) {
		OnClickListener refreshOCL = new OnClickListener() {
			@Override
			public void onClick(View v) {
				((CActivity) mActivity).dismissTipsLayout();
			}
		};
		((CActivity) mActivity).showTipsLayout(title, message, "确定", refreshOCL);
	}

	public void sentMsgToHandler() {
		Message message = new Message();
		message.what = 1;
		mHandler.sendMessage(message);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// handler处理消息
			if (msg.what == 1) {
				handlerMsgCallback();
			} else {

			}
		}
	};

	public void handlerMsgCallback() {

		Log.e(TAG, "handlerMsgCallback,没有被子函数重写");

	}

}
