//@formatter:off
package com.everhomes.order;

import com.everhomes.rest.order.PayMethodDTO;

import java.util.List;

/**
 * <ul>
 * </ul>
 */
public interface PayProvider {
    PaymentOrderRecord findOrderRecordByOrder(String orderType, Long orderId);

    PaymentOrderRecord findOrderRecordByOrderIdAndPaymentOrderId(Long orderId, Long paymentOrderId);

    PaymentUser findPaymentUserByOwner(String ownerType, Long ownerId);

    PaymentAccount findPaymentAccountBySystemId(Integer systemId);

    void createPaymentOrderRecord(PaymentOrderRecord orderRecord);

    Long getNewPaymentOrderRecordId();

    void createPaymentUser(PaymentUser paymentUser);

    Long getNewPaymentUserId();

    List<PayMethodDTO> listPayMethods(Integer namespaceId, Integer paymentType, String orderType, String ownerType, Long ownerId, String resourceType, Long resourceId);
}
