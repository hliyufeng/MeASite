package com.mea.site.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 * 日期处理
 */
public class DateUtils {
    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 计算距离现在多久，非精确
     *
     * @param date
     * @return
     */
    public static String getTimeBefore(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        } else if (hour > 0) {
            r += hour + "小时";
        } else if (min > 0) {
            r += min + "分";
        } else if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }

    /**
     * 计算距离现在多久，精确
     *
     * @param date
     * @return
     */
    public static String getTimeBeforeAccurate(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        }
        if (hour > 0) {
            r += hour + "小时";
        }
        if (min > 0) {
            r += min + "分";
        }
        if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }
    /**
     * ******************************************
     * 增加天数，以当前时间.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @param 参数名 参数类型 参数描述
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年11月5日::x05::创建此方法<br>
     ********************************************
     */
    public static Date addDay(int day) {
        return AddDay(new Date(), day);
    }

    public static Date AddDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 加一年
     * */
    public static Date addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * ******************************************
     * 增加分钟，以当前时间.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @param 参数名 参数类型 参数描述
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年11月11日::x05::创建此方法<br>
     ********************************************
     */
    public static Date AddMinute(int minute) {
        return AddMinute(new Date(), minute);
    }

    public static Date AddMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * ******************************************
     * 增加小时.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @param 参数名 参数类型 参数描述
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年11月24日::x05::创建此方法<br>
     ********************************************
     */
    public static Date addHour(int day) {
        return AddDay(new Date(), day);
    }

    public static Date AddHour(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, day);
        return calendar.getTime();
    }

    /**
     * ******************************************
     * 2014-12-12.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @param 参数名 参数类型 参数描述
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年11月22日::x05::创建此方法<br>
     ********************************************
     */
    public static String getDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getDate(Date date, String fmt) {
        if (fmt == null || fmt.trim().length() == 0) {
            fmt = "yyyy-MM-dd";
        }
        DateFormat format = new SimpleDateFormat(fmt);
        return format.format(date);
    }

    public static String getDateYYYYMMddHHmmss(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getDateYYMMdd_HHmmss(Date date) {
        DateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // 获取当天时间
    public static String getToday() {
        Date now = new Date();
        return sdf.format(now);
    }

    // 获取凌晨零点
    public static Date getMidnightDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 获取凌晨零点
    public static Date getMidnightDateTime(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 获取23点59分59秒
    public static Date getDateEndTime(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    // 获取当天的下一天日期
    public static String getTommorow() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);
        Date tommorow = c.getTime();
        return sdf.format(tommorow);
    }

    // 获取当天的前一天的日期
    public static String getYesterday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = c.getTime();
        return sdf.format(yesterday);
    }

    // 获取本周一日期
    public static String getWeekA() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sdf.format(cal.getTime());
    }

    // 获得本周的指定日期(周一到周日到下周一)
    public static String getWeekDay(int day) {
        Calendar cal = Calendar.getInstance();
        switch (day) {
            case 1:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case 2:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case 3:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case 4:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case 5:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case 6:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case 7:
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case 8:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                break;
        }
        return sdf.format(cal.getTime());
    }

    // 获得上星期的每一天
    public static String getLastWeekDay(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        switch (day) {
            case 1:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case 2:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case 3:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case 4:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case 5:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case 6:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case 7:
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case 8:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                break;
        }
        return sdf.format(cal.getTime());
    }

    // 获取下周一日期
    public static String getWeekB() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(cal.getTime());
    }

    // 获取本月第一天
    public static String getMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        String time = sdf.format(c.getTime());
        return time;
    }

    // 获取下个月第一天
    public static String getNextMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        String time = sdf.format(c.getTime());
        return time;
    }

    // 获取上个月第一天
    public static String getLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(c.getTime());
    }

    // 获得格式化日期
    public static String getFormatDay(Date day) {
        return sdf.format(day);
    }

    // 获得24小时字符串的数据存储容器
    public static TreeMap<String, String> getDayHour() {
        TreeMap<String, String> map = new TreeMap<String, String>();
        String hours = "00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23";
        String[] dayhour = hours.split(",");
        for (int i = 0; i < dayhour.length; i++) {
            map.put(dayhour[i], "0");
        }
        return map;
    }

    // 获得一周的每一天的map数据
    public static TreeMap<String, BigDecimal> getWeekDayMap(int type) {
        TreeMap<String, BigDecimal> map = new TreeMap<String, BigDecimal>();
        for (int i = 1; i <= 7; i++) {
            // 本周
            if (type == 1) {
                map.put(getWeekDay(i), new BigDecimal(0));
            } else if (type == 2) {
                // 上周
                map.put(getLastWeekDay(i), new BigDecimal(0));
            }
        }
        return map;
    }

    public static void main(String[] args) {
        RexUtil.timeFormat("2017年1月1日");
        System.out.println(transformYYYYMMdd("2016-02-05"));
        // System.out.println(getDateYYYYMMddHHmmss(new Date()));
    }

    /********************************************
     * 方法简述加英文符号.<br>
     * 日期字符串格式化<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2016年4月28日::x09::创建此方法<br>
     *********************************************/
    public static Date formatDate(String dateStr) {
        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.M.dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.M.d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        if (dateStr.length() < 8) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        if (dateStr.length() < 7) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy.M");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/M/dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/M/d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        if (dateStr.length() < 8) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        if (dateStr.length() < 7) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy/M");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-ddHH:mm");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-ddHH:mm:ss");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-ddHH:mm");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dHH:mm:ss");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dHH:mm");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-dHH:mm:ss");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-dHH:mm");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        if (dateStr.length() < 8) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        if (dateStr.length() < 7) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-M");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年M月dd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 9))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年M月d");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 6), dateStr.substring(7, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        if (dateStr.length() < 8) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(5, 7), null)) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(4, 6), dateStr.substring(6, 8))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(4, 6), dateStr.substring(6, 7))) {
                return date;
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMd");
            Date date = sf.parse(dateStr);
            if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(4, 5), dateStr.substring(5, 6))) {
                return date;
            }
        } catch (Exception e) {
        }
        if (dateStr.length() < 7) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
                Date date = sf.parse(dateStr);
                if (chackDate(date, dateStr.substring(0, 4), dateStr.substring(4, 6), null)) {
                    return date;
                }
                return date;
            } catch (Exception e) {
            }
        }
        if (dateStr.length() < 5) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy");
                Date date = sf.parse(dateStr);
                return date;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /********************************************
     * 方法简述加英文符号.<br>
     * 用于检查上面方法日期转换是否正确<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2017年3月16日::x09::创建此方法<br>
     *********************************************/
    private static boolean chackDate(Date date, String year_str, String month_str, String day_str) {
        if (date == null || org.apache.commons.lang.StringUtils.isEmpty(year_str)) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String year = String.valueOf(c.get(Calendar.YEAR));
        if (!year_str.equals(year)) {
            return false;
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(month_str)) {
            String month = String.valueOf(c.get(Calendar.MONTH) + 1);
            if (month_str.startsWith("0")) {
                month_str = month_str.substring(1);
            }
            if (!month_str.equals(month)) {
                return false;
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(day_str)) {
                if (day_str.startsWith("0")) {
                    day_str = day_str.substring(1);
                }
                String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                if (!day_str.equals(day)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ******************************************
     * 格式化精确度到分的日期<br>
     * @since v1.0.0
     * @param dateStr
     * @return
     * Date
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2016年10月13日::x04::创建此方法<br>
     ********************************************
     */
    public static Date formatDate2(String dateStr) {
        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd a hh:mm");
            Date date = sf.parse(dateStr);
            return date;
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年M月dd日 HH:mm");
            Date date = sf.parse(dateStr);
            return date;
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-d a HH点mm分");
            Date date = sf.parse(dateStr);
            return date;
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            Date date = sf.parse(dateStr);
            return date;
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
            Date date = sf.parse(dateStr);
            return date;
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            Date date = sf.parse(dateStr);
            return date;
        } catch (Exception e) {
        }
        return null;
    }

    /********************************************
     * 返回传入日期是周几.<br>
     * 规则：如果是今天，返回今天；如果是昨天，返回昨天；如果在本周内并不为昨天或者今天，显示周几；如果在上周，显示更早；去年一律显示为更早；
     * @since v1.0.0
     * @return 返回类型 字符串
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2016年4月28日::cl::创建此方法<br>
     *********************************************/
    public static String getDayOfWeek(Date date) throws ParseException {

        // Date date = new Date();
        // SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        // try{
        // date =sdf.parse(d);
        // }catch (Exception e){
        // return "";
        // }
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Calendar today = Calendar.getInstance();

        // int dayOfWeek = 0;
        // 获取星期对应的int，例如周一对应1，周二对应2
        // if(today.get(Calendar.DAY_OF_WEEK)==1){
        // dayOfWeek = 7;
        // }else{
        // dayOfWeek = today.get(Calendar.DAY_OF_WEEK)-1;
        // }
        return getDayDetailOfWeek(calendar);
    }

    /**
     * 该函数为getDayOfWeek辅助函数
     * */
    public static String getDayDetailOfWeek(Calendar before) {
        String result = "";
        Calendar today = Calendar.getInstance();
        if (before.compareTo(today) > 0) {
            result = "已排期";
        } else if (today.get(Calendar.YEAR) > before.get(Calendar.YEAR) || today.get(Calendar.MONTH) > before.get(Calendar.MONTH)) {
            result = "更早";
        } else if (today.get(Calendar.DAY_OF_YEAR) - before.get(Calendar.DAY_OF_YEAR) == 0) {
            result = "今天";
        } else if (today.get(Calendar.DAY_OF_YEAR) - before.get(Calendar.DAY_OF_YEAR) == 1) {
            result = "昨天";
            // 国外把周日作为一周第一天，这里要单独处理下
        } else if (((today.get(Calendar.WEEK_OF_MONTH) == before.get(Calendar.WEEK_OF_MONTH))) && (before.get(Calendar.DAY_OF_WEEK) == 1)) {
            result = "上周";
        } else if (today.get(Calendar.WEEK_OF_MONTH) > before.get(Calendar.WEEK_OF_MONTH)) {
            if ((today.get(Calendar.WEEK_OF_MONTH) - before.get(Calendar.WEEK_OF_MONTH) == 1)) {
                result = "上周";
            } else {
                result = (today.get(Calendar.WEEK_OF_MONTH) - before.get(Calendar.WEEK_OF_MONTH)) + "周前";
            }
        } else {
            switch (before.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    result = "周日";
                    break;
                case 2:
                    result = "周一";
                    break;
                case 3:
                    result = "周二";
                    break;
                case 4:
                    result = "周三";
                    break;
                case 5:
                    result = "周四";
                    break;
                case 6:
                    result = "周五";
                    break;
                case 7:
                    result = "周六";
                    break;
            }
        }
        return result;
    }

    /**
     * 判断[续费时间]与[结束日期] ， 续费日期与结束日期的日期在一天就不算超期
     * @param jssj
     * @return
     */
    public static boolean judgeDate(Date jssj) {
        Calendar calendar = Calendar.getInstance();
        // 当前年月
        int day_of_year_now = calendar.get(Calendar.DAY_OF_YEAR);
        int year_now = calendar.get(Calendar.YEAR);

        calendar.setTime(jssj);// 结束日期
        int day_of_year_finish = calendar.get(Calendar.DAY_OF_YEAR);
        int year_finish = calendar.get(Calendar.YEAR);
        if (year_finish > year_now) {
            return true;
        }
        return day_of_year_finish > day_of_year_now;

    }

    /**
     * ******************************************
     * convertCnDate<br>
     * 将中文日期格式转为Date,<br>
     * @since v1.0.0
     * @param cprq
     * @return
     * Date
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2016年11月3日::TZB::创建此方法<br>
     ********************************************
     */
    public static Date convertCnDate(String cprq) {
        try {
            int yearPos = cprq.indexOf("年");
            int monthPos = cprq.indexOf("月");
            String cnYear = cprq.substring(0, yearPos);
            String cnMonth = cprq.substring(yearPos + 1, monthPos);
            String cnDay = cprq.substring(monthPos + 1, cprq.length() - 1);
            String year = ConvertCnYear(cnYear);
            String month = ConvertCnDateNumber(cnMonth);
            String day = ConvertCnDateNumber(cnDay);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.parseInt(year));
            c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            return c.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    private static String ConvertCnYear(String cnYear) {
        if (cnYear.length() == 2) {
            return "20" + ConvertCnNumberChar(cnYear);
        } else {
            return ConvertCnNumberChar(cnYear);
        }
    }

    private static String ConvertCnDateNumber(String cnNumber) {
        if (cnNumber.length() == 1) {
            if (cnNumber.equals("十")) {
                return "10";
            } else {
                return ConvertCnNumberChar(cnNumber);
            }
        } else if (cnNumber.length() == 2) {
            if (cnNumber.startsWith("十")) {
                return "1" + ConvertCnNumberChar(cnNumber.substring(1, 2));
            } else if (cnNumber.endsWith("十")) {
                return ConvertCnNumberChar(cnNumber.substring(0, 1)) + "0";
            } else {
                return ConvertCnNumberChar(cnNumber);
            }
        } else if (cnNumber.length() == 3) {
            return ConvertCnNumberChar(cnNumber.substring(0, 1) + cnNumber.substring(2, 3));
        }
        return null;
    }

    private static String ConvertCnNumberChar(String cnNumberStr) {
        String ALL_CN_NUMBER = "〇○零一二三四五六七八九";
        String ALL_NUMBER = "000123456789";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < cnNumberStr.length(); i++) {
            char c = cnNumberStr.charAt(i);
            int index = ALL_CN_NUMBER.indexOf(c);
            if (index != -1) {
                buf.append(ALL_NUMBER.charAt(index));
            } else {
                buf.append(cnNumberStr.charAt(i));
            }
        }
        return buf.toString();
    }

    static SimpleDateFormat YYMMdd = new SimpleDateFormat("YYYY-MM-dd");

    public static String getDate_YYYYMMdd(Date date) {
        return YYMMdd.format(date);
    }

    /**
     * 获取两个日期相差天数
     * */
    public static Integer getBetweenDays(Date endDate, Date today) {
        if (null == endDate || null == today) {
            return null;
        }
        long intervalMilli = endDate.getTime() - today.getTime();
        long result = intervalMilli / (24 * 60 * 60 * 1000);
        if (intervalMilli < 0 && result == 0) {
            result = -1;
        }
        return (int) result;
    }

    public static Date transformYYYYMMdd(String time_f) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(time_f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 获取过去的分钟
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime()-date.getTime();
        return t/(60*1000);
    }
}
