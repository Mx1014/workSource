package com.everhomes.rest.pmtask;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/10/20.
 */
public class ListOrganizationCommunityByUserResponse {

    private Long nextPageAnchor;

    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> communities;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<CommunityDTO> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CommunityDTO> communities) {
        this.communities = communities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
