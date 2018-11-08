package com.everhomes.rest.yellowPage.faq;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> nextPageAnchor: 下一页锚点</li>
 *  <li> dtos: 样式列表 {@link com.everhomes.rest.yellowPage.faq.ServiceRecordDTO}</li>
 * </ul>
 */
public class ListUiServiceRecordsResponse {
	private Long nextPageAnchor;
	private List<ServiceRecordDTO> dtos;

	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ServiceRecordDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ServiceRecordDTO> dtos) {
		this.dtos = dtos;
	}
}
