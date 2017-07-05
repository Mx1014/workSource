package com.everhomes.rest.parking;

public interface ParkingErrorCode {

	String SCOPE = "parking";
    int ERROR_PLATE_LENGTH = 10001;  //车牌号长度错误

    int ERROR_PLATE_EXIST = 10002;  //车牌已有月卡
    int ERROR_PLATE_APPLIED = 10003;  //车牌已申请
    int ERROR_RECHARGE_ORDER = 10005;  //网络通讯失败，充值出错
    int ERROR_REQUEST_SERVER = 10004;  //服务器通讯异常，请稍后再试！

    int ERROR_TEMP_FEE = 10010;  //费用已过期

    int ERROR_MAX_REQUEST_NUM = 10011;  //抱歉，你申请的月卡数量已到上限
    
    int ERROR_ISSUE_CARD_SURPLUS_NUM = 10012;  //发放月卡资格数量不可大于当前剩余月卡数

    int ERROR_ISSUE_CARD_QUEQUE_NUM = 10013;  //发放月卡资格数量不可大于当前排队数

    int ERROR_ISSUE_CARD = 10014;  //操作失败，当前无剩余月卡
    
    int ERROR_PROCESS_CARD_SURPLUS_NUM = 10015;  //发放月卡数量不可大于当前剩余月卡数
    
    int ERROR_PROCESS_CARD_QUEQUE_NUM = 10016;  //发放月卡数量不可大于当前待办理月卡数

    int ERROR_FLOW_NODE_PARAM = 10017;  //工作流节点参数错误

    int ERROR_RECHARGE_MONTH_COUNT = 10018;  //过期充值月数小于1

    int NOT_SUPPORT_OLD_VERSION = 10019;  //版本过低，请更新App至最新版本

    int NOT_SUPPORT_APP_RECHARGE = 10020;  //不支持APP缴费

    // ------------------ 车辆放行 ----------------------
    String SCOPE_CLEARANCE = "parking.clearance";

    int ERROR_DELETE_PARKING_CLEARANCE_OPERATOR = 10001;  //删除用户失败
    int ERROR_NO_WORK_FLOW_ENABLED = 10002;  //没有启用的工作流
    int ERROR_USER_ALREADY_IN_DATABASE = 10003;  //用户已添加
}
