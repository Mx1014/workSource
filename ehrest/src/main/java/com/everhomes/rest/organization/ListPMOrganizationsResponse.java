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

    private Long anchor;

    public List<OrganizationDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<OrganizationDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
