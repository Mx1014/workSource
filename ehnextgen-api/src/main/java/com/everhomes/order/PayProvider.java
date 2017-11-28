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
     * 根据帐号列出提现列表
     * @param ownerType 帐号类型，如EhOrganizations, EhUsers
     * @param ownerId 帐号ID， 如企业ID、用户ID
     * @param pageAnchor页码锚点
     * @param pageSize 每页的数量
     * @return 提现订单列表
     */
    List<PaymentWithdrawOrder> listPaymentWithdrawOrders(String ownerType, Long ownerId,
            Long pageAnchor, int pageSize);
    
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
