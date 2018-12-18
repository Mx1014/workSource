// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.math.BigDecimal;
import java.util.List;

public interface EnterprisePaymentAuthEmployeeLimitDetailProvider {

    void createEnterprisePaymentAuthEmployeeLimitDetail(EnterprisePaymentAuthEmployeeLimitDetail enterprisePaymentAuthEmployeeLimitDetail);

    void updateEnterprisePaymentAuthEmployeeLimitDetail(EnterprisePaymentAuthEmployeeLimitDetail enterprisePaymentAuthEmployeeLimitDetail);

    void incrHistoricalTotalPayAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, Long paymentSceneAppId, BigDecimal payAmount, Integer payCount);

    List<EnterprisePaymentAuthEmployeeLimitDetail> listEnterprisePaymentAuthEmployeeLimitDetail(Integer namespaceId, Long organizationId, Long detailId);

    Integer countSceneEmployeeCount(Long organizationId, Integer currentNamespaceId, Long sceneAppId);

    void autoDeleteDismissEmployeePaymentAuthLimitDetail(String shouldDeleteMonth);

    void markAutoDeleteDismissEmployeePaymentAuthLimitDetail(Integer namespaceId, Long organizationId, Long detailId, String waitAutoDeleteMonth);

    List<EnterprisePaymentAuthEmployeeLimitDetail> listEnterprisePaymentAuthEmployeeLimitDetailByScene(Integer currentNamespaceId, Long organizationId, Long sceneAppId, int pageSize, int pageOffSet);

    EnterprisePaymentAuthEmployeeLimitDetail findEnterprisePaymentAuthEmployeeLimitDetailByDetailId(Integer namespaceId, Long organizationId, Long detailId, Long sceneAppId);

    void deleteEnterprisePaymentAuthEmployeeLimitDetailsByDetailIdAndAppId(Integer namespaceId, Long organizationId, Long detailId, List<Long> existsAppId);
}