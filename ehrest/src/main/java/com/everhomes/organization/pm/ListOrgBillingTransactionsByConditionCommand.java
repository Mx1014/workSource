package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>pageOffSet : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>address : 地址</li>
 *	<li>startTime : 起始日期</li>
 *	<li>endTime : 结束日期</li>
 *	<li>organizationId : 组织id</li>

 *</ul>
 *
 */
public class ListOrgBillingTransactionsByConditionCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	
	private String address;
	private Long startTime;
	private Long endTime;
	
	@NotNull
	private Long organizationId;

	public Long getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
	

}
