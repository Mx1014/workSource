package com.everhomes.rest.business;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *<ul>
 *     <li>nextPageAnchor:下一页</li>
 *     <li>entities: 商品列表 {@link com.everhomes.rest.business.BusinessPromotionEntityDTO}</li>
 *</ul>
 */
public class ListBusinessPromotionEntitiesReponse {

    private Long nextPageAnchor;
    @ItemType(value = BusinessPromotionEntityDTO.class)
    private List<BusinessPromotionEntityDTO> entities = new ArrayList<>();

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BusinessPromotionEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<BusinessPromotionEntityDTO> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
