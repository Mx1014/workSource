package com.everhomes.rest.archives;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ArchivesDateUtil {

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

    public static Timestamp dateToTimestamp(java.sql.Date date) {
        if (date != null)
            return new Timestamp(date.getTime());
        else
            return null;
    }
}
