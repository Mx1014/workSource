// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>groups: 圈列表</li>
 * </ul>
 */
public class ListGroupsByApprovalStatusResponse {

	private Long nextPageAnchor;

	@ItemType(GroupDTO.class)
	private List<GroupDTO> groups;

	public ListGroupsByApprovalStatusResponse() {

	}

	public ListGroupsByApprovalStatusResponse(Long nextPageAnchor, List<GroupDTO> groups) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.groups = groups;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
