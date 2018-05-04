package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 系统原生应用参考{@link com.everhomes.rest.servicemoduleapp.AppCommunityConfigDTO}</li>
 *     <li>thirdDtos: 第三方应用，此处暂时没有用到，永远是空 参考{@link com.everhomes.rest.servicemoduleapp.AppCommunityConfigDTO}</li>
 * </ul>
 */
public class ListAppCommunityConfigsResponse {
	private List<AppCommunityConfigDTO> dtos;

	private List<AppCommunityConfigDTO> thirdDtos;

	public List<AppCommunityConfigDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<AppCommunityConfigDTO> dtos) {
		this.dtos = dtos;
	}

	public List<AppCommunityConfigDTO> getThirdDtos() {
		return thirdDtos;
	}

	public void setThirdDtos(List<AppCommunityConfigDTO> thirdDtos) {
		this.thirdDtos = thirdDtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
