package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.incubator.IncubatorDTO}</li>
 * </ul>
 */
public class ListIncubatorResponse {


	@ItemType(IncubatorDTO.class)
	private List<IncubatorDTO> dtos;

	public List<IncubatorDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<IncubatorDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
