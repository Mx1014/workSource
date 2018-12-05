// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.math.BigDecimal;

public interface EnterprisePaymentAuthSceneLimitProvider {

	void createEnterprisePaymentAuthSceneLimit(EnterprisePaymentAuthSceneLimit enterprisePaymentAuthSceneLimit);

	void updateEnterprisePaymentAuthSceneLimit(EnterprisePaymentAuthSceneLimit enterprisePaymentAuthSceneLimit);

	void incrHistoricalTotalPayAmountAndPayCount(Integer namespaceId, Long organizationId, Long paymentSceneAppId, BigDecimal payAmount, Integer payCount);

	EnterprisePaymentAuthSceneLimit findEnterprisePaymentAuthSceneLimitByAppId(Long organizationId, Integer currentNamespaceId, Long sceneAppId);
}