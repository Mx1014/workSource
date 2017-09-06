//@formatter:off
package com.everhomes.order;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public interface PayService {
    PreOrderCallBack createPreOrder(PreOrderMessage message);
    PaymentResult paymentNotify(PaymentMessage message);
}
