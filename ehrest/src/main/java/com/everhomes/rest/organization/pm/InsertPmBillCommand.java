package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *	<li>organizationId : 物业id</li>
 *	<li>address : 楼栋-门牌号</li>
 *	<li>startDate : 账单开始日期</li>
 *	<li>endDate : 账单截止日期</li>
 *	<li>payDate : 还款日期</li>
 *	<li>description : 描述</li>
 *	<li>dueAmount : 本月金额</li>
 *	<li>oweAmount : 往期欠款</li>
 *</ul>
 *
 */
public class InsertPmBillCommand {
	
	@NotNull
	private java.lang.Long       organizationId;
	@NotNull
	private java.lang.String     address;
	@NotNull
	private java.lang.Long        startDate;
	@NotNull
	private java.lang.Long        endDate;
	@NotNull
	private java.lang.Long        payDate;
	
	private java.lang.String     description;
	@NotNull
	private java.math.BigDecimal dueAmount;
	
	private java.math.BigDecimal oweAmount;
	
	public java.lang.Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(java.lang.Long organizationId) {
		this.organizationId = organizationId;
	}
	public java.lang.String getAddress() {
		return address;
	}
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	public java.lang.Long getStartDate() {
		return startDate;
	}
	public void setStartDate(java.lang.Long startDate) {
		this.startDate = startDate;
	}
	public java.lang.Long getEndDate() {
		return endDate;
	}
	public void setEndDate(java.lang.Long endDate) {
		this.endDate = endDate;
	}
	public java.lang.Long getPayDate() {
		return payDate;
	}
	public void setPayDate(java.lang.Long payDate) {
		this.payDate = payDate;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.math.BigDecimal getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(java.math.BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}
	public java.math.BigDecimal getOweAmount() {
		return oweAmount;
	}
	public void setOweAmount(java.math.BigDecimal oweAmount) {
		this.oweAmount = oweAmount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
