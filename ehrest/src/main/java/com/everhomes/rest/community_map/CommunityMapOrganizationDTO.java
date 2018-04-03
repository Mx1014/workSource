package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/14.
 */
public class CommunityMapOrganizationDTO {

    private Long    id;
    private String  name;

    private String logo;

    @ItemType(CommunityMapBuildingDTO.class)
    private List<CommunityMapBuildingDTO> buildings;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommunityMapBuildingDTO> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<CommunityMapBuildingDTO> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
