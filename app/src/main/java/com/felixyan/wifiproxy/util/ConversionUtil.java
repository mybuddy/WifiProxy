package com.felixyan.wifiproxy.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yanfei on 2017/08/13.
 */

public class ConversionUtil {
    private static final String CLASS_TAG = ConversionUtil.class.getSimpleName();

    public static String bool2Str(boolean b) {
        return Boolean.toString(b);
    }

    public static boolean str2Bool(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Boolean.valueOf(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String int2Str(int num){
        return int2Str(num, "0");
    }

    public static String int2Str(int num, String emptyVal) {
        return num != 0 ? Integer.toString(num) : emptyVal;
    }

    public static int str2Int(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Integer.valueOf(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static String long2Str(long num){
        return long2Str(num, "0");
    }

    public static String long2Str(long num, String emptyVal) {
        return num != 0 ? Long.toString(num) : emptyVal;
    }

    public static long str2Long(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Long.valueOf(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static String float2Str(float num){
        return float2Str(num, "0");
    }

    public static String float2Str(float num, String emptyVal) {
        return num != 0 ? Float.toString(num) : emptyVal;
    }

    public static float str2Float(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Float.valueOf(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static String double2Str(double num){
        return double2Str(num, "0");
    }

    public static String double2Str(double num, String emptyVal) {
        return num != 0 ? Double.toString(num) : emptyVal;
    }

    public static double str2Double(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return Double.valueOf(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    /**
     * double设置保留小数位数（向下取整，正数、负数均趋于0）
     *
     * @param value 要设置小数位数的小数
     * @param scale 保留的小数位数
     * @return
     */
    public static double scaleDouble(double value, int scale) {
        try {
            BigDecimal decimal = new BigDecimal(value);
            return decimal.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * float类型的小数设置保留小数位数（向下取整，正数、负数均趋于0）
     *
     * @param value 要设置小数位数的小数
     * @param scale 保留的小数位数
     * @return
     */
    public static float scaleFloat(float value, int scale) {
        try {
            BigDecimal decimal = new BigDecimal(value);
            return decimal.setScale(scale, BigDecimal.ROUND_DOWN).floatValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String date2Str(Date date) {
        return date2Str(date, null);
    }

    public static String date2Str(Date date, String format) {
        if (date == null) return "";
        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = "";
        try {
            dateStr = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateStr;
    }

    public static Date str2Date(String dateStr){
        return str2Date(dateStr, null);
    }

    public static Date str2Date(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) return null;
        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static long str2TimeStamp(String dateStr){
        return str2TimeStamp(dateStr, null);
    }

    public static long str2TimeStamp(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) return 0;

        format = TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long timestamp = 0;
        try {
            Date date = sdf.parse(dateStr);
            timestamp = date.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return timestamp;
    }

    public static String timeStamp2DateStr(long timeStamp) {
        return timeStamp2DateStr(timeStamp, null);
    }

    public static String timeStamp2DateStr(long timeStamp, String dateFormat) {
        if (dateFormat == null) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateStr = "";
        try {
            Date date = new Date(timeStamp);
            dateStr = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateStr;
    }
}
