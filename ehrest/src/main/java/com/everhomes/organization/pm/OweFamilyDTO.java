package com.everhomes.organization.pm;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;



/**
 * <ul>
 *	<li>lastPayTime : 上次缴费时间</li>
 *	<li>address : 楼栋门牌号</li>
 *	<li>telephone : 业主电话</li>
 * 	<li>oweAmount : 应付金额</li>
 *	<li>billDescription : 账单说明</li>
 *
 *	<li>billId : 账单id</li>
 *	<li>addressId : 家庭地址Id</li>
 *	<li>lastBillingTransactionId : 最后缴费记录id</li>
 *	<li>ownerName : 业主姓名</li>
 *	<li>ownerId : 业主Id</li>
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
	private Timestamp lastPayTime;
	private Long lastBillingTransactionId;

	//organizationOwner
	private String ownerName;
	private String telephone;
	private Long ownerId;
	
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
	public Timestamp getLastPayTime() {
		return lastPayTime;
	}
	public void setLastPayTime(Timestamp lastPayTime) {
		this.lastPayTime = lastPayTime;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}





}
