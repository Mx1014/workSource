package com.everhomes.workReport;

import com.everhomes.rest.workReport.ReportValiditySettingDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface WorkReportTimeService {

    LocalDateTime getSettingTime(Byte rType, Long rTime, Byte sType, String sMask, String sTime);

    java.sql.Date toSqlDate(Long time);

    String displayReportTime(Byte rType, Long rTime);

    Timestamp getReportTime(Byte rType, LocalDateTime time, ReportValiditySettingDTO setting);

    String formatTime(LocalDateTime time);

    public  String currenHHmmTime();
}
