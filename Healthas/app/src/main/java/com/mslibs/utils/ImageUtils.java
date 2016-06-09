
package com.mslibs.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageUtils {

	private ImageUtils() {
	}
	
	public static void setImage(Context mContext, ImageView imageView, String url, int defaultResource) {
		if(TextUtils.isEmpty(url)) {
			imageView.setImageBitmap(readBitMap(mContext, defaultResource));
			return;
		}		
		Picasso.with(mContext).load(url).into(imageView);	
	}
	
	public static Boolean isLoaded(ImageView imageView) {
		Boolean loaded = (Boolean) imageView.getTag(imageView.getId());
		if(loaded!=null && loaded) return true;		
		return false;
		
	}
	public static void clear(ImageView imageView) {
		imageView.setImageBitmap(null);
		imageView.setTag(imageView.getId(), false);		
	}
	public static Bitmap readBitmapFromNetwork(URL url) {
		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bmp = null;
		try {
			URLConnection conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);	
			bmp = BitmapFactory.decodeStream(bis);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (is != null)
					is.close();
				if (bis != null)
					bis.close();
			} catch (IOException e) {
			}
		}
		return bmp;
	}

	public static void resampleImageAndSaveToNewLocation(String pathInput,
			String pathOutput) throws Exception {
		resampleImageAndSaveToNewLocation(pathInput, pathOutput, 480);
	}
	
	public static void resampleImageAndSaveToNewLocation(String pathInput,
			String pathOutput, int maxDim) throws Exception {
		Bitmap bmp = resampleImage(pathInput, maxDim);

		OutputStream out = new FileOutputStream(pathOutput);
		bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
		
		if (bmp != null) {
			bmp.recycle();
			bmp = null;
		}
		
	}


	public static Bitmap resampleImage(String path, int maxDim)
			throws Exception {

		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bfo);

		BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
		optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth,
				bfo.outHeight, maxDim);

		Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);

		Matrix m = new Matrix();

		if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
			BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(),
					bmpt.getHeight(), maxDim);
			m.postScale((float) optsScale.outWidth / (float) bmpt.getWidth(),
					(float) optsScale.outHeight / (float) bmpt.getHeight());
		}

		/*
		 * int sdk = new Integer(Build.VERSION.SDK).intValue(); if (sdk > 4) {
		 * int rotation = ExifUtils.getExifRotation(path); if (rotation != 0) {
		 * m.postRotate(rotation); } }
		 */

		Bitmap result = Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt
				.getHeight(), m, true);
		
		if (bmpt != result) {
			bmpt.recycle();
		}
		bmpt = null;
		
		return result;
				 
	}

	private static BitmapFactory.Options getResampling(int cx, int cy, int max) {
		float scaleVal = 1.0f;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		if (cx > cy) {
			scaleVal = (float) max / (float) cx;
		} else if (cy > cx) {
			scaleVal = (float) max / (float) cy;
		} else {
			scaleVal = (float) max / (float) cx;
		}
		bfo.outWidth = (int) (cx * scaleVal + 0.5f);
		bfo.outHeight = (int) (cy * scaleVal + 0.5f);
		return bfo;
	}

	private static int getClosestResampleSize(int cx, int cy, int maxDim) {
		int max = Math.max(cx, cy);

		int resample = 1;
		for (resample = 1; resample < Integer.MAX_VALUE; resample++) {
			if (resample * maxDim > max) {
				resample--;
				break;
			}
		}

		if (resample > 0) {
			return resample;
		}
		return 1;
	}

	public static BitmapFactory.Options getBitmapDims(String path)
			throws Exception {
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bfo);
		return bfo;
	}
	
	 public static byte[] getBytes(InputStream is) {
	        try {
	            ByteArrayOutputStream os = new ByteArrayOutputStream();
	            byte[] buf = new byte[1024];
	            for (int i = 0; (i = is.read(buf)) > 0;) {
	                os.write(buf, 0, i);
	            }
	            os.close();
	            return os.toByteArray();
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage(), e);
	        }
	    }
	
	 public static void compressAndSaveBitmap(Bitmap rawBitmap,
			String fileName) {
		
		int quality = 90;
		
		String saveFilePaht = fileName;
		
		System.out.println("saveFilePaht="+saveFilePaht);
		File saveFile = new File(saveFilePaht);
		//if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(
						saveFile);
				if (fileOutputStream != null) {
					// imageBitmap.compress(format, quality, stream);
					// 把位图的压缩信息写入到一个指定的输出流中
					// 第一个参数format为压缩的格式
					// 第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
					// 第三个参数stream为输出流
					rawBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
							fileOutputStream);
				}
				fileOutputStream.flush();
				fileOutputStream.close();
				
				System.out.println("test");
			} catch (IOException e) {
				e.printStackTrace();

			}
		//}
	}
	 
	 public static void saveBitmap(InputStream ins, String fileName) {
			String saveFilePaht = fileName;
			
			System.out.println("saveFilePaht="+saveFilePaht);
			File saveFile = new File(saveFilePaht);
			
			try {
				
			
			 OutputStream os = new FileOutputStream(saveFile);
			 int bytesRead = 0;
			 byte[] buffer = new byte[8192];
			 while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			 os.write(buffer, 0, bytesRead);
			 }
			 os.close();
			 ins.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
	 }
	  public static Bitmap readBitMap(Context context, int resId){ 

	        BitmapFactory.Options opt = new BitmapFactory.Options();

	        opt.inPreferredConfig = Bitmap.Config.RGB_565;

	        opt.inPurgeable = true;

	        opt.inInputShareable = true;

	        // 获取资源图片
	        InputStream is = context.getResources().openRawResource(resId);

	        return BitmapFactory.decodeStream(is, null, opt);

	        }
}