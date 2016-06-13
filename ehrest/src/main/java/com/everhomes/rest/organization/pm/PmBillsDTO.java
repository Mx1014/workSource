package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyBillingTransactionDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *	<li>id : 账单id</li>
 *
 *	<li>startDate : 账单开始日期</li>
 *	<li>endDate : 账单截止日期</li>
 *	<li>payDate : 还款日期</li>
 *	<li>address : 楼栋-门牌号</li>
 * 	<li>dueAmount : 本月金额</li>
 *	<li>oweAmount : 往期欠款</li>
 *	<li>payedAmount : 已付金额</li>
 *	<li>waitPayAmount : 应付金额</li>
 *	<li>totalAmount : 本月应付总额</li>
 *	<li>description : 账单说明</li>
 *	
 *	<li>payList : 账单缴费记录,详见: {@link com.everhomes.rest.family.FamilyBillingTransactionDTO}</li>
 *	<li>billItems : 账单明细,详见: {@link com.everhomes.rest.organization.pm.PmBillItemDTO}</li>
 *	
 *</ul>
 *
 */

public class PmBillsDTO{
	
	private java.lang.Long       id;
	private java.lang.Long       organizationId;
	private java.lang.Long       entityId;
	private java.lang.String     address;
	private java.lang.String     name;
	private java.lang.Long        startDate;
	private java.lang.Long        endDate;
	private java.lang.Long        payDate;
	private java.lang.String     description;
	private java.math.BigDecimal dueAmount;
	private java.math.BigDecimal oweAmount;
	
	//expand
	private BigDecimal payedAmount;
	private BigDecimal waitPayAmount;

	private java.math.BigDecimal totalAmount;
	
	@ItemType(FamilyBillingTransactionDTO.class)
	private List<FamilyBillingTransactionDTO> payList;
	
	@ItemType(PmBillItemDTO.class)
	private List<PmBillItemDTO> billItems;
	
	public BigDecimal getWaitPayAmount() {
		return waitPayAmount;
	}

	public void setWaitPayAmount(BigDecimal waitPayAmount) {
		this.waitPayAmount = waitPayAmount;
	}

	public BigDecimal getPayedAmount() {
		return payedAmount;
	}
	
	public void setPayedAmount(BigDecimal payedAmount) {
		this.payedAmount = payedAmount;
	}
	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(java.lang.Long organizationId) {
		this.organizationId = organizationId;
	}

	public java.lang.Long getEntityId() {
		return entityId;
	}

	public void setEntityId(java.lang.Long entityId) {
		this.entityId = entityId;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
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

	public java.math.BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(java.math.BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<FamilyBillingTransactionDTO> getPayList() {
		return payList;
	}

	public void setPayList(List<FamilyBillingTransactionDTO> payList) {
		this.payList = payList;
	}
	
	
	public List<PmBillItemDTO> getBillItems() {
		return billItems;
	}

	public void setBillItems(List<PmBillItemDTO> billItems) {
		this.billItems = billItems;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
