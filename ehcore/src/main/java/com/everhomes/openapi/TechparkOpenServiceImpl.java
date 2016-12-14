package com.everhomes.openapi;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
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
import com.everhomes.rest.organization.CreateOrganizationAccountCommand;
import com.everhomes.rest.organization.NamespaceOrganizationType;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.pm.PmAddressMappingStatus;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class TechparkOpenServiceImpl implements TechparkOpenService{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(TechparkOpenServiceImpl.class);

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
	private OrganizationService organizationService;
	
	@Autowired
	private ContractProvider contractProvider;
	
	@Autowired
	private ContractBuildingMappingProvider contractBuildingMappingProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	
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
		dbProvider.execute(s->{
			insertOrUpdateBuildings(appNamespaceMapping, varDataList);
			deleteBuildings(appNamespaceMapping, delDataList);
			return true;
		});
	}

	private void insertOrUpdateBuildings(AppNamespaceMapping appNamespaceMapping, String varDataList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update buildings~~~~");
		}
		if (StringUtils.isBlank(varDataList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync insert or update buildings, no data and return directly");
			}
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		
		List<CustomerBuilding> list = JSONObject.parseArray(varDataList, CustomerBuilding.class);
		for (CustomerBuilding customerBuilding : list) {
			insertOrUpdateBuilding(namespaceId, communityId, customerBuilding);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update buildings, total="+list.size());
		}
	}

	private Building insertOrUpdateBuilding(Integer namespaceId, Long communityId, CustomerBuilding customerBuilding) {
		if (StringUtils.isBlank(customerBuilding.getBuildingName())) {
			return null;
		}
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
			building.setOperatorUid(1L);
			building.setOperateTime(building.getCreateTime());
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
			building.setOperatorUid(1L);
			building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			buildingProvider.updateBuilding(building);
		}
		return building;
	}

	private Timestamp getTimestampDate(String dateString) {
		if (StringUtils.isNotBlank(dateString)) {
			SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = datetimeSF.parse(dateString);
				return new Timestamp(date.getTime());
			} catch (ParseException e) {
				try {
					Long time = Long.parseLong(dateString);
					return new Timestamp(time);
				} catch (Exception e2) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("parse date error, date="+dateString);
					}
				}
			}
		}
		return null;
	}

	private void deleteBuildings(AppNamespaceMapping appNamespaceMapping, String delDataList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync delete buildings");
		}
		if (StringUtils.isBlank(delDataList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync delete buildings, no data and return directly");
			}
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();

		List<CustomerBuilding> list = JSONObject.parseArray(delDataList, CustomerBuilding.class);
		for (CustomerBuilding customerBuilding : list) {
			Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerBuilding.getBuildingName());
			if (building != null) {
				building.setStatus(CommonStatus.INACTIVE.getCode());
				building.setOperatorUid(1L);
				building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				building.setDeleteTime(building.getOperateTime());
				buildingProvider.updateBuilding(building);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync delete buildings, total="+list.size());
		}
	}

	private void syncApartments(AppNamespaceMapping appNamespaceMapping, String varDataList, String delDataList) {
		dbProvider.execute(s->{
			insertOrUpdateApartments(appNamespaceMapping, varDataList);
			deleteApartments(appNamespaceMapping, delDataList);
			return true;
		});
	}

	private void insertOrUpdateApartments(AppNamespaceMapping appNamespaceMapping, String varDataList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update apartments");
		}
		if (StringUtils.isBlank(varDataList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync insert or update apartments, no data and return directly");
			}
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		Community community = communityProvider.findCommunityById(communityId);
		if (community == null) {
			community = new Community();
			community.setId(communityId);
		}
		
		List<CustomerApartment> list = JSONObject.parseArray(varDataList, CustomerApartment.class);
		for (CustomerApartment customerApartment : list) {
			insertOrUpdateApartment(namespaceId, community, customerApartment);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update apartments, total="+list.size());
		}
	}

	private Address insertOrUpdateApartment(Integer namespaceId, Community community,
			CustomerApartment customerApartment) {
		if (StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
			return null;
		}
		Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, community.getId(), customerApartment.getBuildingName(), customerApartment.getApartmentName());
		if (address == null) {
			address = new Address();
			address.setUuid(UUID.randomUUID().toString());
			address.setCommunityId(community.getId());
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
			address.setOperatorUid(1L);
			address.setOperateTime(address.getCreateTime());
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
			address.setOperatorUid(1L);
			address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			addressProvider.updateAddress(address);
		}
		return address;
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
		if (customerLivingStatus == null) {
			return PmAddressMappingStatus.DEFAULT.getCode();
		}
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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync delete apartments");
		}
		if (StringUtils.isBlank(delDataList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync end apartments, no data and return directly");
			}
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		
		List<CustomerApartment> list = JSONObject.parseArray(delDataList, CustomerApartment.class);
		for (CustomerApartment customerApartment : list) {
			Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerApartment.getBuildingName(), customerApartment.getApartmentName());
			if (address != null) {
				address.setStatus(CommonStatus.INACTIVE.getCode());
				address.setOperatorUid(1L);
				address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				address.setDeleteTime(address.getOperateTime());
				addressProvider.updateAddress(address);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync delete apartments, total="+list.size());
		}
	}

	private void syncRentings(AppNamespaceMapping appNamespaceMapping, String varDataList, String delDataList) {
		dbProvider.execute(s->{
			insertOrUpdateRentings(appNamespaceMapping, varDataList);
			deleteRentings(appNamespaceMapping, delDataList);
			return true;
		});
	}

	private void insertOrUpdateRentings(AppNamespaceMapping appNamespaceMapping, String varDataList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update rentings");
		}
		if (StringUtils.isBlank(varDataList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync insert or update rentings, no data and return directly");
			}
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		Long communityId = appNamespaceMapping.getCommunityId();
		Community community = communityProvider.findCommunityById(communityId);
		if (community == null) {
			community = new Community();
			community.setId(communityId);
		}
		
		List<CustomerRental> list = JSONObject.parseArray(varDataList, CustomerRental.class);
		for (CustomerRental customerRental : list) {
			Organization organization = organizationProvider.findOrganizationByName(customerRental.getName(), namespaceId);
//			contractProvider.deleteContractByOrganizationName(namespaceId, customerRental.getName());
//			contractBuildingMappingProvider.deleteContractBuildingMappingByOrganizatinName(namespaceId, customerRental.getName());
			if (organization == null) {
				organization = new Organization();
				organization.setParentId(0L);
				organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
				organization.setName(customerRental.getName());
				organization.setAddressId(0L);
//				organization.setDescription(customerRental.getNumber());
				organization.setPath("");
				organization.setLevel(1);
				organization.setStatus(OrganizationStatus.ACTIVE.getCode());
				organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
				organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				organization.setUpdateTime(organization.getCreateTime());
				organization.setDirectlyEnterpriseId(0L);
				organization.setNamespaceId(namespaceId);
				organization.setShowFlag((byte)1);
				organization.setNamespaceOrganizationType(NamespaceOrganizationType.JINDIE.getCode());
				organization.setNamespaceOrganizationToken(customerRental.getNumber());
				organizationProvider.createOrganization(organization);
				insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
				insertOrUpdateOrganizationCommunityRequest(communityId, organization);
				insertOrUpdateOrganizationMembers(namespaceId, organization, customerRental.getContact(), customerRental.getContactPhone());
				insertOrUpdateContracts(namespaceId, community, organization, customerRental.getContracts());
			}else {
				organization.setName(customerRental.getName());
//				organization.setDescription(customerRental.getNumber());
				organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				organization.setNamespaceOrganizationType(NamespaceOrganizationType.JINDIE.getCode());
				organization.setNamespaceOrganizationToken(customerRental.getNumber());
				organizationProvider.updateOrganization(organization);
				insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
				insertOrUpdateOrganizationCommunityRequest(communityId, organization);
				insertOrUpdateOrganizationMembers(namespaceId, organization, customerRental.getContact(), customerRental.getContactPhone());
				insertOrUpdateContracts(namespaceId, community, organization, customerRental.getContracts());
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update rentings, total="+list.size());
		}
	}

	private void insertOrUpdateOrganizationCommunityRequest(Long communityId, Organization organization) {
		OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.findOrganizationCommunityRequestByOrganizationId(communityId, organization.getId());
		if (organizationCommunityRequest == null) {
			organizationCommunityRequest = new OrganizationCommunityRequest();
			organizationCommunityRequest.setCommunityId(communityId);
			organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
			organizationCommunityRequest.setMemberId(organization.getId());
			organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
			organizationCommunityRequest.setCreatorUid(1L);
			organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationCommunityRequest.setOperatorUid(1L);
			organizationCommunityRequest.setApproveTime(organizationCommunityRequest.getCreateTime());
			organizationCommunityRequest.setUpdateTime(organizationCommunityRequest.getCreateTime());
			organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);
		}else {
			organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
			organizationCommunityRequest.setOperatorUid(1L);
			organizationCommunityRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationProvider.updateOrganizationCommunityRequest(organizationCommunityRequest);
		}
	}

	// 需要把金蝶同步过来的业务人员添加为我司系统对应组织的管理员
	private void insertOrUpdateOrganizationMembers(Integer namespaceId, Organization organization, String contact, String contactPhone) {
		if (StringUtils.isBlank(contact) || StringUtils.isBlank(contactPhone) || organization == null) {
			return ;
		}
		try {
			String[] contactArray = contact.split(",");
			String[] contactPhoneArray = contactPhone.split(",");
			int length = Math.min(contactArray.length, contactPhoneArray.length);
			
			for(int i=0; i<length; i++) {
				if (StringUtils.isNotBlank(contactArray[i]) && StringUtils.isNotBlank(contactPhoneArray[i])) {
					CreateOrganizationAdminCommand cmd = new CreateOrganizationAdminCommand();
					cmd.setContactName(contactArray[i]);
					cmd.setContactToken(contactPhoneArray[i]);
					cmd.setOrganizationId(organization.getId());
					rolePrivilegeService.createOrganizationAdmin(cmd, namespaceId);
				}
			}
		} catch (Exception e) {
			LOGGER.error("sync organization members error: organizationId="+organization.getId()+", contact="+contact+", contactPhone="+contactPhone);
		}
	}

	private void insertOrUpdateContracts(Integer namespaceId, Community community, Organization organization, List<CustomerContract> contracts) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update contracts");
		}
		if (contracts == null || contracts.size() == 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync insert or update contracts, no data and return directly");
			}
			return;
		}
		
		for (CustomerContract customerContract : contracts) {
			Contract contract = contractProvider.findContractByNumber(organization.getNamespaceId(), organization.getId(), customerContract.getContractNumber());
			if (contract == null) {
				contract = new Contract();
				contract.setNamespaceId(organization.getNamespaceId());
				contract.setOrganizationId(organization.getId());
				contract.setOrganizationName(organization.getName());
				contract.setContractNumber(customerContract.getContractNumber());
				contract.setContractEndDate(getTimestampDate(customerContract.getContractEndDate()));
				contract.setStatus(CommonStatus.ACTIVE.getCode());
				contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				contractProvider.createContract(contract);
				insertOrUpdateContractBuildingMappings(contract, customerContract.getBuildings());
				insertOrUpdateOrganizationAddresses(namespaceId, community, organization, customerContract.getBuildings());
			}else {
				contract.setContractNumber(customerContract.getContractNumber());
				contract.setContractEndDate(getTimestampDate(customerContract.getContractEndDate()));
				contractProvider.updateContract(contract);
				insertOrUpdateContractBuildingMappings(contract, customerContract.getBuildings());
				insertOrUpdateOrganizationAddresses(namespaceId, community, organization, customerContract.getBuildings());
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update contracts, total="+contracts.size());
		}
	}

	private void insertOrUpdateOrganizationAddresses(Integer namespaceId, Community community, Organization organization,
			List<CustomerContractBuilding> buildings) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update contract organization addresses");
		}
		if (buildings == null || buildings.size() == 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync insert or update contract organization addresses, no data and return directly");
			}
			return;
		}
		for (CustomerContractBuilding customerContractBuilding : buildings) {
			if (StringUtils.isBlank(customerContractBuilding.getBuildingName())) {
				continue;
			}
			Building building = insertOrUpdateBuilding(namespaceId, community.getId(), new CustomerBuilding(customerContractBuilding.getBuildingName()));
			Address address = insertOrUpdateApartment(namespaceId, community, new CustomerApartment(customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName()));
			
			if (building != null && address != null) {
				insertOrUpdateOrganizationAddress(organization, building, address);
				insertOrUpdateOrganizationAddressMapping(organization, community, address);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update contract organization addresses, total="+buildings.size());
		}
		
	}

	private void insertOrUpdateOrganizationAddressMapping(Organization organization, Community community,
			Address address) {
		CommunityAddressMapping addressMapping = organizationProvider.findOrganizationAddressMapping(organization.getId(), community.getId(), address.getId());
		if (addressMapping == null) {
			addressMapping = new CommunityAddressMapping();
			addressMapping.setOrganizationId(organization.getId());
			addressMapping.setCommunityId(community.getId());
			addressMapping.setAddressId(community.getId());
			addressMapping.setOrganizationAddress(address.getAddress());
			addressMapping.setLivingStatus(PmAddressMappingStatus.RENT.getCode());
			organizationProvider.createOrganizationAddressMapping(addressMapping);
		}else {
			addressMapping.setOrganizationAddress(address.getAddress());
//			addressMapping.setLivingStatus(PmAddressMappingStatus.RENT.getCode());
			organizationProvider.updateOrganizationAddressMapping(addressMapping);
		}
	}

	private void insertOrUpdateOrganizationAddress(Organization organization, Building building, Address address) {
		OrganizationAddress organizationAddress = organizationProvider.findOrganizationAddress(organization.getId(), address.getId(), building.getId());
		if (organizationAddress == null) {
			organizationAddress = new OrganizationAddress();
			organizationAddress.setOrganizationId(organization.getId());
			organizationAddress.setAddressId(address.getId());
			organizationAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
			organizationAddress.setCreatorUid(1L);
			organizationAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationAddress.setBuildingId(building.getId());
			organizationAddress.setBuildingName(building.getName());
			organizationProvider.createOrganizationAddress(organizationAddress);
		}else {
			organizationAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
			organizationAddress.setOperatorUid(1L);
			organizationAddress.setBuildingName(building.getName());
			organizationProvider.updateOrganizationAddress(organizationAddress);
		}
	}

	private void insertOrUpdateContractBuildingMappings(Contract contract, List<CustomerContractBuilding> buildings) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update contract building mappings");
		}
		if (buildings == null || buildings.size() == 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync insert or update contract building mappings, no data and return directly");
			}
			return;
		}
		for (CustomerContractBuilding customerContractBuilding : buildings) {
			if (StringUtils.isBlank(customerContractBuilding.getBuildingName())) {
				continue;
			}
			ContractBuildingMapping contractBuildingMapping = contractBuildingMappingProvider.findContractBuildingMappingByName(contract.getContractNumber(), customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
			if (contractBuildingMapping == null) {
				contractBuildingMapping = new ContractBuildingMapping();
				contractBuildingMapping.setNamespaceId(contract.getNamespaceId());
				contractBuildingMapping.setOrganizationId(contract.getOrganizationId());
				contractBuildingMapping.setOrganizationName(contract.getOrganizationName());
				contractBuildingMapping.setContractId(contract.getId());
				contractBuildingMapping.setContractNumber(contract.getContractNumber());
				contractBuildingMapping.setBuildingName(customerContractBuilding.getBuildingName());
				contractBuildingMapping.setApartmentName(customerContractBuilding.getApartmentName());
				contractBuildingMapping.setAreaSize(customerContractBuilding.getAreaSize());
				contractBuildingMapping.setStatus(CommonStatus.ACTIVE.getCode());
				contractBuildingMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				contractBuildingMappingProvider.createContractBuildingMapping(contractBuildingMapping);
			}else {
				contractBuildingMapping.setBuildingName(customerContractBuilding.getBuildingName());
				contractBuildingMapping.setApartmentName(customerContractBuilding.getApartmentName());
				contractBuildingMapping.setAreaSize(customerContractBuilding.getAreaSize());
				contractBuildingMappingProvider.updateContractBuildingMapping(contractBuildingMapping);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update contract building mappings, total="+buildings.size());
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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync delete rentings");
		}
		if (StringUtils.isBlank(delDataList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~end sync delete rentings, no data and return directly");
			}
			return;
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		
		List<CustomerRental> list = JSONObject.parseArray(delDataList, CustomerRental.class);
		for (CustomerRental customerRental : list) {
			Organization organization = organizationProvider.findOrganizationByName(customerRental.getName(), namespaceId);
			if (organization != null) {
				// 组织应该不能删除
//				organization.setStatus(OrganizationStatus.DELETED.getCode());
//				organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//				organizationProvider.updateOrganization(organization);
//				contractProvider.deleteContractByOrganizationName(namespaceId, organization.getName());
//				contractBuildingMappingProvider.deleteContractBuildingMappingByOrganizatinName(namespaceId, organization.getName());
				
				List<CustomerContract> customerContractList = customerRental.getContracts();
				if (customerContractList != null && !customerContractList.isEmpty()) {
					for (CustomerContract customerContract : customerContractList) {
						Contract contract = contractProvider.findContractByNumber(organization.getNamespaceId(), organization.getId(), customerContract.getContractNumber());
						if (contract != null) {
							List<CustomerContractBuilding> customerContractBuildingList = customerContract.getBuildings();
							if (customerContractBuildingList != null && !customerContractBuildingList.isEmpty()) {
								for (CustomerContractBuilding customerContractBuilding : customerContractBuildingList) {
									ContractBuildingMapping contractBuildingMapping = contractBuildingMappingProvider.findContractBuildingMappingByName(contract.getContractNumber(), customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
									if (contractBuildingMapping != null) {
										contractBuildingMappingProvider.deleteContractBuildingMapping(contractBuildingMapping);
									}
								}
								//判断这个合同下还有没有楼栋，如果没有了，把这个合同删除了
								ContractBuildingMapping anyContractBuildingMapping = contractBuildingMappingProvider.findAnyContractBuildingMappingByNumber(contract.getContractNumber());
								if (anyContractBuildingMapping == null) {
									contractProvider.deleteContract(contract);
								}
							}
						}
						
					}
				}
				
				
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync delete rentings, total="+list.size());
		}
	}
	
}
