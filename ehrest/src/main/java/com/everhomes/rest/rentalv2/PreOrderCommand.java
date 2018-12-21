package com.everhomes.rest.rentalv2;

import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.promotion.order.PayerInfoDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>communityId: 园区id</li>
 *     <li>clientAppName: 客户端realm值</li>
 *     <li>orderType: orderType {@link com.everhomes.rest.order.OrderType}</li>
 *     <li>orderId: 订单Id</li>
 *     <li>payerId: 付款方ID</li>
 *     <li>amount: 支付金额，BigDecimal转换本类提供了方法changePayAmount</li>
 *     <li>resourceType: 订单资源类型</li>
 *     <li>resourceId: 订单资源类型ID</li>
 *     <li>openid: 微信用户的openId</li>
 *     <li>summary: summary</li>
 *     <li>expiration:  过期时间</li>
 *     <li>extendInfo: 额外信息，将原样返回</li>
 *     <li>paymentType: 支付类型，不传则获取除微信公众号以外的所有方式，微信公众号必传 参考{@link com.everhomes.pay.order.PaymentType}</li>
 *     <li>paymentParams: 支付参数 微信公众号必传 {@link com.everhomes.rest.order.PaymentParamsDTO}</li>
 *     <li>bizPayeeType:收款方账户类型：EhUsers/EhOrganizations</li>
 *     <li>bizPayeeId:收款方账户id</li>
 *     <li>accountName:收款方账户名称</li>
 * </ul>
 */
public class PreOrderCommand {

    @NotNull
    private Integer namespaceId;
    private Long communityId;
    @NotNull
    private String clientAppName;
    @NotNull
    private String orderType;
    @NotNull
    private Long orderId;

    /** 付款方类型，参考{@link com.everhomes.rest.order.OwnerType}，若不填则默认是个人 */
    // 由于后面付款方有可能是企业钱包，故需要加上该类型来区分  by lqs 20180526
    private String payerType;

    @NotNull
    private Long payerId;
    private Long merchantId;


    /** 收款方ID，注意：该ID来自于支付系统，并记录在业务自己的关联表中 */
    // 由于产品方案修改，支付收款方由各业务自己记录，故添加该ID，并且做兼容，当业务不填该ID时则取自通用表 by lqs 20180526
    private String bizPayeeType;
    private Long bizPayeeId;
    private String accountName;

    @NotNull
    private BigDecimal amount;

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


    private Integer commitFlag;

    public Integer getCommitFlag() {
        return commitFlag;
    }

    public void setCommitFlag(Integer commitFlag) {
        this.commitFlag = commitFlag;
    }

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

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public String getBizPayeeType() {
        return bizPayeeType;
    }

    public void setBizPayeeType(String bizPayeeType) {
        this.bizPayeeType = bizPayeeType;
    }

    public Long getBizPayeeId() {
        return bizPayeeId;
    }

    public void setBizPayeeId(Long bizPayeeId) {
        this.bizPayeeId = bizPayeeId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

}
