package com.everhomes.techpark.punch;

public interface PunchServiceErrorCode {
    static final String SCOPE = "punch";

    static final int ERROR_USER_NOT_IN_PUNCHAREA = 10001;  //用户不在打卡区域内
    static final int ERROR_QUERY_YEAR_ERROR = 10002;  //查询年份出错
    static final int ERROR_PUNCH_RULE = 10003;  //打卡规则有问题
}
