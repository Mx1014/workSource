package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>allAmount: 全部金额</li>
 *     <li>frozenAmount: 已冻结金额</li>
 *     <li>unsettledPaymentAmount: 未结算支付金额</li>
 *     <li>unsettledPaymentAmount: 未结算充值金额</li>
 *     <li>unsettledWithdrawAmount: 未结算退款金额</li>
 *     <li>withdrawableAmount: 可提现金额</li>
 * </ul>
 */
public class PaymentBalanceDTO {
    private Long allAmount;
    
    private Long frozenAmount;
    
    private Long unsettledPaymentAmount;        // including purchase payments, refunded fee & purchase
    
    private Long unsettledRechargeAmount; 
    
    private Long unsettledWithdrawAmount;
	
	private Long withdrawableAmount;

    public Long getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(Long allAmount) {
        this.allAmount = allAmount;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Long getUnsettledPaymentAmount() {
        return unsettledPaymentAmount;
    }

    public void setUnsettledPaymentAmount(Long unsettledPaymentAmount) {
        this.unsettledPaymentAmount = unsettledPaymentAmount;
    }

    public Long getUnsettledRechargeAmount() {
        return unsettledRechargeAmount;
    }

    public void setUnsettledRechargeAmount(Long unsettledRechargeAmount) {
        this.unsettledRechargeAmount = unsettledRechargeAmount;
    }

    public Long getUnsettledWithdrawAmount() {
        return unsettledWithdrawAmount;
    }

    public void setUnsettledWithdrawAmount(Long unsettledWithdrawAmount) {
        this.unsettledWithdrawAmount = unsettledWithdrawAmount;
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
