package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>userId: 用户ID</li>
 * <li>appId: 支付应用ID</li>
 * <li>payAmount: 支付金额，金额*100</li>
 * <li>merchantOrderId: 统一订单系统订单号</li>
 * <li>paymentType: 支付方式</li>
 * <li>bizOrderNum: 业务对应的订单编号</li>
 * <li>refundFlag: 是否退款操作，0: 否 1: 是</li>
 * </ul>
 */
public class PaymentAuthUnFrozenCommand {
    private Integer namespaceId;
    private Long organizationId;
    private Long userId;
    private Long appId;
    private Long payAmount;
    private Long merchantOrderId;
    private Integer paymentType;
    private String bizOrderNum;
    private Byte refundFlag;
    private String appkey;
    private String signature;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(Long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getBizOrderNum() {
        return bizOrderNum;
    }

    public void setBizOrderNum(String bizOrderNum) {
        this.bizOrderNum = bizOrderNum;
    }

    public Byte getRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(Byte refundFlag) {
        this.refundFlag = refundFlag;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
