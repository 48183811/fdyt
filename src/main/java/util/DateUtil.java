package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String getDayOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        return sdf.format(date);
    }

    public static String getDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getDay2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String getChineseDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }

    public static String getChineseMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        return sdf.format(date);
    }
    public static String getChineseYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年");
        return sdf.format(date);
    }

    public static String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDayTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDayTime2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String geTimeToMin(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static Date parse(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseToMin(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseToDay(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseToMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 计算传入时间和今天时间相差几天的方法
     *
     * @param date 传入的时间
     * @return
     * @description getIntervalDay
     * @author ligy
     * @date 2016年3月29日 上午12:45:46
     */
    public static int getIntervalDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long nowTime = df.parse(df.format(new Date())).getTime();
            long dateTime = df.parse(df.format(date)).getTime();
            return (int) ((dateTime - nowTime) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取date月份最大天数
     *
     * @param date
     * @return
     */
    public static int getDaysWithMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }


    /**
     * 返回当前月第一天，00:00:00
     *
     * @return
     */
    public static Calendar getNowMonthBegin() {
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        return c;
    }

    /**
     * 返回月第一天，00:00:00
     * */
    public static Calendar getMonthBegin(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        return c;
    }

    /**
     * 返回下月第一天，00:00:00
     * */
    public static Calendar getMonthEnd(Date date) {
        Calendar c = getMonthBegin(date);
        c.add(Calendar.MONTH, 1);

        return c;
    }

    public static Date getBeginTimeInDay(Date date) {
        Calendar inCalendar = Calendar.getInstance();
        inCalendar.setTime(date);

        int day = inCalendar.get(Calendar.DAY_OF_MONTH);
        int month = inCalendar.get(Calendar.MONTH);
        int year = inCalendar.get(Calendar.YEAR);

        Calendar outCalendar = Calendar.getInstance();
        outCalendar.set(year, month, day, 0, 0, 0);
        return outCalendar.getTime();
    }

    public static Date getEndTimeInDay(Date date) {
        return getLastDay(getBeginTimeInDay(date));
    }

    public static Date getLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.add(Calendar.DAY_OF_MONTH, 1);

        return c.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 处理特殊时间转换，格式：2019-03-22T09:11:52.000+0000
     * @param dateStr 时间字符串
     * @return
     */
    public static Date parseT(String dateStr){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date  date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}
