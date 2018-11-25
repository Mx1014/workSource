// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentAuthInfoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface EnterprisePaymentAuthEmployeeLimitProvider {

    void createEnterprisePaymentAuthEmployeeLimit(EnterprisePaymentAuthEmployeeLimit enterprisePaymentAuthEmployeeLimit);

    void updateEnterprisePaymentAuthEmployeeLimit(EnterprisePaymentAuthEmployeeLimit enterprisePaymentAuthEmployeeLimit);

    EnterprisePaymentAuthEmployeeLimit findEnterprisePaymentAuthEmployeeLimitByDetailId(Integer namespaceId, Long organizationId, Long detailId);

    GetEnterprisePaymentAuthInfoResponse countEnterpriseAuthInfo(Long organizationId);

    List<EnterprisePaymentAuthEmployeeLimit> listEnterprisePaymentAuthEmployeeLimit(Integer currentNamespaceId, Long organizationId);

    void autoDeleteDismissEmployeePaymentAuthLimit(String shouldDeleteMonth);

    void incrHistoricalTotalPayAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, BigDecimal payAmount, Integer payCount);
}