package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>communities: 小区列表 {@link com.everhomes.rest.organization.CommunityOrganizationTreeDTO}</li>
 * </ul>
 */
public class CommunityOrganizationTreeResponse {

    @ItemType(CommunityOrganizationTreeDTO.class)
    private List<CommunityOrganizationTreeDTO> communities;

    public List<CommunityOrganizationTreeDTO> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CommunityOrganizationTreeDTO> communities) {
        this.communities = communities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
