package com.everhomes.rest.techpark.punch;

public interface PunchServiceErrorCode {
    static final String SCOPE = "punch";

    static final int ERROR_QUERY_YEAR_ERROR = 10002;  //查询年份出错
    static final int ERROR_PUNCH_RULE = 10003;  //打卡规则有问题
    static final int ERROR_PUNCH_ADD_DAYLOG = 10004;  //计算打卡日志有问题
    static final int ERROR_PUNCH_REFRESH_DAYLOG = 10005;  //计算打卡日志有问题
    

    static final int ERROR_NAME_REPEAT = 12000;  //名称重复
    static final int ERROR_RULE_USING = 12001;  //规则被使用


    static final int ERROR_USER_NOT_IN_PUNCHAREA = 10001;  //用户不在打卡区域内
    static final int ERROR_NOT_IN_AREA_AND_WIFI = 10008;  //用户不在打卡区域内也不在wifi范围内
    static final int ERROR_GEOPOINT_NULL = 13000;  //没有定位信息,请定位后再此请求
    static final int ERROR_WIFI_NULL = 10006;  //用户wifi位空
    static final int ERROR_WIFI_WRONG = 10007;  //用户wifi地址错误
    static final int ERROR_PUNCH_TYPE = 10009;  //用户打卡类型错误,重新请求
    static final int ERROR_ENTERPRISE_DIDNOT_SETTING = 10010;  //公司没有设置打卡规则


    static final int ERROR_PUNCH_TOKEN_TIMEOUT = 10100;  //token过期了,返回首页

    
}
