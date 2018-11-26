package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>sceneAppId: 支付应用场景id</li>
 * <li>sceneAppName: 支付应用场景名称</li>
 * <li>limitAmount: 月额度</li>
 * </ul>
 */
public class EmployeePaymentSceneLimitSimpleDTO {
    private Long sceneAppId;
    private String sceneAppName;
    private BigDecimal limitAmount;

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

	public String getSceneAppName() {
		return sceneAppName;
	}

	public void setSceneAppName(String sceneAppName) {
		this.sceneAppName = sceneAppName;
	}

}
