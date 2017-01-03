package com.everhomes.rest.ui.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *<ul>
 *     <li>nextPageAnchor:下一页</li>
 *     <li>entities: 活动列表 {@link com.everhomes.rest.ui.activity.ActivityPromotionEntityDTO}</li>
 *</ul>
 */
public class ListActivityPromotionEntitiesBySceneReponse {

    private Long nextPageAnchor;
    @ItemType(value = ActivityPromotionEntityDTO.class)
    private List<ActivityPromotionEntityDTO> entities = new ArrayList<>();

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ActivityPromotionEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<ActivityPromotionEntityDTO> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
