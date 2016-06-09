package com.health.app.widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.health.app.MainApplication;
import com.health.app.R;
import com.mslibs.api.CallBack;
import com.mslibs.utils.ImageUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.Preferences;

public abstract class FLActivity extends NavbarActivity {

	public MainApplication mApp;

	CallBack callback = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApp = (MainApplication) mainApp;

		// 设置loading参数
		super.setLlayoutOverViewRsID(R.id.llayoutOverView);
		super.setLoadingLayoutRsID(R.layout.over_view_loading);
		super.setTipsLayoutRsID(R.layout.over_view_tips);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {

		String className = this.getClass().getName();
		Log.e("MSActivity", "OnResume() : " + className);
		super.onResume();
	}

	public String getExtraString(String key) {
		if (getIntent().hasExtra(key)) {
			return getIntent().getStringExtra(key);
		}

		Log.e(TAG, "not exsit for key:" + key);

		return null;
	}

	public void startCameraIntent() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_upload.jpg"));
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

	public void BuildImageDialog(Context context, CallBack _callback) {

		callback = _callback;

		CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
		builder.setTitle("上传图片");
		builder.setMessage("什么方式上传图片呢?");
		builder.setPositiveButton("拍照上传",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						startCameraIntent();
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("相册选择",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						startGalleryIntent();
						dialog.dismiss();
					}
				});
		builder.create().show();
//		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//		builder.setTitle("上传图片");
//		builder.setMessage("什么方式上传图片呢?");
//		builder.setPositiveButton("拍照上传",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						startCameraIntent();
//					}
//				});
//		builder.setNegativeButton("相册选择",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						startGalleryIntent();
//					}
//				});
//		builder.show();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String pathInput = null;
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {

		case Preferences.REQUEST_CODE.TAKE_PHOTO:

			try {
				String mImagePath = Environment.getExternalStorageDirectory()
						+ "/tmp_upload.jpg";
				ImageUtils.resampleImageAndSaveToNewLocation(mImagePath,
						mImagePath);
				Bitmap bmp = BitmapFactory.decodeFile(mImagePath);

				if (callback != null) {
					callback.setExtra(bmp);
					callback.onSuccess(mImagePath);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case Preferences.REQUEST_CODE.GET_PHOTO:

			try {
				Uri uri = data.getData();
				String urlScheme = uri.getScheme();
				pathInput = uri.getPath();

				if (urlScheme.equals("file")) {
					pathInput = uri.getPath();
				} else if (urlScheme.equals("content")) {
					String[] proj = { MediaStore.Images.Media.DATA };
					// Cursor query = ContentResolver.query(uri, proj, null,
					// null, null);
					// @SuppressWarnings("deprecation")
					Cursor cursor = getContentResolver().query(uri, proj, null,
							null, null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					pathInput = cursor.getString(column_index);
					cursor.close();
				}

				if (!TextUtils.isEmpty(pathInput)) {
					String mImagePath = Environment
							.getExternalStorageDirectory() + "/tmp_upload.jpg";
					ImageUtils.resampleImageAndSaveToNewLocation(pathInput,
							mImagePath);
					Bitmap bmp = BitmapFactory.decodeFile(mImagePath);
					if (callback != null) {
						callback.setExtra(bmp);
						callback.onSuccess(mImagePath);
					}
				} else {
					NotificationsUtil.ToastMessage(getBaseContext(), "无法读入图片");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		}

		callback = null;

	}

	public void saveBitmap(Bitmap image) {

		OutputStream outStream = null;
		File file = new File(Environment.getExternalStorageDirectory(),
				"tmp_upload.jpg");
		try {
			outStream = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
		}
	}

	public void call(final String tel) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("拨打电话: " + tel + "?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ tel));
				mActivity.startActivity(intent);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();

	}

	public String initImagePath() {

		String image = null;

		try {
			// String cachePath =
			// cn.sharesdk.framework.utils.R.getCachePath(this, null);
			// image = cachePath + "sns.jpg";
			// File file = new File(image);
			// if (!file.exists()) {
			// file.createNewFile();
			// Bitmap pic = BitmapFactory.decodeResource(getResources(),
			// R.drawable.ic_launcher);
			// FileOutputStream fos = new FileOutputStream(file);
			// pic.compress(CompressFormat.JPEG, 100, fos);
			// fos.flush();
			// fos.close();
			// }
		} catch (Throwable t) {
			t.printStackTrace();
			image = null;
		}

		return image;
	}

	
}
