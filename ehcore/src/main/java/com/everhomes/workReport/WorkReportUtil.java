package com.everhomes.workReport;

import com.everhomes.rest.workReport.ReportValiditySettingDTO;
import com.everhomes.rest.workReport.WorkReportType;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class WorkReportUtil {

    /**
     * 根据汇报时间与设定得到具体时间
     */
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
                if (sType == 1)
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

    /**
     * 将Timestamp转化为 java.sql.Date
     */
    static java.sql.Date timestampToDate(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        LocalDate date = LocalDateTime.ofInstant(instant, zone).toLocalDate();
        return java.sql.Date.valueOf(date);
    }

    /**
     * 根据汇报类型显示日期
     */
    static String displayReportTime(Byte rType, java.sql.Date rTime) {
        DateTimeFormatter mdFormat = DateTimeFormatter.ofPattern("M月d日");
        DateTimeFormatter ymFormat = DateTimeFormatter.ofPattern("yyyy年M月");

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

    /* ----------------------------------- 汇报时间获取规则总结start(add by Ryan) ----------------------------------- */
    /*startType-起始类型 currentTime当前时间 cureentDay-当前日期(周期) endTpye-结束类型 endMask-结束日期(周期) endTime-结束时间

    日报规则
    1.startType=endType=0 -> Today
    2.startType=endTpye=1 -> Yesterday
    3.startType=0,endType=1
        3-1.currentTime>endTime -> Today
        3-2.currentTime<=endTime -> Yesterday

    周报规则
    1.startType=endType=0 -> ThisWeek
    2.startType=endTpye=1 -> LastWeek
    3.startType=0,endType=1
        3-1.currentDay>endMask -> ThisWeek
        3-2.cureentDay<endMask -> LastWeek
        3-3.currentDay=endMask
            3-3-1.currentTime>endTime -> ThisWeek
            3-3-2.currentTime<=endTime -> LastWeek

    月报规则
    1.startType=endType=0 -> ThisMonth
    2.startType=endTpye=1 -> LastMonth
    3.startType=0,endType=1
        3-1.currentDay>endMask -> ThisMonth
        3-2.cureentDay<endMask -> LastMonth
        3-3.currentDay=endMask
            3-3-1.currentTime>endTime -> ThisMonth
            3-3-2.currentTime<=endTime -> LastMonth*/
    /* ------------------------------------------- 汇报时间获取规则总结end ------------------------------------------- */
    static LocalDate getReportTime(Byte rType, ReportValiditySettingDTO setting) {
        LocalDateTime current = LocalDateTime.now();
        //  截止时间点
        Integer endTime = Integer.valueOf(setting.getEndTime().replace(":", ""));
        //  截止周几或日期
        Integer endDay = null;
        if (setting.getEndMark() != null)
            endDay = Integer.valueOf(setting.getEndMark());
        LocalDate reportTime = null;
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                if (setting.getStartType() == 0 && setting.getEndType() == 0)
                    reportTime = current.toLocalDate();
                else if (setting.getStartType() == 1 && setting.getEndType() == 1)
                    reportTime = current.minusDays(1L).toLocalDate();
                else {
                    //  取时间点进行比较
                    Integer currentTime = current.getHour() * 100 + current.getMinute();
                    if (currentTime > endTime)
                        reportTime = current.toLocalDate();
                    else
                        reportTime = current.minusDays(1L).toLocalDate();
                }
                break;
            case WEEK:
                if (setting.getStartType() == 0 && setting.getEndType() == 0)
                    reportTime = firstOfWeek(current.toLocalDate());
                else if (setting.getStartType() == 1 && setting.getEndType() == 1)
                    reportTime = firstOfWeek(current.minusWeeks(1L).toLocalDate());
                else {
                    //  取周几进行比较 dayOfWeek
                    Integer currentDay = current.getDayOfWeek().getValue();
                    if (currentDay > endDay)
                        reportTime = firstOfWeek(current.toLocalDate());
                    else if (currentDay < endDay)
                        reportTime = firstOfWeek(current.minusWeeks(1L).toLocalDate());
                    else {
                        Integer currentTime = current.getHour() * 100 + current.getMinute();
                        if (currentTime > endTime)
                            reportTime = firstOfWeek(current.toLocalDate());
                        else
                            reportTime = firstOfWeek(current.minusWeeks(1L).toLocalDate());
                    }
                }
                break;
            case MONTH:
                if (setting.getStartType() == 0 && setting.getEndType() == 0)
                    reportTime = firstOfMonth(current.toLocalDate());
                else if (setting.getStartType() == 1 && setting.getEndType() == 1)
                    reportTime = firstOfMonth(current.minusMonths(1L).toLocalDate());
                else {
                    //  取日期进行比较 dayOfMonth
                    Integer currentDay = current.getDayOfMonth();
                    if (currentDay > endDay)
                        reportTime = firstOfMonth(current.toLocalDate());
                    else if (currentDay < endDay)
                        reportTime = firstOfMonth(current.minusMonths(1L).toLocalDate());
                    else {
                        Integer currentTime = current.getHour() * 100 + current.getMinute();
                        if (currentTime > endTime)
                            reportTime = firstOfMonth(current.toLocalDate());
                        else
                            reportTime = firstOfMonth(current.minusMonths(1L).toLocalDate());
                    }
                }
                break;
        }
        return reportTime;
    }

    /**
     * 获取本周起始日
     */
    private static LocalDate firstOfWeek(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }

    /**
     * 获取本月1号
     */
    private static LocalDate firstOfMonth(LocalDate date) {
        return date.minusDays(date.getDayOfMonth() - 1);
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
    }

    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String s = "2019-02-25 09:30:01";
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(s);
        ZoneId zone = ZoneId.systemDefault();

        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("M月d日");
        Long reportTime = LocalDateTime.from(accessor).atZone(zone).toInstant().toEpochMilli();
        LocalDateTime time1 = getSettingTime(reportTime, WorkReportType.DAY.getCode(), WorkReportType.DAY.getCode(), null, "15:30");
        LocalDateTime time2 = getSettingTime(reportTime, WorkReportType.DAY.getCode(), WorkReportType.WEEK.getCode(), null, "16:30");
        LocalDateTime time3 = getSettingTime(reportTime, WorkReportType.WEEK.getCode(), WorkReportType.DAY.getCode(), "4", "12:30");
        LocalDateTime time4 = getSettingTime(reportTime, WorkReportType.WEEK.getCode(), WorkReportType.WEEK.getCode(), "5", "13:30");
        LocalDateTime time5 = getSettingTime(reportTime, WorkReportType.MONTH.getCode(), WorkReportType.DAY.getCode(), "11", "11:30");
        LocalDateTime time6 = getSettingTime(reportTime, WorkReportType.MONTH.getCode(), WorkReportType.WEEK.getCode(), "12", "12:30");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(time1));
        System.out.println(formatter.format(time2));
        System.out.println(time1.isBefore(time2));

        System.out.println(formatter.format(time3));
        System.out.println(formatter.format(time4));
        System.out.println(formatter.format(time5));
        System.out.println(formatter.format(time6));

        System.out.println(dayFormat.format(time1));

        System.out.println(displayReportTime(WorkReportType.DAY.getCode(), java.sql.Date.valueOf(time1.toLocalDate())));
        System.out.println(displayReportTime(WorkReportType.WEEK.getCode(), java.sql.Date.valueOf(time1.toLocalDate())));
        System.out.println(displayReportTime(WorkReportType.MONTH.getCode(), java.sql.Date.valueOf(time1.toLocalDate())));

    }*/
}
