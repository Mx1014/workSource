package com.everhomes.techpark.punch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class PunchDateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PunchDateUtils.class);

    private static final String DATE_SDF_1 = "yyyyMMdd";
    private static final String DATE_SDF_2 = "yyyy-MM-dd";

    private PunchDateUtils() {
    }

    public static String getTheFirstMonthDate(String month, String defaultValue) {
        SimpleDateFormat df1 = new SimpleDateFormat(DATE_SDF_1);
        SimpleDateFormat df2 = new SimpleDateFormat(DATE_SDF_2);

        try {
            return df2.format(df1.parse(month + "01"));
        } catch (ParseException e) {
            LOGGER.error("transfer format error");
            return defaultValue;
        }
    }

    public static String getTheLastMonthDate(String month, String defaultValue) {
        SimpleDateFormat df1 = new SimpleDateFormat(DATE_SDF_1);
        SimpleDateFormat df2 = new SimpleDateFormat(DATE_SDF_2);

        try {
            Date date = df1.parse(month + "01");
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //  获取当前月最后一天
            return df2.format(c.getTime());
        } catch (ParseException e) {
            LOGGER.error("transfer format error");
            return defaultValue;
        }
    }

}
