package com.everhomes.organization.pm;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * 	<li>familyId : 家庭Id</li>
 *	<li>payDate : 支付日期</li>
 *	<li>payAmount : 支付金额</li>
 *	<li>description : 描述</li>
 *	<li>txType : 交易方式,详情:{@link com.everhomes.organization.TxType}</li>
 *	<li>vendor : 第三方支付</li>
 *	<li>paidType : 支付方式,详情:{@link com.everhomes.organization.PaidType}</li>
 *</ul>
 *
 */

public class PayPmBillByFamilyIdCommand {
	
	@NotNull
	private Long familyId;
	@NotNull
	private String payDate;
	@NotNull
	private BigDecimal payAmount;
	
	private String description;
	
	//other
	private Byte txType;
	private String vendor;
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
	
	public Long getFamilyId() {
		return familyId;
	}
	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
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
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
}
