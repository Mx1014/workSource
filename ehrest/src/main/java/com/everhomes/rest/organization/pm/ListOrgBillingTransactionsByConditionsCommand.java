package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>pageOffSet : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>address : 地址</li>
 *	<li>payDate : 支付日期</li>
 *	<li>organizationId : 组织id</li>

 *</ul>
 *
 */
public class ListOrgBillingTransactionsByConditionsCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	
	private String address;
	private String payDate;
	
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
	

}
