package com.everhomes.promotion;

import com.everhomes.listing.ListingLocator;

public class OpPromotionUserVisitor {
    private OpPromotionActivity promotion;
    private OpPromotionUserVisitor parent;
    private Object value;
    private long pushCount;

    public OpPromotionActivity getPromotion() {
        return promotion;
    }

    public void setPromotion(OpPromotionActivity promotion) {
        this.promotion = promotion;
    }
    

    public OpPromotionUserVisitor getParent() {
        return parent;
    }

    public void setParent(OpPromotionUserVisitor parent) {
        this.parent = parent;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getPushCount() {
        return pushCount;
    }

    public void setPushCount(long pushCount) {
        this.pushCount = pushCount;
    }
    
    
   
}
