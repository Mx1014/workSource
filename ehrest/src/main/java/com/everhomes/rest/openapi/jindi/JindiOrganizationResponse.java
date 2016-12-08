// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>organizationList: 组织列表</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>hasMore: 是否有更多记录，1有</li>
 * </ul>
 */
public class JindiOrganizationResponse {
	@ItemType(JindiOrganizationDTO.class)
	private List<JindiOrganizationDTO> organizationList;
	private Long nextPageAnchor;
	private Byte hasMore;

	public Byte getHasMore() {
		return hasMore;
	}

	public void setHasMore(Byte hasMore) {
		this.hasMore = hasMore;
	}

	public List<JindiOrganizationDTO> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<JindiOrganizationDTO> organizationList) {
		this.organizationList = organizationList;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
