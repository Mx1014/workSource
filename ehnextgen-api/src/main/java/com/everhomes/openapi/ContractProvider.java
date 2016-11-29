// @formatter:off
package com.everhomes.openapi;

import java.util.List;

public interface ContractProvider {

	void createContract(Contract contract);

	void updateContract(Contract contract);

	Contract findContractById(Long id);

	List<Contract> listContract();

	Contract findContractByNumber(Integer namespaceId, Long id, String contractNumber);

	void deleteContractByOrganizationName(Integer namespaceId, String name);

	List<Contract> listContractByContractNumbers(Integer namespaceId, List<String> contractNumbers);

	List<Contract> listContractByNamespaceId(Integer namespaceId, int from, int pageSize);

}