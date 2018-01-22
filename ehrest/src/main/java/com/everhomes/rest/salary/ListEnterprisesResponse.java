package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>
 * <li>enterprises: 子公司列表 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * </ul>
 */
public class ListEnterprisesResponse {
    public List<OrganizationDTO> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<OrganizationDTO> enterprises) {
        this.enterprises = enterprises;
    }

    @ItemType(OrganizationDTO.class)
    List<OrganizationDTO> enterprises;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
