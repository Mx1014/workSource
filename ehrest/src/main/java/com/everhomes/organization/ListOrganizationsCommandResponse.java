// @formatter:off
package com.everhomes.organization;

import java.util.List;

import com.everhomes.discover.ItemType;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：政府机构信息，参考{@link com.everhomes.organization.OrganizationDTO}</li>
 * </ul>
 */
public class ListOrganizationsCommandResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> members;
	public ListOrganizationsCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<OrganizationDTO> getMembers() {
		return members;
	}

	public void setMembers(List<OrganizationDTO> members) {
		this.members = members;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
