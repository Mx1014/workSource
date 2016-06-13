package com.everhomes.rest.pmsy;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>customerId: 用户id</li>
 * <li>resourceId: 资源id</li>
 * <li>projectId: 项目id(根据第三方获取的id)</li>
 * <li>startDate: 开始时间</li>
 * <li>endDate: 结束时间</li>
 * <li>billType:UNPAID 表示查询所有欠费，不包括已交清；
 *			ALL 表示查询所有费用，包括未交清和已交清费用； {@link com.everhomes.rest.pmsy.PmsyBillType}}
 *	</li>
 *	<li>payerId: 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 * </ul>
 */
public class ListPmsyBillsCommand {
	
	@NotNull
	private String customerId;
	@NotNull
	private String projectId;
	@NotNull
	private String resourceId;
	private Long startDate;
	private Long endDate;
	@NotNull
	private Long payerId;
	@NotNull
	private PmsyBillType billType;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public PmsyBillType getBillType() {
		return billType;
	}
	public void setBillType(PmsyBillType billType) {
		this.billType = billType;
	}
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	
}
