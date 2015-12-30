package com.everhomes.rest.techpark.punch;

public interface PunchServiceErrorCode {
    static final String SCOPE = "punch";

    static final int ERROR_USER_NOT_IN_PUNCHAREA = 10001;  //用户不在打卡区域内
    static final int ERROR_QUERY_YEAR_ERROR = 10002;  //查询年份出错
    static final int ERROR_PUNCH_RULE = 10003;  //打卡规则有问题
    static final int ERROR_PUNCH_ADD_DAYLOG = 10004;  //计算打卡日志有问题
    static final int ERROR_PUNCH_REFRESH_DAYLOG = 10005;  //计算打卡日志有问题
    
    static final int ERROR_ENTERPRISE_DIDNOT_SETTING = 10010;  //公司没有设置打卡规则
    
}
