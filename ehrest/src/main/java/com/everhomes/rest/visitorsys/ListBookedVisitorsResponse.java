// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: (选填)下一页锚点</li>
 * <li>visitorDtoList: (必填)访客/预约列表，{@link com.everhomes.rest.visitorsys.BaseVisitorDTO}</li>
 * </ul>
 */
public class ListBookedVisitorsResponse {
    private Long nextPageAnchor;
    @ItemType(BaseVisitorDTO.class)
    private List<BaseVisitorDTO> visitorDtoList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BaseVisitorDTO> getVisitorDtoList() {
        return visitorDtoList;
    }

    public void setVisitorDtoList(List<BaseVisitorDTO> visitorDtoList) {
        this.visitorDtoList = visitorDtoList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
