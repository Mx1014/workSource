package com.everhomes.rest.address;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 获得最近的小区或者园区 on 2017/8/14.
 */
public class ListNearbyMixCommunitiesCommandV2Response {

    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> resudentials;
    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> commercials;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<CommunityDTO> getResudentials() {
        return resudentials;
    }

    public void setResudentials(List<CommunityDTO> resudentials) {
        this.resudentials = resudentials;
    }

    public List<CommunityDTO> getCommercials() {
        return commercials;
    }

    public void setCommercials(List<CommunityDTO> commercials) {
        this.commercials = commercials;
    }
}
