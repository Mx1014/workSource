package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>userId: 用户ID</li>
 * <li>paymentSceneAppId: 支付应用场景ID</li>
 * <li>payAmount: 支付金额，金额*100</li>
 * <li>payStatus: 0:支付失败 1: 支付成功</li>
 * </ul>
 */
public class TestPaymentCallbackCommand {
    private Integer namespaceId;
    private Long organizationId;
    private Long userId;
    private Long paymentSceneAppId;
    private Long payAmount;
    private Byte payStatus;

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

    public Long getPaymentSceneAppId() {
        return paymentSceneAppId;
    }

    public void setPaymentSceneAppId(Long paymentSceneAppId) {
        this.paymentSceneAppId = paymentSceneAppId;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
