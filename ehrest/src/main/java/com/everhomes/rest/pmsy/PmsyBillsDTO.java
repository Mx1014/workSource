package com.everhomes.rest.pmsy;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>customerId: 用户id</li>
 * <li>resourceId: 资源id</li>
 * <li>projectId: 项目id(根据第三方获取的id)</li>
 * <li>payerId: 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 * 
 * <li>monthCount：待缴月数</li>
 * <li>totalAmount: 待缴金额</li>
 * <li>tip: 物业提示信息</li>
 * <li>contact: 物业客服电话</li>
 * <li>requests: 账单列表  com.everhomes.rest.property.MonthlyBill</li>
 * </ul>
 */
public class PmsyBillsDTO {
	private String customerId;
	private String projectId;
	private String resourceId;
	private Long payerId;
	
	private Integer monthCount;
	private BigDecimal totalAmount;
	private String tip;
	private String contact;
	@ItemType(MonthlyBill.class)
	private List<MonthlyBill> requests;
	
	public Integer getMonthCount() {
		return monthCount;
	}
	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
	}
	
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public List<MonthlyBill> getRequests() {
		return requests;
	}
	public void setRequests(List<MonthlyBill> requests) {
		this.requests = requests;
	}
	
}
