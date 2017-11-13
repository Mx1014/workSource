package com.everhomes.rest.archives;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArchivesUtil {

    public static java.sql.Date parseDate(String strDate) {
        String pattern = null;
        if (strDate != null) {
            if (strDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                pattern = "yyyy-MM-dd";
            } else if (strDate.matches("\\d{2}/\\d{2}/\\d{2}")) {
                pattern = "MM/dd/yy";
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
     * 当前日期(sql.Date类型)
     * @return
     */
    public static java.sql.Date currentDate(){
        java.util.Date now  = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
/*        DateFormat df = DateFormat.getDateInstance();0
        String str = df.format(now);*/
        return date;
    }
    /*      Date now = new Date();
      Calendar cal = Calendar.getInstance();

      DateFormat d1 = DateFormat.getDateInstance(); //默认语言（汉语）下的默认风格（MEDIUM风格，比如：2008-6-16 20:54:53）
      String str1 = d1.format(now);*/
}
