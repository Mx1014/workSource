package com.everhomes.rest.equipment;

public interface EquipmentServiceErrorCode {

	static final String SCOPE = "equipment";
    static final int ERROR_EQUIPMENT_NOT_EXIST = 10001;//设备不存在
    static final int ERROR_EQUIPMENT_NOT_SET_LOCATION = 10002;//设备没有设置经纬度
    static final int ERROR_USER_NOT_IN_AREA = 10003;//不在设备附近
    static final int ERROR_STANDARD_NOT_EXIST = 10004;//设备标准不存在
    static final int ERROR_STANDARD_ALREADY_DELETED = 10005;//设备标准已失效
    static final int ERROR_EQUIPMENT_LOCATION_CANNOT_MODIFY = 10006;//设备经纬度不能修改
    static final int ERROR_EQUIPMENT_STATUS_CANNOT_SET_IN_MAINTENANCE = 10007;//设备状态后台不能设为维修中
    static final int ERROR_EQUIPMENT_ALREADY_DELETED = 10008;//设备已失效
    static final int ERROR_ACCESSORY_NOT_EXIST = 10009;//备品备件不存在
    static final int ERROR_ACCESSORY_ALREADY_DELETED = 10010;//备品备件已失效
    static final int ERROR_EQUIPMENT_REVIEW_STATUS_ONLY_INACTIVE_CAN_DELETE = 10011;//只有已失效的设备-标准关联关系可以删除
    static final int ERROR_EQUIPMENT_WAITING_FOR_APPROVAL_CAN_REVIEW = 10012;//只有待审核的设备-标准关联关系可以审核
    static final int ERROR_EQUIPMENT_TASK_NOT_EXIST = 10013;//任务不存在
    static final int ERROR_EQUIPMENT_TASK_NOT_WAITING_EXECUTE_OR_IN_MAINTENANCE = 10014;//只有待执行和维修中的任务可以上报

    static final int EQUIPMENT_PARAMETER_RECORD = 10015;//设备参数记录
    
    static final int ERROR_CREATE_EXCEL = 10016;  //生成excel信息有问题
    static final int ERROR_DOWNLOAD_EXCEL = 10017;  //下载excel信息有问题
    static final int ERROR_EQUIPMENT_TASK_INACTIVE = 10018;//任务已失效
    
    static final int ERROR_EQUIPMENT_TASK_QRCODE = 10019;//二维码和任务设备不对应
    
    static final int ERROR_EQUIPMENT_CATEGORY_NULL = 10020;//设备类型不存在
    static final int ERROR_CATEGORY_EXIST = 10021;//设备类型已存在
    static final int ERROR_TEMPLATE_NOT_EXIST = 10022;//模板不存在
    static final int ERROR_EQUIPMENT_STANDARD_MAP_NOT_EXIST = 10023;//设备-标准关联不存在
    static final int ERROR_EQUIPMENT_TASK_CLOSE = 10024;//任务已关闭
}
