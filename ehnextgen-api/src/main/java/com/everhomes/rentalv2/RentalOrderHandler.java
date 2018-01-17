package com.everhomes.rentalv2;

import java.math.BigDecimal;

/**
 * @author sw on 2018/1/9.
 */
public interface RentalOrderHandler {
    String RENTAL_ORDER_HANDLER_PREFIX = "RentalOrderHandler-";
    String DEFAULT = "default";

    /**
     * 计算订单退款价格
     * @param order
     * @param time
     * @return
     */
    public BigDecimal calculateRefundAmount(RentalOrder order, Long time);

    /**
     * 新增订单时更新订单资源信息
     * @param order
     */
    public void updateOrderResourceInfo(RentalOrder order);

    /**
     * 订单完成时更新订单信息
     * @param order
     */
    public void completeRentalOrder(RentalOrder order);
}
