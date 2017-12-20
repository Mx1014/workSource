package com.everhomes.workReport;

public interface WorkReportErrorCode {

    String WORK_REPORT = "WORK_REPORT";

    int ERROR_WORK_REPORT_ABNORMAL = 10001; //  工作汇报异常

    int ERROR_REPORT_VAL_NOT_FOUND = 10002; //  工作汇报单不存在

    int ERROR_NO_READ_PERMISSIONS = 10003;  //  没有阅读权限
}
