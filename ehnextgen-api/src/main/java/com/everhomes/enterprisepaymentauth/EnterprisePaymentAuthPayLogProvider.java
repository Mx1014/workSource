// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.sql.Timestamp;
import java.util.List;

public interface EnterprisePaymentAuthPayLogProvider {

	void createEnterprisePaymentAuthPayLog(EnterprisePaymentAuthPayLog enterprisePaymentAuthPayLog);

	List<EnterprisePaymentAuthPayLog> listEnterprisePaymentAuthPayLogByDetailId(Integer namespaceId, Long organizationId, Long detailId, Integer offset, Integer pageSize);

	List<EnterprisePaymentAuthPayLog> listEnterprisePaymentAuthPayLogs(Integer currentNamespaceId, Long organizationId, Long paymentSceneAppId, Timestamp paymentStartDate, Timestamp paymentEndDate, List<Long> detailIds, Long orderId, Integer offset, Integer i);
}