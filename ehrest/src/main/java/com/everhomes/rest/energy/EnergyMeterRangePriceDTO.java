package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>minValue: 下界值</li>
 *     <li>maxValue: 上界值</li>
 *     <li>upperBoundary: 上边界</li>
 *     <li>lowerBoundary: 下边界 参考</li>
 *     <li>price: 区间价格</li>
 * </ul>
 */
public class EnergyMeterRangePriceDTO {

    private String minValue;

    private String maxValue;

    private String upperBoundary;

    private String lowerBoundary;

    private BigDecimal price;

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(String upperBoundary) {
        this.upperBoundary = upperBoundary;
    }

    public String getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(String lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
