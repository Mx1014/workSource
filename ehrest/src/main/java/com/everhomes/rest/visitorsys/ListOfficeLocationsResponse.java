// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: (选填)下一页锚点</li>
 * <li>officeLocationList: (必填)办公地点列表，{@link com.everhomes.rest.visitorsys.BaseOfficeLocationDTO}</li>
 * </ul>
 */
public class ListOfficeLocationsResponse {
    private Long nextPageAnchor;
    @ItemType(BaseOfficeLocationDTO.class)
    private List<BaseOfficeLocationDTO> officeLocationList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BaseOfficeLocationDTO> getOfficeLocationList() {
        return officeLocationList;
    }

    public void setOfficeLocationList(List<BaseOfficeLocationDTO> officeLocationList) {
        this.officeLocationList = officeLocationList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
