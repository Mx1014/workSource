//@formatter:off
package com.everhomes.order;

/**
 * Created by Wentian on 2017/9/7.
 */
public interface PayProvider {
    PaymentOrderRecord findOrderRecordById(Long orderId);
}
