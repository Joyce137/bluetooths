package com.mslibs.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

public class MsStringUtils {

	public static int str2int(String text) {
		int myNum = 0;

		try {
		    myNum = Integer.parseInt(text);
		} catch(Exception nfe) {	
			nfe.getStackTrace();
		}
		return myNum;
	}
	public static long str2long(String text) {
		long myNum = 0;

		try {
		    myNum = Long.parseLong(text);
		} catch(Exception nfe) {	
			nfe.getStackTrace();
		}
		return myNum;
	}
	public static double str2double(String text) {
		double myNum = 0;

		try {
		    myNum = Double.parseDouble(text);
		} catch(Exception nfe) {
			nfe.getStackTrace();
		}
		return myNum;
	}	
	public static float str2float(String text) {
		float myNum = 0;

		try {
		    myNum = Float.parseFloat(text);
		} catch(Exception nfe) {	
			nfe.getStackTrace();
		}
		return myNum;
	}
	//join(String array,delimiter)
	public static String join(ArrayList<String> r,String d)
	{
	        if (r.size() == 0) return "";
	        StringBuilder sb = new StringBuilder();
	        int i;
	        for(i=0;i<r.size()-1;i++)
	            sb.append(r.get(i)+d);
	        return sb.toString()+r.get(i);	        
	}
	
	public static String uniqid() {
	    Random random = new Random();
	    String tag = Long.toString(Math.abs(random.nextLong()), 36)+Long.toString(Math.abs(random.nextLong()), 36);
	    return tag.substring(2, 15);
	}
	
	public static String formatDouble(double value) {
        BigDecimal b2 = new BigDecimal(value);        
        b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
        
        DecimalFormat fnum  =  new  DecimalFormat("##0.00");  
        
        return fnum.format(b2.doubleValue());
	}
	public static String timeToString(String time) {
		long time2 = str2long(time);
		Date dat = new Date(time2*1000);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd");

		return format.format(gc.getTime());
	}
	public static String timeToString2(String time) {
		long time2 = str2long(time);
		//Date dat = new Date(time2*1000);
		Date dat = new Date(time2);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		return format.format(gc.getTime());
	}

	public static String byteFormat(long appSize) {
		
		//long appSize = str2long(appSizeStr);
		String result = "";
		float lastSize = (float) (((int) (appSize / 1024.0 * 100)) / 100.0);
		if (appSize >= 1024) {
			
			lastSize = (float) (((int) (lastSize / 1024.0 * 100)) / 100.0);
			
			result = lastSize + "MB";
		} else {
			result = appSize + "KB";
		}
		
		return result;
	}
}
