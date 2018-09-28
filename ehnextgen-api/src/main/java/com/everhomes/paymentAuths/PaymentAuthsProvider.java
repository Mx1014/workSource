// @formatter:off
package com.everhomes.paymentAuths;

import java.util.List;

import com.everhomes.serviceModuleApp.ServiceModuleApp;


public interface PaymentAuthsProvider {

	Long findPaymentAuthByAppIdOrgId(String appId, Long orgId);

}