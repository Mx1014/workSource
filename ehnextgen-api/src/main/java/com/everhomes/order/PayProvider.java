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

    PaymentOrderRecord findOrderRecordById(Long id);

    PaymentUser findPaymentUserByOwner(String ownerType, Long ownerId);

    PaymentAccount findPaymentAccountBySystemId(Integer systemId);

    void createPaymentOrderRecord(PaymentOrderRecord orderRecord);

    Long getNewPaymentOrderRecordId();

    void createPaymentUser(PaymentUser paymentUser);

    Long getNewPaymentUserId();

    List<PayMethodDTO> listPayMethods(Integer namespaceId, Integer paymentType, String orderType, String ownerType, Long ownerId, String resourceType, Long resourceId);

    PaymentOrderRecord findOrderRecordByOrderNum(String bizOrderNum);
    
    /**
     * 创建提现订单
     * @param order 订单信息
     */
    void createPaymentWithdrawOrder(PaymentWithdrawOrder order);
    
    /**
     * 根据提现订单编号来查询订单信息。（订单编号与订单ID不是同一字段）
     * @param orderNo 订单编号
     * @return 订单信息
     */
    PaymentWithdrawOrder findPaymentWithdrawOrderByOrderNo(Long orderNo);
    
    /**
     * 更新提现订单信息
     * @param order 订单信息
     */
    void updatePaymentWithdrawOrder(PaymentWithdrawOrder order);
    
}
