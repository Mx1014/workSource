package com.everhomes.workReport;

import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.workReport.WorkReportType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class WorkReportUtil {

    public static LocalDateTime convertSettingToTime(Long rTime, Byte rType, Byte sType, String sMask, String sTime) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime reportTime = new Timestamp(rTime).toLocalDateTime();
        int year, month, day;
        String temp = null;
        switch (WorkReportType.fromCode(rType)) {
            case DAY:
                if (sType == 1)
                    reportTime = reportTime.plusDays(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                day = reportTime.getDayOfMonth();
                temp = year + "-" + month + "-" + day + " " + sTime + ":" + "00";
                break;
            case WEEK:
                if(sType == 1)
                    reportTime = reportTime.plusWeeks(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                break;
            case MONTH:
                if (sType == 1)
                    reportTime = reportTime.plusMonths(1L);
                year = reportTime.getYear();
                month = reportTime.getMonth().getValue();
                temp = year + "-" + month + "-" + sMask + " " + sTime + ":" + "00";
        }

        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(temp);
        return LocalDateTime.from(accessor);
    }

    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String s = "2018-02-31 09:30:01";
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(s);
        LocalDateTime time = LocalDateTime.from(accessor);
        System.out.println(formatter.format(time));

        Byte t = TrueOrFalseFlag.TRUE.getCode();
        System.out.println(t == 1);
    }
}
