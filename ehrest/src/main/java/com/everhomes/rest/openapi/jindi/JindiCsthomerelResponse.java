// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>csthomerelList: 客房列表</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>hasMore: 是否有更多记录，1有</li>
 * </ul>
 */
public class JindiCsthomerelResponse {
	@ItemType(JindiCsthomerelDTO.class)
	private List<JindiCsthomerelDTO> csthomerelList;
	private Long nextPageAnchor;
	private Byte hasMore;

	public Byte getHasMore() {
		return hasMore;
	}

	public void setHasMore(Byte hasMore) {
		this.hasMore = hasMore;
	}

	public List<JindiCsthomerelDTO> getCsthomerelList() {
		return csthomerelList;
	}

	public void setCsthomerelList(List<JindiCsthomerelDTO> csthomerelList) {
		this.csthomerelList = csthomerelList;
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
