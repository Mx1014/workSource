package com.everhomes.rest.workReport;

public interface WorkReportErrorCode {

    String SCOPE = "WORK_REPORT";

    int ERROR_WORK_REPORT_ABNORMAL = 10001; //  工作汇报异常

    int ERROR_REPORT_VAL_NOT_FOUND = 10002; //  工作汇报单不存在

    int ERROR_NO_READ_PERMISSIONS = 10003;  //  没有阅读权限.

    int ERROR_WRONG_POST_TIME = 10004;      //  提交时间未在规定范围内

    int ERROR_WRONG_POST_TIME_V2 = 10005;     //  提交时间未在规定范围内(返回空白页)
}
