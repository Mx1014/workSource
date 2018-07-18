package com.everhomes.rest.customer.openapi;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Rui.Jia  2018/7/13 17 :26
 */

public class ListBuildingResponse {
    private Long nextPageAnchor;

    @ItemType(BuildingDTO.class)
    private List<BuildingDTO> buildings;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BuildingDTO> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingDTO> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
