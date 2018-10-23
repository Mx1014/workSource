package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> nextPageAnchor: 下一页锚点</li>
 *  <li> dtos: 问题列表 {@link com.everhomes.yellowPage.faq.FAQDTO}</li>
 * </ul>
 */
public class ListFAQsResponse {
	
	private Long nextPageAnchor;
	private List<FAQDTO> dtos;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<FAQDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<FAQDTO> dtos) {
		this.dtos = dtos;
	}
}
