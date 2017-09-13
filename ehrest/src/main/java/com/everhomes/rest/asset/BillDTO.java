//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemId:账单收费项目id</li>
 * <li>defaultOrder:排序</li>
 * <li>dateStr:账期</li>
 * <li>billItemName:收费项目名称</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户类型</li>
 * <li>buildingName:楼栋</li>
 * <li>apartmentName:门牌</li>
 * <li>amountReceivable:应收金额</li>
 * <li>amountReceived:实收金额</li>
 * <li>amountOwed:欠收金额</li>
 * <li>billStatus:缴费状态,0:待缴;1:已缴</li>
 *</ul>
 */
public class BillDTO {
    private Long billItemId;
    private Integer defaultOrder;
    private String dateStr;
    private String billItemName;
    private String targetName;
    private String targetId;
    private Long targetType;
    private String buildingName;
    private String apartmentName;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountOwed;
    private Byte billStatus;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
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

    public String getBillItemName() {
        return billItemName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Long getTargetType() {
        return targetType;
    }

    public void setTargetType(Long targetType) {
        this.targetType = targetType;
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

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public BillDTO() {

    }
}
