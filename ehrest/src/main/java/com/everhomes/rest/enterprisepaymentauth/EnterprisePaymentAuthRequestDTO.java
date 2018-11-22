package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>userId: 用户ID</li>
 * <li>paymentSceneAppId: 支付应用场景ID</li>
 * <li>random: 随机数</li>
 * </ul>
 */
public class EnterprisePaymentAuthRequestDTO {
    private Integer namespaceId;
    private Long organizationId;
    private Long userId;
    private Long paymentSceneAppId;
    private Long random;

    public EnterprisePaymentAuthRequestDTO() {

    }

    public EnterprisePaymentAuthRequestDTO(Integer namespaceId, Long organizationId, Long userId, Long paymentSceneAppId, Long random) {
        this.namespaceId = namespaceId;
        this.organizationId = organizationId;
        this.userId = userId;
        this.paymentSceneAppId = paymentSceneAppId;
        this.random = random;
    }

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

    public Long getRandom() {
        return random;
    }

    public void setRandom(Long random) {
        this.random = random;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
