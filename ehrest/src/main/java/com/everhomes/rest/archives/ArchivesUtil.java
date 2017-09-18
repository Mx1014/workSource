package com.everhomes.rest.archives;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArchivesUtil {

    public static java.sql.Date parseDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
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
        Calendar cal = Calendar.getInstance();
        java.util.Date date = cal.getTime();
        return new java.sql.Date(date.getTime());
    }
}
