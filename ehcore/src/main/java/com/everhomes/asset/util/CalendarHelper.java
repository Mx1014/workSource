package com.everhomes.asset.util;

import java.util.Calendar;

public class CalendarHelper {
    /**
     * 创建一个当年当月1日的日期
     */
    public static Calendar newClearedCalendar(){
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH,1);
        return instance;
    }
}
