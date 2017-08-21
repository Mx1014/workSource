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
 *</ul>
 */
public class ListSettledBillExemptionItemsDTO {
    private Date dateStr;
    private Long exemptionId;
    private BigDecimal amount;
    private String remark;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Date getDateStr() {
        return dateStr;
    }

    public void setDateStr(Date dateStr) {
        this.dateStr = dateStr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ListSettledBillExemptionItemsDTO() {

    }
}
