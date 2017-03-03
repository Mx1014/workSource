// @formatter:off

package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communities: 小区列表，{@link com.everhomes.rest.address.CommunityDTO}</li>
 * <li>nextPageAnchor: 下一个锚点</li>
 * </ul>
 */
public class ListCommunityByNamespaceIdResponse {
	@ItemType(CommunityDTO.class)
	private List<CommunityDTO> communities;
	private Long nextPageAnchor;

	public List<CommunityDTO> getCommunities() {
		return communities;
	}

	public void setCommunities(List<CommunityDTO> communities) {
		this.communities = communities;
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