//@formatter:off
package com.everhomes.rest.asset.bill;


import java.math.BigDecimal;
/**
 * @author created by ycx
 * @date 2018年11月14日 上午10:17:18
 */

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>amountReceived:已收</li>
 *</ul>
 */
public class ChangeChargeStatusCommand {
    private Long billId;
    private BigDecimal amountReceived;
    private Integer paymentType;
    
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
    
}
