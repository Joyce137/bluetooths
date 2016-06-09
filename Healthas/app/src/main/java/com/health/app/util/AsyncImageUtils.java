package com.health.app.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Picasso;

public class AsyncImageUtils {

	public static void setImage(ImageView imageView, String url,
			int defaultResource) {
		if (TextUtils.isEmpty(url)) {
			imageView.setImageResource(defaultResource);
			return;
		}
		UrlImageViewHelper.setUrlDrawable(imageView, url, defaultResource);
	}

	public static void loadUrlDrawable(Context context, String url,
			final UrlImageViewCallback callback) {
		UrlImageViewHelper.loadUrlDrawable(context, url, callback);
	}

	public static void loadUrlDrawable(Context context, String url,
			final FutureCallback<Bitmap> callback) {
		// UrlImageViewHelper.loadUrlDrawable(context, url, callback);
		Ion.with(context).load(url).asBitmap().setCallback(callback);

	}

	public static void setImagePicasso(Context mContext, ImageView imageView,
			String url, int defaultResource) {
		if (TextUtils.isEmpty(url)) {
			imageView.setImageResource(defaultResource);
			return;
		}
		if (defaultResource > 0) {
			imageView.setImageResource(defaultResource);
		}
		Picasso.with(mContext).load(url).error(defaultResource).into(imageView);

	}

	public static void setImagePicasso(Context mContext, ImageView imageView,
			String url, int defaultResource, int width, int height) {
		if (TextUtils.isEmpty(url)) {
			imageView.setImageResource(defaultResource);
			return;
		}
		Log.e("setImagePicasso", "resize width:" + width + " height:" + height
				+ " url:" + url);

		Picasso.with(mContext).load(url).resize(width, height).centerCrop()
				.into(imageView);

	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */

	public static Bitmap readBitMap(Context context, int resId) {

		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);

	}
}
