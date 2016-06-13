// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构成员信息，参考{@link com.everhomes.rest.organization.OrganizationMemberDTO}</li>
 * </ul>
 */
public class ListOrganizationMemberCommandResponse {
	
	private Integer nextPageOffset;
	
	private Long nextPageAnchor;
	
	@ItemType(OrganizationMemberDTO.class)
    private List<OrganizationMemberDTO> members;
	public ListOrganizationMemberCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<OrganizationMemberDTO> getMembers() {
		return members;
	}

	public void setMembers(List<OrganizationMemberDTO> members) {
		this.members = members;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
