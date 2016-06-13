package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *	<li>address : 地址</li>
 *	<li>lastPayDate : 最后的缴费日期</li>
 *	<li>organizationId : 组织id</li>
 *</ul>
 *
 */
public class ListOweFamilysByConditionsCommand {
	
	private String address;
	private String lastPayDate;
	
	@NotNull
	private Long organizationId;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLastPayDate() {
		return lastPayDate;
	}

	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
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
