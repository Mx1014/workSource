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

}
