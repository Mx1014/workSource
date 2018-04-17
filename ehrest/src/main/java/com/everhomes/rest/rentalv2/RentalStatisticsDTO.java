package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>name: 名字 可能是公司名 可能是资源名</li>
 * <li>amount: 金额</li>
 * <li>OrderCount: 订单数量</li>
 * <li>usedTime: 使用时长</li>
 * </ul>
 */
public class RentalStatisticsDTO {
    private String name;
    private BigDecimal amount;
    private Integer OrderCount;
    private BigDecimal usedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(Integer orderCount) {
        OrderCount = orderCount;
    }

    public BigDecimal getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
    }
}
