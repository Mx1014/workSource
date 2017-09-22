// @formatter:off
package com.everhomes.openapi;

import java.util.List;

import com.everhomes.rest.contract.BuildingApartmentDTO;

public interface ContractBuildingMappingProvider {

	void createContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

	void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

	ContractBuildingMapping findContractBuildingMappingById(Long id);

	List<ContractBuildingMapping> listContractBuildingMapping();

	ContractBuildingMapping findContractBuildingMappingByName(Integer namespaceId, Long organizationId, String contractNumber, String buildingName,
			String apartmentName);

	void deleteContractBuildingMappingByOrganizatinName(Integer namespaceId, String name);

	List<String> listContractByKeywords(Integer namespaceId, String buildingName, String keywords);

	List<BuildingApartmentDTO> listBuildingsByContractNumber(Integer namespaceId, String contractNumber);

	void deleteContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

	ContractBuildingMapping findAnyContractBuildingMappingByNumber(String contractNumber);

	List<ContractBuildingMapping> listContractBuildingMappingByContract(Integer namespaceId, Long organizationId,
			String contractNumber);
	List<ContractBuildingMapping> listByContract(Long contractId);
}