package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 * <li>contracts: 对应的工作经历，参考{@link com.everhomes.rest.organization.OrganizationMemberContractsDTO}</li>
 * </ul>
 */
public class ListOrganizationMemberContractsResponse {


    @ItemType(OrganizationMemberContractsDTO.class)
    private List<OrganizationMemberContractsDTO> contracts;

    public ListOrganizationMemberContractsResponse() {
    }

    public List<OrganizationMemberContractsDTO> getContracts() {
        return contracts;
    }

    public void setContracts(List<OrganizationMemberContractsDTO> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
