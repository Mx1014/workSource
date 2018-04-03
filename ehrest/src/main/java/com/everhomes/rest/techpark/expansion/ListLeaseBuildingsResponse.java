package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/3.
 */
public class ListLeaseBuildingsResponse {
    private Long nextPageAnchor;
    @ItemType(LeaseBuildingDTO.class)
    private List<LeaseBuildingDTO> leaseBuildingDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<LeaseBuildingDTO> getLeaseBuildingDTOs() {
        return leaseBuildingDTOs;
    }

    public void setLeaseBuildingDTOs(List<LeaseBuildingDTO> leaseBuildingDTOs) {
        this.leaseBuildingDTOs = leaseBuildingDTOs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
