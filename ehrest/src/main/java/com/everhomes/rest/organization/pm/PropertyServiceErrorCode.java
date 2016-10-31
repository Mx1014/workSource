// @formatter:off
package com.everhomes.rest.organization.pm;

public interface PropertyServiceErrorCode {
    String SCOPE = "pm";

    int ERROR_INVALID_FAMILY = 10001;
    int ERROR_INVALID_ADDRESS = 10002;
    int ERROR_INVALID_BILL = 10003;

    int ERROR_INVALID_TOPIC = 11001;
    int ERROR_INVALID_COMMENT = 12002;
    int ERROR_INVALID_TASK = 12003;
    int ERROR_INVALID_TASK_STATUS = 12004;
    int ERROR_OWNER_COMMUNITY = 13001;

    int ERROR_OWNER_EXIST = 14001;// 业主已经存在
    int ERROR_OWNER_NOT_EXIST = 14002;// 业主不存在
    int ERROR_OWNER_CONTACT_TOKEN_REPEAT = 14003;// 业主手机重复

    int ERROR_IMPORT = 15001;// 导入失败
    int ERROR_IMPORT_NO_DATA = 15002;// 导入失败, 没有解析到数据
    int ERROR_IMPORT_ADDRESS_ERROR = 15003;// 导入失败, 楼栋门牌信息错误
    int ERROR_IMPORT_BIRTHDAY_ERROR = 15004;// 导入失败, 生日信息错误

    int ERROR_OWNER_CAR_EXIST = 16001;// 车辆已存在
    int ERROR_OWNER_CAR_NOT_EXIST = 16002;// 车辆不存在
    int ERROR_OWNER_CAR_PLATE_NUMBER_REPEAT = 16003;// 车牌号码重复
    int ERROR_OWNER_CAR_USER_EXIST = 16004;// 用户已经在车辆的使用者列表中

    int ERROR_OWNER_ADDRESS_EXIST = 17001;// 用户已经在楼栋门牌中
    int ERROR_OWNER_ADDRESS_ALREADY_IS_THIS_STATUS = 17002;// 用户楼栋门牌已经处于某种状态

    int ERROR_OWNER_ADDRESS_ALREADY_INACTIVE = 18001;// 该记录已经处于未认证状态
}
