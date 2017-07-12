package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页</li>
 * <li>members: 参考{@link com.everhomes.rest.community.admin.ComOrganizationMemberDTO}</li>
 * </ul>
 */
public class ListCommunityAuthPersonnelsResponse {

	private Long nextPageAnchor;
	@ItemType(ComOrganizationMemberDTO.class)
	private List<ComOrganizationMemberDTO> members;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ComOrganizationMemberDTO> getMembers() {
		return members;
	}

	public void setMembers(List<ComOrganizationMemberDTO> members) {
		this.members = members;
	}
	
	
}
