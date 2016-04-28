package com.everhomes.rest.pmsy;

import java.math.BigDecimal;

/**
 *<ul>
 *	<li>ownerType: 归属的类型，{@link com.everhomes.rest.pmsy.PmOwnerType}</li>
 *	<li>ownerId : 归属ID</li>
 *	<li>projectId : 项目ID</li>
 *	<li>customerId : 客户ID</li>
 *	<li>resourceId: 资源ID</li>
 *	<li>creatorId : 登录的用户ID（左邻）</li>
 *	<li>orderAmount : 订单金额</li>
 *	<li>paidType : 支付方式,10001-支付宝，10002-微信 com.everhomes.rest.organization.VendorType</li>
 * 	<li>revId : 账单ID（是一个数组，同时缴纳多个item的费用）</li>
 * 	<li>pmPayerId : 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 *</ul>
 *
 */
public class CreatePmBillOrderCommand {
	private String ownerType;
	private Long ownerId;
	private String projectId;
	private String customerId;
	private String resourceId;
	private Long creatorId;
	private BigDecimal orderAmount;
	private String paidType;
	private String[] billId;
	private Long pmPayerId;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPaidType() {
		return paidType;
	}
	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}
	
	public String[] getBillId() {
		return billId;
	}
	public void setBillId(String[] billId) {
		this.billId = billId;
	}
	public Long getPmPayerId() {
		return pmPayerId;
	}
	public void setPmPayerId(Long pmPayerId) {
		this.pmPayerId = pmPayerId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	
}
