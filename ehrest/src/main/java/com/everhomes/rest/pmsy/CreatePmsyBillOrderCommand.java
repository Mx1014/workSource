package com.everhomes.rest.pmsy;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 *<ul>
 *	<li>ownerType: 归属的类型，{@link com.everhomes.rest.pmsy.PmsyOwnerType}</li>
 *	<li>projectId : 项目ID</li>
 *	<li>customerId : 客户ID</li>
 *	<li>resourceId: 资源ID</li>
 *	<li>orderAmount : 订单金额</li>
 *	<li>paidType : 支付方式,10001-支付宝，10002-微信 com.everhomes.rest.organization.VendorType</li>
 * 	<li>revId : 账单ID（是一个list，同时缴纳多个item的费用）</li>
 * 	<li>pmPayerId : 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 *  <li>haianCommunityName : 海岸的园区名称</li>
 *</ul>
 *
 */
public class CreatePmsyBillOrderCommand {
	private String ownerType;
	//private Long ownerId;
	private String projectId;
	private String customerId;
	private String resourceId;
	//private Long creatorId;
	private BigDecimal orderAmount;
	private String paidType;
	@ItemType(String.class)
	private List<String> billIds;
	private Long pmPayerId;
	
	private String clientAppName;
	
	private String haianCommunityName;
	 
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
	
	public List<String> getBillIds() {
		return billIds;
	}
	public void setBillIds(List<String> billIds) {
		this.billIds = billIds;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getClientAppName() {
		return clientAppName;
	}
	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}
	public String getHaianCommunityName() {
		return haianCommunityName;
	}
	public void setHaianCommunityName(String haianCommunityName) {
		this.haianCommunityName = haianCommunityName;
	}
}
