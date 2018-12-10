package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

public class ShopInfoDTO {
    private String shopName;
    private String shopNo;
    private String shopUrl;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
