// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.math.BigDecimal;
import java.util.List;

public interface EnterprisePaymentAuthScenePayHistoryProvider {

	void createEnterprisePaymentAuthScenePayHistory(EnterprisePaymentAuthScenePayHistory enterprisePaymentAuthScenePayHistory);

	void updateEnterprisePaymentAuthScenePayHistory(EnterprisePaymentAuthScenePayHistory enterprisePaymentAuthScenePayHistory);

	EnterprisePaymentAuthScenePayHistory findEnterprisePaymentAuthScenePayHistoryByAppId(Long organizationId, Integer currentNamespaceId, Long sceneAppId, String format);

	void decrUsedAmountAndPayCount(Integer namespaceId, Long organizationId, Long sceneAppId, BigDecimal payAmount, String payMonth);

}