package com.everhomes.rest.equipment;

public interface EquipmentServiceErrorCode {

	 String SCOPE = "equipment";
     /**
      * 设备不存在
      */
     int ERROR_EQUIPMENT_NOT_EXIST = 10001;
     /**
      * 设备没有设置经纬度
      */
     int ERROR_EQUIPMENT_NOT_SET_LOCATION = 10002;
     /**
      * 不在设备附近
      */
     int ERROR_USER_NOT_IN_AREA = 10003;
     /**
      * 设备标准不存在
      */
     int ERROR_STANDARD_NOT_EXIST = 10004;
     /**
      * 设备标准已失效
      */
     int ERROR_STANDARD_ALREADY_DELETED = 10005;
     /**
      * 设备经纬度不能修改
      */
     int ERROR_EQUIPMENT_LOCATION_CANNOT_MODIFY = 10006;
     /**
      * 设备状态后台不能设为维修中
      */
     int ERROR_EQUIPMENT_STATUS_CANNOT_SET_IN_MAINTENANCE = 10007;
     /**
      * 设备已失效
      */
     int ERROR_EQUIPMENT_ALREADY_DELETED = 10008;
     /**
      * 备品备件不存在
      */
     int ERROR_ACCESSORY_NOT_EXIST = 10009;
     /**
      * 备品备件已失效
      */
     int ERROR_ACCESSORY_ALREADY_DELETED = 10010;
     /**
      * 只有已失效的设备-标准关联关系可以删除
      */
     int ERROR_EQUIPMENT_REVIEW_STATUS_ONLY_INACTIVE_CAN_DELETE = 10011;
     /**
      * 只有待审核的设备-标准关联关系可以审核
      */
     int ERROR_EQUIPMENT_WAITING_FOR_APPROVAL_CAN_REVIEW = 10012;
     /**
      * 任务不存在
      */
     int ERROR_EQUIPMENT_TASK_NOT_EXIST = 10013;
     /**
      * 只有待执行和维修中的任务可以上报
      */
     int ERROR_EQUIPMENT_TASK_NOT_WAITING_EXECUTE_OR_IN_MAINTENANCE = 10014;
     /**
      * 设备参数记录
      */
     int EQUIPMENT_PARAMETER_RECORD = 10015;
     /**
      * 生成excel信息有问题
      */
     int ERROR_CREATE_EXCEL = 10016;
     /**
      * 下载excel信息有问题
      */
     int ERROR_DOWNLOAD_EXCEL = 10017;
     /**
      * 任务已失效
      */
     int ERROR_EQUIPMENT_TASK_INACTIVE = 10018;
     /**
      * 二维码和任务设备不对应
      */
     int ERROR_EQUIPMENT_TASK_QRCODE = 10019;
     /**
      * 设备类型不存在
      */
     int ERROR_EQUIPMENT_CATEGORY_NULL = 10020;
     /**
      * 设备类型已存在
      */
     int ERROR_CATEGORY_EXIST = 10021;
     /**
      * 模板不存在
      */
     int ERROR_TEMPLATE_NOT_EXIST = 10022;
     /**
      *设备标准关联关系不存在
      */
     int ERROR_EQUIPMENT_STANDARD_MAP_NOT_EXIST = 10023;
     /**
      * 任务已关闭
      */
     int ERROR_EQUIPMENT_TASK_CLOSE = 10024;
     /**
      * 计划已经删除
      */
     int ERROR_PLAN_ALREADY_DELETED = 10025;
     /**
      * 计划已经删除
      */
     int ERROR_EQUIPMENT_PLAN_NOT_EXIST = 10026;
     /**
      * 报修类型未建立
      */
     int ERROR_EQUIPMENT_REPAIR_CATEGORY_NOT_EXIST = 10027;
     /**
      * 报修类型未建立
      */
     int ERROR_EQUIPMENT_TASK_SYNC_ERROR = 10028;
}
