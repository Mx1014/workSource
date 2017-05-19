package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by R on 2017/5/19.
 * <ul>
 * <li>insurances: 对应的工作经历，参考{@link com.everhomes.rest.organization.OrganizationMemberInsurancesDTO}</li>
 * </ul>
 */
public class ListOrganizationMemberInsurancesResponse {


    @ItemType(OrganizationMemberInsurancesDTO.class)
    private List<OrganizationMemberInsurancesDTO> insurances;

    public ListOrganizationMemberInsurancesResponse() {
    }

    public List<OrganizationMemberInsurancesDTO> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<OrganizationMemberInsurancesDTO> insurances) {
        this.insurances = insurances;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
