//@formatter:off
package com.everhomes.rest.order;


import javax.validation.constraints.NotNull;


/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>clientAppName: 客户端realm值</li>
 *     <li>orderType: orderType {@link com.everhomes.rest.order.OrderType}</li>
 *     <li>orderId: 订单Id</li>
 *     <li>payerId: 卖家用户ID</li>
 *     <li>amount: 支付金额，BigDecimal转换本类提供了方法changePayAmount</li>
 *     <li>resourceType: 订单资源类型</li>
 *     <li>resourceId: 订单资源类型ID</li>
 *     <li>openid: 微信用户的openId</li>
 *     <li>summary: summary</li>
 *     <li>expiration:  过期时间</li>
 *     <li>extendInfo: 额外信息，将原样返回</li>
 *     <li>paymentType: 支付类型，不传则获取除微信公众号以外的所有方式，微信公众号必传 参考{@link com.everhomes.pay.order.PaymentType}</li>
 *     <li>paymentParams: 支付参数 微信公众号必传 {@link com.everhomes.rest.order.PaymentParamsDTO}</li>
 * </ul>
 */
public class PreOrderCommand {

    @NotNull
    private Integer namespaceId;
    @NotNull
    private String clientAppName;
    @NotNull
    private String orderType;
    @NotNull
    private Long orderId;
    @NotNull
    private Long payerId;
    @NotNull
    private Long amount;

    //    private String ownerType;
//    private Long ownerId;
    private String resourceType;
    private Long resourceId;
    private String openid;
    //private String summary;
    private Long expiration;
    private String extendInfo;
    private Integer paymentType;
    //支付微信公众号必填
    private PaymentParamsDTO paymentParams;

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

//    public String getOwnerType() {
//        return ownerType;
//    }
//
//    public void setOwnerType(String ownerType) {
//        this.ownerType = ownerType;
//    }
//
//    public Long getOwnerId() {
//        return ownerId;
//    }
//
//    public void setOwnerId(Long ownerId) {
//        this.ownerId = ownerId;
//    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

//    public String getSummary() {
//        return summary;
//    }
//
//    public void setSummary(String summary) {
//        this.summary = summary;
//    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentParamsDTO getPaymentParams() {
        return paymentParams;
    }

    public void setPaymentParams(PaymentParamsDTO paymentParams) {
        this.paymentParams = paymentParams;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public PreOrderCommand() {
    }

}
