package com.everhomes.openapi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.Count;
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
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.address.NamespaceBuildingType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.openapi.TechparkDataType;
import com.everhomes.rest.openapi.techpark.AllFlag;
import com.everhomes.rest.openapi.techpark.CustomerApartment;
import com.everhomes.rest.openapi.techpark.CustomerBuilding;
import com.everhomes.rest.openapi.techpark.CustomerContract;
import com.everhomes.rest.openapi.techpark.CustomerContractBuilding;
import com.everhomes.rest.openapi.techpark.CustomerLivingStatus;
import com.everhomes.rest.openapi.techpark.CustomerRental;
import com.everhomes.rest.openapi.techpark.SyncDataCommand;
import com.everhomes.rest.organization.DeleteOrganizationIdCommand;
import com.everhomes.rest.organization.NamespaceOrganizationType;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationCommunityRequestStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.PmAddressMappingStatus;
import com.everhomes.rest.techpark.expansion.LeasePromotionStatus;
import com.everhomes.rest.techpark.expansion.LeasePromotionType;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryProvider;
import com.everhomes.techpark.expansion.LeasePromotion;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
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
	
	@Autowired
	private TechparkSyncdataBackupProvider techparkSyncdataBackupProvider;

	@Autowired
	private EnterpriseProvider enterpriseProvider;
	
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	
	@Autowired
	private OrganizationSearcher organizationSearcher;
	
	//创建一个线程的线程池，这样三种类型的数据如果一起过来就可以排队执行了
	private ExecutorService queueThreadPool = Executors.newFixedThreadPool(1); 
	
	private ExecutorService rentalThreadPool = Executors.newFixedThreadPool(6); 
	
	// 从同步过来的数据中取出真正的楼栋和门牌号，以便电商使用，add by tt, 20170213
	private static List<String> ruleList = new ArrayList<>();
	private static Map<String, String> ruleMap = new HashMap<>();
	
	static {
		ruleList.add("生产力大楼");
		ruleList.add("科技工业大厦");
		ruleList.add("金融基地");
		ruleList.add("金融科技大厦");
		
		//规则为1-2-3-4，前两个数字表示楼栋的分隔线，后两个数字表示门牌的分隔线，最后一个省略表示分隔到底，未指明规则的按最后一个分隔线分隔
		
		ruleMap.put("生产力大楼", "1-2+1-2+1");	//生产力大楼-生产力大楼-A101，2+1表示第二个横线+1个字符的位置
		ruleMap.put("科技工业大厦", "0-1-2");
		ruleMap.put("金融基地", "1-2-2");
		ruleMap.put("金融科技大厦", "0-1-2");
	}
	
	@Override
	public void syncData(SyncDataCommand cmd) {
		if (StringUtils.isBlank(cmd.getAppKey()) || cmd.getDataType() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters: cmd="+cmd);
		}
		
		if (TechparkDataType.fromCode(cmd.getDataType()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error data type");
		}
		
		if (AllFlag.fromCode(cmd.getAllFlag()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error allFlag value");
		}
		String appKey = cmd.getAppKey();
		
		AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(appKey);
		if (appNamespaceMapping == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"not exist app namespace mapping");
		}
		
		// 插入备份表中
		TechparkSyncdataBackup techparkSyncdataBackup = new TechparkSyncdataBackup();
		techparkSyncdataBackup.setNamespaceId(appNamespaceMapping.getNamespaceId());
		techparkSyncdataBackup.setDataType(cmd.getDataType());
		techparkSyncdataBackup.setAllFlag(cmd.getAllFlag());
		techparkSyncdataBackup.setNextPage(cmd.getNextPage());
		techparkSyncdataBackup.setVarDataList(cmd.getVarDataList());
		techparkSyncdataBackup.setDelDataList(cmd.getDelDataList());
		techparkSyncdataBackup.setStatus(CommonStatus.ACTIVE.getCode());
		techparkSyncdataBackup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		techparkSyncdataBackup.setCreatorUid(1L);
		techparkSyncdataBackup.setUpdateTime(techparkSyncdataBackup.getCreateTime());
		techparkSyncdataBackupProvider.createTechparkSyncdataBackup(techparkSyncdataBackup);
		
		//如果到最后一页了，则开始更新到我们数据库中
		if (cmd.getNextPage() == null) {
			queueThreadPool.execute(()->{
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("enter into thread=================");
				}
				
				List<TechparkSyncdataBackup> backupList = techparkSyncdataBackupProvider.listTechparkSyncdataBackupByParam(appNamespaceMapping.getNamespaceId(), cmd.getDataType(), cmd.getAllFlag());
				if (backupList == null || backupList.isEmpty()) {
					return ;
				}
				try {
					if (AllFlag.fromCode(cmd.getAllFlag()) == AllFlag.ALL) {
						// 全量更新
						updateAllDate(cmd.getDataType(),appNamespaceMapping , backupList);
					}else {
						// 增量更新
						updatePartDate(cmd.getDataType(), appNamespaceMapping, backupList);
					}
				} finally {
					techparkSyncdataBackupProvider.updateTechparkSyncdataBackupInactive(backupList);
				}
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("get out thread=================");
				}
			});
		}
	}

	//全量更新基本思路：在我们数据中不在同步数据里的，删除；两边都在的，更新；不在我们数据库中，在同步数据里的，插入；
	private void updateAllDate(Byte dataType, AppNamespaceMapping appNamespaceMapping,
			List<TechparkSyncdataBackup> backupList) {
		TechparkDataType techparkDataType = TechparkDataType.fromCode(dataType);
		switch (techparkDataType) {
		case BUILDING:
			//楼栋同步从门牌中提取
//			syncAllBuildings(appNamespaceMapping, backupList);
			break;
		case APARTMENT:
			syncAllApartments(appNamespaceMapping, backupList);
			break;
		case RENTING:
			syncAllRentings(appNamespaceMapping, backupList);
			break;
		case WAITING_FOR_RENTING:
			//暂无
			break;

		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"error data type");
		}
	}

	private void syncAllBuildings(AppNamespaceMapping appNamespaceMapping, List<TechparkSyncdataBackup> backupList) {
		dbProvider.execute(s->{
			//必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
			List<Building> myBuildingList = buildingProvider.listBuildingByNamespaceType(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), NamespaceBuildingType.JINDIE.getCode());
			List<CustomerBuilding> theirBuildingList = mergeBackupList(backupList, CustomerBuilding.class);
			syncAllBuildings(appNamespaceMapping, myBuildingList, theirBuildingList);
			return true;
		});
	}

	private void syncAllBuildings(AppNamespaceMapping appNamespaceMapping, List<Building> myBuildingList, List<CustomerBuilding> theirBuildingList) {
		// 如果两边都有，更新；如果我们有，他们没有，删除；
		for (Building myBuilding : myBuildingList) {
			CustomerBuilding customerBuilding = findFromTheirBuildingList(myBuilding, theirBuildingList);
			if (customerBuilding != null) {
				updateBuilding(myBuilding, customerBuilding);
			}else {
				deleteBuilding(myBuilding);
			}
		}
		// 如果他们有，我们没有，插入
		// 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
		if (theirBuildingList != null) {
			for (CustomerBuilding customerBuilding : theirBuildingList) {
				if ((customerBuilding.getDealed() != null && customerBuilding.getDealed().booleanValue() == true) || StringUtils.isBlank(customerBuilding.getBuildingName())) {
					continue;
				}
				// 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
				Building building = buildingProvider.findBuildingByName(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerBuilding.getBuildingName());
				if (building == null) {
					insertBuilding(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerBuilding);
				}else {
					updateBuilding(building, customerBuilding);
				}
			}
		}
	}
	
	private void insertBuilding(Integer namespaceId, Long communityId, CustomerBuilding customerBuilding) {
		if (StringUtils.isBlank(customerBuilding.getBuildingName())) {
			return;
		}
		Building building = new Building();
		building.setCommunityId(communityId);
		building.setName(customerBuilding.getBuildingName());
		building.setAliasName(customerBuilding.getBuildingName());
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
		building.setNamespaceBuildingToken(customerBuilding.getBuildingNumber());
		buildingProvider.createBuilding(building);
	}

	private void deleteBuilding(Building building) {
		if (CommonStatus.fromCode(building.getStatus()) != CommonStatus.INACTIVE) {
			building.setOperatorUid(1L);
			building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			building.setStatus(CommonStatus.INACTIVE.getCode());
			buildingProvider.updateBuilding(building);
		}
	}

	private void updateBuilding(Building building, CustomerBuilding customerBuilding) {
		building.setName(customerBuilding.getBuildingName());
		building.setAliasName(customerBuilding.getBuildingName());
		building.setProductType(customerBuilding.getProductType());
		building.setCompleteDate(getTimestampDate(customerBuilding.getCompleteDate()));
		building.setJoininDate(getTimestampDate(customerBuilding.getJoininDate()));
		building.setFloorCount(customerBuilding.getFloorCount());
		building.setOperatorUid(1L);
		building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		building.setNamespaceBuildingToken(customerBuilding.getBuildingNumber());
		building.setStatus(CommonStatus.ACTIVE.getCode());
		buildingProvider.updateBuilding(building);
	}

	private CustomerBuilding findFromTheirBuildingList(Building myBuilding, List<CustomerBuilding> theirBuildingList) {
		if (theirBuildingList != null) {
			for (CustomerBuilding customerBuilding : theirBuildingList) {
				if (myBuilding.getName().equals(customerBuilding.getBuildingName())) {
					customerBuilding.setDealed(true);
					return customerBuilding;
				}
			}
		}
		return null;
	}

	private <T> List<T> mergeBackupList(List<TechparkSyncdataBackup> backupList, Class<T> targetClz) {
		List<T> resultList = new ArrayList<>();
		for (TechparkSyncdataBackup techparkSyncdataBackup : backupList) {
			if (StringUtils.isNotBlank(techparkSyncdataBackup.getVarDataList())) {
				List<T> list = JSONObject.parseArray(techparkSyncdataBackup.getVarDataList(), targetClz);
				if (list != null) {
					resultList.addAll(list);
				}
			}
		}
		return resultList;
	}

	private void syncAllApartments(AppNamespaceMapping appNamespaceMapping, List<TechparkSyncdataBackup> backupList) {
		//楼栋的同步按照从门牌中提取来同步
		//必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
		List<Address> myApartmentList = addressProvider.listAddressByNamespaceType(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), NamespaceAddressType.JINDIE.getCode());
		List<CustomerApartment> theirApartmentList = mergeBackupList(backupList, CustomerApartment.class);
		formatCustomerBuildingName(theirApartmentList);
		List<CustomerBuilding> theirBuildingList = fetchBuildingsFromApartments(theirApartmentList);
		List<Building> myBuildingList = buildingProvider.listBuildingByNamespaceType(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), NamespaceBuildingType.JINDIE.getCode());
		dbProvider.execute(s->{
			syncAllBuildings(appNamespaceMapping, myBuildingList, theirBuildingList);
			syncAllApartments(appNamespaceMapping, myApartmentList, theirApartmentList);
			return true;
		});
	}

	private void formatCustomerBuildingName(List<CustomerApartment> theirApartmentList) {
		for (CustomerApartment customerApartment : theirApartmentList) {
			if (customerApartment.getApartmentName() != null && customerApartment.getApartmentName().contains("-")) {
				customerApartment.setBuildingName(customerApartment.getApartmentName().split("-")[0]);
			}
		}
	}

	private List<CustomerBuilding> fetchBuildingsFromApartments(List<CustomerApartment> theirApartmentList) {
		Set<CustomerBuilding> buildingSet = new HashSet<>();
		for (CustomerApartment customerApartment : theirApartmentList) {
			buildingSet.add(new CustomerBuilding(customerApartment.getBuildingName()));
		}
		
		return new ArrayList<>(buildingSet);
	}

	private void syncAllApartments(AppNamespaceMapping appNamespaceMapping, List<Address> myApartmentList, List<CustomerApartment> theirApartmentList) {
		// 如果两边都有，更新；如果我们有，他们没有，删除；
		for (Address myApartment : myApartmentList) {
			CustomerApartment customerApartment = findFromTheirApartmentList(myApartment, theirApartmentList);
			if (customerApartment != null) {
				updateAddress(myApartment, customerApartment);
			}else {
				deleteAddress(myApartment);
			}
		}
		// 如果他们有，我们没有，插入
		// 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
		if (theirApartmentList != null) {
			for (CustomerApartment customerApartment : theirApartmentList) {
				if ((customerApartment.getDealed() != null && customerApartment.getDealed().booleanValue() == true) || StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
					continue;
				}
				// 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
				Address address = addressProvider.findAddressByBuildingApartmentName(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerApartment.getBuildingName(), customerApartment.getApartmentName());
				if (address == null) {
					insertAddress(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerApartment);
				}else {
					updateAddress(address, customerApartment);
				}
			}
		}
		
		// 同步完地址后更新community表中的门牌总数，add by tt, 20170308
		Community community = communityProvider.findCommunityById(appNamespaceMapping.getCommunityId());
		Integer count = addressProvider.countApartment(appNamespaceMapping.getCommunityId());
		if (community.getAptCount().intValue() != count.intValue()) {
			community.setAptCount(count);
			communityProvider.updateCommunity(community);
		}
	}
	
	private void insertAddress(Integer namespaceId, Long communityId, CustomerApartment customerApartment) {
		if (StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
			return;
		}
		Community community = communityProvider.findCommunityById(communityId);
		if (community == null) {
			community = new Community();
			community.setId(communityId);
		}
		Address address = new Address();
		address.setUuid(UUID.randomUUID().toString());
		address.setCommunityId(community.getId());
		address.setCityId(community.getCityId());
		address.setCityName(community.getCityName());
		address.setAreaId(community.getAreaId());
		address.setAreaName(community.getAreaName());
		address.setZipcode(community.getZipcode());
		address.setAddress(getAddress(customerApartment.getBuildingName(), customerApartment.getApartmentName()));
		address.setAddressAlias(address.getAddress());
		address.setBuildingName(customerApartment.getBuildingName());
		address.setBuildingAliasName(customerApartment.getBuildingName());
		address.setApartmentName(customerApartment.getApartmentName());
		address.setApartmentFloor(customerApartment.getApartmentFloor());
		address.setStatus(CommonStatus.ACTIVE.getCode());
		address.setCreatorUid(1L);
		address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		address.setOperatorUid(1L);
		address.setOperateTime(address.getCreateTime());
		address.setAreaSize(customerApartment.getAreaSize()==null?customerApartment.getRentArea():customerApartment.getAreaSize());
		address.setNamespaceId(namespaceId);
		address.setRentArea(customerApartment.getRentArea());
		address.setBuildArea(customerApartment.getBuildArea());
		address.setInnerArea(customerApartment.getInnerArea());
		address.setLayout(customerApartment.getLayout());
		address.setLivingStatus(getLivingStatus(customerApartment.getLivingStatus()));
		address.setNamespaceAddressType(NamespaceAddressType.JINDIE.getCode());
		
		//添加电商用到的楼栋和门牌
		String[] businessBuildingApartmentNames = getBusinessBuildingApartmentName(address.getApartmentName());
		address.setBusinessBuildingName(businessBuildingApartmentNames[0]);
		address.setBusinessApartmentName(businessBuildingApartmentNames[1]);
		
		addressProvider.createAddress(address);
		insertOrUpdateLeasePromotion(customerApartment.getLivingStatus(), address.getNamespaceId(), address.getCommunityId(), address.getRentArea(), address.getBuildingName(), address.getApartmentName());
	}
	
	private String getRulePrefix(String apartmentName) {
		if (apartmentName != null) {
			for (String str : ruleList) {
				if (apartmentName.startsWith(str)) {
					return str;
				}
			}
		}
		return null;
	}
	
	// 查找“-”第几次出现的位置
	private int indexOf(String source, Integer count, Integer offset) {
		try {
			if (count != null) {
				if (count.intValue() <= 0) {
					return -1;
				}else if (count.intValue() == 1) {
					if (offset == null || offset.intValue() == 0) {
						return source.indexOf("-");
					}
					return source.indexOf("-", offset);
				}else {
					count--;
					return indexOf(source, count, source.indexOf("-")+1);
				}
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}
	
	private int getActualPos(String apartmentName, String rule) {
		try {
			Integer offset = 0; //在横线基础上的偏移量
			Integer lineCount = null; //第几个横线
			if (rule.contains("+")) {
				String[] arr = rule.split("\\+");
				lineCount = Integer.parseInt(arr[0]);
				offset = Integer.parseInt(arr[1]);
			}else {
				lineCount = Integer.parseInt(rule);
			}
			return indexOf(apartmentName, lineCount, 0) + offset.intValue();			
		} catch (Exception e) {
			return -1;
		}
	}
	
	// 0存储楼栋名称，1存储门牌名称
	private String[] getBusinessBuildingApartmentName(String apartmentName) {
		String[] result = new String[2];
		try {
			String rule = ruleMap.get(getRulePrefix(apartmentName));
			if (rule != null) {
				String[] ruleArray = rule.split("-");
				if (ruleArray.length >= 3) {
					int buildingStart = getActualPos(apartmentName, ruleArray[0]) + 1;
					int buildingEnd = getActualPos(apartmentName, ruleArray[1]);
					if (apartmentName.charAt(buildingEnd) >= '0' && apartmentName.charAt(buildingEnd) <= '9') {
						throw new Exception();
					}
					if (apartmentName.charAt(buildingEnd) != '-') {
						buildingEnd++;
					}
					result[0] = apartmentName.substring(buildingStart, buildingEnd);
					int apartmentStart = getActualPos(apartmentName, ruleArray[2]) + 1;
					if (ruleArray.length == 4) {
						int apartmentEnd = getActualPos(apartmentName, ruleArray[3]);
						result[1] = apartmentName.substring(apartmentStart, apartmentEnd);
					}else {
						result[1] = apartmentName.substring(apartmentStart);
					}
				}else {
					result = getDefaultBusinessBuildingApartmentName(apartmentName);
				}
			}else {
				result = getDefaultBusinessBuildingApartmentName(apartmentName);
			}
		} catch (Exception e) {
			result = getDefaultBusinessBuildingApartmentName(apartmentName);
		}
		
		return result;
	}
	
	private String[] getDefaultBusinessBuildingApartmentName(String apartmentName) {
		String[] result = new String[2];
		if (apartmentName != null) {
			if (apartmentName.contains("-")) {
				int lastIndex = apartmentName.lastIndexOf("-");
				result[0] = apartmentName.substring(0, lastIndex);
				result[1] = apartmentName.substring(lastIndex + 1);
			}else {
				result[0] = result[1] = apartmentName;
			}
		}
		return result;
	}

	// 插入或更新招租管理
	private void insertOrUpdateLeasePromotion(Byte theirLivingStatus, Integer namespaceId, Long communityId, Double rentArea, String buildingName, String apartmentName){
		CustomerLivingStatus livingStatus = CustomerLivingStatus.fromCode(theirLivingStatus);
		if (livingStatus == null) {
			return;
		}
		
		String namespaceType = "jindie";
		String namespaceToken = generateLeasePromotionToken(buildingName, apartmentName);
		if (livingStatus == CustomerLivingStatus.WAITING_FOR_RENTING) {
			// 待租状态时，在招租管理里面插入一条数据，状态为已下线（因为需要科技园区编辑图文信息之后再发布到APP端），招租标题就是楼栋门牌，发布时间和入驻时间=插入数据时间，面积=传过来的面积，负责人和电话都为空
			// 如果有数据则不变
			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionByToken(namespaceId, communityId, namespaceType, namespaceToken);
			if (leasePromotion == null) {
				leasePromotion = new LeasePromotion();
				leasePromotion.setNamespaceId(namespaceId);
				leasePromotion.setCommunityId(communityId);
				leasePromotion.setRentType(LeasePromotionType.ORDINARY.getCode());
				leasePromotion.setSubject(apartmentName);
				leasePromotion.setRentAreas(String.valueOf(rentArea));
				leasePromotion.setCreateUid(1L);
				leasePromotion.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				leasePromotion.setUpdateTime(leasePromotion.getCreateTime());
				leasePromotion.setStatus(LeasePromotionStatus.OFFLINE.getCode());
				Building building = buildingProvider.findBuildingByName(namespaceId, communityId, buildingName);
				if (building != null) {
					leasePromotion.setBuildingId(building.getId());
				}else {
					leasePromotion.setBuildingId(0L);
				}
				leasePromotion.setRentPosition(buildingName);
				leasePromotion.setEnterTime(leasePromotion.getCreateTime());
				leasePromotion.setNamespaceType(namespaceType);
				leasePromotion.setNamespaceToken(namespaceToken);
				enterpriseApplyEntryProvider.createLeasePromotion(leasePromotion);
			}
		}else if (livingStatus == CustomerLivingStatus.NEW_RENTING || livingStatus == CustomerLivingStatus.CONTINUE_RENTING) {
			// 新租和已租时，将招租管理对应的门牌的招租状态改为“已出租”
			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionByToken(namespaceId, communityId, namespaceType, namespaceToken);
			if (leasePromotion != null && LeasePromotionStatus.fromType(leasePromotion.getStatus()) != LeasePromotionStatus.RENTAL) {
				leasePromotion.setStatus(LeasePromotionStatus.RENTAL.getCode());
				enterpriseApplyEntryProvider.updateLeasePromotion(leasePromotion);
			}
		}
	}
	
	private String generateLeasePromotionToken(String buildingName, String apartmentName) {
		return buildingName+"-"+apartmentName;
	};
	
	private String getAddress(String buildingName, String apartmentName){
		if (apartmentName.contains(buildingName)) {
			return apartmentName;
		}
		return buildingName+"-"+apartmentName;
	}

	private void deleteAddress(Address address) {
		if (CommonStatus.fromCode(address.getStatus()) != CommonStatus.INACTIVE) {
			address.setOperatorUid(1L);
			address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			address.setStatus(CommonStatus.INACTIVE.getCode());
			addressProvider.updateAddress(address);
		}
	}

	private void updateAddress(Address address, CustomerApartment customerApartment) {
		address.setApartmentFloor(customerApartment.getApartmentFloor());
		address.setAreaSize(customerApartment.getAreaSize()==null?customerApartment.getRentArea():customerApartment.getAreaSize());
		address.setRentArea(customerApartment.getRentArea());
		address.setBuildArea(customerApartment.getBuildArea());
		address.setInnerArea(customerApartment.getInnerArea());
		address.setLayout(customerApartment.getLayout());
		address.setLivingStatus(getLivingStatus(customerApartment.getLivingStatus()));
		address.setOperatorUid(1L);
		address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		address.setStatus(CommonStatus.ACTIVE.getCode());

		//添加电商用到的楼栋和门牌
		String[] businessBuildingApartmentNames = getBusinessBuildingApartmentName(address.getApartmentName());
		address.setBusinessBuildingName(businessBuildingApartmentNames[0]);
		address.setBusinessApartmentName(businessBuildingApartmentNames[1]);
		
		addressProvider.updateAddress(address);
		insertOrUpdateLeasePromotion(customerApartment.getLivingStatus(), address.getNamespaceId(), address.getCommunityId(), address.getRentArea(), address.getBuildingName(), address.getApartmentName());
	}

	private CustomerApartment findFromTheirApartmentList(Address myApartment,
			List<CustomerApartment> theirApartmentList) {
		if (theirApartmentList != null) {
			for (CustomerApartment customerApartment : theirApartmentList) {
				if (myApartment.getBuildingName().equals(customerApartment.getBuildingName()) && myApartment.getApartmentName().equals(customerApartment.getApartmentName())) {
					customerApartment.setDealed(true);
					return customerApartment;
				}
			}
		}
		return null;
	}

	private void syncAllRentings(AppNamespaceMapping appNamespaceMapping, List<TechparkSyncdataBackup> backupList) {
		/**
		 * 合同比较复杂，牵涉到表比较多，包括以下表：
		 * eh_contracts
		 * eh_contract_building_mappings
		 * eh_organizations
		 * eh_organization_addresses
		 * eh_organization_address_mappings
		 * eh_organization_community_requests
		 * eh_organization_details
		 * eh_organization_members
		 * eh_addresses
		 * eh_buildings
		 * eh_users
		 * eh_user_identifiers
		 */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("begin sync all rentings, size="+backupList.size());
		}
		List<Organization> myOrganizationList = organizationProvider.listOrganizationByNamespaceType(appNamespaceMapping.getNamespaceId(), NamespaceOrganizationType.JINDIE.getCode());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sync all rentings, flag 1");
		}
		List<CustomerRental> theirRentalList = mergeBackupList(backupList, CustomerRental.class);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sync all rentings, flag 2");
		}
		//有的组织名称过长，格式化一下
		for (CustomerRental customerRental : theirRentalList) {
			String name = customerRental.getName();
			if (name != null && name.length() > 64) {
				customerRental.setName(name.substring(0,64).trim());
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sync all rentings, flag 3");
		}
		formatBuildingName(theirRentalList);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sync all rentings, flag 4");
		}
		for (Organization myOrganization : myOrganizationList) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("sync all rentings, flag 5");
			}
			CustomerRental customerRental = findFromTheirRentalList(myOrganization, theirRentalList);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("sync all rentings, flag 6");
			}
			//一个组织起一个线程和一个事务来做更新，否则太慢了会导致超时导致事务回滚
			rentalThreadPool.execute(()->{
				try {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("sync all rentings, flag 7");
					}
					dbProvider.execute(s->{
						try {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("sync all rentings, flag 8");
							}
							if (customerRental != null) {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("sync organization, name="+customerRental.getName()+", number="+customerRental.getNumber());
								}
								updateOrganization(myOrganization, customerRental);
								insertOrUpdateOrganizationDetail(myOrganization, customerRental.getContact(), customerRental.getContactPhone());
								insertOrUpdateOrganizationCommunityRequest(appNamespaceMapping.getCommunityId(), myOrganization);
								insertOrUpdateOrganizationMembers(appNamespaceMapping.getNamespaceId(), myOrganization, customerRental.getContact(), customerRental.getContactPhone());
								insertOrUpdateAllContracts(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), myOrganization, customerRental.getContracts());
								insertOrUpdateAllOrganizationAddresses(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), myOrganization, extractCustomerContractBuilding(customerRental));
							}else {
//								deleteOrganization(myOrganization);
//								deleteOrganizationCommunityRequest(appNamespaceMapping.getCommunityId(), myOrganization);
								//改成以下方式，否则es中的数据无法删除
								DeleteOrganizationIdCommand deleteOrganizationIdCommand = new DeleteOrganizationIdCommand();
								deleteOrganizationIdCommand.setId(myOrganization.getId());
								organizationService.deleteEnterpriseById(deleteOrganizationIdCommand);
								deleteContracts(myOrganization);
							}
							return true;
						} catch (Exception e) {
							LOGGER.error("transaction error", e);
							throw e;
						}
					});
				} catch (Exception e) {
					LOGGER.error("thread error", e);
					throw e;
				}
				
			});
			
		}
		for (CustomerRental customerRental : theirRentalList) {
			if (customerRental.getDealed() != null && customerRental.getDealed().booleanValue() == true) {
				continue;
			}
			if (StringUtils.isBlank(customerRental.getName()) || StringUtils.isBlank(customerRental.getNumber())) {
				continue;
			}
			rentalThreadPool.execute(()->{
				dbProvider.execute(s->{
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("sync organization, name="+customerRental.getName()+", number="+customerRental.getNumber());
					}
					Organization organization = organizationProvider.findOrganizationByNameAndNamespaceIdForJindie(customerRental.getName(), appNamespaceMapping.getNamespaceId(), customerRental.getNumber(), NamespaceOrganizationType.JINDIE.getCode());
					if (organization == null) {
						organization = insertOrganization(appNamespaceMapping, customerRental);
						insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdateOrganizationCommunityRequest(appNamespaceMapping.getCommunityId(), organization);
						insertOrUpdateOrganizationMembers(appNamespaceMapping.getNamespaceId(), organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdateAllContracts(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, customerRental.getContracts());
						insertOrUpdateAllOrganizationAddresses(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, extractCustomerContractBuilding(customerRental));
					}else {
						updateOrganization(organization, customerRental);
						insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdateOrganizationCommunityRequest(appNamespaceMapping.getCommunityId(), organization);
						insertOrUpdateOrganizationMembers(appNamespaceMapping.getNamespaceId(), organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdateAllContracts(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, customerRental.getContracts());
						insertOrUpdateAllOrganizationAddresses(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, extractCustomerContractBuilding(customerRental));
					}
					return true;
				});
			});
		}
	}

	//楼栋名称改成从门牌中提取
	private void formatBuildingName(List<CustomerRental> theirRentalList) {
		for (CustomerRental customerRental : theirRentalList) {
			List<CustomerContract> customerContractList = customerRental.getContracts();
			if (customerContractList != null) {
				for (CustomerContract customerContract : customerContractList) {
					List<CustomerContractBuilding> customerContractBuildingList = customerContract.getBuildings();
					if (customerContractBuildingList != null) {
						for (CustomerContractBuilding customerContractBuilding : customerContractBuildingList) {
							if (customerContractBuilding.getApartmentName() != null && customerContractBuilding.getApartmentName().contains("-")) {
								customerContractBuilding.setBuildingName(customerContractBuilding.getApartmentName().split("-")[0]);
							}
						}
					}
				}
			}
		}
	}

	private Organization insertOrganization(AppNamespaceMapping appNamespaceMapping, CustomerRental customerRental) {
		Organization organization = new Organization();
		organization.setParentId(0L);
		organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
		organization.setName(customerRental.getName());
		organization.setAddressId(0L);
		organization.setPath("");
		organization.setLevel(1);
		organization.setStatus(OrganizationStatus.ACTIVE.getCode());
		organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
		organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organization.setUpdateTime(organization.getCreateTime());
		organization.setDirectlyEnterpriseId(0L);
		organization.setNamespaceId(appNamespaceMapping.getNamespaceId());
		organization.setShowFlag((byte)1);
		organization.setNamespaceOrganizationType(NamespaceOrganizationType.JINDIE.getCode());
		organization.setNamespaceOrganizationToken(customerRental.getNumber());
		organizationProvider.createOrganization(organization);
		return organization;
	}

	private void deleteOrganization(Organization organization) {
		if (OrganizationStatus.fromCode(organization.getStatus()) != OrganizationStatus.INACTIVE) {
			organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organization.setStatus(OrganizationStatus.INACTIVE.getCode());
			organizationProvider.updateOrganization(organization);
		}
	}

	private void deleteContracts(Organization organization) {
		contractProvider.deleteContractByOrganizationName(organization.getNamespaceId(), organization.getName());
		contractBuildingMappingProvider.deleteContractBuildingMappingByOrganizatinName(organization.getNamespaceId(), organization.getName());
	}

	private void deleteOrganizationCommunityRequest(Long communityId, Organization organization) {
		OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.findOrganizationCommunityRequestByOrganizationId(communityId, organization.getId());
		if (organizationCommunityRequest != null && OrganizationCommunityRequestStatus.fromCode(organizationCommunityRequest.getMemberStatus()) != OrganizationCommunityRequestStatus.INACTIVE) {
			organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.INACTIVE.getCode());
			organizationProvider.updateOrganizationCommunityRequest(organizationCommunityRequest);
		}
	}

	private void updateOrganization(Organization organization, CustomerRental customerRental) {
		organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organization.setNamespaceOrganizationToken(customerRental.getNumber());
		organization.setStatus(OrganizationStatus.ACTIVE.getCode());
		organizationProvider.updateOrganization(organization);
	}

	private List<CustomerContractBuilding> extractCustomerContractBuilding(CustomerRental customerRental) {
		List<CustomerContractBuilding> resultList = new ArrayList<>();
		if (customerRental != null) {
			List<CustomerContract> customerContractList = customerRental.getContracts();
			if (customerContractList != null && !customerContractList.isEmpty()) {
				for (CustomerContract customerContract : customerContractList) {
					List<CustomerContractBuilding> customerContractBuildingList = customerContract.getBuildings();
					if (customerContractBuildingList != null && !customerContractBuildingList.isEmpty()) {
						resultList.addAll(customerContractBuildingList);
					}
				}
			}
		}
		return resultList;
	}

	private CustomerRental findFromTheirRentalList(Organization myOrganization, List<CustomerRental> theirRentalList) {
		for (CustomerRental customerRental : theirRentalList) {
			if (myOrganization.getName().equals(customerRental.getName()) && myOrganization.getNamespaceOrganizationToken().equals(customerRental.getNumber())) {
				customerRental.setDealed(true);
				return customerRental;
			}
		}
		return null;
	}

	//增量同步，基本原理是有就更新，无就插入，不做删除
	private void updatePartDate(Byte dataType, AppNamespaceMapping appNamespaceMapping,
			List<TechparkSyncdataBackup> backupList) {
		TechparkDataType techparkDataType = TechparkDataType.fromCode(dataType);
		switch (techparkDataType) {
		case BUILDING:
			//楼栋改成按门牌来同步
//			syncPartBuildings(appNamespaceMapping, backupList);
			break;
		case APARTMENT:
			syncPartApartments(appNamespaceMapping, backupList);
			break;
		case RENTING:
			syncPartRentings(appNamespaceMapping, backupList);
			break;
		case WAITING_FOR_RENTING:
			//暂无
			break;

		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"error data type");
		}
	}

	private void syncPartBuildings(AppNamespaceMapping appNamespaceMapping, List<TechparkSyncdataBackup> backupList) {
		dbProvider.execute(s->{
			List<CustomerBuilding> theirBuildingList = mergeBackupList(backupList, CustomerBuilding.class);
			syncPartCustomerBuildings(appNamespaceMapping, theirBuildingList);
			return true;
		});
	}

	private void syncPartCustomerBuildings(AppNamespaceMapping appNamespaceMapping, List<CustomerBuilding> theirBuildingList) {
		for (CustomerBuilding customerBuilding : theirBuildingList) {
			Building building = buildingProvider.findBuildingByName(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerBuilding.getBuildingName());
			if (building == null) {
				insertBuilding(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerBuilding);
			}else {
				updateBuilding(building, customerBuilding);
			}
		}
	}
	
	private void syncPartApartments(AppNamespaceMapping appNamespaceMapping, List<TechparkSyncdataBackup> backupList) {
		//楼栋改成从门牌中提取来同步
		List<CustomerApartment> theirApartmentList = mergeBackupList(backupList, CustomerApartment.class);
		formatCustomerBuildingName(theirApartmentList);
		List<CustomerBuilding> theirBuildingList = fetchBuildingsFromApartments(theirApartmentList);
		dbProvider.execute(s->{
			syncPartCustomerBuildings(appNamespaceMapping, theirBuildingList);
			syncPartCustomerApartments(appNamespaceMapping, theirApartmentList);
			return true;
		});
	}
	
	private void syncPartCustomerApartments(AppNamespaceMapping appNamespaceMapping, List<CustomerApartment> theirApartmentList) {
		for (CustomerApartment customerApartment : theirApartmentList) {
			if (StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
				continue;
			}
			Address address = addressProvider.findAddressByBuildingApartmentName(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerApartment.getBuildingName(), customerApartment.getApartmentName());
			if (address == null) {
				insertAddress(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerApartment);
			}else {
				updateAddress(address, customerApartment);
			}
		}
	}

	private void syncPartRentings(AppNamespaceMapping appNamespaceMapping, List<TechparkSyncdataBackup> backupList) {
		List<CustomerRental> theirRentalList = mergeBackupList(backupList, CustomerRental.class);
		//有的组织名称过长，格式化一下
		for (CustomerRental customerRental : theirRentalList) {
			String name = customerRental.getName();
			if (name != null && name.length() > 64) {
				customerRental.setName(name.substring(0,64).trim());
			}
		}
		formatBuildingName(theirRentalList);
		for (CustomerRental customerRental : theirRentalList) {
			//一个组织起一个线程和一个事务来做更新，否则太慢了会导致超时导致事务回滚
			rentalThreadPool.execute(()->{
				dbProvider.execute(s->{
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("sync organization, name="+customerRental.getName()+", number="+customerRental.getNumber());
					}
					Organization organization = organizationProvider.findOrganizationByNameAndNamespaceIdForJindie(customerRental.getName(), appNamespaceMapping.getNamespaceId(), customerRental.getNumber(), NamespaceOrganizationType.JINDIE.getCode());
					if (organization == null) {
						organization = insertOrganization(appNamespaceMapping, customerRental);
						insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdateOrganizationCommunityRequest(appNamespaceMapping.getCommunityId(), organization);
						insertOrUpdateOrganizationMembers(appNamespaceMapping.getNamespaceId(), organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdatePartContracts(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, customerRental.getContracts());
						insertOrUpdatePartOrganizationAddresses(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, extractCustomerContractBuilding(customerRental));
					}else {
						updateOrganization(organization, customerRental);
						insertOrUpdateOrganizationDetail(organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdateOrganizationCommunityRequest(appNamespaceMapping.getCommunityId(), organization);
						insertOrUpdateOrganizationMembers(appNamespaceMapping.getNamespaceId(), organization, customerRental.getContact(), customerRental.getContactPhone());
						insertOrUpdatePartContracts(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, customerRental.getContracts());
						insertOrUpdatePartOrganizationAddresses(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), organization, extractCustomerContractBuilding(customerRental));
					}
					return true;
				});
			});
		}
	}

	private void insertOrUpdatePartContracts(Integer namespaceId, Long communityId, Organization organization,
			List<CustomerContract> customerContractList) {
		if (customerContractList == null || customerContractList.isEmpty()) {
			return;
		}
		
		for (CustomerContract customerContract : customerContractList) {
			Contract contract = contractProvider.findContractByNumber(namespaceId, organization.getId(), customerContract.getContractNumber());
			if (contract == null) {
				contract = insertContract(organization, customerContract);
				insertOrUpdatePartContractBuildingMappings(contract, customerContract);
			}else {
				updateContract(contract, customerContract);
				insertOrUpdatePartContractBuildingMappings(contract, customerContract);
			}
		}
	}

	private void insertOrUpdatePartContractBuildingMappings(Contract contract, CustomerContract customerContract) {
		List<CustomerContractBuilding> customerContractBuildingList = customerContract.getBuildings();
		if (customerContractBuildingList == null || customerContractBuildingList.isEmpty()) {
			return;
		}
		
		for (CustomerContractBuilding customerContractBuilding : customerContractBuildingList) {
			if (StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
				continue;
			}
			ContractBuildingMapping contractBuildingMapping = contractBuildingMappingProvider.findContractBuildingMappingByName(contract.getNamespaceId(), contract.getCustomerId(), customerContract.getContractNumber(), customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
			if (contractBuildingMapping == null) {
				insertContractBuildingMapping(contract, customerContractBuilding);
			}else {
				updateContractBuildingMapping(contractBuildingMapping, customerContractBuilding);
			}
		}
	}

	private void insertOrUpdatePartOrganizationAddresses(Integer namespaceId, Long communityId,
			Organization organization, List<CustomerContractBuilding> customerContractBuildingList) {
		if (customerContractBuildingList == null || customerContractBuildingList.isEmpty()) {
			return;
		}
		
		//要处理三个表，organizationAddress、enterpriseAddress、organizationAddressMapping
		for (CustomerContractBuilding customerContractBuilding : customerContractBuildingList) {
			if (StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
				continue;
			}
			Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
			if (address != null) {
				OrganizationAddress organizationAddress = organizationProvider.findOrganizationAddressByOrganizationIdAndAddressId(organization.getId(), address.getId());
				if (organizationAddress == null) {
					insertOrganizationAddress(namespaceId, communityId, organization, customerContractBuilding);
				}else {
					updateOrganizationAddress(namespaceId, communityId, organizationAddress, customerContractBuilding);
				}
				
				EnterpriseAddress enterpriseAddress = enterpriseProvider.findEnterpriseAddressByEnterpriseIdAndAddressId(organization.getId(), address.getId());
				if (enterpriseAddress == null) {
					insertEnterpriseAddress(namespaceId, communityId, organization, customerContractBuilding);
				}else {
					updateEnterpriseAddress(namespaceId, communityId, enterpriseAddress, customerContractBuilding);
				}
				
				Long superOrganizationId = getSuperOrganizationId(namespaceId, communityId); //管理公司的id
				CommunityAddressMapping organizationAddressMapping = organizationProvider.findOrganizationAddressMapping(superOrganizationId, communityId, address.getId());
				if (organizationAddressMapping == null) {
					insertOrganizationAddressMapping(communityId, superOrganizationId, address);
				}else {
					updateOrganizationAddressMapping(organizationAddressMapping, address);
				}
			}
		}
	}

	private void updateOrganizationAddressMapping(CommunityAddressMapping organizationAddressMapping, Address address) {
		organizationAddressMapping.setOrganizationAddress(address.getAddress());
		organizationAddressMapping.setLivingStatus(address.getLivingStatus());
		organizationProvider.updateOrganizationAddressMapping(organizationAddressMapping);
	}

	private void insertOrganizationAddressMapping(Long communityId, Long superOrganizationId, Address address) {
		CommunityAddressMapping organizationAddressMapping = new CommunityAddressMapping();
		organizationAddressMapping.setOrganizationId(superOrganizationId);
		organizationAddressMapping.setCommunityId(communityId);
		organizationAddressMapping.setAddressId(address.getId());
		organizationAddressMapping.setOrganizationAddress(address.getAddress());
		organizationAddressMapping.setLivingStatus(address.getLivingStatus());
		organizationAddressMapping.setNamespaceType(NamespaceAddressType.JINDIE.getCode());
		organizationProvider.createOrganizationAddressMapping(organizationAddressMapping);
	}

	private Long getSuperOrganizationId(Integer namespaceId, Long communityId){
		List<OrganizationCommunityDTO> organizationCommunityDTOList = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
		Long organizationId = null;
		if (organizationCommunityDTOList == null || organizationCommunityDTOList.isEmpty() || organizationCommunityDTOList.size() > 1) {
			organizationId = 1000001L;
		}else {
			organizationId = organizationCommunityDTOList.get(0).getOrganizationId();
		}
		Organization organization = organizationProvider.findOrganizationById(organizationId);
		if (organization.getNamespaceId().intValue() == namespaceId.intValue()) {
			return organization.getId();
		}else {
			return 1000001L;
		}
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
			return AddressMappingStatus.DEFAULT.getCode();
		}
		switch (customerLivingStatus) {
		case NOT_RENTAL:
		case NOT_RENTING:
		case RESERVE:
		case RETAIN:
			return AddressMappingStatus.DEFAULT.getCode();
			
		case WAITING_FOR_RENTING:
			return AddressMappingStatus.FREE.getCode();
			
		case NEW_RENTING:
		case CONTINUE_RENTING:
		case EXTEND_RENTING:
			return AddressMappingStatus.RENT.getCode();
		default:
			return AddressMappingStatus.DEFAULT.getCode();
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

			User user = new User();
			user.setId(1L);
			UserContext.setCurrentUser(user);
			UserContext.setCurrentNamespaceId(namespaceId);
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
			LOGGER.error("sync organization members error: organizationId="+organization.getId()+", contact="+contact+", contactPhone="+contactPhone, e);
		}
	}

	//全量同步合同
	private void insertOrUpdateAllContracts(Integer namespaceId, Long communityId, Organization organization, List<CustomerContract> customerContractList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update contracts");
		}
		
		// 列出这个组织所有的合同，然后比较，我有他无删，我无他有增，无有他有更新
		List<Contract> contractList = contractProvider.listContractByOrganizationId(namespaceId, organization.getId());
		for (Contract contract : contractList) {
			CustomerContract customerContract = findFromTheirContractList(contract, customerContractList);
			if (customerContract != null) {
				updateContract(contract, customerContract);
				insertOrUpdateAllContractBuildingMappings(contract, customerContract);
			}else {
				deleteContract(contract);
				deleteContractBuildingMappings(contract);
			}
		}
		if (customerContractList != null) {
			for (CustomerContract customerContract : customerContractList) {
				if (customerContract.getDealed() != null && customerContract.getDealed().booleanValue() == true) {
					continue;
				}
				Contract contract = insertContract(organization, customerContract);
				insertAllContractBuildingMappings(contract, customerContract);
			}
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update contracts, total="+customerContractList.size());
		}
	}

	private void insertAllContractBuildingMappings(Contract contract, CustomerContract customerContract) {
		List<CustomerContractBuilding> customerContractBuildingList = customerContract.getBuildings();
		if (customerContractBuildingList == null || customerContractBuildingList.isEmpty()) {
			return;
		}
		for (CustomerContractBuilding customerContractBuilding : customerContractBuildingList) {
			insertContractBuildingMapping(contract, customerContractBuilding);
		}
	}

	private Contract insertContract(Organization organization, CustomerContract customerContract) {
		Contract contract = new Contract();
		contract.setNamespaceId(organization.getNamespaceId());
//		contract.setOrganizationId(organization.getId());
//		contract.setOrganizationName(organization.getName());
		contract.setCustomerType((byte) 0);
		contract.setCustomerId(organization.getId());
		contract.setCustomerName(organization.getName());
		contract.setContractNumber(customerContract.getContractNumber());
		contract.setContractEndDate(getTimestampDate(customerContract.getContractEndDate()));
		contract.setStatus(CommonStatus.ACTIVE.getCode());
		contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractProvider.createContract(contract);
		return contract;
	}

	private void deleteContractBuildingMappings(Contract contract) {
		List<ContractBuildingMapping> myContractBuildingMappingList = contractBuildingMappingProvider.listContractBuildingMappingByContract(contract.getNamespaceId(), contract.getCustomerId(), contract.getContractNumber());
		myContractBuildingMappingList.forEach(c->{
			if (CommonStatus.fromCode(c.getStatus()) != CommonStatus.INACTIVE) {
				c.setStatus(CommonStatus.INACTIVE.getCode());
				contractBuildingMappingProvider.updateContractBuildingMapping(c);
			}
		});
	}

	private void deleteContract(Contract contract) {
		if (CommonStatus.fromCode(contract.getStatus()) != CommonStatus.INACTIVE) {
			contract.setStatus(CommonStatus.INACTIVE.getCode());
			contractProvider.updateContract(contract);
		}
	}

	private void updateContract(Contract contract, CustomerContract customerContract) {
		contract.setContractEndDate(getTimestampDate(customerContract.getContractEndDate()));
		contract.setStatus(CommonStatus.ACTIVE.getCode());
		contractProvider.updateContract(contract);
	}
	
	//全量同步合同地址
	private void insertOrUpdateAllContractBuildingMappings(Contract contract, CustomerContract customerContract){
		//比较合同中的地址
		List<ContractBuildingMapping> myContractBuildingMappingList = contractBuildingMappingProvider.listContractBuildingMappingByContract(contract.getNamespaceId(), contract.getCustomerId(), contract.getContractNumber());
		List<CustomerContractBuilding> theirContractBuildingList = customerContract.getBuildings();
		for (ContractBuildingMapping myContractBuildingMapping : myContractBuildingMappingList) {
			CustomerContractBuilding customerContractBuilding = findFromTheirContractBuildingList(myContractBuildingMapping, theirContractBuildingList);
			if (customerContractBuilding != null) {
				updateContractBuildingMapping(myContractBuildingMapping, customerContractBuilding);
			}else {
				deleteContractBuildingMapping(myContractBuildingMapping);
			}
		}
		if (theirContractBuildingList != null) {
			for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
				if ((customerContractBuilding.getDealed() != null && customerContractBuilding.getDealed().booleanValue() == true) 
						|| StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
					continue;
				}
				insertContractBuildingMapping(contract, customerContractBuilding);
			}
		}
	}
	
	private CustomerContractBuilding findFromTheirContractBuildingList(
			ContractBuildingMapping myContractBuildingMapping,
			List<CustomerContractBuilding> theirContractBuildingList) {
		if (theirContractBuildingList != null) {
			for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
				if (myContractBuildingMapping.getBuildingName().equals(customerContractBuilding.getBuildingName()) && myContractBuildingMapping.getApartmentName().equals(customerContractBuilding.getApartmentName())) {
					customerContractBuilding.setDealed(true);
					return customerContractBuilding;
				}
			}
		}
		return null;
	}

	private void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping,
			CustomerContractBuilding customerContractBuilding) {
		contractBuildingMapping.setAreaSize(customerContractBuilding.getAreaSize());
		contractBuildingMapping.setStatus(CommonStatus.ACTIVE.getCode());
		contractBuildingMappingProvider.updateContractBuildingMapping(contractBuildingMapping);
	}

	private void deleteContractBuildingMapping(ContractBuildingMapping contractBuildingMapping) {
		if (CommonStatus.fromCode(contractBuildingMapping.getStatus()) != CommonStatus.INACTIVE) {
			contractBuildingMapping.setStatus(CommonStatus.INACTIVE.getCode());
			contractBuildingMappingProvider.updateContractBuildingMapping(contractBuildingMapping);
		}
	}

	private void insertContractBuildingMapping(Contract contract, CustomerContractBuilding customerContractBuilding) {
		if (StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
			return;
		}
		ContractBuildingMapping contractBuildingMapping = new ContractBuildingMapping();
		contractBuildingMapping.setNamespaceId(contract.getNamespaceId());
		contractBuildingMapping.setOrganizationId(contract.getCustomerId());
		contractBuildingMapping.setOrganizationName(contract.getCustomerName());
		contractBuildingMapping.setContractId(contract.getId());
		contractBuildingMapping.setContractNumber(contract.getContractNumber());
		contractBuildingMapping.setBuildingName(customerContractBuilding.getBuildingName());
		contractBuildingMapping.setApartmentName(customerContractBuilding.getApartmentName());
		contractBuildingMapping.setAreaSize(customerContractBuilding.getAreaSize());
		contractBuildingMapping.setStatus(CommonStatus.ACTIVE.getCode());
		contractBuildingMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractBuildingMappingProvider.createContractBuildingMapping(contractBuildingMapping);
	}

	private CustomerContract findFromTheirContractList(Contract contract, List<CustomerContract> customerContractList) {
		if (customerContractList != null) {
			for (CustomerContract customerContract : customerContractList) {
				if (contract.getContractNumber().equals(customerContract.getContractNumber())) {
					customerContract.setDealed(true);
					return customerContract;
				}
			}
		}
		return null;
	}

	//全量同步地址
	private void insertOrUpdateAllOrganizationAddresses(Integer namespaceId, Long communityId, Organization organization,
			List<CustomerContractBuilding> theirContractBuildingList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~begin sync insert or update contract organization addresses");
		}
		//因为合同那里已经处理过一次楼栋门牌，这里初始化一下
		if (theirContractBuildingList != null) {
			theirContractBuildingList.forEach(b->b.setDealed(null));
		}
		
		// 娘的，有三个organization address关联表：eh_enterprise_addresses、eh_organization_addresses、eh_organization_address_mappings，蛋疼，都要比较下
		//处理OrganizationAddress
		List<OrganizationAddress> myOrganizationAddressList = organizationProvider.listOrganizationAddressByOrganizationId(organization.getId());
		for (OrganizationAddress organizationAddress : myOrganizationAddressList) {
			CustomerContractBuilding customerContractBuilding = findFromTheirContractBuildingList(namespaceId, communityId, organizationAddress, theirContractBuildingList);
			if (customerContractBuilding != null) {
				updateOrganizationAddress(namespaceId, communityId, organizationAddress, customerContractBuilding);
			}else {
				deleteOrganizationAddress(organizationAddress);
			}
		}
		if (theirContractBuildingList != null) {
			for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
				if (customerContractBuilding.getDealed() != null && customerContractBuilding.getDealed().booleanValue() == true) {
					continue;
				}
				//因为不同的合同里可能会有一样的地址，所以这里要检查一下，把前面插入过的相同的记录就不用再插入了
				insertOrganizationAddress(namespaceId, communityId, organization, customerContractBuilding);
			}
		}
		
		//处理EnterpriseAddress
		if (theirContractBuildingList != null) {
			theirContractBuildingList.forEach(b->b.setDealed(null));
		}
		List<EnterpriseAddress> myEnterpriseAddressList = organizationProvider.listEnterpriseAddressByOrganization(organization.getId());
		for (EnterpriseAddress enterpriseAddress : myEnterpriseAddressList) {
			CustomerContractBuilding customerContractBuilding = findFromTheirContractBuildingList(namespaceId, communityId, enterpriseAddress, theirContractBuildingList);
			if (customerContractBuilding != null) {
				updateEnterpriseAddress(namespaceId, communityId, enterpriseAddress, customerContractBuilding);
			}else {
				deleteEnterpriseAddress(enterpriseAddress);
			}
		}
		if (theirContractBuildingList != null) {
			for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
				if (customerContractBuilding.getDealed() != null && customerContractBuilding.getDealed().booleanValue() == true) {
					continue;
				}
				insertEnterpriseAddress(namespaceId, communityId, organization, customerContractBuilding);
			}
		}
		
		//处理organizationAddressMapping，这个表比较蛋疼，organizationId用的是管理的公司的id，更蛋疼的是，不知道哪个起的名字CommunityAddressMapping
		if (theirContractBuildingList != null) {
			theirContractBuildingList.forEach(b->b.setDealed(null));
		}
		Long superOrganizationId = getSuperOrganizationId(namespaceId, communityId);
		List<CommunityAddressMapping> myOrganizationAddressMappingList = organizationProvider.listOrganizationAddressMappingByNamespaceType(superOrganizationId, communityId, NamespaceAddressType.JINDIE.getCode());
		for (CommunityAddressMapping organizationAddressMapping : myOrganizationAddressMappingList) {
			CustomerContractBuilding customerContractBuilding = findFromTheirContractBuildingList(organizationAddressMapping, theirContractBuildingList);
			if (customerContractBuilding != null) {
				Address address = addressProvider.findAddressById(organizationAddressMapping.getAddressId());
				updateOrganizationAddressMapping(organizationAddressMapping, address);
			}else {
				//这里不能删除，因为是所有的企业的父id都一样，删了就完了
//				deleteOrganizationAddressMapping(organizationAddressMapping);
			}
		}
		if (theirContractBuildingList != null) {
			for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
				if ((customerContractBuilding.getDealed() != null && customerContractBuilding.getDealed().booleanValue() == true) 
						|| StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
					continue;
				}
				//有的地址可能是我们系统里本来就有的，这部分也是做更新操作
				Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
				if (address != null) {
					CommunityAddressMapping organizaitonAddressMapping = organizationProvider.findOrganizationAddressMapping(superOrganizationId, communityId, address.getId());
					if (organizaitonAddressMapping == null) {
						insertOrganizationAddressMapping(communityId, superOrganizationId, address);
					}else {
						updateOrganizationAddressMapping(organizaitonAddressMapping, address);
					}
				}
			}
		}
		
		if (theirContractBuildingList != null) {
			theirContractBuildingList.forEach(b->b.setDealed(null));
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("~~end sync insert or update contract organization addresses, total="+theirContractBuildingList.size());
		}
		
	}

	private void deleteOrganizationAddressMapping(CommunityAddressMapping organizationAddressMapping) {
		organizationProvider.deleteOrganizationAddressMapping(organizationAddressMapping);
	}

	private CustomerContractBuilding findFromTheirContractBuildingList(
			CommunityAddressMapping organizationAddressMapping,
			List<CustomerContractBuilding> theirContractBuildingList) {
		if (theirContractBuildingList != null) {
			Address address = addressProvider.findAddressById(organizationAddressMapping.getAddressId());
			if (address != null && address.getBuildingName() != null && address.getApartmentName() != null) {
				for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
					if (address.getBuildingName().equals(customerContractBuilding.getBuildingName()) && address.getApartmentName().equals(customerContractBuilding.getApartmentName())) {
						customerContractBuilding.setDealed(true);
						return customerContractBuilding;
					}
				}
			}
		}
		return null;
	}

	private void insertEnterpriseAddress(Integer namespaceId, Long communityId, Organization organization,
			CustomerContractBuilding customerContractBuilding) {
		if (StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
			return;
		}
		Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
		if (address == null) {
			return;
		}
		Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerContractBuilding.getBuildingName());
		//因为不同的合同里可能会有一样的地址，所以这里要检查一下，把前面插入过的相同的记录就不用再插入了
		EnterpriseAddress enterpriseAddress = enterpriseProvider.findEnterpriseAddressByEnterpriseIdAndAddressId(organization.getId(), address.getId());
		if (enterpriseAddress != null) {
			return;
		}
		enterpriseAddress = new EnterpriseAddress();
		enterpriseAddress.setEnterpriseId(organization.getId());
		enterpriseAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
		enterpriseAddress.setCreatorUid(1L);
		enterpriseAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		enterpriseAddress.setOperatorUid(1L);
		enterpriseAddress.setUpdateTime(enterpriseAddress.getCreateTime());
		enterpriseAddress.setAddressId(address.getId());
		if (building == null) {
			enterpriseAddress.setBuildingId(0L);
		}else {
			enterpriseAddress.setBuildingId(building.getId());
		}
		enterpriseAddress.setBuildingName(customerContractBuilding.getBuildingName());
		enterpriseProvider.createEnterpriseAddress(enterpriseAddress);
	}

	private void deleteEnterpriseAddress(EnterpriseAddress enterpriseAddress) {
		if (OrganizationAddressStatus.fromCode(enterpriseAddress.getStatus()) != OrganizationAddressStatus.INACTIVE) {
			enterpriseAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
			enterpriseAddress.setOperatorUid(1L);
			enterpriseAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			enterpriseProvider.updateEnterpriseAddress(enterpriseAddress);
		}
	}

	private void updateEnterpriseAddress(Integer namespaceId, Long communityId, EnterpriseAddress enterpriseAddress,
			CustomerContractBuilding customerContractBuilding) {
		enterpriseAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
		enterpriseAddress.setOperatorUid(1L);
		Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerContractBuilding.getBuildingName());
		if (building == null) {
			enterpriseAddress.setBuildingId(0L);
		}else {
			enterpriseAddress.setBuildingId(building.getId());
		}
		enterpriseAddress.setBuildingName(customerContractBuilding.getBuildingName());
		enterpriseAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		enterpriseProvider.updateEnterpriseAddress(enterpriseAddress);
	}

	private CustomerContractBuilding findFromTheirContractBuildingList(Integer namespaceId, Long communityId,
			EnterpriseAddress enterpriseAddress, List<CustomerContractBuilding> theirContractBuildingList) {
		if (theirContractBuildingList != null) {
			Address address = addressProvider.findAddressById(enterpriseAddress.getAddressId());
			if (address != null && address.getBuildingName() != null && address.getApartmentName() != null) {
				for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
					if (address.getBuildingName().equals(customerContractBuilding.getBuildingName()) && address.getApartmentName().equals(customerContractBuilding.getApartmentName())) {
						customerContractBuilding.setDealed(true);
						return customerContractBuilding;
					}
				}
			}
		}
		return null;
	}

	private void insertOrganizationAddress(Integer namespaceId, Long communityId, Organization organization, CustomerContractBuilding customerContractBuilding) {
		if (StringUtils.isBlank(customerContractBuilding.getBuildingName()) || StringUtils.isBlank(customerContractBuilding.getApartmentName())) {
			return;
		}
		Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, customerContractBuilding.getBuildingName(), customerContractBuilding.getApartmentName());
		if (address == null) {
			return;
		}
		Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerContractBuilding.getBuildingName());
		//因为不同的合同里可能会有一样的地址，所以这里要检查一下，把前面插入过的相同的记录就不用再插入了
		OrganizationAddress organizationAddress = organizationProvider.findOrganizationAddress(organization.getId(), address.getId(), building==null?0L:building.getId());
		if (organizationAddress != null) {
			return;
		}
		
		organizationAddress = new OrganizationAddress();
		organizationAddress.setOrganizationId(organization.getId());
		organizationAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
		organizationAddress.setCreatorUid(1L);
		organizationAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organizationAddress.setOperatorUid(1L);
		organizationAddress.setUpdateTime(organizationAddress.getCreateTime());
		organizationAddress.setAddressId(address.getId());
		if (building == null) {
			organizationAddress.setBuildingId(0L);
		}else {
			organizationAddress.setBuildingId(building.getId());
		}
		organizationAddress.setBuildingName(customerContractBuilding.getBuildingName());
		organizationProvider.createOrganizationAddress(organizationAddress);
	}

	private void deleteOrganizationAddress(OrganizationAddress organizationAddress) {
		if (OrganizationAddressStatus.fromCode(organizationAddress.getStatus()) != OrganizationAddressStatus.INACTIVE) {
			organizationAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
			organizationAddress.setOperatorUid(1L);
			organizationAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationProvider.updateOrganizationAddress(organizationAddress);
		}
	}

	private void updateOrganizationAddress(Integer namespaceId, Long communityId, OrganizationAddress organizationAddress,
			CustomerContractBuilding customerContractBuilding) {
		organizationAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
		organizationAddress.setOperatorUid(1L);
		Building building = buildingProvider.findBuildingByName(namespaceId, communityId, customerContractBuilding.getBuildingName());
		if (building == null) {
			organizationAddress.setBuildingId(0L);
		}else {
			organizationAddress.setBuildingId(building.getId());
		}
		organizationAddress.setBuildingName(customerContractBuilding.getBuildingName());
		organizationAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organizationProvider.updateOrganizationAddress(organizationAddress);
	}

	private CustomerContractBuilding findFromTheirContractBuildingList(Integer namespaceId, Long communityId, OrganizationAddress organizationAddress,
			List<CustomerContractBuilding> theirContractBuildingList) {
		if (theirContractBuildingList != null) {
			Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
			if (address != null && address.getBuildingName() != null && address.getApartmentName() != null) {
				for (CustomerContractBuilding customerContractBuilding : theirContractBuildingList) {
					if (address.getBuildingName().equals(customerContractBuilding.getBuildingName()) && address.getApartmentName().equals(customerContractBuilding.getApartmentName())) {
						customerContractBuilding.setDealed(true);
						return customerContractBuilding;
					}
				}
			}
		}
		return null;
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
		organizationSearcher.feedDoc(organization);
	}
}
