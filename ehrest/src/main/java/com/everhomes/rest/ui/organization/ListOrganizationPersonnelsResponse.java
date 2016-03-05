// @formatter:off
package com.everhomes.rest.ui.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：下一页的页码（如果没有则为空）</li>
 * <li>dtos：机构成员信息，参考{@link com.everhomes.rest.organization.OrganizationMemberDTO}</li>
 * </ul>
 */
public class ListOrganizationPersonnelsResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(OrganizationMemberDTO.class)
    private List<OrganizationMemberDTO> dtos;
	public ListOrganizationPersonnelsResponse() {
    }

	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public List<OrganizationMemberDTO> getDtos() {
		return dtos;
	}


	public void setDtos(List<OrganizationMemberDTO> dtos) {
		this.dtos = dtos;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
