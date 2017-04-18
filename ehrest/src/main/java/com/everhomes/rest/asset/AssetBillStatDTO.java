package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>unpaidMonth: 待缴月数</li>
 *     <li>unpaidAmount: 待缴费用</li>
 * </ul>
 * Created by ying.xiong on 2017/4/12.
 */
public class AssetBillStatDTO {
    private BigDecimal unpaidMonth = BigDecimal.ZERO;
    private BigDecimal unpaidAmount = BigDecimal.ZERO;

    public BigDecimal getUnpaidMonth() {
        return unpaidMonth;
    }

    public void setUnpaidMonth(BigDecimal unpaidMonth) {
        this.unpaidMonth = unpaidMonth;
    }

    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
