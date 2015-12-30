package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>pageOffset : 页码</li>
 * <li>pageSize : 页大小</li>
 * <li>addressId : 地址Id</li>
 *</ul>
 *
 */
public class ListBillTxByAddressIdCommand {
	
	private Long pageOffset;
	
	private Integer pageSize;
	
	@NotNull
	private Long addressId;
	
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

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
