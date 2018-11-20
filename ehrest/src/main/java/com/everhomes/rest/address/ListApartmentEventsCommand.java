package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>addressId: 房源id</li>
 * 	<li>pageAnchor: 锚点</li>
 * 	<li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListApartmentEventsCommand {
	
	private Long addressId;
	private Long pageAnchor;
	private Integer pageSize;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
