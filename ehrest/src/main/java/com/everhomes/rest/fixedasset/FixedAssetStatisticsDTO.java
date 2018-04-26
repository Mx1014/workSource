package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>totalCount: 数量合计</li>
 * <li>totalAmount: 金额合计</li>
 * </ul>
 */
public class FixedAssetStatisticsDTO {
    private Long totalCount;
    private Double totalAmount;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
