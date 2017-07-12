//@formatter:off
package com.everhomes.rest.pmkexing;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>name：名称</li>
 *     <li>amount：金额</li>
 * </ul>
 */
public class PmKeXingBillItemDTO {

    private String name;
    private BigDecimal amount = BigDecimal.ZERO;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
