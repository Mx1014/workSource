// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>dtos: 列表数据</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListScopeResponse {

	@ItemType(ScopeDTO.class)
	private List<ScopeDTO> dtos;

	private Long nextPageAnchor;

	public List<ScopeDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ScopeDTO> dtos) {
		this.dtos = dtos;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
