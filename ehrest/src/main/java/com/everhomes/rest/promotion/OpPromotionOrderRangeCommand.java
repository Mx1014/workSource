package com.everhomes.rest.promotion;

public class OpPromotionOrderRangeCommand {
    Long userId;
    Double price;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}
