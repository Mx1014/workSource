package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>activityId: 活动id</li>
 *     <li>name: 物品名称</li>
 *     <li>price: 单价</li>
 *     <li>quantity: 数量</li>
 *     <li>totalPrice: 总价</li>
 *     <li>handlers: 经手人</li>
 * </ul>
 */
public class CreateActivityGoodsCommand {

    private Long activityId;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String handlers;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getHandlers() {
        return handlers;
    }

    public void setHandlers(String handlers) {
        this.handlers = handlers;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
