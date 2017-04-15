package com.edm.utils;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.edm.utils.web.Validates;

/**
 * @author xiaobo
 * @see 日历封装类.
 */
public class Calendars {
    
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm";
    
    /**
     * @see 获取DateTime.
     */
    public static DateTime parse(String str, String fmt) {
        DateTime datetime = null;
        if (StringUtils.isNotBlank(str) && Validates.formatter(str, fmt))
            datetime = DateTimeFormat.forPattern(fmt).parseDateTime(str);
        return datetime;
    }
    
    /**
     * @see 获取Date字符串.
     */
    public static String format(Date date, String fmt) {
        if (date == null)
            return null;
        DateTime datetime = new DateTime(date);
        return datetime.toString(fmt);
    }
    
    /**
     * @see 获取Date.
     */
    public static Date date(String millis) {
        Date d = null;
        if (StringUtils.isNotBlank(millis)) {
            d = new DateTime(Long.valueOf(millis)).toDate();
        }
        return d;
    }
    
    /**
     * @see 获取毫秒.
     */
    public static long millis(String date, String fmt) {
        long ms = 0;
        if (StringUtils.isNotBlank(date)) {
            DateTimeFormatter fmter = DateTimeFormat.forPattern(fmt);
            ms = fmter.parseDateTime(date).getMillis();
        }
        return ms;
    }
    
    /**
     * @see 获取week|month第一天.
     */
    public static DateTime start(Property property) {
        return property.withMinimumValue().millisOfDay().withMinimumValue();
    }
    
    /**
     * @see 获取week|month最后一天.
     */
    public static DateTime end(Property property) {
        return property.withMaximumValue().millisOfDay().withMaximumValue();
    }
    
    /**
     * @see 获取季度. (return: 1|2|3|4)
     */
    public static int season(int month) {
        return month / 3 + (month % 3 > 0 ? 1 : 0);
    }
    
    /**
     * @see 获取季度月份. (return: [1,3]|[4,6]|[7,9]|[10,12])
     */
    public static int[] seasonPos(DateTime datetime) {
        int month = datetime.getMonthOfYear(), min = 0, max = 0;
        if (month >= 1 && month <= 3) { min = 1; max = 3; }
        else if (month >= 4 && month <= 6) { min = 4; max = 6; }
        else if (month >= 7 && month <= 9) { min = 7; max = 9; }
        else if (month >= 10 && month <= 12) { min = 10; max = 12; }
        return new int[] { min, max };
    }
    
    /**
     * @see 获取总自然周数.
     */
    public static int weeks(DateTime start, DateTime end) {
        return Weeks.weeksBetween(start(start.dayOfWeek()), end(end.dayOfWeek())).getWeeks() + 1;
    }
    
    /**
     * @see 获取总自然月数.
     */
    public static int months(DateTime start, DateTime end) {
        return Months.monthsBetween(start(start.dayOfMonth()), end(end.dayOfMonth())).getMonths() + 1;
    }
    
    /**
     * @see 获取总自然季度数.
     */
    public static int seasons(DateTime start, DateTime end) {
        return season(Months.monthsBetween(
                    start(start.withMonthOfYear(seasonPos(start)[0]).dayOfMonth()),
                    end(end.withMonthOfYear(seasonPos(end)[1]).dayOfMonth())).getMonths());
    }
}
