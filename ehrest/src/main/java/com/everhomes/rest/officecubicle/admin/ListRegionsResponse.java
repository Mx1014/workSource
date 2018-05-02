// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 *<li>regions:区域集合 {@link com.everhomes.rest.officecubicle.admin.RegionDTO}</li>
 * <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListRegionsResponse {

    @ItemType(RegionDTO.class)
    private List<RegionDTO> regions;
    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

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
