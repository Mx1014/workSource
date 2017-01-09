//@formatter:off
package com.everhomes.rest.ui.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *<ul>
 *     <li>nextPageAnchor:下一页</li>
 *     <li>entities: 活动列表 {@link com.everhomes.rest.promotion.ModulePromotionEntityDTO}</li>
 *</ul>
 */
public class ListActivityPromotionEntitiesBySceneReponse {

    private Long nextPageAnchor;
    @ItemType(value = ModulePromotionEntityDTO.class)
    private List<ModulePromotionEntityDTO> entities = new ArrayList<>();

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ModulePromotionEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<ModulePromotionEntityDTO> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
