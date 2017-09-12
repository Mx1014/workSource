//@formatter:off
package com.everhomes.asset;


import java.math.BigDecimal;

public class NoticeInfo {
    private String phoneNum;
    private BigDecimal amountRecevable;
    private BigDecimal amountOwed;
    private String targetType;
    private Long targetId;
    private String appName;
    private String targetName;
    public NoticeInfo() {
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public BigDecimal getAmountRecevable() {
        return amountRecevable;
    }

    public void setAmountRecevable(BigDecimal amountRecevable) {
        this.amountRecevable = amountRecevable;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
