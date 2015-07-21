package com.everhomes.organization.pm;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.family.FamilyBillingTransactionDTO;
import com.everhomes.util.StringHelper;

public class PmBillsDTO{
	
	private java.lang.Long       id;
	private java.lang.Long       organizationId;
	private java.lang.String     entityType;
	private java.lang.Long       entityId;
	private java.lang.String     address;
	private java.lang.String     name;
	private java.lang.String     dateStr;
	private java.sql.Date        startDate;
	private java.sql.Date        endDate;
	private java.sql.Date        payDate;
	private java.lang.String     description;
	private java.math.BigDecimal dueAmount;
	private java.math.BigDecimal oweAmount;
	private java.lang.Long       creatorUid;
	private java.lang.Integer    notifyCount;
	private java.sql.Timestamp   notifyTime;
	private java.sql.Timestamp   createTime;
	
	//expand
	private BigDecimal payedAmount;

	private java.math.BigDecimal totalAmount;
	
	@ItemType(FamilyBillingTransactionDTO.class)
	private List<FamilyBillingTransactionDTO> payList;

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

	public java.lang.String getEntityType() {
		return entityType;
	}

	public void setEntityType(java.lang.String entityType) {
		this.entityType = entityType;
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

	public java.lang.String getDateStr() {
		return dateStr;
	}

	public void setDateStr(java.lang.String dateStr) {
		this.dateStr = dateStr;
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

	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public java.lang.Integer getNotifyCount() {
		return notifyCount;
	}

	public void setNotifyCount(java.lang.Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	public java.sql.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}

	public java.sql.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Date endDate) {
		this.endDate = endDate;
	}

	public java.sql.Date getPayDate() {
		return payDate;
	}

	public void setPayDate(java.sql.Date payDate) {
		this.payDate = payDate;
	}

	public java.sql.Timestamp getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(java.sql.Timestamp notifyTime) {
		this.notifyTime = notifyTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
