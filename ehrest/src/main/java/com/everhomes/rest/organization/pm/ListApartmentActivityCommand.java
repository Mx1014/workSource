package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>addressId: 房源id</li>
 * </ul>
 */
public class ListApartmentActivityCommand {
	
	private Long addressId;

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
