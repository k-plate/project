package com.doug.kplate.utils;


import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
    //public static List sexList; //性别
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_COMPACT = "yyyyMMdd";
    public static final String FORMAT_FULL_COMPACT = "yyyyMMddHHmmss";
    public static final String FORMAT_MONTH = "yyyy-MM";

    public static String getDefaultDateFormatString(Date date) {
        return format.format(date);
    }

    public static String getDefaultDateOrNullFormatString(Date date) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    private DateUtil() {
    }

    ;

    /**
     * 把long 转换成 日期 再转换成String类型
     */
    public static String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String formatStringToOther(String dateString, String format) throws ParseException {
        Date date = new SimpleDateFormat(DEFAULT_PATTERN).parse(dateString);
        String now = new SimpleDateFormat(format).format(date);
        return now;
    }

    /**
     * 获取时间对象
     *
     * @return Date
     */
    public static Date getDate() {
        Date date = new Date();
        return date;
    }

    /**
     * 获取时间对象
     *
     * @return Calendar
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 获取当前年份
     *
     * @param calendar Calendar
     * @return int
     */
    public static int getYear(Calendar calendar) {

        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @param calendar Calendar
     * @return int
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static String getMonth(Integer offset) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, offset);
        Date date = calendar.getTime();
        return formatDateToString(date, "yyyy-MM");
    }

    public static String getYear(Integer offset) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.YEAR, offset);
        Date date = calendar.getTime();
        return formatDateToString(date, "yyyy");
    }

    /**
     * 获取日期的天数
     *
     * @param calendar Calendar
     * @return int
     */
    public static int getDay(Calendar calendar) {
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取小时
     *
     * @param calendar Calendar
     * @return int
     */
    public static int getHour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 把日期对象格式化输出字符串
     *
     * @param date   Date
     * @param format String
     * @return String
     */
    public static String formatDateToString(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DEFAULT_PATTERN;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String timeInfo = "";
        if (date != null)
            timeInfo = dateFormat.format(date);
        return timeInfo;
    }

    public static String defaultFormatDateToString(Date date) {
        return formatDateToString(date, DEFAULT_PATTERN);
    }

    public static String getSubPartOfDate(String date, String dateFormat, String subFormat) {
        SimpleDateFormat subFormater = new SimpleDateFormat(subFormat);
        Date dateValue = formatStringToDate(date, dateFormat);
        String timeInfo = "";
        timeInfo = subFormater.format(dateValue);
        return timeInfo;
    }

    /**
     * 把字符串转化成特定格式的日期对象
     *
     * @param stringInfo String
     * @param format     String
     * @return Date
     * @throws Exception
     */
    public static Date formatStringToDate(String stringInfo, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        if (stringInfo != null) {
            if (!stringInfo.equals(""))
                try {
                    date = dateFormat.parse(stringInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return date;
    }


    public static Date defaultFormatStringToDate(String info) {
        return formatStringToDate(info, DEFAULT_PATTERN);
    }

    /**
     * 日期计算类，两日期间相差多少小时
     *
     * @param date_1 Date
     * @param date_2 Date
     * @return long
     */
    public static long TwoDate(Date date_1, Date date_2) {
        return date_2.getTime() - date_1.getTime();
    }

    /**
     * 日期计算类，两日期间相差多少小时
     *
     * @param startTime String
     * @param endTime   String
     * @return long
     */
    public static long TwoDate(String startTime, String endTime) {
        GregorianCalendar gc1 = new GregorianCalendar(1995, 11, 1, 3, 2, 1);
        GregorianCalendar gc2 = new GregorianCalendar(1995, 11, 1, 3, 2, 2);
        Date d1 = gc1.getTime();
        Date d2 = gc2.getTime();
        long l1 = d1.getTime();
        long l2 = d2.getTime();
        long difference = l2 - l1;
        return difference;
    }

    public static String getPriorDay(String format, int offset) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar theday = Calendar.getInstance();
        theday.add(Calendar.DATE, offset);
        return df.format(theday.getTime());
    }

    public static String getPriorDay(String time, String format, int offset) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar theday = Calendar.getInstance();
        try {
            theday.setTime(df.parse(time));
        } catch (Exception e) {
        }
        theday.add(Calendar.DATE, offset);
        return df.format(theday.getTime());
    }

    public static int getMonthDays(String year, String month) {
        int i = 0;
        try {
            Calendar cal = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
            i = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
        }
        return i;
    }

    public static String[] getTwoTimeMobth(int startY, int startM) {
        int endY = getYear(getCalendar());
        int endM = getMonth(getCalendar());

        int size = 1 + (endY - startY) * 12 + endM - startM;
        if (size < 0)
            return null;
        else {
            String s[] = new String[size];
            for (int i = 0; i < size; i++) {
                s[i] = startY + "年";
                if (startM < 10)
                    s[i] = s[i] + "0" + startM + "月";
                else
                    s[i] = s[i] + "" + startM + "月";
                startM++;
                if (startM > 12) {
                    startM = 1;
                    startY++;
                }
            }
            return s;
        }
    }

    public static boolean isTimeOut(String rtime, String ltime) {
        return false;
    }

    public static Date getDateByDate(Date date, int dist) {
        Date rs = new Date();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, dist);
        rs = calendar.getTime();

        return rs;
    }

    public static String getWeekNum(String date) {
        String rs = "0";
        Date chkDate = formatStringToDate(date, "yyyy-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(chkDate);
        int dd = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dd == 0) {
            dd = 7;
        }
        rs = dd + "";

        return rs;
    }

    public static int getWeekNum(Date chkDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(chkDate);
        int dd = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dd == 0) {
            dd = 7;
        }
        return dd;
    }

    public static Date getMondayBeginTime(Date chkDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(chkDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int dd = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dd == 0) {
            dd = 7;
        }
        //dd is [1-7]
        calendar.add(Calendar.DATE, -(dd - 1));
        return calendar.getTime();
    }

    public static String getWeekNum(String date, String format) {
        String rs = "1";
        Date chkDate = formatStringToDate(date, format);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(chkDate);
        int dd = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dd == 0) {
            dd = 7;
        }
        rs = dd + "";

        return rs;
    }

    public static String getTimeOfDay() {
        String time = "";
        Date now = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        String hh = "" + calendar.get(Calendar.HOUR_OF_DAY);
        if (hh.length() == 1) {
            hh = "0" + hh;
        }
        String mm = "" + calendar.get(Calendar.MINUTE);
        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        time = hh + ":" + mm;
        return time;
    }

    public static Date getDateByOffset(int offset) {
        Date now = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, offset);
        return calendar.getTime();
    }

    public static Date truncateDay(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateByHourOffset(int hourOffset) {
        Date now = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, hourOffset);
        return calendar.getTime();
    }

    /**
     * @param
     * @param birthday 出生日期（yyyy-MM-dd）
     * @param
     * @param toDate   截止日期(yyyy-MM-dd)
     * @param
     * @param months   月份限制
     * @param
     * @return int 返回类型 1：年龄在指定限制月份内；0：年龄不在指定限制月份内；
     * @throws
     * @Title: isAgeLessThan
     * @Description: 判断年龄是否在指定限制月份内
     */
    public static int isAgeLessThan(String birthday, Date toDate, int months) {
        int res = 0;
        GregorianCalendar calendar = new GregorianCalendar();
        Date from = formatStringToDate(birthday, "yyyy-MM-dd");
        calendar.setTime(from);
        calendar.add(Calendar.MONTH, months);
        Date shouldBe = calendar.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String toChk = sf.format(toDate);
        calendar.setTime(formatStringToDate(toChk, "yyyy-MM-dd"));
        Date chkDate = calendar.getTime();
        res = (chkDate.compareTo(shouldBe) > 0) ? 0 : 1;
        return res;
    }

    /**
     * @param
     * @param
     * @param yearOffset 设定文件
     * @return void 返回类型
     * @throws
     * @Title: getYearByDate
     * @Description: 获取date日期所在年份+yearOffset后的年份
     */
    public static String getYearByDate(String stime, String format, int yearOffset) {
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = formatStringToDate(stime, format);
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.YEAR, yearOffset);
        Date newDate = calendar.getTime();
        return formatDateToString(newDate, "yyyy");
    }

    public static boolean isTodayInTimeSpan(String sdate, String edate) {
        boolean res = false;
        String today = DateUtil.getPriorDay("yyyy-MM-dd", 0);
        res = ((today.compareTo(edate) <= 0) && (today.compareTo(sdate) >= 0));
        return res;
    }

    public static Integer getSeconds(Date nowTime) {
        String time = DateUtil.formatDateToString(nowTime, "hh-MM-ss");
        String[] timeStr = time.split("-");
        Integer nowSeconds = Integer.parseInt(timeStr[0]) * 3600
                + Integer.parseInt(timeStr[1]) * 60
                + Integer.parseInt(timeStr[2]);
        return nowSeconds;
    }

    public static Date getLastDateOfThisMonth(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date getFirstDateOfThisMonth(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date getFirstDateOfNextMonth(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getFirstDateOfLastMonth(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getFirstDateOfThisWeek(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        return calendar.getTime();
    }

    public static Date getFirstDateOfNextYear(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Date getFirstDateOfThisYear(Date today) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Date getFirstNDateOfThisYear(Date today, Integer dayIndex) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_YEAR, dayIndex);
        return calendar.getTime();
    }

    public static Date getFirstNDateOfLastYear(Date today, Integer dayIndex) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, dayIndex);
        return calendar.getTime();
    }

    /**
     * 获得持续时间（暂时不用）
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getContinueTime(Date startTime, Date endTime){
        long seconds = TwoDate(startTime,endTime)/1000;//获得秒数
        //1d = 86400s  1h = 3600s 1min = 60s
        String continueTime = "";
        long day = seconds/86400;
        long dayleft = seconds%86400;//不足一天的秒数

        long hour = dayleft/3600;
        long hourleft = dayleft%3600;//不足一小时的秒数

        long min = hourleft/60;
        long minleft = hourleft%60;//不足一分钟的秒数


        if (day > 0){
            continueTime = continueTime.concat(day + "天");
        }
        if (hour > 0){
            continueTime = continueTime.concat(hour+"小时");
        }
        if (min > 0){
            continueTime = continueTime.concat(min+"分");
        }
        if (minleft > 0){
            continueTime = continueTime.concat(minleft+"秒");
        }
        return continueTime;
    }

    public static void main(String args[]) {
        // getTwoTimeMobth(2005,10);
        /*
         * Date hDate = DateUtil.formatStringToDate("2009-12-20 12:00",
		 * "yyyy-MM-dd HH:mm"); Date beforeDate = DateUtil.getDateByDate(hDate,
		 * -1); Date compareDate = new Date(); boolean ss =
		 * compareDate.before(beforeDate);
		 */
        // System.out.println(getPriorDay("2007-01-01", "yyyy-MM-dd", -2));
        // System.out.println(ss);
        // String from = "2009-11-28";
        // Date to = new Date();
        // int month = 6;
        // System.out.println(isAgeLessThan(from, to, month));
//		System.out.println(DateUtil.getWeekNum("2011-05-02 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-03 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-04 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-05 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-06 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-07 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-08 01:11:11", "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.getWeekNum("2011-05-09 01:11:11", "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.getFirstNDateOfLastYear(new Date(), 1));
        System.out.println(DateUtil.getDateByDate(DateUtil.getFirstDateOfThisWeek(new Date()), -7));

//		System.out.println(getDateByOffset(1));
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        cal.add(Calendar.HOUR,-1);
        cal.add(Calendar.MINUTE,-1);
        cal.add(Calendar.SECOND,-4);

        Date time=cal.getTime();
        Date d1 =time;

        Date d2 = new Date();
     	System.out.println(getContinueTime(d1,d2));


    }

}
