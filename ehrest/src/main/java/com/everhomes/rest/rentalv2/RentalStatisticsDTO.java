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
    private Long enterpriseId;
    private BigDecimal amount;
    private Integer orderCount;
    private Long usedTime;

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
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Long usedTime) {
        this.usedTime = usedTime;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
