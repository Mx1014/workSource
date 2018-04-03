package com.everhomes.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>shopNo: shopNo</li>
 *     <li>commoNo: commoNo</li>
 * </ul>
 */
public class ShopCommodityCmd {

    private String shopNo;
    private String commoNo;

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getCommoNo() {
        return commoNo;
    }

    public void setCommoNo(String commoNo) {
        this.commoNo = commoNo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
