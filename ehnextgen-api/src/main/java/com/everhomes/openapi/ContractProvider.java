// @formatter:off
package com.everhomes.openapi;

import com.everhomes.contract.ContractCategory;
import com.everhomes.contract.ContractParam;
import com.everhomes.contract.ContractParamGroupMap;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.news.NewsCategory;
import com.everhomes.rest.contract.ContractLogDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContractProvider {

	void createContract(Contract contract);

	void updateContract(Contract contract);

	Contract findContractById(Long id);

	List<Contract> listContract();

	Contract findContractByNumber(Integer namespaceId, Long organizationId, String contractNumber);

	void deleteContractByOrganizationName(Integer namespaceId, String name);

	List<Contract> listContractByContractNumbers(Integer namespaceId, List<String> contractNumbers, Long categoryId);

	List<Contract> listContractByNamespaceId(Integer namespaceId, int from, int pageSize, Long categoryId);

	List<Contract> listContractsByEndDateRange(Timestamp minValue, Timestamp maxValue, Integer namespaceId);

	List<Contract> listContractsByCreateDateRange(Timestamp minValue, Timestamp maxValue, Integer namespaceId);

	void deleteContract(Contract contract);

	List<Contract> listContractByOrganizationId(Integer namespaceId, Long organizationId, Long categoryId);

	List<Contract> listContractByOrganizationId(Long organizationId, Long categoryId);
	List<Contract> listContractByCustomerId(Long communityId, Long customerId, byte customerType);
	List<Contract> listContractByCustomerId(Long communityId, Long customerId, byte customerType, Byte status, Long categoryId);

	Map<Long, Contract> listContractsByIds(List<Long> ids);
	List<Contract> listContracts(CrossShardListingLocator locator, Integer pageSize);

	Contract findActiveContractByContractNumber(Integer namespaceId, String contractNumber);

	List<Contract> listContractByAddressId(Long addressId);
	List<Contract> listContractByBuildingName(String buildingName, Long communityId);

    List<Object> findCustomerByContractNum(String contractNum);

	void createContractParam(ContractParam param);
	void updateContractParam(ContractParam param);
	ContractParam findContractParamByCommunityId(Integer namespaceId, Long communityId, Byte payorreceiveContractType, Long categoryId);

	void createContractParamGroupMap(ContractParamGroupMap map);
	void deleteContractParamGroupMap(ContractParamGroupMap map);
	List<ContractParamGroupMap> listByParamId(Long paramId, Byte groupType);


	Map<Long, List<Contract>> listContractGroupByCommunity();
	String findLastContractVersionByCommunity(Integer namespaceId, Long communityId);

	List<Contract> listContractByNamespaceType(Integer namespaceId, String namespaceType, Long communityId);
	List<Contract> listContractsByAddressId(Long addressId);

    String findContractIdByThirdPartyId(String contractId, String code);


	List<Long> SimpleFindContractByNumber(String header);

    List<ContractLogDTO> listContractsBySupplier(Long supplierId, Long pageAnchor, Integer pageSize);
    //add by steve
	List<Contract> listContractByNamespaceId(Integer namespaceId);

    //多入口
    void createContractCategory(ContractCategory newsCategory);

	ContractCategory findContractCategoryById(Long categoryId);

	void updateContractCategory(ContractCategory contractCategory);

    boolean isNormal(Long cid);

    Long findContractCategoryIdByContractId(Long contractId);

	void createContractTemplate(ContractTemplate contractTemplate);
	void updateContractTemplate(ContractTemplate contractTemplate);

	ContractTemplate findContractTemplateById(Long id);

	List<ContractTemplate> listContractTemplates(Integer namespaceId, Long ownerId, String ownerType, Long categoryId,
			String name, Long pageAnchor, Integer pageSize);

	void setPrintContractTemplate(Integer namespaceId, Long contractId, Long categoryId, String contractNumber,
			Long ownerId, Long templateId);

	Boolean getContractTemplateById(Long id);
}