// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.group.IndustryTypeDTO}</li>
 * </ul>
 */
public class ListIndustryTypesResponse {

	@ItemType(IndustryTypeDTO.class)
	private List<IndustryTypeDTO> dtos;

	public List<IndustryTypeDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<IndustryTypeDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
