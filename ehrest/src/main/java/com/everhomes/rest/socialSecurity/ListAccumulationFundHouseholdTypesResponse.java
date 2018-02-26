// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>householdTypes: 户籍档次列表</li>
 * </ul>
 */
public class ListAccumulationFundHouseholdTypesResponse {

	@ItemType(HouseholdTypesDTO.class)
	private List<HouseholdTypesDTO> householdTypes;

	public ListAccumulationFundHouseholdTypesResponse() {

	}

	public ListAccumulationFundHouseholdTypesResponse(List<HouseholdTypesDTO> householdTypes) {
		super();
		this.householdTypes = householdTypes;
	}

	public List<HouseholdTypesDTO> getHouseholdTypes() {
		return householdTypes;
	}

	public void setHouseholdTypes(List<HouseholdTypesDTO> householdTypes) {
		this.householdTypes = householdTypes;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
