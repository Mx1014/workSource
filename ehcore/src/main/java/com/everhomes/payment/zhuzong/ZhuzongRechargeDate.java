package com.everhomes.payment.zhuzong;

/**
 * UserID : 账号
 * HappenDate : 消费日期
 * HappenTime : 消费时间
 * ConsumeValue : 消费金额
 * ConsumeBalance : 剩余金额
 * DepositType : 微信、支付宝、存款、补贴
 */
public class ZhuzongRechargeDate {
    private String UserId;
    private String HappenDate;
    private String HappenTime;
    private String DepositValue;
    private String DepositBalance;
    private String DepositType;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getHappenDate() {
        return HappenDate;
    }

    public void setHappenDate(String happenDate) {
        HappenDate = happenDate;
    }

    public String getHappenTime() {
        return HappenTime;
    }

    public void setHappenTime(String happenTime) {
        HappenTime = happenTime;
    }

    public String getDepositValue() {
        return DepositValue;
    }

    public void setDepositValue(String depositValue) {
        DepositValue = depositValue;
    }

    public String getDepositBalance() {
        return DepositBalance;
    }

    public void setDepositBalance(String depositBalance) {
        DepositBalance = depositBalance;
    }

    public String getDepositType() {
        return DepositType;
    }

    public void setDepositType(String depositType) {
        DepositType = depositType;
    }
}
