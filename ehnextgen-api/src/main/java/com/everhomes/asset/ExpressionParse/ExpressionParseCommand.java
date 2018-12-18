package com.everhomes.asset.ExpressionParse;

import org.apache.log4j.Logger;

/**
 * 时间表达式解析参数集
 */
public class ExpressionParseCommand {
    Logger LOGGER = Logger.getLogger(ExpressionParseCommand.class);
    //生成账单周期类型
    private Byte billingCycle;
    //生成账单周期表达式
    private String billingCycleExpression;
    //出账单日类型
    private Byte billDayType;
    //出账单日表达式
    private String billDayExpression;
    //最晚还款日类型
    private Byte dueDayType;
    //最晚还款日表达式
    private String dueDayExpression;

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getBillingCycleExpression() {
        return process(billingCycleExpression);
    }

    public void setBillingCycleExpression(String billingCycleExpression) {
        this.billingCycleExpression = billingCycleExpression;
    }

    public Byte getBillDayType() {
        return billDayType;
    }

    public void setBillDayType(Byte billDayType) {
        this.billDayType = billDayType;
    }

    public String getBillDayExpression() {
        return process(billDayExpression);
    }

    public void setBillDayExpression(String billDayExpression) {
        this.billDayExpression = billDayExpression;
    }

    public Byte getDueDayType() {
        return dueDayType;
    }

    public void setDueDayType(Byte dueDayType) {
        this.dueDayType = dueDayType;
    }

    public String getDueDayExpression() {
        return process(dueDayExpression);
    }

    public void setDueDayExpression(String dueDayExpression) {
        this.dueDayExpression = dueDayExpression;
    }

    //将表达式的外围中括号去掉
    private String process(String expression){
        if (expression!=null&&expression.length()>0){
            return expression.substring(1,expression.length()-1);
        }else {
            LOGGER.error("param expression is null or length of it is less than 1");
            return "";
        }

    }
}
