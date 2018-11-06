package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * </ul>
 */
public class ListPMOrganizationsResponse {

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> dtos;

    private Long nextPageAnchor;

    public List<OrganizationDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<OrganizationDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
