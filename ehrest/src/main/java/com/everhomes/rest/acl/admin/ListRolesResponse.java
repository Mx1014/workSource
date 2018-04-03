package com.everhomes.rest.acl.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.AuthorizationRelationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>nextPageAnchor:下一页的锚点</li>
 * <li>dtos:角色列表, 参考{@link RoleDTO}</li>
 * </ul>
 */
public class ListRolesResponse {

	private Long nextPageAnchor;

	@ItemType(RoleDTO.class)
	private List<RoleDTO> dtos;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<RoleDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<RoleDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
