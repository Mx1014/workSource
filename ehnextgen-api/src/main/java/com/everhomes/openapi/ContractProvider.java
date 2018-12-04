// @formatter:off
package com.everhomes.openapi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.everhomes.contract.ContractAttachment;
import com.everhomes.contract.ContractCategory;
import com.everhomes.contract.ContractChargingChange;
import com.everhomes.contract.ContractChargingItem;
import com.everhomes.contract.ContractEvents;
import com.everhomes.contract.ContractParam;
import com.everhomes.contract.ContractParamGroupMap;
import com.everhomes.contract.ContractReportformStatisticCommunitys;
import com.everhomes.contract.ContractTaskOperateLog;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractChargingItemReportformDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractLogDTO;
import com.everhomes.rest.contract.statistic.ContractStaticsListDTO;
import com.everhomes.rest.contract.statistic.TotalContractStaticsDTO;

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

	Contract findActiveContractByContractNumber(Integer namespaceId, String contractNumber, Long categoryId);

	List<Contract> listContractByAddressId(Long addressId);
	List<Contract> listContractByBuildingName(String buildingName, Long communityId);

    List<Object> findCustomerByContractNum(String contractNum);

	void createContractParam(ContractParam param);
	void updateContractParam(ContractParam param);
	ContractParam findContractParamByCommunityId(Integer namespaceId, Long communityId, Byte payorreceiveContractType, Long ownerId,Long categoryId, Long appId);

	void createContractParamGroupMap(ContractParamGroupMap map);
	void deleteContractParamGroupMap(ContractParamGroupMap map);
	List<ContractParamGroupMap> listByParamId(Long paramId, Byte groupType);


	Map<Long, List<Contract>> listContractGroupByCommunity();
	String findLastContractVersionByCommunity(Integer namespaceId, Long communityId);
	Timestamp findLastContractVersionByCommunity(Integer namespaceId);

	List<Contract> listContractByNamespaceType(Integer namespaceId, String namespaceType, Long communityId, Long categoryId);
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

	//记录合同修改日志 by tangcen
	void saveContractEvent(int i, Contract contract, Contract exist);
	void insertContractEvents(ContractEvents event);
	void saveContractEventAboutApartments(int opearteType, Contract contract, ContractBuildingMapping mapping);
	void saveContractEventAboutChargingItem(int opearteType, Contract contract,ContractChargingItem contractChargingItem);
	void saveContractEventAboutAttachment(int opearteType, Contract contract, ContractAttachment contractAttachment);
	void saveContractEventAboutChargingChange(int adjustAdd, Contract contract,ContractChargingChange contractChargingChange);
	List<ContractEvents> listContractEvents(Long contractId);

    boolean isNormal(Long cid);

	void saveContractEventAboutApartments(int opearteType, Contract contract, String oldApartmnets,String newApartmnets);

    Long findContractCategoryIdByContractId(Long contractId);

    //合同模板 by jm.ding
	void createContractTemplate(ContractTemplate contractTemplate);
	void updateContractTemplate(ContractTemplate contractTemplate);
	ContractTemplate findContractTemplateById(Long id);
	List<ContractTemplate> listContractTemplates(Integer namespaceId, Long ownerId, String ownerType,Long orgId, Long categoryId, String name, Long pageAnchor, Integer pageSize, Long appId);
	void setPrintContractTemplate(Integer namespaceId, Long contractId, Long categoryId, String contractNumber, Long ownerId, Long templateId);
	//void deletePrintContractTemplate(Integer namespaceId, Long contractId, Long categoryId, String contractNumber, Long ownerId);
	Boolean getContractTemplateById(Long id);

	Double getTotalRentInCommunity(Long communityId);
	Integer countRelatedContractNumberInBuilding(String buildingName,Long communityId);

	Double getTotalRentInBuilding(String buildingName);

	List<Contract> findContractByAddressId(Long addressId);

	//合同模块筛选资质用户 by pengyu.huang
	Byte filterAptitudeCustomer(Long ownerId, Integer namespaceId);

	EnterpriseCustomerAptitudeFlag updateAptitudeCustomer(Long ownerId, Integer namespaceId, Byte adptitudeFlag);

	Long findCategoryIdByNamespaceId(Integer namespaceId);

	Integer getRelatedContractCountByAddressIds(List<Long> addressIdList);

	Double getTotalRentByAddressIds(List<Long> addressIdList);

	List<Contract> listContractsByNamespaceIdAndStatus(Integer namespaceId, byte statusCode);

	Contract findContractByNamespaceToken(Integer namespaceId, String namespaceContractType, Long namespaceContractToken, Long categoryId);

	List<ContractCategory> listContractAppCategory(Integer namespaceId);

	Map<String, BigDecimal> getChargeAreaByContractIdAndAddress(List<Long> contractIds, List<String> buildindNames, List<String> apartmentNames);

	BigDecimal getTotalChargeArea(List<Long> contractIds, List<String> buildindNames, List<String> apartmentNames);

	//合同4.0
	Boolean possibleEnterContractFuture(ContractDetailDTO contractDetailDTO,ContractBuildingMapping contractBuildingMapping);
	Boolean resoucreReservationsFuture(ContractDetailDTO contractDetailDTO,BuildingApartmentDTO apartment);
	void createContractOperateTask(ContractTaskOperateLog job);
	void updateContractOperateTask(ContractTaskOperateLog job);
	ContractTaskOperateLog findContractOperateTaskById(Long id);
	
	List<Contract> findAnyStatusContractByAddressId(Long addressId);
	
	//合同报表Date firstdateUpdateTime = null;Date lastdateUpdateTime = null;
	int getTotalContractCount(Timestamp firstdateUpdateTime, Timestamp lastdateUpdateTime);
	void createCommunityStatics(ContractReportformStatisticCommunitys communityStatistics);
	void deleteCommunityDataByDateStr(String todayDateStr);
	List<ContractChargingItemReportformDTO> getContractChargingItemInfoList(ContractDTO contract);
	List<ContractStaticsListDTO> listCommunityContractStaticsList(Integer namespaceId, List<Long> communityIds, String formatDateStr, String startTimeStr,String endTimeStr,  Byte dateType, Integer pageOffSet, Integer pageSize);
	TotalContractStaticsDTO getTotalContractStatics(Integer namespaceId, List<Long> communityIds, String formatDateStr, String startTimeStr,String endTimeStr,  Byte dateType);
	List<TotalContractStaticsDTO> listcontractStaticsListTimeDimension(Integer namespaceId, List<Long> communityIds, String formatDateStr, String startTimeStr,String endTimeStr,  Byte dateType, Integer pageOffSet, Integer pageSize);
	List<TotalContractStaticsDTO> listcontractStaticsListCommunityTotal(Integer namespaceId, List<Long> communityIds, String formatDateStr, String startTimeStr, String endTimeStr, Byte dateType, Integer pageOffSet, Integer pageSize);
	List<ContractStaticsListDTO> listSearchContractStaticsTimeDimension(Integer namespaceId, List<Long> communityIds, String formatDateStr, String startTimeStr, String endTimeStr, Byte dateType, Integer pageOffSet, Integer pageSize);

}