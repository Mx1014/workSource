package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link IncubatorApplyDTO}</li>
 *     <li>nextPageOffset: 下页页码</li>
 * </ul>
 */
public class ListIncubatorApplyResponse {


	@ItemType(IncubatorApplyDTO.class)
	private List<IncubatorApplyDTO> dtos;

	private Integer nextPageOffset;

	public List<IncubatorApplyDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<IncubatorApplyDTO> dtos) {
		this.dtos = dtos;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
