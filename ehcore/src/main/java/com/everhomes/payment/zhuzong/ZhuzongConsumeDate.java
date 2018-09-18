package com.everhomes.payment.zhuzong;

/**
 * UserID : 账号
 * HappenDate : 充值日期
 * HappenTime : 充值时间
 * ConsumeValue : 充值金额
 * ConsumeBalance : 剩余金额
 */
public class ZhuzongConsumeDate {
    private String UserId;
    private String HappenDate;
    private String HappenTime;
    private String ConsumeValue;
    private String ConsumeBalance;

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

    public String getConsumeValue() {
        return ConsumeValue;
    }

    public void setConsumeValue(String consumeValue) {
        ConsumeValue = consumeValue;
    }

    public String getConsumeBalance() {
        return ConsumeBalance;
    }

    public void setConsumeBalance(String consumeBalance) {
        ConsumeBalance = consumeBalance;
    }
}
