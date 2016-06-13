// @formatter:off
package com.everhomes.rest.fleamarket;

import com.everhomes.util.StringHelper;

public class FleaMarketItemDTO {
    private Integer barterFlag;
    private String price;
    private Integer closeFlag;
    
    public FleaMarketItemDTO() {
    }

    public Integer getBarterFlag() {
        return barterFlag;
    }

    public void setBarterFlag(Integer barterFlag) {
        this.barterFlag = barterFlag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(Integer closeFlag) {
        this.closeFlag = closeFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
