//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/8/31.
 */

import java.math.BigDecimal;

public class RentalExcelTemplate {
    private String dateStr;
    private String buildingName;
    private String apartmentName;
    private String targetType;
    private String targetName;
    private String contractNum;
    private String noticeTel;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountOwed;
    private BigDecimal exemption;
    private String exemptionRemark;
    private BigDecimal supplement;
    private String supplementRemark;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        amountReceivable = amountReceivable.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        amountReceived = amountReceived.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountReceived = amountReceived;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        amountOwed = amountOwed.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountOwed = amountOwed;
    }

    public BigDecimal getExemption() {
        return exemption;
    }

    public void setExemption(BigDecimal exemption) {
        exemption = exemption.setScale(2,BigDecimal.ROUND_CEILING);
        this.exemption = exemption;
    }

    public String getExemptionRemark() {
        return exemptionRemark;
    }

    public void setExemptionRemark(String exemptionRemark) {
        this.exemptionRemark = exemptionRemark;
    }

    public BigDecimal getSupplement() {
        return supplement;
    }

    public void setSupplement(BigDecimal supplement) {
        supplement = supplement.setScale(2,BigDecimal.ROUND_CEILING);
        this.supplement = supplement;
    }

    public String getSupplementRemark() {
        return supplementRemark;
    }

    public void setSupplementRemark(String supplementRemark) {
        this.supplementRemark = supplementRemark;
    }
}
