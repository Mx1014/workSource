package com.everhomes.rest.community;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下页的锚点</li>
 *     <li>list: list小区 {@link CommunityDTO}</li>
 * </ul>
 */
public class ListCommunitiesResponse {

	private Long nextPageAnchor;

	@ItemType(CommunityDTO.class)
	private List<CommunityDTO> list;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommunityDTO> getList() {
		return list;
	}

	public void setList(List<CommunityDTO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
