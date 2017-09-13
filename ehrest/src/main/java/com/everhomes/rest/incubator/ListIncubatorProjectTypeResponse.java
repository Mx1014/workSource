package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.incubator.IncubatorProjectTypeDTO}</li>
 * </ul>
 */
public class ListIncubatorProjectTypeResponse {
	@ItemType(IncubatorProjectTypeDTO.class)
	private List<IncubatorProjectTypeDTO> dtos;

	public List<IncubatorProjectTypeDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<IncubatorProjectTypeDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
