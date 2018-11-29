package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>sourceName: 修改的资源名称</li>
 * <li>operateType: 操作类型：-1：删除，0：变更额度，1：添加</li>
 * <li>changedAmount: 变更额度，不包含单位</li>
 * <li>currentLimitAmount: 当前额度</li>
 * </ul>
 */
public class EmployeePaymentLimitChangeLogItemDTO {
    private String sourceName;
    private Byte operateType;
    private BigDecimal changedAmount;
    private BigDecimal currentLimitAmount;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public BigDecimal getChangedAmount() {
        return changedAmount;
    }

    public void setChangedAmount(BigDecimal changedAmount) {
        this.changedAmount = changedAmount;
    }

    public BigDecimal getCurrentLimitAmount() {
        return currentLimitAmount;
    }

    public void setCurrentLimitAmount(BigDecimal currentLimitAmount) {
        this.currentLimitAmount = currentLimitAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
