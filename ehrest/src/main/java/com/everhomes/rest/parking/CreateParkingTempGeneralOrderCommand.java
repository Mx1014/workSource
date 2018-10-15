package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId : 用户公司id</li>
 * </ul>
 */
public class CreateParkingTempGeneralOrderCommand extends CreateParkingTempOrderCommand{
	
	private Long organizationId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
