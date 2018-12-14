package com.everhomes.asset.ExpressionParse;

import java.util.Map;

public class ExpressionParseConfig {

    //自定义周期开始时间
    private Integer billingCustomStartDate;
    //自定义周期
    private Integer billingCustomCycle;
    //自定义周期偏移量
    private Integer billingCustomCycleOffset;
    //出账单日期
    private Integer billDate;
    //最晚还款日
    private Integer dueDayDate;



    public void init(Map<String,Integer> params) {
        if (params.get("billingCustomStartDate")!=null){
            this.billingCustomStartDate = params.get("billingCustomStartDate");
        }
        if (params.get("billingCustomCycle")!=null){
            this.billingCustomCycle = params.get("billingCustomCycle");
        }
        if (params.get("billingCustomCycleOffset")!=null){
            this.billingCustomCycleOffset = params.get("billingCustomCycleOffset");
        }
        if (params.get("billDate")!=null){
            this.billDate = params.get("billDate");
        }
        if (params.get("dueDayDate")!=null){
            this.dueDayDate = params.get("dueDayDate");
        }
    }

    public ExpressionParseConfig() {
    }

    public Integer getBillingCustomStartDate() {
        return billingCustomStartDate;
    }

    public void setBillingCustomStartDate(Integer billingCustomStartDate) {
        this.billingCustomStartDate = billingCustomStartDate;
    }

    public Integer getBillingCustomCycle() {
        return billingCustomCycle;
    }

    public void setBillingCustomCycle(Integer billingCustomCycle) {
        this.billingCustomCycle = billingCustomCycle;
    }

    public Integer getBillingCustomCycleOffset() {
        return billingCustomCycleOffset;
    }

    public void setBillingCustomCycleOffset(Integer billingCustomCycleOffset) {
        this.billingCustomCycleOffset = billingCustomCycleOffset;
    }

    public Integer getBillDate() {
        return billDate;
    }

    public void setBillDate(Integer billDate) {
        this.billDate = billDate;
    }

    public Integer getDueDayDate() {
        return dueDayDate;
    }

    public void setDueDayDate(Integer dueDayDate) {
        this.dueDayDate = dueDayDate;
    }
}
