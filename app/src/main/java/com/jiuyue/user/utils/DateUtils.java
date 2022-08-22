package com.jiuyue.user.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getNowDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    /**
     * 获取当前日期往后的年月日
     *
     * @param days
     * @return
     */
    public static String beforeAfterDateStr(int days) {
        long nowTime = System.currentTimeMillis();
        long changeTimes = days * 24L * 60 * 60 * 1000;
        return getStrTime(String.valueOf(nowTime + changeTimes), "yyyy-MM-dd");
    }

    /**
     * 获取当前日期往后的月日
     *
     * @param days
     * @return
     */
    public static String beforeAfterDateMonthDay(int days) {
        long nowTime = System.currentTimeMillis();
        long changeTimes = days * 24L * 60 * 60 * 1000;
        return getStrTime(String.valueOf(nowTime + changeTimes), "MM-dd");
    }

    /**
     * 获取当前时间往后的N分钟的时间
     *
     * @param minute
     * @return
     */
    public static String beforeAfterTimeStr(int minute) {
        long nowTime = System.currentTimeMillis();
        long changeTimes = minute * 60L * 1000;
        return getStrTime(String.valueOf(nowTime + changeTimes), "HH:mm");
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp, String format) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    /**
     * 获取当前日期往后的日期格式
     *
     * @param days
     * @return
     */
    public static Date beforeAfterDates(int days) {
        long nowTime = System.currentTimeMillis();
        long changeTimes = days * 24L * 60 * 60 * 1000;
        long l = Long.parseLong(String.valueOf(nowTime + changeTimes));
        return new Date(l);
    }

    /**
     * 根据日期获取周几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 字符串日期转Date
     *
     * @param dateStr
     * @return
     */
    public static Date getDateOfStr(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String formatDate(String date) {
        String newDate = "";
        if (date.length() > 0) {
            if (date.length() > 4) {
                newDate = date.substring(0, 4) + "年";
            }
            if (date.length() > 7) {
                newDate += date.substring(5, 7) + "月";
            }
            if (date.length() > 10) {
                newDate += date.substring(8, 10) + "日";
            }
            newDate += date.substring(10, date.length() - 1);
        }
        return newDate;
    }
}
