package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;
/**
 * apartmentManagementPrivilegeFlag:0-否，1-是
 * @author steve
 *
 */
public class ApartmentManagementPrivilegeDTO {
	
	private Byte apartmentManagementPrivilegeFlag;

	public Byte getApartmentManagementPrivilegeFlag() {
		return apartmentManagementPrivilegeFlag;
	}

	public void setApartmentManagementPrivilegeFlag(Byte apartmentManagementPrivilegeFlag) {
		this.apartmentManagementPrivilegeFlag = apartmentManagementPrivilegeFlag;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
