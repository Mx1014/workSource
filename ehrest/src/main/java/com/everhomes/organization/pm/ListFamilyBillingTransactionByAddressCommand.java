package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>pageOffset : 页码</li>
 * <li>pageSize : 页大小</li>
 * <li>address : 地址</li>
 *</ul>
 *
 */
public class ListFamilyBillingTransactionByAddressCommand {
	
	private Long pageOffset;
	
	private Integer pageSize;
	
	@NotNull
	private String address;
	
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
