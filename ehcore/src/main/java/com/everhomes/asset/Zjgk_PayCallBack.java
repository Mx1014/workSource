//@formatter:off
package com.everhomes.asset;

import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import org.springframework.stereotype.Component;

/**
 * Created by Wentian Wang on 2017/9/13.
 */
@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+ "zjgkrentalcode")
public class Zjgk_PayCallBack implements PaymentCallBackHandler{
    @Override
    public void paySuccess(OrderPaymentNotificationCommand cmd) {
        //success
        //调用张江高科的支付接口
    }

    @Override
    public void payFail(OrderPaymentNotificationCommand cmd) {
        //failed
        //调用张江高科的支付接口
    }
}
