package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/14.
 */
public class SearchCommunityMapContentsResponse {

    @ItemType(CommunityMapOrganizationDTO.class)
    private List<CommunityMapOrganizationDTO> organizations;

    @ItemType(CommunityMapBuildingDTO.class)
    private List<CommunityMapBuildingDTO> buildings;

    @ItemType(CommunityMapShopDTO.class)
    private List<CommunityMapShopDTO> shops;

    private Long nextPageAnchor;

    public List<CommunityMapOrganizationDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<CommunityMapOrganizationDTO> organizations) {
        this.organizations = organizations;
    }

    public List<CommunityMapBuildingDTO> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<CommunityMapBuildingDTO> buildings) {
        this.buildings = buildings;
    }

    public List<CommunityMapShopDTO> getShops() {
        return shops;
    }

    public void setShops(List<CommunityMapShopDTO> shops) {
        this.shops = shops;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
