// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>dtos：政府机构信息，参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * </ul>
 */
public class ListOrganizationsCommandResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> dtos;
	public ListOrganizationsCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	

	public List<OrganizationDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<OrganizationDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
