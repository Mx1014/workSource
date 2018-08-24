package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> nextPageAnchor: 下一页锚点</li>
 *  <li> dtos: 样式列表 {@link com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO}</li>
 * </ul>
 */
public class ListServiceAllianceCategoriesAdminResponse {
	
	private Long nextPageAnchor;
	private List<ServiceAllianceCategoryDTO> dtos;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ServiceAllianceCategoryDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ServiceAllianceCategoryDTO> dtos) {
		this.dtos = dtos;
	}
 }
