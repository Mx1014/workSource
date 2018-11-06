package com.everhomes.techpark.punch.utils;

import java.math.BigDecimal;

public final class PunchDayParseUtils {
    private static final Long MILLISECOND_OF_ONE_DAY = 8 * 3600 * 1000L;
    private static final int MILLISECOND_OF_ONE_HOUR = 60 * 60 * 1000;
    private static final int MILLISECOND_OF_ONE_MINUTE = 60 * 1000;

    private PunchDayParseUtils() {
    }

    public static String parseHourMinuteDisplayStringZeroWithUnit(Long milliSecond, String hourUnit, String minuteUnit) {
        if (milliSecond == null || milliSecond <= 0) {
            return "0" + minuteUnit;
        }
        long h = Math.abs(milliSecond) / MILLISECOND_OF_ONE_HOUR;
        long m = (Math.abs(milliSecond) % MILLISECOND_OF_ONE_HOUR) / MILLISECOND_OF_ONE_MINUTE;
        StringBuffer s = new StringBuffer();
        if (h > 0) {
            s.append(h).append(hourUnit);
        }
        if (m > 0 || s.length() == 0) {
            s.append(m).append(minuteUnit);
        }
        return s.toString();
    }

    public static String parseHourMinuteDisplayStringZeroWithoutUnit(Long milliSecond, String hourUnit, String minuteUnit) {
        if (milliSecond == null || milliSecond <= 0) {
            return "0";
        }
        long h = Math.abs(milliSecond) / MILLISECOND_OF_ONE_HOUR;
        long m = (Math.abs(milliSecond) % MILLISECOND_OF_ONE_HOUR) / MILLISECOND_OF_ONE_MINUTE;
        StringBuffer s = new StringBuffer();
        if (h > 0) {
            s.append(h).append(hourUnit);
        }
        if (m > 0 || s.length() == 0) {
            s.append(m);
            // 0小时0分钟的情况只显示0，不带单位
            if (m > 0) {
                s.append(minuteUnit);
            }
        }
        return s.toString();
    }

    // 该方法用于将天数换算成“3.5天1.5小时”的格式，0时不显示单位
    public static String parseDayTimeDisplayStringZeroWithoutUnit(Double dayCount, String dayUnit, String hourUnit) {
        double halfDays = calculateTimesOfHalfDay(Math.abs(dayCount));
        double halfHours = calculateTimesOfHalfHour(Math.abs(dayCount));
        StringBuffer s = new StringBuffer();
        if (halfDays > 0) {
            s.append(halfDays).append(dayUnit);
        }
        if (halfHours > 0 || s.length() == 0) {
            s.append(halfHours);
            // 0天0小时的情况只显示0不带单位
            if (halfHours > 0) {
                s.append(hourUnit);
            }
        }
        return s.toString().replace(".0", "");
    }

    // 该方法用于将天数换算成“3.5天1.5小时”的格式，0时显示单位
    public static String parseDayTimeDisplayStringZeroWithUnit(Double dayCount, String dayUnit, String hourUnit) {
        double halfDays = calculateTimesOfHalfDay(Math.abs(dayCount));
        double halfHours = calculateTimesOfHalfHour(Math.abs(dayCount));
        return (halfDays + dayUnit + halfHours + hourUnit).replace(".0", "");
    }

    public static double calculateTimesOfHalfDay(Double dayCount) {
        if (dayCount == null || dayCount == 0) {
            return 0;
        }
        BigDecimal timesOfHalfHour = new BigDecimal(String.valueOf(Math.abs(dayCount))).multiply(new BigDecimal(16)).setScale(0, BigDecimal.ROUND_FLOOR);  // 换算成半小时的倍数
        int num1 = timesOfHalfHour.intValue() / 8;  // num1表示多少个半天
        double days = new BigDecimal(num1).divide(BigDecimal.valueOf(2.0)).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue();
        return days;
    }

    public static double calculateTimesOfHalfHour(Double dayCount) {
        if (dayCount == null || dayCount == 0) {
            return 0;
        }
        BigDecimal timesOfHalfHour = new BigDecimal(String.valueOf(Math.abs(dayCount))).multiply(new BigDecimal(16)).setScale(0, BigDecimal.ROUND_FLOOR);  // 换算成半小时的倍数
        int num1 = timesOfHalfHour.intValue() % 8;  // num1表示去除半天（倍数）后剩余多少个半小时
        double hours = new BigDecimal(num1).divide(BigDecimal.valueOf(2.0)).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue();
        return hours;
    }
}
