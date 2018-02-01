package com.everhomes.archives;

import com.everhomes.rest.archives.SocialSecurityMonthType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        java.util.Date d = null;
        try {
            d = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

    /**
     * 当前日期( sql.Date 类型)
     */
    public static java.sql.Date currentDate(){
        java.util.Date now  = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        return date;
    }

    /**
     * 社保专用月份取值
     */
    public static String socialSecurityMonth(Byte monthType) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMM");
        //  当前月
        if (monthType.equals(SocialSecurityMonthType.THIS_MONTH.getCode())) {
            LocalDate date = LocalDate.now();
            return date.format(format);
        } else {
            //  下一月
            LocalDate date = LocalDate.now().plusMonths(1L);
            return date.format(format);
        }
    }
}
