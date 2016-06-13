package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;



/**
 * <ul>
 *	<li>lastPayTime : 上次缴费时间</li>
 *	<li>address : 楼栋门牌号</li>
 *	<li>ownerTelephone : 业主电话</li>
 * 	<li>oweAmount : 应付金额</li>
 *	<li>billDescription : 账单说明</li>
 *
 *	<li>billId : 账单id</li>
 *	<li>addressId : 家庭地址Id</li>
 *	<li>lastBillingTransactionId : 最后缴费记录id</li>
 *</ul>
 *
 */

public class OweFamilyDTO {
	//bill
	private BigDecimal oweAmount;
	private String billDescription;
	private Long billId;

	//family
	private String address;
	private Long addressId;

	//billingTransaction
	private Long lastPayTime;
	private Long lastBillingTransactionId;

	//organizationOwner
	private String ownerTelephone;
	
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public BigDecimal getOweAmount() {
		return oweAmount;
	}
	public void setOweAmount(BigDecimal oweAmount) {
		this.oweAmount = oweAmount;
	}
	public String getBillDescription() {
		return billDescription;
	}
	public void setBillDescription(String billDescription) {
		this.billDescription = billDescription;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getLastPayTime() {
		return lastPayTime;
	}
	public void setLastPayTime(Long lastPayTime) {
		this.lastPayTime = lastPayTime;
	}
	public String getOwnerTelephone() {
		return ownerTelephone;
	}
	public void setOwnerTelephone(String ownerTelephone) {
		this.ownerTelephone = ownerTelephone;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public Long getLastBillingTransactionId() {
		return lastBillingTransactionId;
	}
	public void setLastBillingTransactionId(Long lastBillingTransactionId) {
		this.lastBillingTransactionId = lastBillingTransactionId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}





}
