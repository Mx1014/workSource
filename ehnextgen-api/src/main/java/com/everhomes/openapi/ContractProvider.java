// @formatter:off
package com.everhomes.openapi;

import com.everhomes.listing.CrossShardListingLocator;

import java.sql.Timestamp;
import java.util.List;

public interface ContractProvider {

	void createContract(Contract contract);

	void updateContract(Contract contract);

	Contract findContractById(Long id);

	List<Contract> listContract();

	Contract findContractByNumber(Integer namespaceId, Long organizationId, String contractNumber);

	void deleteContractByOrganizationName(Integer namespaceId, String name);

	List<Contract> listContractByContractNumbers(Integer namespaceId, List<String> contractNumbers);

	List<Contract> listContractByNamespaceId(Integer namespaceId, int from, int pageSize);

	List<Contract> listContractsByEndDateRange(Timestamp minValue, Timestamp maxValue);

	List<Contract> listContractsByCreateDateRange(Timestamp minValue, Timestamp maxValue);

	void deleteContract(Contract contract);

	List<Contract> listContractByOrganizationId(Integer namespaceId, Long organizationId);

	List<Contract> listContractByOrganizationId(Long organizationId);
	List<Contract> listContractByEnterpriseCustomerId(Long communityId, Long customerId);

	List<Contract> listContractsByIds(List<Long> ids);
	List<Contract> listContracts(CrossShardListingLocator locator, Integer pageSize);

	Contract findActiveContractByContractNumber(Integer namespaceId, String contractNumber);
}