package com.everhomes.workReport;

import com.everhomes.rest.workReport.ReportValiditySettingDTO;
import com.everhomes.rest.workReport.WorkReportType;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class WorkReportUtil {

    /**
     * 根据汇报时间与设定得到具体时间
     */
    public static LocalDateTime getSettingTime(Byte rType, Long rTime, Byte sType, String sMask, String sTime) {
        LocalDateTime reportTime = toLocalDateTime(rTime);
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
     * 将getTime()转化为 LocalDateTime
     */
    public static java.sql.Date toSqlDate(Long time) {
        LocalDateTime temp = toLocalDateTime(time);
        return java.sql.Date.valueOf(temp.toLocalDate());
    }

    /**
     * 根据汇报类型显示日期
     */
    public static String displayReportTime(Byte rType, Long rTime) {
        DateTimeFormatter mdFormat = DateTimeFormatter.ofPattern("M月d日");
        DateTimeFormatter ymFormat = DateTimeFormatter.ofPattern("yyyy年M月");

        LocalDate temp = toLocalDateTime(rTime).toLocalDate();
//        LocalDate temp = rTime.toLocalDate();
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

    public static Timestamp getReportTime(Byte rType, ReportValiditySettingDTO setting) {
        LocalDateTime current = LocalDateTime.now();
        //  截止时间点
        Integer endTime = Integer.valueOf(setting.getEndTime().replace(":", ""));
        //  截止周几或日期
        Integer endDay = null;
        if (setting.getEndMark() != null)
            endDay = Integer.valueOf(setting.getEndMark());
        LocalDate temp = null;
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                if (setting.getStartType() == 0 && setting.getEndType() == 0)
                    temp = current.toLocalDate();
                else if (setting.getStartType() == 1 && setting.getEndType() == 1)
                    temp = current.minusDays(1L).toLocalDate();
                else {
                    //  取时间点进行比较
                    Integer currentTime = current.getHour() * 100 + current.getMinute();
                    if (currentTime > endTime)
                        temp = current.toLocalDate();
                    else
                        temp = current.minusDays(1L).toLocalDate();
                }
                break;
            case WEEK:
                if (setting.getStartType() == 0 && setting.getEndType() == 0)
                    temp = firstOfWeek(current.toLocalDate());
                else if (setting.getStartType() == 1 && setting.getEndType() == 1)
                    temp = firstOfWeek(current.minusWeeks(1L).toLocalDate());
                else {
                    //  取周几进行比较 dayOfWeek
                    Integer currentDay = current.getDayOfWeek().getValue();
                    if (currentDay > endDay)
                        temp = firstOfWeek(current.toLocalDate());
                    else if (currentDay < endDay)
                        temp = firstOfWeek(current.minusWeeks(1L).toLocalDate());
                    else {
                        Integer currentTime = current.getHour() * 100 + current.getMinute();
                        if (currentTime > endTime)
                            temp = firstOfWeek(current.toLocalDate());
                        else
                            temp = firstOfWeek(current.minusWeeks(1L).toLocalDate());
                    }
                }
                break;
            case MONTH:
                if (setting.getStartType() == 0 && setting.getEndType() == 0)
                    temp = firstOfMonth(current.toLocalDate());
                else if (setting.getStartType() == 1 && setting.getEndType() == 1)
                    temp = firstOfMonth(current.minusMonths(1L).toLocalDate());
                else {
                    //  取日期进行比较 dayOfMonth
                    Integer currentDay = current.getDayOfMonth();
                    if (currentDay > endDay)
                        temp = firstOfMonth(current.toLocalDate());
                    else if (currentDay < endDay)
                        temp = firstOfMonth(current.minusMonths(1L).toLocalDate());
                    else {
                        Integer currentTime = current.getHour() * 100 + current.getMinute();
                        if (currentTime > endTime)
                            temp = firstOfMonth(current.toLocalDate());
                        else
                            temp = firstOfMonth(current.minusMonths(1L).toLocalDate());
                    }
                }
                break;
        }
        return Timestamp.valueOf(temp.atTime(0,0,0));
    }

    /**
     * 将getTime()转化为 LocalDateTime
     */
    private static LocalDateTime toLocalDateTime(Long time) {
        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
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

    /**
     * 格式化时间
     */
    public static String formatTime(LocalDateTime endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日 HH:mm");
        return formatter.format(endTime);
    }
}
