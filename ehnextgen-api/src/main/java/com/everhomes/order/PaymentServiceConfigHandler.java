//@formatter:off
package com.everhomes.order;


/**
 * <p>提供给业务实现的方法，此方法用户获取收款方<p>
 * <p>实现支付回调，{@code com.everhomes.organization.PropertyOrderEmbeddedHandler}</p>
 */
public interface PaymentServiceConfigHandler {
    String PAYMENT_SERVICE_CONFIG_HANDLER_PREFIX = "paymentServiceConfig-";
    /**
     * 获取
     */
    PaymentServiceConfig findPaymentServiceConfig(Integer namespaceId, String orderType, String resourceType, Long resourceId);

}
