package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>tutorials: 指南列表 {@link com.everhomes.rest.point.PointTutorialDTO}</li>
 * </ul>
 */
public class ListPointTutorialResponse {

    private Long nextPageAnchor;

    @ItemType(PointTutorialDTO.class)
    private List<PointTutorialDTO> tutorials;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PointTutorialDTO> getTutorials() {
        return tutorials;
    }

    public void setTutorials(List<PointTutorialDTO> tutorials) {
        this.tutorials = tutorials;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
