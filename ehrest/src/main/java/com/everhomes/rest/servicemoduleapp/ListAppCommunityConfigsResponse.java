package com.everhomes.rest.servicemoduleapp;

import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appCategoryDtos: 应用分类列表，参考{@link AppCategoryDTO}</li>
 *     <li>recommendAppsDtos: 推荐应用列表，参考{@link AppCommunityConfigDTO}</li>
 *     <li>thirdDtos: 第三方应用，此处暂时没有用到，永远是空 参考{@link com.everhomes.rest.servicemoduleapp.AppCommunityConfigDTO}</li>
 * </ul>
 */
public class ListAppCommunityConfigsResponse {

	private List<AppCategoryDTO> appCategoryDtos;

	private List<AppCommunityConfigDTO> recommendAppsDtos;

	private List<AppCommunityConfigDTO> thirdDtos;

	public List<AppCategoryDTO> getAppCategoryDtos() {
		return appCategoryDtos;
	}

	public void setAppCategoryDtos(List<AppCategoryDTO> appCategoryDtos) {
		this.appCategoryDtos = appCategoryDtos;
	}


	public List<AppCommunityConfigDTO> getThirdDtos() {
		return thirdDtos;
	}

	public void setThirdDtos(List<AppCommunityConfigDTO> thirdDtos) {
		this.thirdDtos = thirdDtos;
	}

	public List<AppCommunityConfigDTO> getRecommendAppsDtos() {
		return recommendAppsDtos;
	}

	public void setRecommendAppsDtos(List<AppCommunityConfigDTO> recommendAppsDtos) {
		this.recommendAppsDtos = recommendAppsDtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
