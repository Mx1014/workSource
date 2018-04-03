package com.everhomes.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/7.
 */
public class CronDateUtils {
    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    /***
     *
     * @param date 时间
     * @return  cron类型的日期
     */
    public static String getCron(final Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     *
     * @param cron Quartz cron的类型的日期
     * @return  Date日期
     */

    public static Date getDate(final String cron) {

        if(cron == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(cron);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static void main(String[] args) {
        long current = System.currentTimeMillis();
        System.out.print(getCron(new Timestamp(current)));
    }
}
