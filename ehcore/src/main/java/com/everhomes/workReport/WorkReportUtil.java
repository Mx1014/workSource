package com.everhomes.workReport;

import com.everhomes.rest.workReport.WorkReportType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class WorkReportUtil {

    static LocalDateTime getSettingTime(Long rTime, Byte rType, Byte sType, String sMask, String sTime) {
        Instant instant = Instant.ofEpochMilli(rTime);
        ZoneId zone = ZoneId.systemDefault();
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
                temp = year + "-" + toDoubleDigit(month) + "-" + toDoubleDigit(day) + " " + sTime + ":" + "00";
                break;
            case WEEK:
                if(sType == 1)
                    reportTime = reportTime.plusWeeks(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                day = reportTime.getDayOfMonth() + Integer.valueOf(sMask) - 1;
                temp = year + "-" + toDoubleDigit(month) + "-" + toDoubleDigit(day) + " " + sTime + ":" + "00";
                break;
            case MONTH:
                if (sType == 1)
                    reportTime = reportTime.plusMonths(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                day = Integer.valueOf(sMask);
                temp = year + "-" + toDoubleDigit(month) + "-" + toDoubleDigit(day) + " " + sTime + ":" + "00";
                break;
        }
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(temp);
        return LocalDateTime.from(accessor);
    }

    private static String toDoubleDigit(int num) {
        String tem = String.valueOf(num);
        if (tem.length() < 2)
            return "0" + tem;
        return tem;
    }

    static java.sql.Date timestampToDate(Long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        LocalDate date = LocalDateTime.ofInstant(instant, zone).toLocalDate();
        return java.sql.Date.valueOf(date);
    }

    public static String displayReportTime(Byte rType, java.sql.Date rTime) {
        DateTimeFormatter mdFormat = DateTimeFormatter.ofPattern("M月d日");
        DateTimeFormatter ymFormat = DateTimeFormatter.ofPattern("yyyy年M月");
//        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("MM月dd日");

        LocalDate temp = rTime.toLocalDate();
        if (rTime == null)
            return null;
        String result = "";
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                result = mdFormat.format(temp);
                break;
            case WEEK:
                result = mdFormat.format(temp);
                temp = temp.plusDays(6);
                result += " ~ " + mdFormat.format(temp);
                break;
            case MONTH:
                result = ymFormat.format(temp);
                break;
        }
        return result;
    }

    /*public static String formatTime2WorkReportTime(long time, Byte reportType) {
        WorkReportType workReportType = WorkReportType.fromCode(reportType);
        if (workReportType == null) {
            workReportType = WorkReportType.DAY;
        }
        StringBuffer sb = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        switch (workReportType) {
            case DAY: {
                //选中今日
                sb.append(MMDD.format(calendar.getTime()));
                break;
            }
            case WEEK: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                sb.append(MMDD.format(calendar.getTime()));
                sb.append("～");
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                sb.append(MMDD.format(calendar.getTime()));
                break;
            }
            case MONTH: {
                sb.append(YYYYMM.format(calendar.getTime()));
                break;
            }
            default:
                break;
        }
        return sb.toString();
    }*/


    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String s = "2019-02-25 09:30:01";
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(s);
        ZoneId zone = ZoneId.systemDefault();

        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("M月d日");
//        Instant instant = localDateTime;
//        return instant;
        Long reportTime = LocalDateTime.from(accessor).atZone(zone).toInstant().toEpochMilli();
        LocalDateTime time1 = getSettingTime(reportTime, WorkReportType.DAY.getCode(), WorkReportType.DAY.getCode(), null, "15:30");
        LocalDateTime time2 = getSettingTime(reportTime, WorkReportType.DAY.getCode(), WorkReportType.WEEK.getCode(), null, "16:30");
        LocalDateTime time3 = getSettingTime(reportTime, WorkReportType.WEEK.getCode(), WorkReportType.DAY.getCode(), "4", "12:30");
        LocalDateTime time4 = getSettingTime(reportTime, WorkReportType.WEEK.getCode(), WorkReportType.WEEK.getCode(), "5", "13:30");
        LocalDateTime time5 = getSettingTime(reportTime, WorkReportType.MONTH.getCode(), WorkReportType.DAY.getCode(), "11", "11:30");
        LocalDateTime time6 = getSettingTime(reportTime, WorkReportType.MONTH.getCode(), WorkReportType.WEEK.getCode(), "12", "12:30");

/*        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(time1));
        System.out.println(formatter.format(time2));
        System.out.println(time1.isBefore(time2));

        System.out.println(formatter.format(time3));
        System.out.println(formatter.format(time4));
        System.out.println(formatter.format(time5));
        System.out.println(formatter.format(time6));*/

        System.out.println(dayFormat.format(time1));

        System.out.println(displayReportTime(WorkReportType.DAY.getCode(), java.sql.Date.valueOf(time1.toLocalDate())));
        System.out.println(displayReportTime(WorkReportType.WEEK.getCode(), java.sql.Date.valueOf(time1.toLocalDate())));
        System.out.println(displayReportTime(WorkReportType.MONTH.getCode(), java.sql.Date.valueOf(time1.toLocalDate())));



    }
}
