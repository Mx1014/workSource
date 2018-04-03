package com.everhomes.rest.address;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>resudentials:小区列表</li>
 * <li>commercials:园区列表</li>
 * </ul>
 */
public class ListNearbyMixCommunitiesCommandV2Response {

    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> resudentials;
    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> commercials;
    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> dtos;

    private Long nextPageAnchor;

    public ListNearbyMixCommunitiesCommandV2Response() {
    }

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

    public List<CommunityDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CommunityDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
