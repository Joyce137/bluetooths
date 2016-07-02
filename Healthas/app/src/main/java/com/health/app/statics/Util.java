package com.health.app.statics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CaoRuijuan on 6/14/16.
 */
public class Util {

    /** *//**
     * 把字节数组转换成16进制字符串
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    // 大小端转化问题

    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /**
     * 字节转换为浮点
     *
     * @param b 字节（至少4个字节）
     * @return
     */
    public static float byte2float(byte[] b) {
        int l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        return Float.intBitsToFloat(l);
    }


    // short转化成byte数组
    public static byte[] shortToLH(int n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    // int转化成byte数组
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }


    public static int bytesToInt(byte[] ary) {
        int value;
        value = (int) ((ary[0]&0xFF)
                | ((ary[0+1]<<8) & 0xFF00)
                | ((ary[0+2]<<16)& 0xFF0000)
                | ((ary[0+3]<<24) & 0xFF000000));
        return value;
    }

    // byte数组转化为int
    public static int vtolh(byte[] bArr) {
        byte begin = bArr[0];
        if (begin < 0) {
            short n = 0;
            for (int i = 0; i < bArr.length && i < 4; i++) {
                int left = i * 8;
                n += (bArr[i] << left);
            }
//			n = n + 256;
//			if(n<0){
//				n = n + 65536;
//			}
            return n + 256;
        } else {
            int n = 0;
            for (int i = 0; i < bArr.length && i < 4; i++) {
                int left = i * 8;
                n += (bArr[i] << left);
            }
            if (n < 0) {
                n = n + 65536;
            }
            return n;
        }
    }

    // 交换对象
    public static void SWAP(Object a, Object b) {
        Object c = a;
        a = b;
        b = c;
    }

    // 生成随机字符串
    public static String getRandomStr() {
        int SIZE = 18;
        String CCH = "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
        char[] ch = new char[SIZE];
        for (int i = 0; i < SIZE - 1; ++i) {
            int x = (int) (Math.random() * 100 % (CCH.length() - 1));
            ch[i] = CCH.charAt(x);
        }
        String str = new String(ch).trim();

        return str;
    }

    // 使用正则表达式过滤非数字的字符串
    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        // 替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }

    /**
     * 获取当前月的最大天数
     * @return
     */
    public static int getDayOfMonth(){
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day=aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    public static String getCurrentMonth(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        return formatter.format(new Date()).trim();
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        return w;
    }

    // 获取当前日期
    public static String getDate() {
        // 日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(new Date()).trim();
    }

    public static int getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String year = formatter.format(new Date());
        return Integer.parseInt(year);
    }

    public static int getDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String day = formatter.format(new Date());
        return Integer.parseInt(day);
    }

    public static int getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String hour = formatter.format(new Date());
        return Integer.parseInt(hour);
    }

    public static int getMinute(){
        SimpleDateFormat formatter = new SimpleDateFormat("mm");
        String hour = formatter.format(new Date());
        return Integer.parseInt(hour);
    }

    // 获取当前时间
    public static String getTime() {
        // 时间
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        return formatter.format(new Date()).trim();
    }

    // 获取当前日期和时间作为文件名
    public static String getDataAndTime() {
        return getDate() + "-" + getTime();
    }

    //判断是否仅包括数字、下划线和字母
    public static boolean onlyIncludingNumber_Letter(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'a' && ch <= 'z') || ch == '_') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    //获取当前时间
    public static String getAppTime() {
        return getDate() + getTime();
    }

    //获取当前时间
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        System.out.println(str);
        return str;
    }

    //获取现在时间
    public static String getFileNameDate(String filename) {
        //拆分
        String[] fileNameArray = filename.split("-");
        String dataStr = fileNameArray[4];

        //判断是否为时间串
        if (Util.filterUnNumber(dataStr) == dataStr && dataStr.length() == 8) {
            return dataStr;
        }
        //如果不是，则使用现在日期
        return Util.getDate();
    }


    //判断某日期与当前日期的差值(是否一个月以内）
    public static boolean checkValid(String date) {
        String now = getDate();
        for (int i = 0; i < 6; i++) {
            if (now.charAt(i) > date.charAt(i))
                return false;
        }
        return true;
    }

    //Q   判断用户名是否有效
    public static boolean checkUsername(String NumberWord) {
        int[] anArray;
        int[] bnArray;
        int i, m;
        int j = 0, n = 0, k = 0;
        while (true) {
            String s = NumberWord;
            bnArray = new int[s.length()];
            anArray = new int[s.length()];

            for (i = 0; i < s.length(); i++) {
                anArray[i] = (int) s.charAt(i);
            }
            //检查用户名中是否包含A~Z，a~z的字
            for (i = 0; i < s.length(); i++)
                if (anArray[i] < 65 || anArray[i] > 90 & anArray[i] < 97
                        || anArray[i] > 122) {
                    bnArray[j] = anArray[i];
                    j++;

                }
            for (m = 0; m < bnArray.length; m++) {
                if (bnArray[m] != 0 & (bnArray[m] > 47 & bnArray[m] < 58))
                    n++;
                else if (bnArray[m] != 0 & (bnArray[m] < 48 || bnArray[m] > 57))
                    k++;
            }
            if (k > 0) {
                //System.out.println("用户名非法，必须只含有数字和字母");
                return false;
            }
            if (n == s.length()) {
                //System.out.println("用户名非法，用户名不能全为数字");
                return false;
            }
            if (j == 0) {
                //System.out.println("用户名非法，用户名不能全为字母");
                return false;
            }
            return true;
        }
    }

    public static boolean checkEmail(String email) {
        String s = email;
        int i, m;
        //检测Email的长度
        if (s.length() == 0 || s.length() > 40)
            return false;
        else {
            //检测"@"在字符串中的位置
            for (i = 0; i < s.length(); i++) {
                System.out.println(s.charAt(i));
                if (s.charAt(i) == '@')
                    break;
            }
            //检测‘.’在字符串中的位置
            for (m = 0; m < s.length(); m++) {
                if (s.charAt(m) == '.')
                    break;
            }
            //判断Email地址中的'@'和'.'是否合法
            if (i == 0 || i == s.length() - 1 || m - i <= 1 || m == s.length() - 1) {
                System.out.println("Email地址不合法");
                return false;
            } else
                return true;
        }

    }

    public static boolean checkNumber(String Number) {
        String s = Number;
        int i = 0, j = 0;
        //检查是否有除数字以外的字符
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
                j++;
            else {
                System.out.println("账号名不合法");
                return false;
            }
        }
        //若字符串中不全是数字，则结果为false
        if (j != s.length())
            return false;
        return true;
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //byte[]->String
    public static String changeByteToString(byte[] bytes) {
        try {
            return new String(bytes, "GBK").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean checkThreeMinute(String time1, String time2){
        String minute1Str = time1.substring(time1.length()-4,time1.length()-2);
        String minute2Str = time2.substring(time2.length()-4,time2.length()-2);
        int minute1 = Integer.parseInt(minute1Str);
        int minute2 = Integer.parseInt(minute2Str);
        if(minute2-minute1 >= 3){
            return true;
        }
        return false;
    }

    public static int getStrWidth(String str,Paint paint){
        Rect bounds = new Rect();
        paint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.width();
    }

    public static byte changeStringToByte(String s) {
        int value = Integer.valueOf(s);
        byte b = (byte) (value &0xff);
        if(value < 0){
            b = (byte) (b|0x80);
        }
        return b;
    }
}
