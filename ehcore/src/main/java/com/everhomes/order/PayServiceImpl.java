//@formatter:off
package com.everhomes.order;

import org.springframework.stereotype.Service;

/**
 * Created by Wentian Wang on 2017/9/6.
 *
 * This is a "util" like service class for other businesses to call to make a payment. It is belongs to payment-2.0.
 *
 */


@Service
public class PayServiceImpl implements PayService {
    @Override
    public PreOrderCallBack createPreOrder(PreOrderMessage message) {
        return null;
    }

    @Override
    public PaymentResult paymentNotify(PaymentMessage message) {
        return null;
    }
}
