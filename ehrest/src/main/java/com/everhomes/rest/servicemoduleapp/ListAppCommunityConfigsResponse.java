package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos参考{@link com.everhomes.rest.servicemoduleapp.AppCommunityConfigDTO}</li>
 * </ul>
 */
public class ListAppCommunityConfigsResponse {
	private List<AppCommunityConfigDTO> dtos;

	public List<AppCommunityConfigDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<AppCommunityConfigDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
