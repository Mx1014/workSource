package com.everhomes.rest.enterprise;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos 参考{@link OrganizationDTO}</li>
 * </ul>
 */
public class ListUserOrganizationsResponse {
    private List<OrganizationDTO> dtos;

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
