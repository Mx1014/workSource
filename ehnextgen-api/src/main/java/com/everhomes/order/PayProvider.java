//@formatter:off
package com.everhomes.order;

import com.everhomes.rest.order.PayMethodDTO;

import java.util.List;

/**
 * Created by Wentian on 2017/9/7.
 */
public interface PayProvider {
    PaymentOrderRecord findOrderRecordByOrder(String orderType, Long orderId);

    PaymentUser findPaymentUserByOwner(String ownerType, Long ownerId);

    PaymentAccount findPaymentAccountBySystemId(Integer systemId);

    void createPaymentOrderRecord(PaymentOrderRecord orderRecord);

    Long getNewPaymentOrderRecordId();

    List<PayMethodDTO> listPayMethods(Integer namespaceId, String orderType, String ownerType, Long ownerId, String resourceType, Long resourceId);
}
