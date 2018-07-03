// @formatter:off
package com.everhomes.parking;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/6/8 9:47
 */
public interface ParkingOrderEmbeddedV2Handler {
    void payCallBack(OrderPaymentNotificationCommand cmd);
}
