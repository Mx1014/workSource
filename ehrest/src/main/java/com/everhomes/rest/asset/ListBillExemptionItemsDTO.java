//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.Date;

/**
 *<ul>
 * <li>dateStr:账期，格式为2017-06，参与排序</li>
 * <li>exemptionId:减免项id</li>
 * <li>targetName:客户名称</li>
 * <li>amount:数额(元)</li>
 * <li>remark:备注</li>
 * <li>isPlus:0:减项目;1:加项</li>
 *</ul>
 */
public class ListBillExemptionItemsDTO {
    private String dateStr;
    private Long exemptionId;
    private BigDecimal amount;
    private String remark;
    private Byte isPlus;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public Long getExemptionId() {
        return exemptionId;
    }

    public Byte getIsPlus() {
        return isPlus;
    }

    public void setIsPlus(Byte isPlus) {
        this.isPlus = isPlus;
    }

    public void setExemptionId(Long exemptionId) {
        this.exemptionId = exemptionId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        amount = amount.setScale(2,BigDecimal.ROUND_CEILING);
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ListBillExemptionItemsDTO() {

    }
}
