package com.everhomes.rest.promotion;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListOpPromotionActivityResponse {

    List<OpPromotionActivityDTO> promotions;
    
    private Long nextPageAnchor;
    
    
    
    public List<OpPromotionActivityDTO> getPromotions() {
        return promotions;
    }



    public void setPromotions(List<OpPromotionActivityDTO> promotions) {
        this.promotions = promotions;
    }



    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }



    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
