package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>systems: systems</li>
 * </ul>
 */
public class GetEnabledPointSystemResponse {

    @ItemType(PointSystemDTO.class)
    private List<PointSystemDTO> systems;

    public GetEnabledPointSystemResponse(List<PointSystemDTO> dtoList) {
        this.systems = dtoList;
    }

    public GetEnabledPointSystemResponse() {
    }

    public List<PointSystemDTO> getSystems() {
        return systems;
    }

    public void setSystems(List<PointSystemDTO> systems) {
        this.systems = systems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
