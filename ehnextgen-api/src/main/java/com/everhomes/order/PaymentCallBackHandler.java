//@formatter:off
package com.everhomes.order;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;

/**
 * Created by Wentian on 2017/9/7.
 */

/**
 * <p>订单支付回调处理类<p>
 * <ul>
 * 	<li>1.创建新类，实现本接口{@link com.everhomes.order.PaymentCallBackHandler}</li>
 * 	<li>2.在新类头部添加注解，如：{@code @Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+OrderType.XXX)}</li>
 * 	<li>3.实现{@code void paySuccess(OrderPaymentNotificationCommand cmd)}和{@code void payFail(OrderPaymentNotificationCommand cmd)}方法</li>
 * </ul>
 */
public interface PaymentCallBackHandler {
    String ORDER_PAYMENT_BACK_HANDLER_PREFIX = "orderPayment-";
    /**
     * 支付成功
     */
    void paySuccess(OrderPaymentNotificationCommand cmd);
    /**
     * 支付失败
     */
    void payFail(OrderPaymentNotificationCommand cmd);
}
