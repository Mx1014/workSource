package com.everhomes.workReport;

import com.everhomes.rest.workReport.ReportValiditySettingDTO;
import com.everhomes.rest.workReport.WorkReportType;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Component
public class WorkReportTimeServiceImpl implements WorkReportTimeService{

    /**
     * 根据汇报时间与设定得到具体时间
     */
    @Override
    public LocalDateTime getSettingTime(Byte rType, Long rTime, Byte sType, String sMask, String sTime) {
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
    @Override
    public java.sql.Date toSqlDate(Long time) {
        LocalDateTime temp = toLocalDateTime(time);
        return java.sql.Date.valueOf(temp.toLocalDate());
    }

    /**
     * 根据汇报类型显示日期
     */
    @Override
    public String displayReportTime(Byte rType, Long rTime) {
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
    1.startType=endType=0 -> 根据时间点判断
    2.startType=endTpye=1 -> 根据时间点判断
    3.startType=0,endType=1
        3-1.currentTime>endTime -> Today
        3-2.currentTime<=endTime -> Yesterday

    周报规则
    1.startType=endType=0 -> 根据时间点判断
    2.startType=endTpye=1 -> 根据时间点判断
    3.startType=0,endType=1
        3-1.currentDay>endMask -> ThisWeek
        3-2.cureentDay<endMask -> LastWeek
        3-3.currentDay=endMask
            3-3-1.currentTime>endTime -> ThisWeek
            3-3-2.currentTime<=endTime -> LastWeek

    月报规则
    1.startType=endType=0 -> 根据时间点判断
    2.startType=endTpye=1 -> 根据时间点判断
    3.startType=0,endType=1
        3-1.currentDay>endMask -> ThisMonth
        3-2.cureentDay<endMask -> LastMonth
        3-3.currentDay=endMask
            3-3-1.currentTime>endTime -> ThisMonth
            3-3-2.currentTime<=endTime -> LastMonth*/
    /* ------------------------------------------- 汇报时间获取规则总结end ------------------------------------------- */
    @Override
    public Timestamp getReportTime(Byte rType, LocalDateTime time, ReportValiditySettingDTO setting) {
        LocalDate temp = null;
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                temp = getDayReportTime(time, setting);
                break;
            case WEEK:
                temp = getWeekReportTime(time, setting);
                break;
            case MONTH:
                temp = getMonthReportTime(time, setting);
                break;
        }
        return Timestamp.valueOf(temp.atTime(0, 0, 0));
    }

    private LocalDate getDayReportTime(LocalDateTime current, ReportValiditySettingDTO setting) {
        LocalDate temp;
        Integer currentTime = current.getHour() * 100 + current.getMinute();
        Integer endTime = Integer.valueOf(setting.getEndTime().replace(":", "")); // 截止时间点
        if (setting.getStartType() == 0 && setting.getEndType() == 0) {
            if (currentTime <= endTime)
                temp = current.toLocalDate();
            else
                temp = current.toLocalDate().plusDays(1);
        } else if (setting.getStartType() == 1 && setting.getEndType() == 1) {
            if (currentTime <= endTime)
                temp = current.toLocalDate().minusDays(1L);
            else
                temp = current.toLocalDate();
        } else {
            //  取时间点进行比较
            if (currentTime <= endTime)
                temp = current.toLocalDate().minusDays(1L);
            else
                temp = current.toLocalDate();
        }
        return temp;
    }

    private LocalDate getWeekReportTime(LocalDateTime current, ReportValiditySettingDTO setting) {
        LocalDate temp;
        Integer currentTime = current.getHour() * 100 + current.getMinute();
        Integer endTime = Integer.valueOf(setting.getEndTime().replace(":", "")); // 截止时间点
        Integer currentDay = current.getDayOfWeek().getValue();
        Integer endDay = Integer.valueOf(setting.getEndMark()); // 截止周几或日期
        if (setting.getStartType() == 0 && setting.getEndType() == 0) {
            if (currentDay < endDay)
                temp = firstOfWeek(current.toLocalDate());
            else if (currentDay > endDay)
                temp = firstOfWeek(current.toLocalDate().plusWeeks(1));
            else {
                if (currentTime <= endTime)
                    temp = firstOfWeek(current.toLocalDate());
                else
                    temp = firstOfWeek(current.toLocalDate().plusWeeks(1));
            }
        } else if (setting.getStartType() == 1 && setting.getEndType() == 1) {
            if (currentDay < endDay)
                temp = firstOfWeek(current.toLocalDate().minusWeeks(1L));
            else if (currentDay > endDay)
                temp = firstOfWeek(current.toLocalDate());
            else {
                if (currentTime <= endTime)
                    temp = firstOfWeek(current.toLocalDate().minusWeeks(1));
                else
                    temp = firstOfWeek(current.toLocalDate());
            }
        } else {
            //  取周几进行比较 dayOfWeek
            if (currentDay > endDay)
                temp = firstOfWeek(current.toLocalDate());
            else if (currentDay < endDay)
                temp = firstOfWeek(current.toLocalDate().minusWeeks(1L));
            else {
                if (currentTime > endTime)
                    temp = firstOfWeek(current.toLocalDate());
                else
                    temp = firstOfWeek(current.toLocalDate().minusWeeks(1L));
            }
        }
        return temp;
    }

    private LocalDate getMonthReportTime(LocalDateTime current, ReportValiditySettingDTO setting) {
        LocalDate temp;
        Integer currentTime = current.getHour() * 100 + current.getMinute();
        Integer endTime = Integer.valueOf(setting.getEndTime().replace(":", "")); // 截止时间点
        Integer currentDay = current.getDayOfMonth();
        Integer endDay = Integer.valueOf(setting.getEndMark()); // 截止周几或日期
        if (setting.getStartType() == 0 && setting.getEndType() == 0) {
            if (currentDay < endDay)
                temp = firstOfMonth(current.toLocalDate());
            else if (currentDay > endDay)
                temp = firstOfMonth(current.toLocalDate().plusMonths(1));
            else {
                if (currentTime <= endTime)
                    temp = firstOfMonth(current.toLocalDate());
                else
                    temp = firstOfMonth(current.toLocalDate().plusMonths(1));
            }
        } else if (setting.getStartType() == 1 && setting.getEndType() == 1) {
            if (currentDay < endDay)
                temp = firstOfMonth(current.toLocalDate().minusMonths(1L));
            else if (currentDay > endDay)
                temp = firstOfMonth(current.toLocalDate());
            else {
                if (currentTime <= endTime)
                    temp = firstOfMonth(current.toLocalDate().minusMonths(1L));
                else
                    temp = firstOfMonth(current.toLocalDate());
            }
        } else {
            //  取日期进行比较 dayOfMonth
            if (currentDay > endDay)
                temp = firstOfMonth(current.toLocalDate());
            else if (currentDay < endDay)
                temp = firstOfMonth(current.minusMonths(1L).toLocalDate());
            else {
                if (currentTime > endTime)
                    temp = firstOfMonth(current.toLocalDate());
                else
                    temp = firstOfMonth(current.minusMonths(1L).toLocalDate());
            }
        }
        return temp;
    }

    /**
     * 将getTime()转化为 LocalDateTime
     */
    private LocalDateTime toLocalDateTime(Long time) {
        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 获取本周起始日
     */
    private LocalDate firstOfWeek(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }

    /**
     * 获取本月1号
     */
    private LocalDate firstOfMonth(LocalDate date) {
        return date.minusDays(date.getDayOfMonth() - 1);
    }

    /**
     * 格式化时间
     */
    @Override
    public String formatTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日 HH:mm");
        return formatter.format(time);
    }

    /**
     * 当前时间点(HH:mm)
     */
    @Override
    public String currenHHmmTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(LocalDateTime.now());
    }
}
