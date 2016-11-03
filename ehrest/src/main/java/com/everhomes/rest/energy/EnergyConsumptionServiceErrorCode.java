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
    int ERR_CURR_READING_LESS_THEN_START_READING = 10007;// 当前读表数小于起始读数
    int ERR_FORMULA_HAS_BEEN_REFERENCE = 10008;// 公式被引用,无法删除

}
