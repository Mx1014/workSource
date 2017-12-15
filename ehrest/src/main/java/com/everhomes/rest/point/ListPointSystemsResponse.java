package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>systems: systems {@link com.everhomes.rest.point.PointSystemDTO}</li>
 * </ul>
 */
public class ListPointSystemsResponse {

    private Long nextPageAnchor;

    @ItemType(PointSystemDTO.class)
    private List<PointSystemDTO> systems;

    public List<PointSystemDTO> getSystems() {
        return systems;
    }

    public void setSystems(List<PointSystemDTO> systems) {
        this.systems = systems;
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
