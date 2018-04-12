package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 活动列表 {@link ActivityDTO}</li>
 * </ul>
 */
public class ListActivitiesByCategoryIdResponse {

	private List<ActivityDTO> dtos;

	public List<ActivityDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ActivityDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
