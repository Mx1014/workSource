// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>sceneAppId: 支付场景id</li>
 * <li>limitAmount: 每月总额 </li>
 * </ul>
 */
public class CreateOrUpdateEnterprisePaymentSceneLimitCommand {
    private Long organizationId;
    private Long sceneAppId;
    private BigDecimal limitAmount;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getSceneAppId() {
        return sceneAppId;
    }

    public void setSceneAppId(Long sceneAppId) {
        this.sceneAppId = sceneAppId;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
