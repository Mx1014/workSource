package com.everhomes.openapi;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.ItemType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.address.NamespaceBuildingType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.openapi.TechparkDataType;
import com.everhomes.rest.openapi.techpark.CustomerApartment;
import com.everhomes.rest.openapi.techpark.CustomerBuilding;
import com.everhomes.rest.openapi.techpark.CustomerContract;
import com.everhomes.rest.openapi.techpark.CustomerContractBuilding;
import com.everhomes.rest.openapi.techpark.CustomerLivingStatus;
import com.everhomes.rest.openapi.techpark.CustomerRental;
import com.everhomes.rest.openapi.techpark.SyncDataCommand;
import com.everhomes.rest.organization.pm.PmAddressMappingStatus;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class TechparkOpenServiceImpl implements TechparkOpenService{

	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;
	
	@Autowired
	private BuildingProvider buildingProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private ContractProvider contractProvider;
	
	@Autowired
	private ContractBuildingMappingProvider contractBuildingMappingProvider;
	
	@Override
	public void syncData(SyncDataCommand cmd) {
		if (StringUtils.isBlank(cmd.getAppKey()) || cmd.getDataType() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters: cmd="+cmd);
		}
		
		String appKey = cmd.getAppKey();
		
		AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(appKey);
		if (appNamespaceMapping == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"not exist app namespace mapping");
		}
		
		TechparkDataType dataType = TechparkDataType.fromCode(cmd.getDataType());
		switch (dataType) {
		case BUILDING:
			syncBuildings(appNamespaceMapping, cmd.getVarDataList(), cmd.getDelDataList());
			break;
		case APARTMENT:
			syncApartments(appNamespaceMapping, cmd.getVarDataList(), cmd.getDelDataList());
			break;
		case RENTING:
			syncRentings(appNamespaceMapping, cmd.getVarDataList(), cmd.getDelDataList());
			break;
		case WAITING_FOR_RENTING:
			//暂无
			break;

		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"error data type");
		}
		
	}

	private void syncBuildings(AppNamespaceMapping appNamespaceMapping, String varDataList, String delDataList) {
		insertOrUpdateBuildings(appNamespaceMapping, varDataList);
		deleteBuildings(appNamespaceMapping, delDataList);
	}

	private void insertOrUpdateBuildings(AppNamespaceMapping appNamespaceMapping, String varDataList) {
		if (StringUtils.isBlank(varDataList)) {
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		
		List<CustomerBuilding> list = JSONObject.parseArray(varDataList, CustomerBuilding.class);
		for (CustomerBuilding customerBuilding : list) {
			Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerBuilding.getBuildingName());
			// 如果不存在就插入，如果存在就更新
			if (building == null) {
				building = new Building();
				building.setCommunityId(communityId);
				building.setName(customerBuilding.getBuildingName());
				building.setAliasName(customerBuilding.getBuildingNumber());
				building.setStatus(CommonStatus.ACTIVE.getCode());
				building.setCreatorUid(1L);
				building.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				building.setNamespaceId(namespaceId);
				building.setProductType(customerBuilding.getProductType());
				building.setCompleteDate(getTimestampDate(customerBuilding.getCompleteDate()));
				building.setJoininDate(getTimestampDate(customerBuilding.getJoininDate()));
				building.setFloorCount(customerBuilding.getFloorCount());
				building.setNamespaceBuildingType(NamespaceBuildingType.JINDIE.getCode());
				buildingProvider.createBuilding(building);
			}else {
				building.setName(customerBuilding.getBuildingName());
				building.setAliasName(customerBuilding.getBuildingNumber());
				building.setProductType(customerBuilding.getProductType());
				building.setCompleteDate(getTimestampDate(customerBuilding.getCompleteDate()));
				building.setJoininDate(getTimestampDate(customerBuilding.getJoininDate()));
				building.setFloorCount(customerBuilding.getFloorCount());
				buildingProvider.updateBuilding(building);
			}
		}
	}

	private Timestamp getTimestampDate(String dateString) {
		SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = datetimeSF.parse(dateString);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			try {
				Long time = Long.parseLong(dateString);
				return new Timestamp(time);
			} catch (Exception e2) {
			}
		}
		return null;
	}

	private void deleteBuildings(AppNamespaceMapping appNamespaceMapping, String delDataList) {
		if (StringUtils.isBlank(delDataList)) {
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();

		List<CustomerBuilding> list = JSONObject.parseArray(delDataList, CustomerBuilding.class);
		for (CustomerBuilding customerBuilding : list) {
			Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerBuilding.getBuildingName());
			if (building != null) {
				buildingProvider.deleteBuilding(building);
			}
		}
	}

	private void syncApartments(AppNamespaceMapping appNamespaceMapping, String varDataList, String delDataList) {
		insertOrUpdateApartments(appNamespaceMapping, varDataList);
		deleteApartments(appNamespaceMapping, delDataList);
	}

	private void insertOrUpdateApartments(AppNamespaceMapping appNamespaceMapping, String varDataList) {
		if (StringUtils.isBlank(varDataList)) {
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		Community community = communityProvider.findCommunityById(communityId);
		if (community == null) {
			community = new Community();
		}
		
		List<CustomerApartment> list = JSONObject.parseArray(varDataList, CustomerApartment.class);
		for (CustomerApartment customerApartment : list) {
			Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerApartment.getBuildingName(), customerApartment.getApartmentName());
			if (address == null) {
				address = new Address();
				address.setUuid(UUID.randomUUID().toString());
				address.setCommunityId(communityId);
				address.setCityId(community.getCityId());
				address.setCityName(community.getCityName());
				address.setAreaId(community.getAreaId());
				address.setAreaName(community.getAreaName());
				address.setZipcode(community.getZipcode());
				address.setAddress(community.getAddress());
				address.setAddressAlias(community.getAddress());
				address.setBuildingName(customerApartment.getBuildingName());
				address.setBuildingAliasName(customerApartment.getBuildingName());
				address.setApartmentName(customerApartment.getApartmentName());
				address.setApartmentFloor(customerApartment.getApartmentFloor());
				address.setStatus(CommonStatus.ACTIVE.getCode());
				address.setCreatorUid(1L);
				address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				address.setAreaSize(customerApartment.getAreaSize());
				address.setNamespaceId(namespaceId);
				address.setRentArea(customerApartment.getRentArea());
				address.setBuildArea(customerApartment.getBuildArea());
				address.setInnerArea(customerApartment.getInnerArea());
				address.setLayout(customerApartment.getLayout());
				address.setLivingStatus(getLivingStatus(customerApartment.getLivingStatus()));
				address.setNamespaceAddressType(NamespaceAddressType.JINDIE.getCode());
				addressProvider.createAddress(address);
			}else {
				address.setBuildingName(customerApartment.getBuildingName());
				address.setBuildingAliasName(customerApartment.getBuildingName());
				address.setApartmentName(customerApartment.getApartmentName());
				address.setApartmentFloor(customerApartment.getApartmentFloor());
				address.setAreaSize(customerApartment.getAreaSize());
				address.setRentArea(customerApartment.getRentArea());
				address.setBuildArea(customerApartment.getBuildArea());
				address.setInnerArea(customerApartment.getInnerArea());
				address.setLayout(customerApartment.getLayout());
				address.setLivingStatus(getLivingStatus(customerApartment.getLivingStatus()));
				addressProvider.updateAddress(address);
			}
		}
	}

	private Byte getLivingStatus(Byte livingStatus) {
//		0-非租赁（其他） 
//		1-未放租（其他）
//		2-待租（空闲）
//		3-保留（其他）
//		4- 新租（出租)
//		5-续租（出租） 
//		6-扩租（出租）
//		7-保留（其他）
		CustomerLivingStatus customerLivingStatus = CustomerLivingStatus.fromCode(livingStatus);
		switch (customerLivingStatus) {
		case NOT_RENTAL:
		case NOT_RENTING:
		case RESERVE:
		case RETAIN:
			return PmAddressMappingStatus.DEFAULT.getCode();
			
		case WAITING_FOR_RENTING:
			return PmAddressMappingStatus.FREE.getCode();
			
		case NEW_RENTING:
		case CONTINUE_RENTING:
		case EXTEND_RENTING:
			return PmAddressMappingStatus.RENT.getCode();
		default:
			return PmAddressMappingStatus.DEFAULT.getCode();
		}
	}

	private void deleteApartments(AppNamespaceMapping appNamespaceMapping, String delDataList) {
		if (StringUtils.isBlank(delDataList)) {
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		
		List<CustomerApartment> list = JSONObject.parseArray(delDataList, CustomerApartment.class);
		for (CustomerApartment customerApartment : list) {
			Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerApartment.getBuildingName(), customerApartment.getApartmentName());
			if (address != null) {
				addressProvider.deleteAddress(address);
			}
		}
	}

	private void syncRentings(AppNamespaceMapping appNamespaceMapping, String varDataList, String delDataList) {
		insertOrUpdateRentings(appNamespaceMapping, varDataList);
		deleteRentings(appNamespaceMapping, delDataList);
	}

	private void insertOrUpdateRentings(AppNamespaceMapping appNamespaceMapping, String varDataList) {
		if (StringUtils.isBlank(varDataList)) {
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		Community community = communityProvider.findCommunityById(communityId);
		if (community == null) {
			community = new Community();
		}
		
		List<CustomerRental> list = JSONObject.parseArray(varDataList, CustomerRental.class);
		for (CustomerRental customerRental : list) {
			Organization organization = organizationProvider.findOrganizationByNameAndNamespaceId(customerRental.getName(), namespaceId);
			if (organization == null) {
				organization = new Organization();
				
				
				
				organizationProvider.createOrganization(organization);
				insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
				insertOrUpdateContracts(organization, customerRental.getContracts());
			}else {
				organization.setName(customerRental.getName());
				organization.setDescription(customerRental.getNumber());
				organizationProvider.updateOrganization(organization);
				insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
				insertOrUpdateContracts(organization, customerRental.getContracts());
			}
		}
	}

	private void insertOrUpdateContracts(Organization organization, List<CustomerContract> contracts) {
		if (contracts == null || contracts.size() == 0) {
			return;
		}
		
		for (CustomerContract customerContract : contracts) {
			Contract contract = contractProvider.findContractByNumber(organization.getNamespaceId(), organization.getId(), customerContract.getContractNumber());
			if (contract == null) {
				contract = new Contract();
				
				
				
				contractProvider.createContract(contract);
			}else {
				contract.setContractNumber(customerContract.getContractNumber());
				contract.setContractEndDate(getTimestampDate(customerContract.getContractEndDate()));
				contractProvider.updateContract(contract);
				insertOrUpdateContractBuildingMappings(contract, customerContract.getBuildings());
			}
		}
		
	}

	private void insertOrUpdateContractBuildingMappings(Contract contract, List<CustomerContractBuilding> buildings) {
		if (buildings == null || buildings.size() == 0) {
			return;
		}
		for (CustomerContractBuilding customerContractBuilding : buildings) {
			ContractBuildingMapping contractBuildingMapping = contractBuildingMappingProvider.findContractBuildingMappingByName(contract.getContractNumber(), customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
			
		}
	}

	private void insertOrUpdateOrganizationDetail(Organization organization, String contact, String contactPhone) {
		OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
		if (organizationDetail == null) {
			organizationDetail = new OrganizationDetail();
			organizationDetail.setOrganizationId(organization.getId());
			organizationDetail.setDescription(organization.getDescription());
			organizationDetail.setContact(contactPhone);
			organizationDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationDetail.setDisplayName(organization.getName());
			organizationDetail.setContactor(contact);
			organizationProvider.createOrganizationDetail(organizationDetail);
		}else {
			organizationDetail.setOrganizationId(organization.getId());
			organizationDetail.setDescription(organization.getDescription());
			organizationDetail.setContact(contactPhone);
			organizationDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationDetail.setDisplayName(organization.getName());
			organizationDetail.setContactor(contact);
			organizationProvider.updateOrganizationDetail(organizationDetail);
		}
	}

	private void deleteRentings(AppNamespaceMapping appNamespaceMapping, String delDataList) {
	}
	
}
