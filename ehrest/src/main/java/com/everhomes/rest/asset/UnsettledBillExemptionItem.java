//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>exemptionItemId:减免项id</li>
 * <li>defaultOrder:排序</li>
 * <li>dateStr:账期</li>
 * <li>billExemptionItemType:减免项目类型,0:追加收款/增收;1:减免</li>
 * <li>targetName:客户名称</li>
 * <li>amountReceivable:应收金额</li>
 * <li>remark:备注</li>
 *</ul>
 */
public class UnsettledBillExemptionItem {
    private Long exemptionItemId;
    private Integer defaultOrder;
    private String dateStr;
    private Byte billExemptionItemType;
    private String targetName;
    private BigDecimal amountReceivable;
    private String remark;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getExemptionItemId() {
        return exemptionItemId;
    }

    public void setExemptionItemId(Long exemptionItemId) {
        this.exemptionItemId = exemptionItemId;
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

    public Byte getBillExemptionItemType() {
        return billExemptionItemType;
    }

    public void setBillExemptionItemType(Byte billExemptionItemType) {
        this.billExemptionItemType = billExemptionItemType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UnsettledBillExemptionItem() {

    }
}
