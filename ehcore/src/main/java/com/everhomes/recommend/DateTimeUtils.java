package com.everhomes.recommend;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Move to somewhere ???
 * @author janson
 *
 */
public class DateTimeUtils {
    public static Timestamp fromString(String s) {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(s);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        }catch(Exception e) {
            return null;
        }
    }
}
