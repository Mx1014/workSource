package com.everhomes.rentalv2;

import com.everhomes.rest.promotion.order.CreateMerchantOrderCommand;

import java.math.BigDecimal;

/**
 * @author sw on 2018/1/9.
 */
public interface RentalOrderHandler {
    String RENTAL_ORDER_HANDLER_PREFIX = "RentalOrderHandler-";
    String DEFAULT = "default";

    /**
     * 获取订单退款价格
     * @param order
     * @param time
     * @return
     */
    public BigDecimal getRefundAmount(RentalOrder order, Long time);

    /**
     * 新增订单时更新订单资源信息,更新资源状态使用中
     * @param order
     */
    public void updateOrderResourceInfo(RentalOrder order);

    /**
     * 锁住订单资源
     * @param order
     */
    public void lockOrderResourceStatus(RentalOrder order);

    /**
     * 释放订单资源状态
     * @param order
     */
    public void releaseOrderResourceStatus(RentalOrder order);

    /**
     * 订单完成时更新订单信息
     * @param order
     */
    public void completeRentalOrder(RentalOrder order);

    public void autoUpdateOrder(RentalOrder order);

    public void checkOrderResourceStatus(RentalOrder order);

    //获取收款方账户id
    public Long getAccountId(RentalOrder order);

    default public Long gerMerchantId(RentalOrder order){return null;}

    default public void processCreateMerchantOrderCmd(CreateMerchantOrderCommand cmd,RentalOrder order){}
}
