package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>details: 详情列表 {@link com.everhomes.rest.point.PointTutorialDetailDTO}</li>
 * </ul>
 */
public class ListPointTutorialDetailResponse {

    private Long nextPageAnchor;

    @ItemType(PointTutorialDetailDTO.class)
    private List<PointTutorialDetailDTO> details;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PointTutorialDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<PointTutorialDetailDTO> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
