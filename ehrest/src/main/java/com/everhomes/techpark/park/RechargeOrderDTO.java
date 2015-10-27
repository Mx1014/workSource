package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>billId : 订单号</li>
 *  <li>amount : 金额</li>
 * </ul>
 */
public class RechargeOrderDTO {
	
	private Long billId;
	
	private Integer amount;
	
	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
