// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构对应的小区信息，参考{@link com.everhomes.rest.organization.OrganizationCommunityDTO}</li>
 * </ul>
 */
public class ListOrganizationCommunityCommandResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(OrganizationCommunityDTO.class)
    private List<OrganizationCommunityDTO> communities;
	public ListOrganizationCommunityCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

    public List<OrganizationCommunityDTO> getCommunities() {
        return communities;
    }

    public void setCommunities(List<OrganizationCommunityDTO> communities) {
        this.communities = communities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
