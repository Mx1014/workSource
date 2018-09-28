// @formatter:off
package com.everhomes.paymentauths;

import java.util.List;

import com.everhomes.serviceModuleApp.ServiceModuleApp;


public interface PaymentAuthsProvider {

	EnterprisePaymentAuths findPaymentAuthByAppIdOrgId(Long appId, Long orgId);

	List<EnterprisePaymentAuths> getPaymentAuths(Integer namespaceId);

}