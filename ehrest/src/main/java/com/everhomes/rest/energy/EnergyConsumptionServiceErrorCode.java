package com.everhomes.rest.energy;

/**
 * Created by xq.tian on 2016/10/28.
 */
public interface EnergyConsumptionServiceErrorCode {

    String SCOPE = "energy";

    int ERR_METER_NOT_EXIST = 10001;// 表记不存在
    int ERR_METER_CATEGORY_NOT_EXIST = 10002;// 表记分类不存在
    int ERR_METER_FORMULA_NOT_EXIST = 10003;// 公式不存在
    int ERR_METER_FORMULA_ERROR = 10004;//公式不能计算
    int ERR_METER_READING_LOG_NOT_EXIST = 10005;// 读表记录不存在
    int ERR_METER_READING_LOG_BEFORE_TODAY = 10006;// 只允许删除今天的读表记录
    int ERR_CURR_READING_GREATER_THEN_MAX_READING = 10007;// 读数超过最大量程
    int ERR_FORMULA_HAS_BEEN_REFERENCE = 10008;//
    // 公式被引用,无法删除
    int ERR_METER_START_GREATER_THEN_MAX = 10009;// 起始读数大于最大量程
    int ERR_METER_CATEGORY_CAN_NOT_DELETE = 10010;// 默认分类无法删除
    int ERR_METER_IMPORT = 10011;// 导入失败,请检查数据准确性
    int ERR_METER_CATEGORY_HAS_BEEN_REFERENCE = 10012;// 分类被引用,无法删除
    int ERR_METER_SETTING_START_TIME_ERROR = 10013;// 开始时间不能小于现在
    int ERR_METER_SETTING_END_TIME_ERROR = 10014;// 结束时间不能小于开始时间
    int ERR_PRICE_CONFIG_HAS_BEEN_REFERENCE = 10015;// 单价方案已被引用，不可删除
    int ERROR_DOWNLOAD_FILE = 10016;// 文件导出错误

    int ERROR_METER_NAME_EXIST = 10017;// 表计名称存在
    int ERROR_METER_NUMBER_EXIST = 10018;// 表计号码存在

    int ERR_METER_TASK_NOT_EXIST = 10019;//任务不存在
    int ERR_METER_PLAN_NOT_ACTIVE = 10020;//计划不存在
    int ERR_METER_TASK_ALREADY_CLOSE = 10021;//任务已关闭
    int ERR_METER_HAS_ASSIGN_PLAN = 10022;//表计已关联计划

    int ERR_METER_TYPE_NOT_EXIST = 10023;//表计类型不存在
    int ERR_BILL_CATEGORY_NOT_EXIST = 10024;//范围不存在
    int ERR_SERVICE_CATEGORY_NOT_EXIST = 10025;//性质不存在
    int ERR_MAX_READING_NOT_EXIST = 10026;//最大量程错误
    int ERR_METER_NOT_EXIST_TASK = 10027;//表记没有任务
    int ERROR_METER_NAME_IS_NULL = 10028;//表计名称为空
    int ERROR_METER_NUMBER_IS_NULL = 10029;//表计号码为空
    int ERROR_READING_IS_NULL = 10030;//读数为空
    int ERROR_READING_IS_NOT_NUMBER = 10031;//读数不为小数
}
