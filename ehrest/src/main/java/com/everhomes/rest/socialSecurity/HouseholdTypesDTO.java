package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>HouseholdTypesDTO值:
 * <li>householdTypeName: 户籍类型名</li>
 * </ul>
 */
public class HouseholdTypesDTO {
	private String householdTypeName;

	public String getHouseholdTypeName() {
		return householdTypeName;
	}

	public void setHouseholdTypeName(String householdTypeName) {
		this.householdTypeName = householdTypeName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
