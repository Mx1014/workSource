// @formatter:off
package com.everhomes.openapi;

import java.util.List;

public interface ContractBuildingMappingProvider {

	void createContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

	void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

	ContractBuildingMapping findContractBuildingMappingById(Long id);

	List<ContractBuildingMapping> listContractBuildingMapping();

	ContractBuildingMapping findContractBuildingMappingByName(String contractNumber, String buildingName,
			String apartmentName);

	void deleteContractBuildingMappingByOrganizatinName(Integer namespaceId, String name);

}