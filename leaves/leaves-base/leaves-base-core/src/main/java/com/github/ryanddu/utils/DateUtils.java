package com.github.ryanddu.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 日期时间操作类
 */
public class DateUtils {

    static Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static final int MILLI = 0; // 毫秒级别
    public static final int SECOND = 1; // 秒级别
    public static final int MINUTE = 2; // 分钟级别
    public static final int HOUR = 3; // 小时级别
    public static final int DAY = 4; // 天数级别
    public static final int WEEK = 5; // 星期级别
    public static final int MONTH = 6; // 月份级别
    public static final int YEAR = 7; // 年份级别

    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";

    public static final String pattern19ToDate = "yyyy-MM-dd HH24:mi:ss";

    private static final long MILLISECOND_IN_MINUTE = 1000 * 60; // 一分钟有多少毫秒
    private static final long MILLISECOND_IN_HOUR = 1000 * 60 * 60; // 一小时有多少毫秒
    private static final long MILLISECOND_IN_DAY = 1000 * 60 * 60 * 24; // 一天有多少毫秒
    private static final long MILLISECOND_IN_WEEK = 1000 * 60 * 60 * 24 * 7; // 一周有多少毫秒

    public static final long MINUTE_IN_HOUR = 60;//一小时有多少分钟

    /**
     * 时间偏移
     *
     * @param localDateTime
     * @param addTime
     * @return
     */
    public static LocalDateTime addTimeToLocalDateTime(LocalDateTime localDateTime, Long addTime) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        Date newDate = new Date(date.getTime() + addTime);
        return defaultDateToLocalDateTime(newDate);
    }

    /**
     * 获取指定月第一天的边界时间 2020-02-01 00：00：00
     *
     * @param date
     * @return
     */
    public static Date getFirstDateInMonth(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        return getFirstDateInMonth(year, month);
    }

    public static Date getFirstDateInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        Date time = calendar.getTime();
        return getStBoundaryDate(time, 1);
    }

    public static LocalDateTime getFirstDateInMonth(LocalDateTime localDateTime) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        int year = getYear(date);
        int month = getMonth(date);
        return getFirstLocalDatetimeInMonth(year, month);
    }

    public static LocalDateTime getFirstLocalDatetimeInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        Date time = calendar.getTime();
        Date date = getStBoundaryDate(time, 1);
        return defaultDateToLocalDateTime(date);
    }

    /**
     * 获取指定月最后一天的边界时间 2月：2020-03-01 00：00：00
     *
     * @param date
     * @return
     */
    public static Date getLastDateInMonth(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        return getLastDateInMonth(year, month);
    }

    public static LocalDateTime getLastDateInMonth(LocalDateTime localDateTime) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        int year = getYear(date);
        int month = getMonth(date);
        Date lastDateInMonth = getLastDateInMonth(year, month);
        return defaultDateToLocalDateTime(lastDateInMonth);
    }

    public static Date getLastDateInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        Date time = calendar.getTime();
        return getEndBoundaryDate(time, 2);
    }

    public static LocalDateTime getLastLocalDateTimeInMonth(int year, int month) {
        Date date = getLastDateInMonth(year, month);
        return defaultDateToLocalDateTime(date);
    }

    /**
     * localDateTime 转 date
     *
     * @param localDateTime
     * @return
     */
    public static Date defaultLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * localDate 转 date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取某天中剩余的时间（秒数）
     * currentDate:new Date
     */
    public static Integer getDayRemainingTime(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * date 转 localDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime defaultDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 获取上一月的第一天零点
     *
     * @param month yyyy-MM
     * @return
     */
    public static Date getBeforeMonthFirstDate(String month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            Date parse = format.parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parse);
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date time = cal.getTime();
            return getStBoundaryDate(time, 1);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期解析异常");
        }
    }

    public static LocalDateTime getBeforeMonthFirstLocalDateTime(String month) {
        Date firstDate = getBeforeMonthFirstDate(month);
        return defaultDateToLocalDateTime(firstDate);
    }

    public static Date getAfterMonthLastDate(String month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            Date parse = format.parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parse);
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date time = cal.getTime();
            return getEndBoundaryDate(time, 2);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期解析异常");
        }
    }

    public static LocalDateTime getAfterMonthLastLocalDateTime(String month) {
        Date lastDate = getAfterMonthLastDate(month);
        return defaultDateToLocalDateTime(lastDate);
    }

    /**
     * 获取星期几数字 1 2 3 4 5 6 7
     *
     * @param date
     * @return
     */
    public static Integer getWeekNum(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNum = 0;
        // 一周的第一天是否是星期天
        boolean weekDay = calendar.getFirstDayOfWeek() == Calendar.SUNDAY;
        if (weekDay) {
            weekNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekNum == 0) {
                weekNum = 7;
            }
        }
        return weekNum;
    }

    public static Integer getWeekNum(LocalDateTime localDateTime) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        return getWeekNum(date);
    }


    /**
     * 获取开始边界
     *
     * @param date
     * @param amOrPm 1 上午 2 下午
     * @return
     */
    public static Date getStBoundaryDate(Date date, Integer amOrPm) {
        if (amOrPm == 1) {
            return getFormatTimeDate(date, "00:00:00");
        } else if (amOrPm == 2) {
            return getFormatTimeDate(date, "12:00:00");
        } else {
            throw new RuntimeException("不是上午或下午");
        }
    }

    public static LocalDateTime getStBoundaryDate(LocalDateTime localDateTime, Integer amOrPm) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        Date stBoundaryDate = getStBoundaryDate(date, amOrPm);
        return defaultDateToLocalDateTime(stBoundaryDate);
    }

    /**
     * 获取开始边界
     *
     * @param date
     * @param amOrPm 1 上午 2 下午
     * @return
     */
    public static Date getEndBoundaryDate(Date date, Integer amOrPm) {
        if (amOrPm == 1) {
            return getFormatTimeDate(date, "12:00:00");
        } else if (amOrPm == 2) {
            Date formatTimeDate = getFormatTimeDate(date, "00:00:00");
            long time = formatTimeDate.getTime() + MILLISECOND_IN_DAY;
            return new Date(time);
        } else {
            throw new RuntimeException("不是上午或下午");
        }
    }

    public static LocalDateTime getEndBoundaryDate(LocalDateTime localDateTime, Integer amOrPm) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        Date endBoundaryDate = getEndBoundaryDate(date, amOrPm);
        return defaultDateToLocalDateTime(endBoundaryDate);
    }

    /**
     * 获取天
     *
     * @param date
     * @return
     */
    public static String getFormatDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 重置时间的时分秒
     *
     * @param date
     * @param timeStr 如 00:00:00
     * @return
     */
    public static Date getFormatTimeDate(Date date, String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day = format.format(date);
        Date formatDate = null;
        try {
            String stDateStr = day + " " + timeStr;
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatDate = format2.parse(stDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return formatDate;
    }

    public static LocalDateTime getFormatTimeDate(LocalDateTime localDateTime, String timeStr) {
        Date date = defaultLocalDateTimeToDate(localDateTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day = format.format(date);
        Date formatDate = null;
        try {
            String stDateStr = day + " " + timeStr;
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatDate = format2.parse(stDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return defaultDateToLocalDateTime(formatDate);
    }

    /**
     * 获取当月所有日期
     *
     * @param month
     * @return
     */
    public static List<Date> getDays(String month) {
        if (StringUtils.isNoneBlank(month)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            List<Date> daysInMonth = new ArrayList<>();
            try {
                Date parse = format.parse(month);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parse);
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH) + 1;
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.YEAR, y);
                calendar2.set(Calendar.MONTH, m - 1);
                calendar2.set(Calendar.DATE, 1);
                calendar2.roll(Calendar.DATE, -1);
                int num = calendar2.get(Calendar.DATE);
                for (int i = 1; i <= num; i++) {
                    String day = null;
                    if (i < 10) {
                        day = month + "-0" + i;
                    } else {
                        day = month + "-" + i;
                    }
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format2.parse(day);
                    daysInMonth.add(date);
                }
                return daysInMonth;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    public static List<LocalDateTime> getDaysRtLocalDateTime(String month) {
        if (StringUtils.isNoneBlank(month)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            List<LocalDateTime> daysInMonth = new ArrayList<>();
            try {
                Date parse = format.parse(month);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parse);
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH) + 1;
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.YEAR, y);
                calendar2.set(Calendar.MONTH, m - 1);
                calendar2.set(Calendar.DATE, 1);
                calendar2.roll(Calendar.DATE, -1);
                int num = calendar2.get(Calendar.DATE);
                for (int i = 1; i <= num; i++) {
                    String day = null;
                    if (i < 10) {
                        day = month + "-0" + i;
                    } else {
                        day = month + "-" + i;
                    }
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format2.parse(day);
                    LocalDateTime localDateTime = defaultDateToLocalDateTime(date);
                    daysInMonth.add(localDateTime);
                }
                return daysInMonth;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 时间转换
     *
     * @param dataStr 11-九月-2016
     * @return
     * @throws ParseException
     */
    public static String dateChage(String dataStr) throws ParseException {
        dataStr = dataStr.replace("一", "1").replace("二", "2").replace("三", "3").replace("四", "4").replace("五", "5")
                .replace("六", "6").replace("七", "7").replace("八", "8").replace("九", "9").replace("十", "10")
                .replace("十一", "11").replace("十二", "12");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M月-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dataStr);
        return sdf2.format(date);
    }

    /**
     * 获取当前时间的星期数
     *
     * @param date
     * @return
     */
    public static String getWeekOfDay(Date date) {
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String ss = dateFm.format(date);
        return ss;
    }

    /**
     * 得到下一天
     *
     * @param tt
     * @return
     */
    public static Timestamp getNextDay(Timestamp tt) {
        if (tt == null)
            tt = new Timestamp(new Date().getTime());// 如果tt为空则变为当前时间
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(tt.getTime()));
        cd.add(Calendar.DAY_OF_MONTH, 1);
        return dateToTimestamp(cd.getTime());

    }

    /**
     * 得到下一月
     *
     * @param tt
     * @return
     */
    public static Timestamp getNextMonth(Timestamp tt) {
        if (tt == null)
            tt = new Timestamp(new Date().getTime());// 如果tt为空则变为当前时间
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(tt.getTime()));
        cd.add(Calendar.MONTH, 1);
        return dateToTimestamp(cd.getTime());

    }

    /**
     * 得到下一年
     *
     * @param tt
     * @return
     */
    public static Timestamp getNextYear(Timestamp tt) {
        if (tt == null)
            tt = new Timestamp(new Date().getTime());// 如果tt为空则变为当前时间
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(tt.getTime()));
        cd.add(Calendar.YEAR, 1);
        return dateToTimestamp(cd.getTime());

    }

    /**
     * 得到下一小时
     *
     * @param tt
     * @return
     */
    public static Timestamp getNextHour(Timestamp tt) {
        if (tt == null)
            tt = new Timestamp(new Date().getTime());// 如果tt为空则变为当前时间
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(tt.getTime()));
        cd.add(Calendar.HOUR, 1);
        return dateToTimestamp(cd.getTime());

    }

    /**
     * 取上月的第一天
     *
     * @return
     */
    public static Date getFirstDayOfPrevMonth() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHHMMSS);
        Calendar eCal = Calendar.getInstance();
        eCal.add(Calendar.MONTH, -1);
        eCal.set(Calendar.DAY_OF_MONTH, 1);
        eCal.set(Calendar.HOUR_OF_DAY, 00);
        eCal.set(Calendar.MINUTE, 00);
        eCal.set(Calendar.SECOND, 00);
        try {
            date = format.parse(format.format(eCal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 取上月的最后一天
     *
     * @return
     */
    public static Date getlastDayOfPrevMonth() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHHMMSS);
        Calendar eCal = Calendar.getInstance();
        eCal.set(Calendar.DAY_OF_MONTH, 1);
        eCal.add(Calendar.DATE, -1);
        eCal.set(Calendar.HOUR_OF_DAY, 23);
        eCal.set(Calendar.MINUTE, 59);
        eCal.set(Calendar.SECOND, 59);
        try {
            date = format.parse(format.format(eCal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 取当月的第一天
     *
     * @return
     */
    public static Date getfirstDayOfMonth() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDD);
        // String sdate = format.format(date);
        Calendar sCal = Calendar.getInstance();
        sCal.setTime(date);

        sCal.add(Calendar.DAY_OF_MONTH, -(sCal.get(Calendar.DAY_OF_MONTH) - 1));

        try {
            date = format.parse(format.format(sCal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 得到当前日期的天数
     *
     * @return int    返回类型
     * @throws
     * @Title: getDayOfMonth
     */
    public static int getDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance();
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    /**
     * 取当月的最后一天
     *
     * @param param
     * @return
     */
    public static String getlastDayOfMonth(String param) {
        Date date = str2Date(param, YYYYMMDD);
        Calendar eCal = Calendar.getInstance();
        eCal.setTime(date);
        eCal.set(Calendar.DAY_OF_MONTH, eCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        eCal.set(Calendar.HOUR, 23);
        eCal.set(Calendar.MINUTE, 59);
        eCal.set(Calendar.SECOND, 59);
        return dateToString(eCal.getTime(), YYYYMMDDHHMMSS);
    }

    /**
     * 取当月的最后一周的最后一天 注意这里使用星期日,为星期的第一天
     *
     * @return
     */
    public static String getlastDayOfMonthWeek() {
        // 获取月的最后一天
        Calendar eCal = Calendar.getInstance();
        eCal.set(Calendar.DAY_OF_MONTH, eCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        eCal.set(Calendar.HOUR, 23);
        eCal.set(Calendar.MINUTE, 59);
        eCal.set(Calendar.SECOND, 59);
        eCal.setTime(eCal.getTime());

        // 获取周的最后一天
        eCal.setFirstDayOfWeek(Calendar.SUNDAY);
        eCal.set(Calendar.DAY_OF_WEEK, eCal.getFirstDayOfWeek() + 6);// 设置星期的最后一天

        return dateToString(eCal.getTime(), YYYYMMDDHHMMSS);
    }

    /**
     * 取当月的第一周的第一天 注意这里使用星期日,为星期的第一天
     *
     * @return
     */
    public static String getFirstDayOfMonthWeek() {
        // 获取月的最后一天
        Calendar eCal = Calendar.getInstance();
        eCal.set(Calendar.DAY_OF_MONTH, 1);
        eCal.set(Calendar.HOUR, 0);
        eCal.set(Calendar.MINUTE, 0);
        eCal.set(Calendar.SECOND, 0);
        eCal.setTime(eCal.getTime());

        // 获取周的最后一天
        eCal.setFirstDayOfWeek(Calendar.SUNDAY);
        eCal.set(Calendar.DAY_OF_WEEK, eCal.getFirstDayOfWeek());// 设置星期的第一天

        return dateToString(eCal.getTime(), YYYYMMDDHHMMSS);
    }

    /**
     * 取当月的最后一天
     *
     * @return
     */
    public static Date getlastDayOfMonth() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHHMMSS);
        // String sdate = format.format(date);

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(date);

        eCal.set(Calendar.DAY_OF_MONTH, 1);
        eCal.add(Calendar.MONTH, 1);
        eCal.add(Calendar.DATE, -1);

        try {
            date = format.parse(format.format(eCal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算两时间相差的月数
     *
     * @param date
     * @return
     */
    public static int getMonthSpace(Date date) {

        SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHHMMSS);
        String sysTime = df.format(new Date());
        String creTime = df.format(date);
        int inum = 0;
        int sysYear = Integer.parseInt(sysTime.substring(0, 4));
        int sysMonth = Integer.parseInt(sysTime.substring(5, 7));
        int creYear = Integer.parseInt(creTime.substring(0, 4));
        int creMonth = Integer.parseInt(creTime.substring(5, 7));

        if (sysYear == creYear) {
            inum = sysMonth - creMonth;
        } else {
            inum = (sysYear * 12 + sysMonth) - (creYear * 12 + creMonth);
        }
        return Math.abs(inum);
    }

    /**
     * 计算两个时间点相差的秒数，分钟数，小时数，天数，星期数，月数，年数等。 例如：2008-4-11 10:26:00 和 2008-4-12
     * 0:0:0 相差1天，相差13小时，相差14*60-26分钟。
     *
     * @param type :分钟类型、小时类型等
     * @param cal1
     * @param cal2
     * @return 为正数，cal2大于cal1
     */
    public static int calculate2Date(int type, Calendar cal1, Calendar cal2) {
        switch (type) {
            case SECOND:
                return (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000);
            case MINUTE:
                return calculateMinuteDiff(cal1, cal2);
            case HOUR:
                return calculateHourDiff(cal1, cal2);
            case DAY:
                return calculateDateDiff(cal1, cal2);
            case WEEK:
                return calculateWeekDiff(cal1, cal2);
            case MONTH:
                return (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
                        + (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
            case YEAR:
                return cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
            default:
                throw new IllegalArgumentException("无效的计算时间类型。");
        }
    }

    /**
     * 格式转换。long转换成String 类型 james 2015/4/13
     *
     * @param lg
     * @return
     */
    public static String getStringByLong(long lg) {
        SimpleDateFormat sf = new SimpleDateFormat(DateUtils.YYYYMMDDHHMMSS);
        return sf.format(new Date(lg));

    }

    /**
     * 计算时间相差时间数
     *
     * @param type
     * @param beginTime
     * @param endTime
     * @return
     */
    public static long calculateTimeDiff(int type, Date beginTime, Date endTime) {
        Calendar calen1 = Calendar.getInstance();
        calen1.setTime(beginTime);
        Calendar calen2 = Calendar.getInstance();
        calen2.setTime(endTime);
        long dateDiff = calculate2Date(type, calen1, calen2);
        return dateDiff;
    }

    /**
     * 计算相差分钟数 cal2-cal1>0
     *
     * @param cal1
     * @param cal2
     * @return
     */
    private static int calculateMinuteDiff(Calendar cal1, Calendar cal2) {
        int mil2 = cal2.get(Calendar.MILLISECOND);
        int mil1 = cal1.get(Calendar.MILLISECOND);
        int sec2 = cal2.get(Calendar.SECOND);
        int sec1 = cal1.get(Calendar.SECOND);

        cal2.set(Calendar.MILLISECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.SECOND, 0);
        cal1.set(Calendar.SECOND, 0);

        int result = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / MILLISECOND_IN_MINUTE);

        cal2.set(Calendar.SECOND, sec2);
        cal1.set(Calendar.SECOND, sec1);
        cal2.set(Calendar.MILLISECOND, mil2);
        cal1.set(Calendar.MILLISECOND, mil1);

        return result;
    }

    /**
     * 计算相差小时数 cal2-cal1>0
     *
     * @param cal1
     * @param cal2
     * @return
     */
    private static int calculateHourDiff(Calendar cal1, Calendar cal2) {
        int mil2 = cal2.get(Calendar.MILLISECOND);
        int mil1 = cal1.get(Calendar.MILLISECOND);
        int sec2 = cal2.get(Calendar.SECOND);
        int sec1 = cal1.get(Calendar.SECOND);
        int min2 = cal2.get(Calendar.MINUTE);
        int min1 = cal1.get(Calendar.MINUTE);

        cal2.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal1.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        int result = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / MILLISECOND_IN_HOUR);

        cal2.set(Calendar.MINUTE, min2);
        cal1.set(Calendar.MINUTE, min1);
        cal2.set(Calendar.SECOND, sec2);
        cal1.set(Calendar.SECOND, sec1);
        cal2.set(Calendar.MILLISECOND, mil2);
        cal1.set(Calendar.MILLISECOND, mil1);
        return result;
    }

    /**
     * 计算相差天数 cal2-cal1>0
     *
     * @param cal1
     * @param cal2
     * @return
     */
    private static int calculateDateDiff(Calendar cal1, Calendar cal2) {
        int mil2 = cal2.get(Calendar.MILLISECOND);
        int mil1 = cal1.get(Calendar.MILLISECOND);
        int sec2 = cal2.get(Calendar.SECOND);
        int sec1 = cal1.get(Calendar.SECOND);
        int min2 = cal2.get(Calendar.MINUTE);
        int min1 = cal1.get(Calendar.MINUTE);
        int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
        int hour1 = cal1.get(Calendar.HOUR_OF_DAY);

        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal1.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        int result = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / MILLISECOND_IN_DAY);

        cal2.set(Calendar.HOUR_OF_DAY, hour2);
        cal1.set(Calendar.HOUR_OF_DAY, hour1);
        cal2.set(Calendar.MINUTE, min2);
        cal1.set(Calendar.MINUTE, min1);
        cal2.set(Calendar.SECOND, sec2);
        cal1.set(Calendar.SECOND, sec1);
        cal2.set(Calendar.MILLISECOND, mil2);
        cal1.set(Calendar.MILLISECOND, mil1);
        return result;
    }

    /**
     * 计算相差星期数 cal2-cal1>0
     *
     * @param cal1
     * @param cal2
     * @return
     */
    private static int calculateWeekDiff(Calendar cal1, Calendar cal2) {
        int mil2 = cal2.get(Calendar.MILLISECOND);
        int mil1 = cal1.get(Calendar.MILLISECOND);
        int sec2 = cal2.get(Calendar.SECOND);
        int sec1 = cal1.get(Calendar.SECOND);
        int min2 = cal2.get(Calendar.MINUTE);
        int min1 = cal1.get(Calendar.MINUTE);
        int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
        int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
        int day2 = cal2.get(Calendar.DAY_OF_WEEK);
        int day1 = cal1.get(Calendar.DAY_OF_WEEK);

        cal2.set(Calendar.DAY_OF_WEEK, 0);
        cal1.set(Calendar.DAY_OF_WEEK, 0);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal1.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        int result = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / MILLISECOND_IN_WEEK);

        cal2.set(Calendar.DAY_OF_WEEK, day2);
        cal1.set(Calendar.DAY_OF_WEEK, day1);
        cal2.set(Calendar.HOUR_OF_DAY, hour2);
        cal1.set(Calendar.HOUR_OF_DAY, hour1);
        cal2.set(Calendar.MINUTE, min2);
        cal1.set(Calendar.MINUTE, min1);
        cal2.set(Calendar.SECOND, sec2);
        cal1.set(Calendar.SECOND, sec1);
        cal2.set(Calendar.MILLISECOND, mil2);
        cal1.set(Calendar.MILLISECOND, mil1);
        return result;
    }

    /**
     * 将指定日期具体精确到type定义的阶段，如为day，则精确到天
     *
     * @param type
     * @param date
     * @return
     */
    public static Date getCurDate(int type, Date date) {
        switch (type) {
            case MINUTE:
                return getCurMinute(date);
            case HOUR:
                return getCurHour(date);
            case DAY:
                return getCurDay(date);
            case MONTH:
                return getCurMonth(date);
            default:
                throw new IllegalArgumentException("无效的计算时间类型。");
        }
    }

    /**
     * 获取该date的分钟，若date为空，则获取当前时间的分钟,比如传入的date是2007-12-26 10:33:21,
     * 则返回的是2007-12-26 10:33:00
     *
     * @param date
     * @return
     */
    private static Date getCurMinute(Date date) {
        Date curDate = null;
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        String strCurDate = sdf.format(date);
        try {
            curDate = sdf.parse(strCurDate);
        } catch (ParseException e) {
            log.warn("解释日期失败", e);
        }
        return curDate;
    }

    /**
     * 获取该date的小时，若date为空，则获取当前时间的小时,比如传入的date是2007-12-26 10:33:21,
     * 则返回的是2007-12-26 10:00:00
     *
     * @param date
     * @return
     */
    private static Date getCurHour(Date date) {
        Date curDate = null;
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH");
        String strCurDate = sdf.format(date);
        try {
            curDate = sdf.parse(strCurDate);
        } catch (ParseException e) {
            log.warn("解释日期失败", e);
        }
        return curDate;
    }

    /**
     * 取得传入的日期精确到天的对象,比如传入的date是2007-12-26 10:33:21, 则返回的是2007-12-26 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getCurDay(Date date) {
        Date curDate = null;
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        String strCurDate = String.format("%1$tY-%1$tm-%1$td", date);
        try {
            curDate = sdf.parse(strCurDate);
        } catch (ParseException e) {
            log.warn("解释日期失败", e);
        }
        return curDate;
    }

    /**
     * 取得传入的日期精确到月的对象,比如传入的date是2007-12-26 10:33:21, 则返回的是2007-12-01 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getCurMonth(Date date) {
        Date curMonth = null;
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM");
        String strCurDate = String.format("%1$tY-%1$tm", date);
        try {
            curMonth = sdf.parse(strCurDate);
        } catch (ParseException e) {
            log.warn("解释日期失败", e);
        }
        return curMonth;
    }

    /**
     * 返回指定日期的年份、月份、日份、小时、分钟、秒、毫秒
     *
     * @param type ：包括年份、月份、日份、小时、分钟、秒、毫秒
     * @param date ：指定日期
     * @return
     */
    public static long getSingleTime(int type, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (type) {
            case MILLI:
                return c.getTimeInMillis();
            case MINUTE:
                return c.get(Calendar.MINUTE);
            case HOUR:
                return c.get(Calendar.HOUR_OF_DAY);
            case DAY:
                return c.get(Calendar.DAY_OF_MONTH);
            case MONTH:
                return c.get(Calendar.MONTH) + 1;
            case YEAR:
                return c.get(Calendar.YEAR);
            default:
                throw new IllegalArgumentException("无效的时间类型。");
        }
    }

    /**
     * 给指定时间添加分钟数
     *
     * @param date    指定的时间
     * @param minutes 分钟数
     * @return
     */
    public static Date addMinutes(Date date, long... minutes) {
        if (minutes == null) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        for (long minute : minutes) {
            c.add(Calendar.MINUTE, (int) minute);
        }

        return c.getTime();
    }

    /**
     * 计算两个日期相差的分钟数 date-subDate
     *
     * @param date
     * @param subDate
     * @return 两个时间相差的分钟数。
     */
    public static long subDate(Date date, Date subDate) {
        long sub = 0;
        if (date == null || subDate == null) {
            throw new IllegalArgumentException();
        }
        sub = date.getTime() - subDate.getTime();
        return sub / (60 * 1000);
    }

    /**
     * 计算两个日期相差的分钟数 date-subDate
     *
     * @param date
     * @param subDate
     * @return
     */
    public static long subDateCeil(Date date, Date subDate) {
        long sub = 0;
        if (date == null || subDate == null) {
            throw new IllegalArgumentException();
        }
        sub = date.getTime() - subDate.getTime();
        return Double.valueOf(Math.ceil(sub * 1.0 / (60 * 1000))).longValue();
    }

    /**
     * String类型变为Date类型
     *
     * @param s
     * @return
     */
    public static Date convertStrToDate(String s) {
        try {
            DateFormat dateformat = DateFormat.getDateInstance();
            Date date = dateformat.parse(s);
            return date;
        } catch (Exception exception) {
            exception.printStackTrace();
            Calendar cal = Calendar.getInstance();
            cal.set(1900, 0, 1);
            return cal.getTime();
        }
    }

    /**
     * 按照指定格式将String类型变为Date类型
     *
     * @param s
     * @param format ：指定格式
     * @return
     */
    public static Date convertStrToDate(String s, String format) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        try {
            Date date = simpledateformat.parse(s);
            return date;
        } catch (Exception exception) {
            exception.printStackTrace();
            Calendar cal = Calendar.getInstance();
            cal.set(1900, 0, 1);
            return cal.getTime();
        }
    }

    /**
     * 按照指定格式将Date类型变为String类型
     *
     * @param d
     * @param format
     * @return
     */
    public static String convertDateToStr(Date d, String format) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        String s;
        try {
            s = simpledateformat.format(d);
            return s;
        } catch (Exception e) {
            s = "1900-01-01";
        }
        return s;
    }

    /**
     * 将格式为YYYY-MI-DD hh:mi:ss字符串转换成日期型
     *
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {
        if (date == null)
            return null;
        String[] temp = date.split("-");
        if (temp.length != 3)
            return null;
        Calendar instance = Calendar.getInstance();
        String[] temp1 = temp[2].split(" ");
        String dd = temp1[0];
        String time = temp1[1];
        String[] temp2 = time.split(":");
        instance.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]) - 1, Integer.parseInt(dd),
                Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]), Integer.parseInt(temp2[2]));
        return instance.getTime();
    }

    /**
     * 将格式为YYYY-MI-DD字符串转换成日期型
     *
     * @param date
     * @param flag :如果flag为true则时间为 YYYY-MI-DD 0:0:0，为false则为23:59:59
     * @return
     */
    public static Date stringToDate(String date, boolean flag) {
        if (date == null)
            return null;
        String[] temp = date.split("-");
        if (temp.length != 3)
            return null;
        Calendar instance = Calendar.getInstance();
        if (flag) {
            instance.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]), 0, 0, 0);
        } else {
            instance.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]), 23, 59,
                    59);
        }
        return instance.getTime();
    }

    /**
     * 将字符串转换为日期型
     *
     * @param year
     * @param month
     * @param flag  :为true则是当前月的时间， 为false是下个月的时间
     * @return
     */
    public static Date stringToMonth(String year, String month, boolean flag) {
        if (year == null || month == null)
            return null;
        Calendar instance = Calendar.getInstance();
        if (flag) {
            instance.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1, 0, 0, 0);
        } else {
            instance.set(Integer.parseInt(year), Integer.parseInt(month), 1, 0, 0, 0);
        }
        return instance.getTime();
    }

    /**
     * 两个日期之间的比较
     *
     * @param strDate1
     * @param strDate2
     * @param format
     * @return strDate1 > strDate2:return 1 strDate1 = strDate2:return 0
     * strDate1 < strDate1:return -1
     */
    public static int compareDate(String strDate1, String strDate2, String format) {
        Date date1 = convertStrToDate(strDate1, format);
        Date date2 = convertStrToDate(strDate2, format);
        return compareDate(date1, date2);
    }

    /**
     * 两个日期之间的比较 默认格式为YYYYMMDDHHMMSS
     *
     * @param strDate1
     * @param strDate2
     * @return strDate1 > strDate2:return 1 strDate1 = strDate2:return 0
     * strDate1 < strDate1:return -1
     */
    public static int compareDate(String strDate1, String strDate2) {
        return compareDate(strDate1, strDate2, YYYYMMDDHHMMSS);
    }

    /**
     * 两个日期之间的比较
     *
     * @param date1
     * @param date2
     * @return strDate1 > strDate2:return 1 strDate1 = strDate2:return 0
     * strDate1 < strDate1:return -1
     */
    public static int compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else if (date1.getTime() == date2.getTime()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 比较一个日期是否在指定日期之间
     *
     * @param date
     * @param startDate ：起始日期
     * @param endDate   ：结束日期
     * @return:返回0，则在指定日期区间之内
     */
    public static int compareDate(Date date, Date startDate, Date endDate) {
        if (date == null || startDate == null || endDate == null)
            return -2;
        if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime())
            return 0;
        else if (date.getTime() > endDate.getTime())
            return 1;
        else
            return -1;
    }


    /**
     * 返回当前日期前num个小时的日期
     *
     * @param num
     * @return
     * @throws Exception
     */
    public static String getDateBeforeHour(int num) {
        long longtimes = System.currentTimeMillis() - num * MILLISECOND_IN_HOUR;
        Date date = new Date(longtimes);
        return convertDateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回当前时间加上指定分钟后的时间
     *
     * @param minute 分钟数
     * @return
     */
    public static String getTimeByMinute(int minute) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, minute);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }

    /**
     * 返回今天前Num天的日期
     *
     * @param num
     * @return
     * @throws Exception
     */
    public static String getBeforeToday(int num, String format) {
        long longtimes = System.currentTimeMillis() - num * MILLISECOND_IN_DAY;
        Date date = new Date(longtimes);
        return convertDateToStr(date, format);
    }

    /**
     * 返回今天前Num天的日期
     *
     * @param num
     * @return
     * @throws Exception
     */
    public static String getBeforeToday(int num) {
        return getBeforeToday(num, YYYYMMDDHHMMSS);
    }

    /**
     * 返回指定日期的前Num天的日期
     *
     * @param date   指定日期
     * @param num
     * @param format
     * @return
     */
    public static String getBeforeToday(Date date, int num, String format) {
        long longtimes = date.getTime() - num * MILLISECOND_IN_DAY;
        Date date2 = new Date(longtimes);
        return convertDateToStr(date2, format);
    }

    /**
     * 返回指定日期的前Num天的日期
     *
     * @param date ：指定日期
     * @param num
     * @return
     */
    public static String getBeforeToday(Date date, int num) {

        return getBeforeToday(date, num, YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前时间
     *
     * @return 返回时间格式为YYYYMMDDHH24MISS的字符串
     */
    public static String getCurDate() {
        return date2Str(new Date(), YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前时间
     *
     * @param dateFormat 时间格式
     * @return 返回指定时间格式的字符串
     */
    public static String getCurDate(String dateFormat) {
        return date2Str(new Date(), dateFormat);
    }

    /**
     * 将时间格式为YYYYMMDDHH24MISS的字符串转化为Date
     *
     * @param dateStr 时间格式为YYYYMMDDHH24MISS的字符串
     * @return Date
     */
    public static Date str2Date(String dateStr) {
        return str2Date(dateStr, YYYYMMDDHHMMSS);
    }

    /**
     * 将时间格式为YYYYMMDD的字符串转化为Date
     *
     * @param dateStr
     * @return
     */
    public static Date dayStr2Date(String dateStr) {
        return str2Date(dateStr, YYYYMMDD);
    }

    /**
     * 将时间字符串转化为时间戳
     *
     * @param dateStr
     * @return
     */
    public static Timestamp str2Timestamp(String dateStr) {
        if (!ObjectUtils.isEmpty(dateStr)) {
            return new Timestamp(str2Date(dateStr, YYYYMMDD).getTime());
        }
        return null;
    }

    /**
     * 时间串转化为Date
     *
     * @param dateStr    dateFormat时间格式的字符串
     * @param dateFormat 时间格式
     * @return Date
     */
    public static Date str2Date(String dateStr, String dateFormat) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        try {
            df.setLenient(false);
            return df.parse(dateStr);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 判断是不是日期格式
     *
     * @return boolean    返回类型
     * @throws
     * @author james
     * @Title: isDate
     */
    public static boolean isDate(String date) {
        /**
         * 判断日期格式和范围
         */
        String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(date);

        boolean dateType = mat.matches();

        return dateType;
    }

    /**
     * 检查是否是日期，并且不能大于当前日期
     * 主要是用来验证出生日期的，但也可以用在其它的地方
     *
     * @return boolean    返回类型
     * @throws
     * @author james
     * @Title: isDateAndNoGtNow
     */
    public static boolean isDateAndNoGtNow(String date) {
        boolean isTrue = isDate(date);
        if (isTrue) {
            //确认是日期是，就需要检查是否大于今天
            long param = str2Date(date, DateUtils.YYYYMMDD).getTime();
            long nowtime = System.currentTimeMillis();
            if (param > nowtime) {
                isTrue = false;
            }
        }
        return isTrue;
    }

    /**
     * Date转化为YYYYMMDDHH24MISS格式的字符串
     *
     * @param date Date
     * @return YYYYMMDDHH24MISS格式的字符串
     */
    public static String date2Str(Date date) {
        return date2Str(date, YYYYMMDDHHMMSS);
    }

    /**
     * Date转化为dateFormat时间格式的字符串
     *
     * @param date       Date
     * @param dateFormat 时间格式
     * @return dateFormat时间格式的字符串
     */
    public static String date2Str(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

    /**
     * 返回Date对象的年份
     *
     * @param date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 返回Date对象的月份
     *
     * @param date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回Date对象的日份
     *
     * @param date 日
     * @return 返回日期
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回Date对象的小时
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回Date对象的分钟
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 返回秒
     *
     * @param date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 日期加月
     *
     * @param date  日期
     * @param month 加的月数
     * @return 返回相加后的日期
     */

    public static Date addMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 日期相加
     *
     * @param date 日期
     * @param day  加的天数
     * @return 返回相加后的日期
     */
    public static Date addDays(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期加小时
     *
     * @param date  日期
     * @param hours 小时数
     * @return 返回相加后的日期
     */
    public static Date addHours(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) hours) * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期加分钟
     *
     * @param date
     * @param minutes 分钟
     * @return 返回相加后的日期
     */
    public static Date addMinutes(Date date, long minutes) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + minutes * 60 * 1000);
        return c.getTime();
    }

    /**
     * 日期加分钟
     *
     * @param date
     * @param minutes 分钟
     * @return 返回相加后的日期
     */
    public static Date add(Date date, long minutes) {
        if (date == null) {
            throw new IllegalArgumentException();
        }

        return addMinutes(date, minutes);
    }

    /**
     * 计算两个时间相差的秒数
     *
     * @param minuend
     * @param subtrahend
     * @return 两个时间点相差的秒数(minuend - subtrahend)
     */
    public static long sub(Date minuend, Date subtrahend) {
        long subResult = 0;
        if (minuend == null || subtrahend == null) {
            throw new IllegalArgumentException();
        }
        subResult = minuend.getTime() - subtrahend.getTime();
        subResult = subResult / 1000;
        return subResult;
    }

    /**
     * 计算两个时间相差的天数
     *
     * @param beginDate
     * @param endDate
     * @return 两个时间点相差的天数(beginDate - endDate)
     */
    public static long between(Date beginDate, Date endDate) {
        Calendar calBegin = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(beginDate);
        calEnd.setTime(endDate);
        calBegin.clear(14);
        calEnd.clear(14);
        long millisecs = calBegin.getTime().getTime() - calEnd.getTime().getTime();
        long remainder = millisecs % 0x5265c00L;
        return (millisecs - remainder) / 0x5265c00L;
    }

    /**
     * 格式化Date对象输出字符串的函数
     *
     * @param date   Date
     * @param format String
     * @return String 为date按指定格式format格式化的string串
     */
    public static String formatDate(Date date, String format) {
        return formatDateByFormat(date, format);
    }

    /**
     * 格式化Date对象输出字符串的函数
     *
     * @param date   Date
     * @param format String
     * @return String 为date按指定格式format格式化的string串
     */
    public static String dateToString(Date date, String format) {
        return formatDateByFormat(date, format);
    }

    /**
     * 以指定的格式来格式化日期
     *
     * @param date   Date
     * @param format String 输出格式
     * @return String String 为date按指定格式format格式化的string串
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                log.info("date:" + date);
            }
        }
        return result;
    }

    /**
     * 以指定的格式和日期字符返回日期
     *
     * @param str    String 需要格式化的日期串
     * @param format String 输出格式
     * @return String String 为按指定格式format格式化的Date
     */
    public static Date formatDate(String str, String format) {
        return stringToDate(str, format);
    }

    /**
     * 以指定的格式和日期字符返回日期
     *
     * @param str    String 需要格式化的日期串
     * @param format String 输出格式
     * @return Date 为按指定格式format格式化的Date,失败返回null
     */
    public static Date stringToDate(String str, String format) {
        if (str != null) {
            DateFormat dateFormat = new SimpleDateFormat(format);
            try {
                return dateFormat.parse(str);
            } catch (ParseException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * 判断"yyyyMMdd"格式日期字符串是否为今天
     *
     * @param checkDate String "yyyyMMdd"格式的日期字符串
     * @return 若checkDate为今天，则返回true否者为false
     */
    public static boolean isToday(String checkDate) {
        Date date = DateUtils.stringToDate(checkDate, YYYYMMDD);
        DateUtils.formatDate(date, YYYYMMDD);
        return DateUtils.formatDate(new Date(), YYYYMMDD).equals(DateUtils.formatDate(date, YYYYMMDD));
    }

    /**
     * 把Date类型转换为Timestamp
     *
     * @param date Date 需要被转换的Date类型日期
     * @return Timestamp类型日期
     */
    public static Timestamp dateToTimestamp(Date date) {
        Timestamp tmResult = null;
        if (date != null)
            tmResult = new Timestamp(date.getTime());
        return tmResult;
    }

    /**
     * 将日期格式化为字符串
     *
     * @param formatDate
     * @param pattern    日期格式
     * @return
     */
    public static String formatDateToStr(Date formatDate, String pattern) {
        if (formatDate == null) {
            return null;
        }
        if ((pattern == null) || (pattern.equals(""))) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat format = null;
        format = new SimpleDateFormat(pattern);
        String dayBefore = format.format(Long.valueOf(formatDate.getTime()));
        return dayBefore;
    }

    /**
     * 将日期格式化为字符串
     *
     * @param pattern 日期格式
     * @return
     */
    public static String formatDateToStr(String pattern) {
        return formatDateToStr(new Date(), pattern);
    }

    /**
     * 返回当前时间
     *
     * @return
     */
    public static Date Now() {
        return new Date();
    }


    public final static LocalDate LOCAL_DATE_1900 = LocalDate.of(1900, 1, 1);

}
