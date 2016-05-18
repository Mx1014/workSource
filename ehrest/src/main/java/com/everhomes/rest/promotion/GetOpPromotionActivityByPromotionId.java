package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>promotionId: promotion对象对应的 Id</li>
 * </ul>
 * @author janson
 *
 */
public class GetOpPromotionActivityByPromotionId {
    Long promotionId;

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
