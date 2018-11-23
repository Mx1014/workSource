// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.util.List;

public interface EnterprisePaymentAuthOperateLogProvider {

	void createEnterprisePaymentAuthOperateLog(EnterprisePaymentAuthOperateLog enterprisePaymentAuthOperateLog);

	List<EnterprisePaymentAuthOperateLog> listEnterprisePaymentAuthOperateLogs(Integer currentNamespaceId, Long organizationId, int i, int offset);

}