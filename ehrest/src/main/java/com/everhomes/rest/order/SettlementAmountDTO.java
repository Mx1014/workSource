package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>settlementAmount: 总结算金额（含可提现的和不可提现的）</li>
 *     <li>withdrawableAmount: 订单类型</li>
 * </ul>
 */
public class SettlementAmountDTO {
	private Long settlementAmount;
	
	private Long withdrawableAmount;

    public Long getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(Long settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public Long getWithdrawableAmount() {
        return withdrawableAmount;
    }

    public void setWithdrawableAmount(Long withdrawableAmount) {
        this.withdrawableAmount = withdrawableAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
