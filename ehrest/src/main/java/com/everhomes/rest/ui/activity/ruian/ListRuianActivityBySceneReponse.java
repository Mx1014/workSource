//@formatter:off
package com.everhomes.rest.ui.activity.ruian;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.activity.ruian.ActivityRuianDetail;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *<ul>
 *     <li>nextPageAnchor:下一页</li>
 *     <li>entities: 活动 {@link ActivityRuianDetail}</li>
 *</ul>
 */
public class ListRuianActivityBySceneReponse {

    private Long nextPageAnchor;

    private List<ActivityRuianDetail> entities = new ArrayList<>();

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ActivityRuianDetail> getEntities() {
        return entities;
    }

    public void setEntities(List<ActivityRuianDetail> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
