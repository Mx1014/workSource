// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.math.BigDecimal;
import java.util.List;

public interface EnterprisePaymentAuthEmployeePaySceneHistoryProvider {

	void createEnterprisePaymentAuthEmployeePaySceneHistory(EnterprisePaymentAuthEmployeePaySceneHistory enterprisePaymentAuthEmployeePaySceneHistory);

	void updateEnterprisePaymentAuthEmployeePaySceneHistory(EnterprisePaymentAuthEmployeePaySceneHistory enterprisePaymentAuthEmployeePaySceneHistory);

	List<EnterprisePaymentAuthEmployeePaySceneHistory> listEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(Integer namespaceId, Long organizationId, Long detailId, String payMonth);

	EnterprisePaymentAuthEmployeePaySceneHistory findEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(Integer namespaceId, Long organizationId, Long detailId, Long appId, String payMonth);

	void decrUsedAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, Long paymentSceneAppId, BigDecimal payAmount, String payMonth);

}