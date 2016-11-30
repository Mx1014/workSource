package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>community: 小区信息</li>
 *     <li>organizationList: 企业列表 {@link OrganizationDetailDTO}</li>
 * </ul>
 */
public class CommunityOrganizationTreeDTO {

    private CommunityDTO community;
    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationDetailDTO> organizationList;

    public CommunityDTO getCommunity() {
        return community;
    }

    public void setCommunity(CommunityDTO community) {
        this.community = community;
    }

    public List<OrganizationDetailDTO> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<OrganizationDetailDTO> organizationList) {
        this.organizationList = organizationList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
