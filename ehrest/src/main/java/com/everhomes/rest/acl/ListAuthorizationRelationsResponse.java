package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>nextPageAnchor:下一页的锚点</li>
 * <li>dtos:具体授权关系详情, 参考{@link com.everhomes.rest.acl.AuthorizationRelationDTO}</li>
 * </ul>
 */
public class ListAuthorizationRelationsResponse {

	private Long nextPageAnchor;

	@ItemType(AuthorizationRelationDTO.class)
	private List<AuthorizationRelationDTO> dtos;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AuthorizationRelationDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<AuthorizationRelationDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
