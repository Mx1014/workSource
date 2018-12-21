package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

public class ShopInfoDTO {
    private String shopName;
    private String shopNo;
    private String shopURL;

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

    public String getShopURL() {
        return shopURL;
    }

    public void setShopURL(String shopURL) {
        this.shopURL = shopURL;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
