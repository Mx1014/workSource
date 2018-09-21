package com.everhomes.rest.workReport;

public interface WorkReportErrorCode {

    String SCOPE = "WORK_REPORT";

    int ERROR_WORK_REPORT_ABNORMAL = 10001; //  工作汇报异常

    int ERROR_REPORT_VAL_NOT_FOUND = 10002; //  工作汇报单不存在

    int ERROR_NO_READ_PERMISSIONS = 10003;  //  没有阅读权限.

    int ERROR_WRONG_POST_TIME = 10004;      //  已超出截止时间，请自行保存汇报内容，退出界面后将清空内容

    int ERROR_WRONG_POST_TIME_V2 = 10005;     //  已超过截止时间，无法提交。下期汇报将于${开始提交时间}开始提交。
}
