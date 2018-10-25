package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.yellowPage.IdNameInfoDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> nextPageAnchor: 下一页锚点</li>
 *  <li> dtos: 列表 {@link com.everhomes.rest.yellowPage.IdNameInfoDTO}</li>
 * </ul>
 */
public class ListUiFAQsResponse {
	private Long nextPageAnchor;
	private List<IdNameInfoDTO> dtos;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<IdNameInfoDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<IdNameInfoDTO> dtos) {
		this.dtos = dtos;
	}

}
