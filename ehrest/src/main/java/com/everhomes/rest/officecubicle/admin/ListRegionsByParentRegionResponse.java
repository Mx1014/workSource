// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *regions:区域集合 {@link com.everhomes.rest.officecubicle.admin.RegionDTO}
 */
public class ListRegionsByParentRegionResponse {

    @ItemType(RegionDTO.class)
    private List<RegionDTO> regions;

    public List<RegionDTO> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionDTO> regions) {
        this.regions = regions;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
