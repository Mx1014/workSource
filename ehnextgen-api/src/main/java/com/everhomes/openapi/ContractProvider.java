// @formatter:off
package com.everhomes.openapi;

import java.util.List;

public interface ContractProvider {

	void createContract(Contract contract);

	void updateContract(Contract contract);

	Contract findContractById(Long id);

	List<Contract> listContract();

}