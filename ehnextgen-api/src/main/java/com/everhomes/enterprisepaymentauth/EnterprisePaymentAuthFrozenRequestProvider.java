// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.sql.Timestamp;
import java.util.List;

public interface EnterprisePaymentAuthFrozenRequestProvider {

	void createEnterprisePaymentAuthRequest(EnterprisePaymentAuthFrozenRequest enterprisePaymentAuthFrozenRequest);

	void updateEnterprisePaymentAuthRequest(EnterprisePaymentAuthFrozenRequest enterprisePaymentAuthFrozenRequest);

	void deleteEnterprisePaymentAuthRequest(EnterprisePaymentAuthFrozenRequest enterprisePaymentAuthFrozenRequest);

	EnterprisePaymentAuthFrozenRequest findEnterprisePaymentAuthRequestByOrderId(Integer namespaceId, Long organizationId, Long userId, Long paymentSceneAppId, Long orderId);

	List<EnterprisePaymentAuthFrozenRequest> findEnterprisePaymentAuthRequestsByFrozened(int count, Timestamp betweenFrom, Timestamp betweenTo);

}