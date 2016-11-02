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

}
