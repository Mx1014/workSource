package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>billId : 订单号</li>
 *  <li>amount : 金额</li>
 * </ul>
 */
public class RechargeOrderDTO {
	
	private Long billId;
	
	private Double amount;
	
	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
