package com.everhomes.archives;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class ArchivesUtil {

    /**
     * String 转 java.sql.Date
     */
    public static java.sql.Date parseDate(String strDate) {
        String pattern = null;
        if (strDate != null) {
            if (strDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                pattern = "yyyy-MM-dd";
            } else if (strDate.matches("\\d{2}/\\d{2}/\\d{2}")) {
                pattern = "MM/dd/yy";
            }else if (strDate.matches("\\d{2}/\\d/\\d{2}")) {
                pattern = "MM/d/yy";
            }else if (strDate.matches("\\d/\\d/\\d{2}")) {
                pattern = "M/d/yy";
            }else if (strDate.matches("\\d/\\d{2}/\\d{2}")) {
                pattern = "M/dd/yy";
            }
        }
        if (pattern == null)
            return null;
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(strDate);
//        DateTimeFormatter formatter = (DateTimeFormatter) accessor;
//        LocalDateTime localDateTime = LocalDateTime.parse(strDate, formatter);
//        java.util.Date d = java.util.Date.from(localDateTime.atZone(zone).toInstant());
        LocalDate date = LocalDate.from(accessor);
        return java.sql.Date.valueOf(date);
    }

    /**
     * 当前日期(sql.Date 类型)
     */
    public static java.sql.Date currentDate(){
        LocalDate date = LocalDate.now();
        return java.sql.Date.valueOf(date);
    }

    /**
     * 当前日期+n天
     */
    public static java.sql.Date plusDate(int n){
        LocalDate plusDate = LocalDate.now().plusDays(n);
        return java.sql.Date.valueOf(plusDate);
    }

    /**
     * 本周起始日
     */
    public static java.sql.Date firstOfWeek(){
        LocalDate nowDate = LocalDate.now();
        LocalDate date = nowDate.minusDays(nowDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        return java.sql.Date.valueOf(date);
    }

    /**
     * 本周截止日
     */
    public static java.sql.Date lastOfWeek(){
        LocalDate nowDate = LocalDate.now();
        LocalDate date = nowDate.plusDays(DayOfWeek.SUNDAY.getValue() - nowDate.getDayOfWeek().getValue());
        return java.sql.Date.valueOf(date);
    }
}
