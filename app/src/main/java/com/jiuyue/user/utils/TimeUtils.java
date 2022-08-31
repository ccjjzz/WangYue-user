package com.jiuyue.user.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TimeUtils {
    //将毫秒转换为小时：分钟：秒格式
    public static String ms2HMS(int _ms) {
        String HMSTime;
        _ms /= 1000;
        int hour = _ms / 3600;
        int mint = (_ms % 3600) / 60;
        int sed = _ms % 60;
        String hourStr = String.valueOf(hour);
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        String mintStr = String.valueOf(mint);
        if (mint < 10) {
            mintStr = "0" + mintStr;
        }
        String sedStr = String.valueOf(sed);
        if (sed < 10) {
            sedStr = "0" + sedStr;
        }
        HMSTime = hourStr + ":" + mintStr + ":" + sedStr;
        return HMSTime;
    }

    // 将毫秒转换为标准日期格式
    public static String ms2Date(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public static String ms2DateOnlyDay(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    // 标准时间转换为时间戳
    public static long Date2ms(String _data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(_data);
            return Objects.requireNonNull(date).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    //计算时间差
    public static String DateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 0) {
            timeLong = 0;
        }
        if (timeLong < 60 * 1000)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if ((timeLong / 1000 / 60 / 60 / 24) < 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(startDate);
        }
    }

    //计算与当前的时间差
    public static String DateDistance2now(long _ms) {
        SimpleDateFormat DateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Long time = _ms;
            String d = DateF.format(time);
            Date startDate = DateF.parse(d);
            Date nowDate = Calendar.getInstance().getTime();
            return DateDistance(startDate, nowDate);
        } catch (Exception ignored) {
        }
        return null;
    }

    // 判断是不是新的一天
    public static Boolean NewDateDistance(long startDate, long endDate) {
        long timeLong = endDate - startDate;
        return timeLong / 60 * 60 * 24 * 1000 == 0;
    }


    //秒数转时分秒
    public static String SecondChangeMinute(long second) {
        long h = 0;
        long d = 0;
        long s = 0;
        long temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        String d1, s1;
        if (d < 10) {
            d1 = "0" + d;
        } else {
            d1 = String.valueOf(d);
        }
        if (s < 10) {
            s1 = "0" + s;
        } else {
            s1 = String.valueOf(s);
        }
        if (h == 0) {
            return d1 + ":" + s1;
        } else {
            return h + ":" + d1 + ":" + s1;
        }
    }

}
