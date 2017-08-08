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
 *</ul>
 */
public class ExemptionItemDTO {
    private Long exemptionId;
    @NotNull
    private BigDecimal amount;
    private String remark;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getExemptionId() {
        return exemptionId;
    }

    public void setExemptionId(Long exemptionId) {
        this.exemptionId = exemptionId;
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

    public ExemptionItemDTO() {

    }
}
