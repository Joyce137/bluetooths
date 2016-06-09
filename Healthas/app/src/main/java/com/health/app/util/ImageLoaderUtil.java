package com.health.app.util;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageLoaderUtil {
	public static DisplayImageOptions options;

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"health/Cache");

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽 // default = device
				// screen dimensions
				.threadPoolSize(3)
				// 线程池内加载数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(14 * 1024 * 1024))
				// You can pass your own memory cache implementation
				// /你可以通过自己的内存缓存实现
				.memoryCache(new WeakMemoryCache())
				// .memoryCacheSize(2 * 1024 * 1024)
				// .diskCacheSize(200 * 1024 * 1024)
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
				// //将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheFileCount(100) //缓存的文件数量
				.diskCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路劲
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout(
																				// 5s
																				// ),
																				// readTimeout
																				// (
																				// 30
																				// )
																				// 超时时间
				// .writeDebugLogs() //Remove for release app
				.build(); // 开始构建

		ImageLoader.getInstance().init(configuration);
	}

	public static void setImage(ImageView imageView, String url, int defaultImge) {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(defaultImge) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(defaultImge) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(defaultImge) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) // 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型//
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions) //设置图片的解码配置
				.delayBeforeLoading(0) // int delayInMillis为你设置的下载前的延迟时间
				.resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
				// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
				.displayer(new SimpleBitmapDisplayer())

				.build();// 构建完成
		L.disableLogging();// 去掉log
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}

	public static void clreaCache() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}
}
