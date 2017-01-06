// @formatter:off
package com.everhomes.rest.openapi;

import com.everhomes.rest.group.GroupDTO;

/**
 * 
 * <ul>
 * <li>groupDTO: group信息</li>
 * </ul>
 */
public class CreateBusinessGroupResponse {
	private GroupDTO groupDTO;

	public CreateBusinessGroupResponse() {
		super();
	}

	public CreateBusinessGroupResponse(GroupDTO groupDTO) {
		super();
		this.groupDTO = groupDTO;
	}

	public GroupDTO getGroupDTO() {
		return groupDTO;
	}

	public void setGroupDTO(GroupDTO groupDTO) {
		this.groupDTO = groupDTO;
	}
	
}
