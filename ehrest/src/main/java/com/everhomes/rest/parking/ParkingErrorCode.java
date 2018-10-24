package com.everhomes.rest.parking;

public interface ParkingErrorCode {

    String SCOPE = "parking";
    int ERROR_PLATE_LENGTH = 10001;  //车牌号长度错误

    int ERROR_PLATE_EXIST = 10002;  //车牌已有月卡
    int ERROR_PLATE_APPLIED = 10003;  //车牌已申请
    int ERROR_RECHARGE_ORDER = 10005;  //网络通讯失败，缴费出错
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

    int ERROR_PLATE_REPEAT_ADD = 10021;  //车牌不可重复添加！
    

    int RECHARE_TIME_OUT = 10031;  //获取充值结果超时

    int ERROR_PARKING_TYPE_NOT_FOUND = 10032;  //未查询到月卡类型信息

    int CAR_ENTRY_INFO_NOT_FOUND = 10033;
    
    int PARAMTER_LOSE = 10034;

    int PARAMTER_UNUSUAL = 10035;

    int ORDER_UNUSUAL = 10036;

    int FAIL_EXPORT_FILE = 10037;

    int NO_WORK_FLOW_ENABLE = 10038;

    int OBJECT_NO_FOUND = 10039;


    //------------------------vip 车位----------------------------
    int ERROR_RAISE_PARKING_LOCK = 10022; // 升起车锁失败
    int ERROR_DOWN_PARKING_LOCK = 10023; //  降下车锁失败
    int ERROR_REPEAT_SPACE_NO = 10124; // 重复车位ID
    int ERROR_REPEAT_LOCK_ID = 10125; //  重复车锁ID
    int ERROR_UNCONN_LOCK_ID = 10126; //  错误的车锁，没有上线，或者id就是错误的
    int ERROR_GET_TOKEN = 11000; //  获取token失败
    int ERROR_GET_RESULT = 11010; //  返回参数失败
    int ERROR_INVITE_FAILD = 12000; //  发起访邀失败
    int ERROR_GENERATE_ORDER_NO= 13000; //  生成订单编号失败
    int ERROR_REPEATE_ACCOUNT= 14000; //  重复账号
    int ERROR_NO_PAYEE_ACCOUNT= 14001; //  未设置收款方账号
    int ERROR_CREATE_USER_ACCOUNT= 14002; //  创建个人付款账户失败
    int ERROR_SELF_DEINFE= 69696; //  自定义异常描述
    int ERROR_SPACE_IN_USE= 1862101; //  车位使用中
    int ERROR_HUB_IN_USE= 1862102; //  HUB使用中


    int ERROR_SELF_DEFINE= 99999; //  自定义异常

    // ------------------ 车辆放行 ----------------------
    String SCOPE_CLEARANCE = "parking.clearance";

    int ERROR_DELETE_PARKING_CLEARANCE_OPERATOR = 10001;  //删除用户失败
    int ERROR_NO_WORK_FLOW_ENABLED = 10002;  //没有启用的工作流
    int ERROR_USER_ALREADY_IN_DATABASE = 10003;  //用户已添加
    
    
    
    // ------------------ 捷顺科技对接 ----------------------
    String SCOPE_JIESHUN = "parking.jieshun";
    int ERROR_FETCH_TOKEN = 10001;  //token获取失败
    int ERROR_MAKE_MD5 = 10002;  //生成摘要失败
    int ERROR_CURRENT_RECHARGE_TYPE_NOT_SUPPORTED = 10003;  //当前充值类型不支持
    int ERROR_RECHARGE_BY_DAY_NOT_SUPPORTED = 10004;  //不支持按天计算yuqi金额
    int ERROR_CARD_INFO_NOT_FOUND = 10005;  //未找到月卡信息
    int ERROR_CARD_TYPES_NOT_FOUND = 10006;  //未获取到费率信息
    int ERROR_CARD_FEE_ITEM_NOT_FOUND = 10007;  //未获取到月卡对应的费率信息
}
