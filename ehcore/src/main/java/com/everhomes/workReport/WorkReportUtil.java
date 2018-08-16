package com.everhomes.workReport;

import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.workReport.WorkReportType;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class WorkReportUtil {

    public static LocalDateTime convertSettingToTime(Long rTime, Byte rType, Byte sType, String sMask, String sTime) {

        Instant instant = Instant.ofEpochMilli(rTime);
        ZoneId zone = ZoneId.systemDefault();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime reportTime = LocalDateTime.ofInstant(instant, zone);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        int year, month, day;
        String temp = null;
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                if (sType == 1)
                    reportTime = reportTime.plusDays(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                day = reportTime.getDayOfMonth();
                temp = year + "-" + doubleDigit(month) + "-" + doubleDigit(day) + " " + sTime + ":" + "00";
                break;
            case WEEK:
                if(sType == 1)
                    reportTime = reportTime.plusWeeks(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                day = reportTime.getDayOfMonth() + Integer.valueOf(sMask) - 1;
                temp = year + "-" + doubleDigit(month) + "-" + doubleDigit(day) + " " + sTime + ":" + "00";
                break;
            case MONTH:
                if (sType == 1)
                    reportTime = reportTime.plusMonths(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                day = Integer.valueOf(sMask);
                temp = year + "-" + doubleDigit(month) + "-" + doubleDigit(day) + " " + sTime + ":" + "00";
        }
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(temp);
        return LocalDateTime.from(accessor);
    }

    private static String doubleDigit(int num) {
        String tem = String.valueOf(num);
        if (tem.length() < 2)
            return "0" + tem;
        return tem;
    }

    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String s = "2019-02-25 09:30:01";
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(s);
        ZoneId zone = ZoneId.systemDefault();
//        Instant instant = localDateTime;
//        return instant;
        Long reportTime = LocalDateTime.from(accessor).atZone(zone).toInstant().toEpochMilli();
        LocalDateTime time1 = convertSettingToTime(reportTime, WorkReportType.DAY.getCode(), WorkReportType.DAY.getCode(), null, "15:30");
        LocalDateTime time2 = convertSettingToTime(reportTime, WorkReportType.DAY.getCode(), WorkReportType.WEEK.getCode(), null, "16:30");
        LocalDateTime time3 = convertSettingToTime(reportTime, WorkReportType.WEEK.getCode(), WorkReportType.DAY.getCode(), "4", "12:30");
        LocalDateTime time4 = convertSettingToTime(reportTime, WorkReportType.WEEK.getCode(), WorkReportType.WEEK.getCode(), "5", "13:30");
        LocalDateTime time5 = convertSettingToTime(reportTime, WorkReportType.MONTH.getCode(), WorkReportType.DAY.getCode(), "11", "11:30");
        LocalDateTime time6 = convertSettingToTime(reportTime, WorkReportType.MONTH.getCode(), WorkReportType.WEEK.getCode(), "12", "12:30");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(time1));
        System.out.println(formatter.format(time2));
        System.out.println(formatter.format(time3));
        System.out.println(formatter.format(time4));
        System.out.println(formatter.format(time5));
        System.out.println(formatter.format(time6));

    }
}
