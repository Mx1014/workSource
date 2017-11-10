package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>name: 套餐名字</li>
 * <li>minPrice: 最小价格</li>
 * <li>maxPrice: 最大价格</li>
 * </ul>
 */
public class SitePricePackageDto {

    private String name;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private String priceStr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }
}
