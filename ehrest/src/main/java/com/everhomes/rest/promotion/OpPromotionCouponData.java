package com.everhomes.rest.promotion;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class OpPromotionCouponData {
    private String url;
    
    @ItemType(Long.class)
    List<Long> couponList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public List<Long> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Long> couponList) {
        this.couponList = couponList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
