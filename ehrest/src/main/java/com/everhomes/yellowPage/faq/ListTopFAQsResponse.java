package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> nextPageAnchor: 下一页锚点</li>
 *  <li> dtos: 列表 {@link com.everhomes.rest.common.IdNameDTO}</li>
 * </ul>
 */
public class ListTopFAQsResponse {
	private Long nextPageAnchor;
	private List<IdNameDTO> dtos;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public List<IdNameDTO> getDtos() {
		return dtos;
	}


	public void setDtos(List<IdNameDTO> dtos) {
		this.dtos = dtos;
	}

}
