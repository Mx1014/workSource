//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *<ul>
 * <li>exemptionId:减免项id,可为空</li>
 * <li>amount:减免金额，当为正数时项目为减免金额，当为负数时项目为增收金额</li>
 * <li>remark:减免项的备注</li>
 * <li>isPlus:1:为加项;0:为减项</li>
 * <li>dateStr:账期</li>
 *</ul>
 */
public class ExemptionItemDTO {
    private Long exemptionId;
    @NotNull
    private BigDecimal amount;
    private String remark;
    private Byte isPlus;
    private String dateStr;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getExemptionId() {
        return exemptionId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setExemptionId(Long exemptionId) {
        this.exemptionId = exemptionId;
    }

    public Byte getIsPlus() {
        return isPlus;
    }

    public void setIsPlus(Byte isPlus) {
        this.isPlus = isPlus;
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

    public ExemptionItemDTO() {

    }
}
