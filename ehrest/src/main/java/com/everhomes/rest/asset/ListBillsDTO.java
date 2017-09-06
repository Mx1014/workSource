//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>defaultOrder:排序数字</li>
 * <li>dateStr:账期，格式为201706，参与排序</li>
 * <li>billId:账单id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户类型</li>
 * <li>contractId:合同id</li>
 * <li>contractNum:合同编号</li>
 * <li>buildingName:楼栋门牌</li>
 * <li>apartmentName:楼栋门牌</li>
 * <li>noticeTel:催缴联系号码</li>
 * <li>amountReceivable:应收(元)</li>
 * <li>amountReceived:实收(元)</li>
 * <li>amountOwed:欠收(元)</li>
 * <li>billStatus:账单状态，0:待缴;1:已缴</li>
 * <li>noticeTimes:已催缴次数</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 *</ul>
 */
public class ListBillsDTO {
    private Integer defaultOrder;
    private String dateStr;
    private String billId;
    private String billGroupName;
    private String targetName;
    private String targetId;
    private String contractId;
    private String contractNum;
    private String targetType;
    private String buildingName;
    private String apartmentName;
    private String noticeTel;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountOwed;
    private Byte billStatus;
    private Integer noticeTimes;
    private String ownerId;
    private String ownerType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
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

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }



    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public Integer getNoticeTimes() {
        return noticeTimes;
    }

    public void setNoticeTimes(Integer noticeTimes) {
        this.noticeTimes = noticeTimes;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }



    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
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

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public ListBillsDTO() {

    }
}
