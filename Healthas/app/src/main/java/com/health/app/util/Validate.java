package com.health.app.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.mslibs.utils.MsStringUtils;

public class Validate {
	// 验证号码
	// public static boolean isMobileNO(String mobiles) {
	// Pattern p = Pattern
	// .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	// Matcher m = p.matcher(mobiles);
	// return m.matches();
	// }

	//
	public static boolean isMobile(String mobile) {

		if (TextUtils.isEmpty(mobile)) {
			System.out.println("00000000");
			return false;
		}
		if (mobile.length() != 11) {
			return false;
		}

		return true;
	}

	// 验证邮箱
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	// 验证车牌
	public static boolean isCarCode(String code) {
		String str = "^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[A-Za-z_0-9]{5}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(code);
		return m.matches();
	}

	// String 保留2位小数
	public static String formateRate(String rateStr) {
		if (rateStr.indexOf("") != -1) {
			// 获取小数点的位置
			int num = 0;
			num = rateStr.indexOf("");

			// 获取小数点后面的数字 是否有两位 不足两位补足两位
			String dianAfter = rateStr.substring(0, num + 1);
			String afterData = rateStr.replace(dianAfter, "");
			if (afterData.length() < 2) {
				afterData = afterData + "0";
			} else {
				afterData = afterData;
			}
			return rateStr.substring(0, num) + "" + afterData.substring(0, 2);
		} else {
			if (rateStr == "1") {
				return "100";
			} else {
				return rateStr;
			}
		}
	}

	/**
	 * 去掉多余的.与0
	 * 
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (TextUtils.isEmpty(s)) {
			return "0";
		}
		if (s.indexOf("") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	public static String formatStr(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		String result = "";
		if (str.contains("")) {
			int index = str.indexOf("");
			result = str.substring(0, index);
		} else {
			result = str;
		}

		return result;
	}

	// double 保留2位小数
	public static double format(double result) {
		BigDecimal bd = new BigDecimal(result);
		BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return Double.parseDouble(bd2.toString());
	}

	public static String timeToString(String time) {
		long time2 = MsStringUtils.str2long(time);
		Date dat = new Date(time2 * 1000);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		return format.format(gc.getTime());
	}

	public static String timeToString4(long time) {
		Date dat = new Date(time);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		return format.format(gc.getTime());
	}

	public static String timeToString2(String time) {
		long time2 = MsStringUtils.str2long(time);
		Date dat = new Date(time2 * 1000);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return format.format(gc.getTime());
	}

	public static String timeToString5(long time) {
		Date dat = new Date(time);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return format.format(gc.getTime());
	}

	public static String timeToString3(String time) {
		long time2 = MsStringUtils.str2long(time);
		Date dat = new Date(time2 * 1000);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		return format.format(gc.getTime());
	}

	public static long parseTime(String time) {
		long timer = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			timer = format.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timer;
	}

	public static String getDay(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = null;
		try {
			date = sdf.parse(s);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			if (cal.get(Calendar.DAY_OF_WEEK) == 2) {
				return "周一";
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 3) {
				return "周二";
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 4) {
				return "周三";
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 5) {
				return "周四";
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 6) {
				return "周五";
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
				return "周六";
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				return "周日";
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "-1";

	}

	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	private static double rad1(double d) {
		return d * Math.PI / 180.0;
	}

	// 2个坐标的距离
	public static String getDistance(String lat21, String lng21, String lat22,
			String lng22) {

		double lat1 = MsStringUtils.str2double(lat21);
		double lng1 = MsStringUtils.str2double(lng21);
		double lat2 = MsStringUtils.str2double(lat22);
		double lng2 = MsStringUtils.str2double(lng22);

		double radLat1 = rad1(lat1);
		double radLat2 = rad1(lat2);
		double a = radLat1 - radLat2;
		double b = rad1(lng1) - rad1(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378.137;
		// s = Math.round(s * 10000) / 10000;

		int d = (int) (s * 100);
		s = ((double) d) / 100;

		String dis = "";

		if (s < 1) {

			dis = (int) (s * 1000) + "m";

		} else {
			dis = s + "km";
		}

		return dis;
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		if (year % 4 != 0) {
			flag = false;
		} else if (year % 100 != 0) {
			flag = true;
		} else if (year % 400 != 0) {
			flag = false;
		} else {
			flag = true;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			if (flag) {
				day = 29;
			} else {
				day = 28;
			}

			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

}
