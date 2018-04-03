package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 企业列表 {@link com.everhomes.rest.organization.OrganizationDetailDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListEnterprisesCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationDetailDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<OrganizationDetailDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<OrganizationDetailDTO> dtos) {
		this.dtos = dtos;
	}

    
}
