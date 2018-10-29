package com.everhomes.rest.parking;

/**
 * @author sw on 2017/8/23.
 */
public class ParkingRechargeConfig {

    //是否支持过期充值
    private Byte expiredRechargeFlag;
    //支持过期充值时，最多过期天数
    private Integer maxExpiredDay;
    //支持过期充值时，至少充值几个月
    private Integer expiredRechargeMonthCount;
    //支持过期充值时，按照什么模式充值 {@link ParkingCardExpiredRechargeType}
    private Byte expiredRechargeType;

    private Byte monthlyDiscountFlag;
    private String monthlyDiscount;

    private Byte tempFeeDiscountFlag;
    private String tempFeeDiscount;
    private String contact;


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Byte getMonthlyDiscountFlag() {
        return monthlyDiscountFlag;
    }

    public void setMonthlyDiscountFlag(Byte monthlyDiscountFlag) {
        this.monthlyDiscountFlag = monthlyDiscountFlag;
    }

    public String getMonthlyDiscount() {
        return monthlyDiscount;
    }

    public void setMonthlyDiscount(String monthlyDiscount) {
        this.monthlyDiscount = monthlyDiscount;
    }

    public String getTempFeeDiscount() {
        return tempFeeDiscount;
    }

    public void setTempFeeDiscount(String tempFeeDiscount) {
        this.tempFeeDiscount = tempFeeDiscount;
    }

    public Byte getTempFeeDiscountFlag() {
        return tempFeeDiscountFlag;
    }

    public void setTempFeeDiscountFlag(Byte tempFeeDiscountFlag) {
        this.tempFeeDiscountFlag = tempFeeDiscountFlag;
    }

    public Byte getExpiredRechargeFlag() {
        return expiredRechargeFlag;
    }

    public void setExpiredRechargeFlag(Byte expiredRechargeFlag) {
        this.expiredRechargeFlag = expiredRechargeFlag;
    }

    public Integer getMaxExpiredDay() {
        return maxExpiredDay;
    }

    public void setMaxExpiredDay(Integer maxExpiredDay) {
        this.maxExpiredDay = maxExpiredDay;
    }

    public Integer getExpiredRechargeMonthCount() {
        return expiredRechargeMonthCount;
    }

    public void setExpiredRechargeMonthCount(Integer expiredRechargeMonthCount) {
        this.expiredRechargeMonthCount = expiredRechargeMonthCount;
    }

    public Byte getExpiredRechargeType() {
        return expiredRechargeType;
    }

    public void setExpiredRechargeType(Byte expiredRechargeType) {
        this.expiredRechargeType = expiredRechargeType;
    }
    
}
