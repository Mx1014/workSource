// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.math.BigDecimal;
import java.util.List;

public interface EnterprisePaymentAuthEmployeePayHistoryProvider {

	void createEnterprisePaymentAuthEmployeePayHistory(EnterprisePaymentAuthEmployeePayHistory enterprisePaymentAuthEmployeePayHistory);

	void updateEnterprisePaymentAuthEmployeePayHistory(EnterprisePaymentAuthEmployeePayHistory enterprisePaymentAuthEmployeePayHistory);

	EnterprisePaymentAuthEmployeePayHistory findEnterprisePaymentAuthEmployeePayHistoryByDetailId(Integer namespaceId, Long organizationId, Long detailId, String payMonth);

	List<EnterprisePaymentAuthEmployeePayHistory> listEnterprisePaymentAuthEmployeePayHistory(Integer namespaceId, Long organizationId, String format);

	void decrUsedAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, BigDecimal payAmount, String payMonth);

}