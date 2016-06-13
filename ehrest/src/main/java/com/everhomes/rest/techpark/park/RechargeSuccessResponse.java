package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;

/**
 * 
 * <ul>
 *  <li>billId : 订单id</li>
 *  <li>validityPeriod : 有效期</li>
 *  <li>payStatus : 支付状态</li>
 *  <li>rechargeStatus : 充值状态</li>
 * </ul>
 *
 */
public class RechargeSuccessResponse {
	
	private Long billId;
	
	private Timestamp validityPeriod;
	
	private Byte payStatus;
	
	private Byte rechargeStatus;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Timestamp getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Timestamp validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Byte getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Byte payStatus) {
		this.payStatus = payStatus;
	}

	public Byte getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}
	
	

}
