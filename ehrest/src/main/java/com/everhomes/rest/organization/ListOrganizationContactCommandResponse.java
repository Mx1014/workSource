// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构成员信息，参考{@link com.everhomes.rest.organization.OrganizationContactDTO}</li>
 * </ul>
 */
public class ListOrganizationContactCommandResponse {
	
	private Integer nextPageOffset;

	private Long nextPageAnchor;
	
	private Integer totalCount;

	private Integer namespaceId;
	
	@ItemType(OrganizationContactDTO.class)
    private List<OrganizationContactDTO> members;
	public ListOrganizationContactCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<OrganizationContactDTO> getMembers() {
		return members;
	}

	public void setMembers(List<OrganizationContactDTO> members) {
		this.members = members;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
