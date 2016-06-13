package com.everhomes.rest.family;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id : 缴费id</li>
 *	<li>createTime : 缴费日期</li>
 *	<li>billType : 缴费类型 {@link com.everhomes.rest.organization.TxType}</li>
 *	<li>chargeAmount : 缴费金额</li>
 *	<li>description : 缴费描述</li>
 *</ul>
 *
 */


public class FamilyBillingTransactionDTO {
	
	private Long id;
	private Long createTime;
	private Byte billType;
	private String description;
	private BigDecimal chargeAmount;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Byte getBillType() {
		return billType;
	}
	public void setBillType(Byte billType) {
		this.billType = billType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	
	
	

}
