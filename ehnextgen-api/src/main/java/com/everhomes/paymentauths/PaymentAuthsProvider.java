// @formatter:off
package com.everhomes.paymentauths;

import java.util.List;


public interface PaymentAuthsProvider {

	EnterprisePaymentAuths findPaymentAuth (Long appId, Long orgId, Long sourceId);

	List<EnterprisePaymentAuths> getPaymentAuths(Integer namespaceId, Long orgId);

	void createEnterprisePaymentAuths(List<EnterprisePaymentAuths> enterpriesAuths, Long appId, Long orgId);
}