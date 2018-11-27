package com.everhomes.workReport;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.workReport.ReportValiditySettingDTO;
import com.everhomes.rest.workReport.WorkReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

@Component
public class WorkReportTimeServiceImpl implements WorkReportTimeService {
    private static final String TIME_DISPLAY_FORMAT_SCOPE = "time_display_format";
    private static final String LOCALE = "zh_CN";
    @Autowired
    private LocaleStringService localeStringService;

    /**
     * 根据汇报时间与设定得到具体时间
     */
    @Override
    public LocalDateTime getSettingTime(Byte rType, Long rTime, Byte sType, String sMask, String sTime) {
        LocalDate reportDate = toLocalDateTime(rTime).toLocalDate();
        LocalTime sLocalTime = LocalTime.parse(sTime + ":" + "00");
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                if (sType == 1)
                    reportDate = reportDate.plusDays(1L);
                break;
            case WEEK:
                if (sType == 1)
                    reportDate = reportDate.plusWeeks(1L);
                reportDate = reportDate.with(DayOfWeek.of(Integer.valueOf(sMask)));
                break;
            case MONTH:
                if (sType == 1)
                    reportDate = reportDate.plusMonths(1L);
                reportDate = reportDate.withDayOfMonth(Math.min(reportDate.lengthOfMonth(), Integer.valueOf(sMask)));
                break;
        }
        return LocalDateTime.of(reportDate, sLocalTime);
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
     * 截止时间显示格式
     * time=当前时间的日期：今天 hh:mm；
     * time=当前时间的日期=1：明天 hh:mm；
     * time时间的周=当前时间的周：本周X hh:mm；
     * time时间的周-当前时间的周=1：下周X hh:mm；
     * 其余： m月d日 hh:mm；
     */
    @Override
    public String formatTime(LocalDateTime time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M月d日 HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String timeStr = timeFormatter.format(time);
        LocalDate today = LocalDate.now();
        // 今天 HH:mm
        if (today.compareTo(time.toLocalDate()) == 0) {
            String tag = localeStringService.getLocalizedString(TIME_DISPLAY_FORMAT_SCOPE, "1", LOCALE, "今天");
            return String.format("%s %s", tag, timeStr);
        }
        // 明天 HH:mm
        if (today.plusDays(1).compareTo(time.toLocalDate()) == 0) {
            String tag = localeStringService.getLocalizedString(TIME_DISPLAY_FORMAT_SCOPE, "2", LOCALE, "明天");
            return String.format("%s %s", tag, timeStr);
        }
        // 今天是一年中的第几周
        int todayOfWeek = today.get(WeekFields.ISO.weekOfWeekBasedYear());
        // time对应的是一年中的第几周
        int timeOfWeek = time.toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear());

        // 本周x HH:mm
        if (todayOfWeek == timeOfWeek) {
            String tag = localeStringService.getLocalizedString(TIME_DISPLAY_FORMAT_SCOPE, String.valueOf(10 + time.getDayOfWeek().getValue()), LOCALE, "");
            return String.format("%s %s", tag, timeStr);
        }
        // 下周x HH:mm
        if (todayOfWeek + 1 == timeOfWeek) {
            String tag = localeStringService.getLocalizedString(TIME_DISPLAY_FORMAT_SCOPE, String.valueOf(100 + time.getDayOfWeek().getValue()), LOCALE, "");
            return String.format("%s %s", tag, timeStr);
        }
        return dateTimeFormatter.format(time);
    }

    /**
     * 当前时间点(HH:mm)
     */
    @Override
    public String currenHHmmTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(LocalDateTime.now());
    }

}
