package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * 	<li>addressId : 地址Id</li>
 *	<li>payTime : 支付日期</li>
 *	<li>payAmount : 支付金额</li>
 *	<li>description : 描述</li>
 *	<li>ownerName : 用户姓名</li>
 *	<li>telephone : 用户电话</li>
 *	<li>txType : 交易方式,详情:{@link com.everhomes.rest.organization.TxType}</li>
 *	<li>vendor : 第三方支付</li>
 *	<li>paidType : 支付方式,详情:{@link com.everhomes.rest.organization.PaidType}</li>
 *</ul>
 *
 */

public class PayPmBillByAddressIdCommand {
	
	@NotNull
	private Long addressId;
	@NotNull
	private Long payTime;
	@NotNull
	private BigDecimal payAmount;
	
	private String description;
	private String ownerName;
	private String telephone;
	
	//other
	private String vendor;
	@NotNull
	private Byte txType;
	@NotNull
	private Byte paidType;
	
	
	public Byte getTxType() {
		return txType;
	}
	public void setTxType(Byte txType) {
		this.txType = txType;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public Byte getPaidType() {
		return paidType;
	}
	public void setPaidType(Byte paidType) {
		this.paidType = paidType;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getPayTime() {
		return payTime;
	}
	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
}
