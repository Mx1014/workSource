package com.everhomes.rest.pmsy;

/**
 * <ul>
 * <li>projectId: 项目ID</li>
 * <li>resourceId: 资源ID</li>
 * <li>resourceName: 资源名称</li>
 * <li>payerId: 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 * <li>customerId: 客户ID（来自第三方）</li>
 * </ul>
 */
public class AddressDTO {
	
	private String projectId;
	private String resourceId;
	private String resourceName;
	private Long payerId;
	private String customerId;
	
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

	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
