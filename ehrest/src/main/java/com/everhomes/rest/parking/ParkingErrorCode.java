package com.everhomes.rest.parking;

public interface ParkingErrorCode {

	static final String SCOPE = "parking";
    static final int ERROR_PLATE_LENGTH = 10001;  //车牌号长度错误

    static final int ERROR_PLATE_EXIST = 10002;  //车牌已有月卡
    static final int ERROR_PLATE_APPLIED = 10003;  //车牌已申请
    static final int ERROR_PLATE_NULL = 10005;  //车牌号为空
    static final int ERROR_PLATE_APPLIED_SERVER = 10004;  //服务器忙

    static final int ERROR_TEMP_FEE = 10010;  //费用已过期

    static final int ERROR_MAX_REQUEST_NUM = 10011;  //抱歉，你申请的月卡数量已到上限

    // ------------------ 车辆放行 ----------------------
    String SCOPE_CLEARANCE = "parking.clearance";

    int ERROR_DELETE_PARKING_CLEARANCE_OPERATOR = 10001;  //删除用户失败
    int ERROR_NO_WORK_FLOW_ENABLED = 10002;  //没有启用的工作流
    int ERROR_USER_ALREADY_IN_DATABASE = 10003;  //用户已添加
}
