// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>sceneAppId: 支付场景id</li>
 * </ul>
 */
public class GetEnterprisePaymentSceneLimitInfoCommand {
	private Long organizationId;
	private Long sceneAppId;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
