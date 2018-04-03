package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.group.GroupMemberDTO;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页</li>
 * <li>dtos: 参考{@link com.everhomes.rest.community.admin.GroupMemberDTO}</li>
 * </ul>
 */
public class CommunityAuthUserAddressResponse {

	private Long nextPageAnchor;
	
	@ItemType(GroupMemberDTO.class)
	private List<GroupMemberDTO> dtos;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<GroupMemberDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<GroupMemberDTO> dtos) {
		this.dtos = dtos;
	}
	
	
	
}
