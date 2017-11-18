//@formatter:off
package com.everhomes.order;


import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.PaymentParamsDTO;

import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.order.SettlementAmountDTO;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public interface PayService {
    /**
     *
     * @param namespaceId
     * @param clientAppName
     * @param orderType
     * @param orderId
     * @param payerId
     * @param amount
     * @return
     */
    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, Long expiration);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId);

    PreOrderDTO createAppPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String resourceType, Long resourceId, Long expiration);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, Long expiration);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, String resourceType, Long resourceId);

    PreOrderDTO createWxJSPreOrder(Integer namespaceId, String clientAppName, String orderType, Long orderId, Long payerId, Long amount, String openid, PaymentParamsDTO paramsDTO, String resourceType, Long resourceId, Long expiration);

    /**
     * 1、检查是否已经下单
     * 2、检查买方是否有会员，无则创建
     * 3、收款方是否有会员，无则报错
     * 4、获取在支付系统中的账号，用户与支付系统交互
     * 5、组装报文，发起下单请求
     * 6、组装支付方式
     * 7、保存订单信息
     * 8、返回结果
     * @param cmd
     * @return
     */
    PreOrderDTO createPreOrder(PreOrderCommand cmd);

    /**
     * 用于接受支付系统的回调信息
     * @param cmd
     */
    void payNotify(OrderPaymentNotificationCommand cmd);


    Long changePayAmount(BigDecimal amount);

    BigDecimal changePayAmount(Long amount);

    CreateOrderRestResponse refund(String orderType, Long payOrderId, Long refundOrderId, Long amount);
    //把这个方法暴露出来，方便我的模块。--闻天
    PaymentUser createPaymentUser(int businessUserType, String ownerType, Long ownerId);
    
    /**
     * 获取帐户结算金额和可提现金额信息
     * @param ownerType 帐户类型（如EhUsers、EhOrganizations）
     * @param ownerId 帐户对应的ID（如用户ID、企业ID）
     * @return  结算金额和可提现金额信息
     */
    SettlementAmountDTO getPaymentSettlementAmounts(String ownerType, Long ownerId);
    
    /**
     * 获取帐户结算金额数量，通过指定的结算状态来获取对应的金额数量
     * @param paymentUserId 帐户ID（支付系统中使用的帐户ID）
     * @param settlementStatus 结算状态：已结算、未结算
     * @return 金额数量
     */
    Long getPaymentAmountBySettlement(Long paymentUserId, Integer settlementStatus);
    
    /**
     * 获取帐户已提款金额数量
     * @param paymentUserId 帐户ID（支付系统中使用的帐户ID）
     * @return 金额数量
     */
    Long getPaymentAmountByWithdraw(Long paymentUserId);
    
    /**
     * 获取帐户已退款金额数量
     * @param paymentUserId 帐户ID（支付系统中使用的帐户ID）
     * @return 金额数量
     */
    Long getPaymentAmountByRefund(Long paymentUserId);
}
