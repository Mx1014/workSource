// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLimitChangeLogsGroupDTO;

import java.util.List;

public interface EnterprisePaymentAuthEmployeeLimitChangeLogProvider {

	List<EmployeePaymentLimitChangeLogsGroupDTO> listEmployeePaymentLimitChangeLogsGroups(Integer namespaceId, Long organizationId, Long detailId, Integer offset, Integer pageSize);

	List<EnterprisePaymentAuthEmployeeLimitChangeLog> listEnterprisePaymentAuthEmployeeLimitChangeLog(Integer namespaceId, Long organizationId, Long detailId, List<Long> operateNoList);

	void batchCreateEnterprisePaymentAuthEmployeeLimitChangeLog(List<EnterprisePaymentAuthEmployeeLimitChangeLog> changeLogList);

}