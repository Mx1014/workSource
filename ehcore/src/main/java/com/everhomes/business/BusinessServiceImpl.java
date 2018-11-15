// @formatter:off
package com.everhomes.business;


import ch.hsr.geohash.GeoHash;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.*;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.http.HttpUtils;
import com.everhomes.launchpad.LaunchPadConstants;
import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.oauth2.Clients;
import com.everhomes.order.PaymentUser;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pay.user.BusinessUserType;
import com.everhomes.promotion.BizHttpRestCallProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.*;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.admin.ListBuildingByCommunityIdsCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.CheckPaymentUserCommand;
import com.everhomes.rest.asset.CheckPaymentUserResponse;
import com.everhomes.rest.business.*;
import com.everhomes.rest.business.admin.*;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.community.GetCommunitiesByNameAndCityIdCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.openapi.*;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.rest.promotion.ModulePromotionInfoDTO;
import com.everhomes.rest.promotion.ModulePromotionInfoType;
import com.everhomes.rest.region.*;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.ui.launchpad.FavoriteBusinessesBySceneCommand;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.server.schema.tables.pojos.EhBusinessPromotions;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.spatial.DistanceUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BusinessServiceImpl implements BusinessService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);
	//private static final String BUSINESS_HOME_URL = "business.home.url";
	private static final String BUSINESS_DETAIL_URL = "business.detail.url";
	//private static final String AUTHENTICATE_PREFIX_URL = "authenticate.prefix.url";
	private static final String PREFIX_URL = "prefix.url";
	private static final String BUSINESS_IMAGE_URL = "business.image.url";
	private static final long CATEOGRY_RECOMMEND = 9999L;
	private static final String CATEOGRY_RECOMMEND_NAME = "推荐";
	private static final String BAIDU_MAP_URI = "baidu.map.uri";
	private static final String BAIDU_MAP_ACCESS_KEY = "baidu.map.access.key";
	@Autowired
	private BusinessProvider businessProvider;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private CategoryProvider categoryProvider;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private UserActivityProvider userActivityProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private UserActivityService userActivityService;
	@Autowired
	private LaunchPadProvider launchPadProvider;
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private GroupProvider groupProvider;
	@Autowired
	private AclProvider aclProvider;
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegionProvider regionProvider;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private NamespaceProvider namespaceProvider;
	
	@Autowired
	private GroupService groupService;

    @Autowired
    private BusinessPromotionProvider businessPromotionProvider;

    @Autowired
    private BizHttpRestCallProvider bizHttpRestCallProvider;

	@Autowired
	private OrganizationProvider organizationProvider;
	
	// 使用新支付SDK，PayService被废弃 by lqs 20180613
	//@Autowired
	//private PayService payService;
	@Override
	public void syncBusiness(SyncBusinessCommand cmd) {
		if(cmd.getUserId() == null){
			LOGGER.error("Invalid paramter userId,userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is null");
		}
		if(StringUtils.isBlank(cmd.getTargetId())){
			LOGGER.error("Invalid paramter targetId,targetId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter targetId,targetId is null");
		}
		User user = userProvider.findUserById(cmd.getUserId());
		if(user == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is not found");
		}
		long userId = user.getId();
		this.dbProvider.execute((TransactionStatus status) -> {
			Business business = this.businessProvider.findBusinessByTargetId(cmd.getTargetId());
			if(business == null){
				business = createBusiness(cmd, userId);
			}else{
				updateBusiness(cmd, business);
			}
			if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
				createBusinessCategories(business, cmd.getCategroies());
			}
			if(cmd.getNamespaceId()!=null)
				createBusinessAssignedNamespaceOnDelOther(business.getId(),cmd.getNamespaceId());
			if(cmd.getScopeType()!=null){
				if(cmd.getScopeType().byteValue()==ScopeType.ALL.getCode()){
					createBusinessAssignedScopeOnDelOther(business.getId(),ScopeType.ALL.getCode(),0L);
				}
				else if(cmd.getScopeType().byteValue()==ScopeType.CITY.getCode()){
					Clients client = new Clients();
					String baiduMapUri = configurationProvider.getValue(BAIDU_MAP_URI, "http://api.map.baidu.com");
					String baiduMapAccessKey = configurationProvider.getValue(BAIDU_MAP_ACCESS_KEY, "9E2825184e77d546b768bbfbf63050f8");
					String uri = String.format("%s/geocoder/v2/?ak=%s&location=%s,%s&output=json&pois=0",baiduMapUri,baiduMapAccessKey,cmd.getLatitude(),cmd.getLongitude());
					String jsonResponse = client.restCall("GET", uri, null, null, null);
					BaiduGeocoderResponse response = (BaiduGeocoderResponse) StringHelper.fromJsonString(jsonResponse, BaiduGeocoderResponse.class);
					String province = response.getResult().getAddressComponent().getProvince();
					String city = response.getResult().getAddressComponent().getCity();
					Region region = regionProvider.findRegionByPath("/"+province.subSequence(0, province.length()-1)+"/"+city);
					createBusinessAssignedScopeOnDelOther(business.getId(),ScopeType.CITY.getCode(),region.getId());
				}
				else{
					List<BusinessAssignedScope> list = businessProvider.listBusinessAssignedScopeByOwnerId(business.getId());
					if(list!=null&&!list.isEmpty())
						for(BusinessAssignedScope r:list)
							businessProvider.deleteBusinessAssignedScope(r.getId());
				}
			}
			return true;
		});
	}
	
	@Override
	public void reSyncBusiness(ReSyncBusinessCommand cmd) {
		
		
		if(cmd.getUserId() == null){
			LOGGER.error("Invalid paramter userId,userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is null");
		}
		if(StringUtils.isBlank(cmd.getTargetId())){
			LOGGER.error("Invalid paramter targetId,targetId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter targetId,targetId is null");
		}
		User user = userProvider.findUserById(cmd.getUserId());
		if(user == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is not found");
		}
		long userId = user.getId();
		this.dbProvider.execute((TransactionStatus status) -> {
			Business business = this.businessProvider.findBusinessByTargetId(cmd.getTargetId());
			if(business == null){
				business = createBusiness(cmd, userId);
			}else{
				updateBusiness(cmd, business);
			}
			if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
				createBusinessCategories(business, cmd.getCategroies());
			}
			reCreateBusinessAssignedNamespaceOnDelOther(business.getId(),cmd.getNamespaceIds());
			if(cmd.getScopeType()!=null){
				if(cmd.getScopeType().byteValue()==ScopeType.ALL.getCode()){
					createBusinessAssignedScopeOnDelOther(business.getId(),ScopeType.ALL.getCode(),0L);
				}
				else if(cmd.getScopeType().byteValue()==ScopeType.CITY.getCode()){
					Clients client = new Clients();
					String baiduMapUri = configurationProvider.getValue(BAIDU_MAP_URI, "http://api.map.baidu.com");
					String baiduMapAccessKey = configurationProvider.getValue(BAIDU_MAP_ACCESS_KEY, "9E2825184e77d546b768bbfbf63050f8");
					String uri = String.format("%s/geocoder/v2/?ak=%s&location=%s,%s&output=json&pois=0",baiduMapUri,baiduMapAccessKey,cmd.getLatitude(),cmd.getLongitude());
					String jsonResponse = client.restCall("GET", uri, null, null, null);
					BaiduGeocoderResponse response = (BaiduGeocoderResponse) StringHelper.fromJsonString(jsonResponse, BaiduGeocoderResponse.class);
					String province = response.getResult().getAddressComponent().getProvince();
					String city = response.getResult().getAddressComponent().getCity();
					Region region = regionProvider.findRegionByPath("/"+province.subSequence(0, province.length()-1)+"/"+city);
					createBusinessAssignedScopeOnDelOther(business.getId(),ScopeType.CITY.getCode(),region.getId());
				}
				else{
					List<BusinessAssignedScope> list = businessProvider.listBusinessAssignedScopeByOwnerId(business.getId());
					if(list!=null&&!list.isEmpty())
						for(BusinessAssignedScope r:list)
							businessProvider.deleteBusinessAssignedScope(r.getId());
				}
			}
			
			//增加item到launchPadItem表里面  by sfyan 20160701
			this.addLaunchPadItems(business);
			return true;
		});
	}
	
	private void addLaunchPadItems(Business business){
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				List<SceneType> sceneTypes = new ArrayList<SceneType>();
				List<Namespace> namespaces = namespaceProvider.listNamespaces();
				for (SceneType sceneType : sceneTypes) {
					for (Namespace namespace : namespaces) {
						LaunchPadItem launchPadItem = launchPadProvider.findLaunchPadItemByTargetAndScopeAndSence(ItemTargetType.BIZ.getCode(), business.getId(), ScopeType.ALL.getCode(), 0L, namespace.getId(),sceneType);
						if(null == launchPadItem){
							launchPadItem = new LaunchPadItem();
							launchPadItem.setNamespaceId(namespace.getId());
							launchPadItem.setAppId(AppConstants.APPID_DEFAULT);
							launchPadItem.setScopeCode(ScopeType.ALL.getCode());
							launchPadItem.setScopeId(0L);
							launchPadItem.setItemLocation("/home");
							launchPadItem.setItemGroup(ItemGroup.BIZS.getCode());
							launchPadItem.setItemName(business.getDisplayName());
							launchPadItem.setItemLabel(business.getName());
							launchPadItem.setIconUri(business.getLogoUri());
							launchPadItem.setItemWidth(1);
							launchPadItem.setItemHeight(1);
							launchPadItem.setActionType(ActionType.THIRDPART_URL.getCode());
							launchPadItem.setActionData("");
							launchPadItem.setDefaultOrder((int) (1000 + business.getId()));
							launchPadItem.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
							launchPadItem.setMinVersion(1L);
							launchPadItem.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
							launchPadItem.setTargetType(ItemTargetType.BIZ.getCode());
							launchPadItem.setTargetId(business.getId());
							launchPadItem.setDeleteFlag(DeleteFlagType.YES.getCode());
							launchPadItem.setSceneType(sceneType.getCode());
							launchPadProvider.createLaunchPadItem(launchPadItem);
						}else{
							launchPadItem.setItemName(business.getDisplayName());
							launchPadItem.setItemLabel(business.getName());
							launchPadItem.setIconUri(business.getLogoUri());
							launchPadProvider.updateLaunchPadItem(launchPadItem);
						}
					}
				}
				
			}
		});
		
	}
	
	private void reCreateBusinessAssignedNamespaceOnDelOther(Long ownerId,List<Integer> namespaceIds) {
		this.deleteBusinessAssignedNamespaces(ownerId);
		if(namespaceIds!=null&&!namespaceIds.isEmpty())
			for(Integer namespaceId:namespaceIds)
				createBusinessAssignedNamespace(ownerId,namespaceId);
	}

	private void deleteBusinessAssignedNamespaces(Long ownerId) {
		List<BusinessAssignedNamespace> list = businessProvider.listBusinessAssignedNamespaceByOwnerId(ownerId,null);
		if(list!=null&&!list.isEmpty())
			for(BusinessAssignedNamespace r:list)
				businessProvider.deleteBusinessAssignedNamespace(r);
	}

	private void createBusinessAssignedNamespaceOnDelOther(Long ownerId,Integer namespaceId) {
		List<BusinessAssignedNamespace> list = businessProvider.listBusinessAssignedNamespaceByOwnerId(ownerId,null);
		if(list!=null&&!list.isEmpty())
			for(BusinessAssignedNamespace r:list)
				businessProvider.deleteBusinessAssignedNamespace(r);
		createBusinessAssignedNamespace(ownerId,namespaceId);
	}

	private void createBusinessAssignedScopeOnDelOther(Long ownerId, Byte scopeCode, Long scopeId) {
		List<BusinessAssignedScope> list = businessProvider.listBusinessAssignedScopeByOwnerId(ownerId);
		if(list!=null&&!list.isEmpty())
			for(BusinessAssignedScope r:list)
				businessProvider.deleteBusinessAssignedScope(r.getId());
		createBusinessAssignedScope(ownerId,scopeCode,scopeId);
	}

	private void createBusinessAssignedScope(Long ownerId, Byte scopeCode, Long scopeId) {
		BusinessAssignedScope scope = new BusinessAssignedScope();
		scope = new BusinessAssignedScope();
		scope.setOwnerId(ownerId);
		scope.setScopeCode(scopeCode);
		scope.setScopeId(scopeId);
		businessProvider.createBusinessAssignedScope(scope);
	}

	private void createBusinessAssignedNamespace(Long businessId, Integer namespaceId) {
		BusinessAssignedNamespace bizNamespace = businessProvider.findBusinessAssignedNamespaceByNamespace(businessId,namespaceId,null);
		if(bizNamespace!=null){
			if(bizNamespace.getVisibleFlag().byteValue()!=BusinessAssignedNamespaceVisibleFlagType.VISIBLE.getCode()){
				bizNamespace.setVisibleFlag(BusinessAssignedNamespaceVisibleFlagType.VISIBLE.getCode());
				businessProvider.updateBusinessAssignedNamespace(bizNamespace);
			}
		}else{
			bizNamespace = new BusinessAssignedNamespace();
			bizNamespace.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			bizNamespace.setNamespaceId(namespaceId);
			bizNamespace.setOwnerId(businessId);
			bizNamespace.setVisibleFlag(BusinessAssignedNamespaceVisibleFlagType.VISIBLE.getCode());
			businessProvider.createBusinessAssignedNamespace(bizNamespace);
		}
	}

	private Business createBusiness(BusinessCommand cmd, long userId){
		if(cmd.getName() == null || cmd.getName().trim().equals(""))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter name,name is null");
		if(cmd.getCategroies() == null || cmd.getCategroies().isEmpty())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter categories,categories is null");

		Business business = ConvertHelper.convert(cmd, Business.class);
		business.setVisibleDistance(Double.valueOf(5000));
		business.setCreatorUid(userId);
		business.setBizOwnerUid(cmd.getBizOwnerUid() == null ? userId : cmd.getBizOwnerUid());
		business.setTargetId(cmd.getTargetId() == null ? "" : cmd.getTargetId());
		business.setDescription(cmd.getDescription() == null ? "" : cmd.getDescription());
		business.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if(cmd.getLatitude() != null && cmd.getLongitude() != null && cmd.getLatitude() != 0 && cmd.getLongitude() != 0){
			String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
			business.setGeohash(geohash);
		}
		this.businessProvider.createBusiness(business);
		return business;
	}

	private void updateBusiness(BusinessCommand cmd, Business business){
		if(cmd.getAddress() != null && !cmd.getAddress().trim().equals(""))
			business.setAddress(cmd.getAddress());
		if(cmd.getContact() != null && !cmd.getContact().trim().equals(""))
			business.setContact(cmd.getContact());
		if(cmd.getDescription() != null && !cmd.getDescription().trim().equals(""))
			business.setDescription(cmd.getDescription());
		if(cmd.getDisplayName() != null && !cmd.getDisplayName().trim().equals(""))
			business.setDisplayName(cmd.getDisplayName());
		if(cmd.getLatitude() != null)
			business.setLatitude(cmd.getLatitude());
		if(cmd.getLongitude() != null)
			business.setLongitude(cmd.getLongitude());
		if(cmd.getLogoUri() != null && !cmd.getLogoUri().trim().equals(""))
			business.setLogoUri(cmd.getLogoUri());
		if(cmd.getName() != null && !cmd.getName().trim().equals(""))
			business.setName(cmd.getName());
		if(cmd.getPhone() != null && !cmd.getPhone().trim().equals(""))
			business.setPhone(cmd.getPhone());
		if(cmd.getTargetId() != null && !cmd.getTargetId().trim().equals(""))
			business.setTargetId(cmd.getTargetId());
		if(cmd.getTargetType() != null)
			business.setTargetType(cmd.getTargetType());
		if(cmd.getUrl() != null && cmd.getUrl().trim().equals(""))
			business.setUrl(cmd.getUrl());

		if(cmd.getLatitude() != null && cmd.getLongitude() != null){
			String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
			business.setGeohash(geohash);
		}
		business.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		this.businessProvider.updateBusiness(business);
	}

	private void createBusinessScopes(Business business, List<BusinessScope> scopes){
		this.businessProvider.deleteBusinessVisibleScopeByBusiness(business);
		scopes.forEach(r ->{
			BusinessVisibleScope scope = ConvertHelper.convert(r,BusinessVisibleScope.class);
			scope.setOwnerId(business.getId());
			this.businessProvider.createBusinessVisibleScope(scope);
		});
	}

	private void createBusinessCategories(Business business, List<Long> categories){
		this.businessProvider.deleteBusinessCategoryByBusiness(business);
		categories.forEach(r ->{
			BusinessCategory category = new BusinessCategory();
			category.setCategoryId(r.longValue());
			category.setOwnerId(business.getId());
			Category c = categoryProvider.findCategoryById(r.longValue());
			if(c != null)
				category.setCategoryPath(c.getPath());
			this.businessProvider.createBusinessCategory(category);
		});
	}

	@Override
	public GetBusinessesByCategoryCommandResponse getBusinessesByCategory(GetBusinessesByCategoryCommand cmd) {
		long startTime = System.currentTimeMillis();
		if(cmd.getCategoryId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter categoryId,categoryId is null");
		}
		GetBusinessesByCategoryCommandResponse response = new GetBusinessesByCategoryCommandResponse();
		int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
		int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
				AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();

		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = UserContext.current().getNamespaceId();
		//域空间可见
		List<Long> businessNamespaceOwnerIds = listBusinessAssignedNamespaceIdByNamespaceId(namespaceId,BusinessAssignedNamespaceVisibleFlagType.VISIBLE.getCode());
		if(businessNamespaceOwnerIds==null||businessNamespaceOwnerIds.isEmpty()){
			LOGGER.debug("businessAssignedNamespaceOwnerIds is empty.namespaceId="+namespaceId);
			return null;
		}
		//查全国，小区，城市,附近5000米可见
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Invalid paramter communityId,community is not exists.,communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Invalid paramter communityId,community is not exists.");
		}
		//CommunityGeoPoint
		List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(community.getId());
		if(points == null || points.isEmpty()){
			LOGGER.error("Community geo points is not exists,communityId=" + community.getId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Community geo points is not exists.");
		}
		CommunityGeoPoint point = points.get(0);
		final double lat = point.getLatitude();
		final double lon = point.getLongitude();
		List<String> geoHashList = getGeoHashCodeList(lat, lon);
		//recommendBizIds
		long communityId = community.getId();
		long cityId = community.getCityId();
		List<Long> recommendBizIds = this.businessProvider.listBusinessAssignedScopeByScope(cityId,communityId,businessNamespaceOwnerIds).stream()
				.map(r->r.getOwnerId()).collect(Collectors.toList());
		//获取指定类型的服务
		List<Long> categoryIds = categoryProvider.getBusinessSubCategories(cmd.getCategoryId());
		List<Business> scopeBusinesses = filterBusinessByCategorysAndBizIds(recommendBizIds,categoryIds);
		List<Business> geoBusinesses = businessProvider.listBusinessByCategroys(categoryIds,geoHashList,businessNamespaceOwnerIds);
		List<Business> businesses = filterBusinessByDistance(scopeBusinesses,geoBusinesses,lat,lon);
		//组装
		final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
		final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
		final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
		Category c = categoryProvider.findCategoryById(cmd.getCategoryId());
		//favoriteBizIds
		List<Long> favoriteBizIds = getFavoriteBizIds(userId, communityId,cityId,namespaceId);
		List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();
		final Integer [] favoriteCount = new Integer [1];
		favoriteCount[0] = 0;

		if(businesses!=null&&!businesses.isEmpty()){
			businesses.forEach(r ->{
				BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
				List<CategoryDTO> categories = new ArrayList<>();
				categories.add(ConvertHelper.convert(c, CategoryDTO.class));
				dto.setCategories(categories);
				dto.setLogoUrl(processLogoUrl(r, userId,imageUrl));
				dto.setUrl(processUrl(r,prefixUrl,businessDetailUrl));
				dto.setRecommendStatus(BusinessRecommendStatus.NONE.getCode());
				if(favoriteBizIds != null && favoriteBizIds.contains(r.getId())){
					dto.setFavoriteStatus(BusinessFavoriteStatus.FAVORITE.getCode());
					favoriteCount[0] += 1;
				}
				else{
					dto.setFavoriteStatus(BusinessFavoriteStatus.NONE.getCode());
				}
				if(lat != 0 || lon != 0)
					dto.setDistance((int)calculateDistance(r.getLatitude(),r.getLongitude(),lat, lon));
				else
					dto.setDistance(0);
				//店铺图标需要裁剪
				if(r.getTargetType().byteValue() == BusinessTargetType.ZUOLIN.getCode()){
					dto.setScaleType(ScaleType.TAILOR.getCode());
				}
				dtos.add(dto);
			});
		}
		//按分类查询剩余推荐的商家
		sortBusinesses(dtos);
		List<BusinessDTO> dtos2 = this.operatorByPage(dtos,response,cmd.getPageOffset(),cmd.getPageSize());
		response.setRequests(dtos2);
		response.setFavoriteCount(favoriteCount[0]);
		long endTime = System.currentTimeMillis();
		LOGGER.debug("GetBusinesses by category,categoryId=" + cmd.getCategoryId() 
				+ ",communityId=" + cmd.getCommunityId() + ",elapse=" + (endTime - startTime));
		return response;
	}

	public List<BusinessDTO> getBusinesses(List<Long> categoryIds,Long communityId){
		
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		if(null == categoryIds || categoryIds.size() == 0){
			LOGGER.error("categoryIds is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"categoryIds is null.");
		}
		
		Community community = communityProvider.findCommunityById(communityId);
		LOGGER.debug("processStat communityId is :" + communityId);
		
		if(community == null){
			LOGGER.error("Invalid paramter communityId,community is not exists.,communityId=" + communityId);
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Invalid paramter communityId,community is not exists.");
		}
		
		//CommunityGeoPoint
		List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(community.getId());
		if(points == null || points.isEmpty()){
			LOGGER.error("Community geo points is not exists,communityId=" + community.getId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
							"Community geo points is not exists.");
		}
		List<Long> businessNamespaceOwnerIds = listBusinessAssignedNamespaceIdByNamespaceId(namespaceId,BusinessAssignedNamespaceVisibleFlagType.VISIBLE.getCode());
		if(businessNamespaceOwnerIds==null||businessNamespaceOwnerIds.isEmpty()){
			LOGGER.debug("businessAssignedNamespaceOwnerIds is empty.namespaceId="+namespaceId);
			return null;
		}
		
		CommunityGeoPoint point = points.get(0);
		final double lat = point.getLatitude();
		final double lon = point.getLongitude();
		List<String> geoHashList = getGeoHashCodeList(lat, lon);
		
		long cityId = community.getCityId();
		List<Long> recommendBizIds = this.businessProvider.listBusinessAssignedScopeByScope(cityId,communityId,businessNamespaceOwnerIds).stream()
				.map(r->r.getOwnerId()).collect(Collectors.toList());
		
		List<Long> childCategoryIds = new ArrayList<Long>();
		for (Long categoryId : categoryIds) {
			List<Long> childIds = categoryProvider.getBusinessSubCategories(categoryId);
			if(null != childIds && childIds.size() > 0){
				childCategoryIds.addAll(childIds);
			}
		}
		
		List<Business> scopeBusinesses = filterBusinessByCategorysAndBizIds(recommendBizIds,childCategoryIds);
		List<Business> geoBusinesses = businessProvider.listBusinessByCategroys(childCategoryIds,geoHashList,businessNamespaceOwnerIds);
		List<Business> businesses = filterBusinessByDistance(scopeBusinesses,geoBusinesses,lat,lon);
		
		if(null != businesses){
			return businesses.stream().map( r ->{
				return ConvertHelper.convert(r, BusinessDTO.class);
			}).collect(Collectors.toList());
		}
		
		return new ArrayList<BusinessDTO>();
	}
	
	private List<Business> filterBusinessByDistance(List<Business> scopeBusinesses, List<Business> geoBusinesses,double lantitude,double longitude) {
		if(scopeBusinesses==null||scopeBusinesses.isEmpty()){
			if(geoBusinesses==null||geoBusinesses.isEmpty()){
				return null;
			}else{
				return filterBusinessByScope(scopeBusinesses,geoBusinesses,lantitude,longitude);
			}
		}else{
			if(geoBusinesses==null||geoBusinesses.isEmpty()){
				return scopeBusinesses;
			}else{
				return filterBusinessByScope(scopeBusinesses,geoBusinesses,lantitude,longitude);
			}
		}
	}

	private List<Business> filterBusinessByScope(List<Business> scopeBusinesses, List<Business> geoBusinesses,double lantitude,double longitude) {
		if(scopeBusinesses==null||scopeBusinesses.isEmpty()){
			List<Business> list = new ArrayList<Business>();
			for(Business r:geoBusinesses){
				double distance = calculateDistance(r.getLatitude(),r.getLongitude(),lantitude, longitude);
				if(distance <= r.getVisibleDistance().doubleValue())
					list.add(r);
			}
			return list;
		}
		List<Business> list = new ArrayList<Business>();
		for(Business r:geoBusinesses){
			boolean isExist = false;
			for(Business scopeBiz:scopeBusinesses){
				if(r.getId().longValue()==scopeBiz.getId().longValue()){
					isExist=true;break;
				}
			}
			if(!isExist){//geoBusinesses不存在于scopeBusinesses,计算附近5000米距离
				double distance = calculateDistance(r.getLatitude(),r.getLongitude(),lantitude, longitude);
				if(distance <= r.getVisibleDistance().doubleValue())
					list.add(r);
			}
		}
		if(list!=null)
			scopeBusinesses.addAll(list);
		return scopeBusinesses;
	}

	private List<Business> filterBusinessByCategorysAndBizIds(List<Long> bizIds,List<Long> categoryIds) {
		if(bizIds==null||bizIds.isEmpty())
			return null;
		return businessProvider.listBusinessByCategorys(categoryIds,bizIds);
	}

	private List<Long> listBusinessAssignedNamespaceIdByNamespaceId(Integer namespaceId, Byte code) {
		List<BusinessAssignedNamespace> list = businessProvider.listBusinessAssignedNamespaceByNamespaceId(namespaceId,code);
		if(list==null||list.isEmpty())
			return null;
		return list.stream().map(r->{return r.getOwnerId();}).collect(Collectors.toList());
	}

	@Override
	public List<String> listBusinessByCommonityId(ListBusinessByCommonityIdCommand cmd) {
		Community community = null;
		if(cmd.getCommunityId()!=null){
			community = communityProvider.findCommunityById(cmd.getCommunityId());
			if(community == null){
				LOGGER.error("Community is not exists,communityId=" + cmd.getCommunityId());
				throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
						"Invalid paramter communityId,communityId is not exists.");
			}
		}

		long startTime = System.currentTimeMillis();

		List<String> bizIds = new ArrayList<String>();
		//CommunityGeoPoint
		CommunityGeoPoint point = null;
		if(cmd.getCommunityId()!=null){
			List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
			if(points == null || points.isEmpty()){
				LOGGER.error("Community is not exists geo points,communityId=" + cmd.getCommunityId());
			}
			else
				point = points.get(0);
		}
		final double lat = point != null ? point.getLatitude() : 0;
		final double lon = point != null ? point.getLongitude() : 0;
		List<String> geoHashList = getGeoHashCodeList(lat, lon);
		List<Business> businesses = null;

		//recommendBizIds
		long cityId = community.getCityId()==null?0:community.getCityId();
		long communityId = cmd.getCommunityId()==null?0:cmd.getCommunityId();
		List<Long> recommendBizIds = this.businessProvider.listBusinessAssignedScopeByScope(cityId,communityId,null).stream()
				.map(r->r.getOwnerId()).collect(Collectors.toList());

		//获取指定类型的服务
		List<Long> categoryPIds = this.categoryProvider.getBusinessSubCategories(cmd.getCategoryId());
		List<Long> categoryIds = new ArrayList<Long>();
		if(categoryPIds!=null&&!categoryPIds.isEmpty()){
			for(Long catePId:categoryPIds){
				List<Long> subCateIds = this.categoryProvider.getBusinessSubCategories(catePId);
				if(subCateIds!=null&&!subCateIds.isEmpty())
					categoryIds.addAll(subCateIds);
			}
		}
		businesses = this.businessProvider.listBusinessByCategroys(categoryIds,geoHashList,null);
		//从算法过滤的范围中再缩小范围
		businesses = this.filterDistance(businesses,lat,lon,recommendBizIds);
		if(recommendBizIds!=null&&!recommendBizIds.isEmpty()){
			List<Business> recombizs = this.businessProvider.listBusinessByIds(recommendBizIds);
			recombizs.forEach(r->{
				if(r.getTargetId()!=null)
					bizIds.add(r.getTargetId());
			});
		}
		businesses.forEach(r ->{
			if(!bizIds.contains(r.getTargetId()))
				bizIds.add(r.getTargetId());
		});
		long endTime = System.currentTimeMillis();
		LOGGER.debug("get businesses by communityId,categoryId=" + cmd.getCategoryId() 
				+ ",communityId=" + cmd.getCommunityId()+ ",elapse=" + (endTime - startTime));
		return bizIds;
	}

	private List<BusinessDTO> operatorByPage(List<BusinessDTO> dtos,GetBusinessesByCategoryCommandResponse resposne,Integer cmdPageOffset, Integer cmdPageSize) {
		if(dtos == null || dtos.size() < 1)
			return new ArrayList<BusinessDTO>();
		int pageOffset = cmdPageOffset == null ? 1 : cmdPageOffset;
		int pageSize = cmdPageSize == null ? 10 : cmdPageSize;
		int offset = (int) PaginationHelper.offsetFromPageOffset((long)pageOffset, pageSize);

		int needRow = offset+pageSize;
		if(dtos.size() > needRow){
			resposne.setNextPageOffset(pageOffset+1);
			return dtos.subList(offset, needRow);
		}
		else{
			return dtos.subList(offset, dtos.size());
		}
	}

	private List<Business> filterDistance(List<Business> businesses, double lat,double lon, List<Long> recommendBizIds) {
		List<Business> list = new ArrayList<Business>();
		if(businesses == null || businesses.isEmpty())
			return businesses;

		for(Business r:businesses){
			if(recommendBizIds != null && recommendBizIds.contains(r.getId())){
				list.add(r);
				continue;
			}
			if(r.getTargetType().byteValue() != BusinessTargetType.ZUOLIN.getCode()){
				list.add(r);
				continue;
			}
			if(r.getVisibleDistance()==null||r.getVisibleDistance().doubleValue()==0){
				list.add(r);
				continue;
			}
			if(lat == 0 && lon == 0){
				list.add(r);
				continue;
			}
			int distance = (int)calculateDistance(r.getLatitude(),r.getLongitude(),lat, lon);
			if(distance <= r.getVisibleDistance().doubleValue())
				list.add(r);
		}
		return list;
	}

	private String processUrl(Business business, String authenticatePrefix,String detailUrl){
		if(authenticatePrefix.trim().equals(""))
			LOGGER.error("Buiness authenticate prefix url is empty.");
		if(detailUrl.trim().equals(""))
			LOGGER.error("Buiness detail url  is empty.");
		if(business.getTargetType() == BusinessTargetType.ZUOLIN.getCode()){
			String businessDetailUrl = null;
			if(detailUrl.contains("spoint")){
				businessDetailUrl = detailUrl.replace("spoint", business.getTargetId()).trim();
			}
			else
				businessDetailUrl = detailUrl.trim() + business.getTargetId();
			return authenticatePrefix.trim() + businessDetailUrl;
		}
		return business.getUrl();
	}

	/*
	 *category : 
	 *3001 -> 3005,3006,等
	 *9999 -> none
	 *	 
	 */
	private void processRecommendBusinesses( List<Long> recommendBizIds, List<BusinessDTO> dtos, 
			Category category, long userId,List<Long> favoriteBizIds, Integer[] favoriteCount){
		if(category.getId().longValue() == CATEOGRY_RECOMMEND)
			return;
		//businessCategories
		List<BusinessCategory> businessCategories = this.businessProvider.listBusinessCategoriesByCatPIdAndOwnerIds(category.getId(),recommendBizIds);
		if(businessCategories == null || businessCategories.isEmpty())
			return;
		List<Long> bizIds = businessCategories.stream().map(r -> r.getOwnerId()).collect(Collectors.toList());
		List<Business> recomBusinesses = this.businessProvider.findBusinessByIds(bizIds);
		if(recomBusinesses != null && !recomBusinesses.isEmpty()){
			String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
			recomBusinesses.forEach(r ->{
				BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
				List<CategoryDTO> categories = new ArrayList<>();
				categories.add(ConvertHelper.convert(category, CategoryDTO.class));
				dto.setCategories(categories);
				dto.setLogoUrl(processLogoUrl(r,userId,imageUrl));
				if(favoriteBizIds != null && favoriteBizIds.contains(r.getId())){
					dto.setFavoriteStatus(BusinessFavoriteStatus.FAVORITE.getCode());
					favoriteCount[0] += 1;
				}
				else
					dto.setFavoriteStatus(BusinessFavoriteStatus.NONE.getCode());
				dto.setRecommendStatus(BusinessRecommendStatus.RECOMMEND.getCode());
				dto.setDistance(0);

				dtos.add(dto);
			});
		}
	}

	private String processLogoUrl(Business business, long userId,String imageUrl){
		if(business.getLogoUri() == null || business.getLogoUri().trim().equals(""))
			return null;
		if(business.getTargetType() != BusinessTargetType.ZUOLIN.getCode())
			return parserUri(business.getLogoUri(),EntityType.USER.getCode(),userId);
		return imageUrl.trim() + business.getLogoUri();
	}

	private double calculateDistance(double latitude, double longitude, double lat, double lon){
		//getDistanceMi计算的是英里
		final double MILES_KILOMETRES_RATIO = 1.609344;
		//return  单位:米
		return DistanceUtils.getDistanceMi(latitude,longitude,lat , lon) * MILES_KILOMETRES_RATIO * 1000;
	}
	private List<BusinessDTO> sortBusinesses(List<BusinessDTO> dtos){
		if(dtos == null || dtos.isEmpty())
			return dtos;
		//sort by distance
		dtos.sort(new Comparator<BusinessDTO>() {
			@Override
			public int compare(BusinessDTO o1, BusinessDTO o2) {

				return (int) (o1.getDistance() - o2.getDistance());
			}
		});
		//recommand business first
		/*dtos.sort(new Comparator<BusinessDTO>() {
			@Override
			public int compareTo(BusinessDTO o1, BusinessDTO o2) {
				return o2.getRecommendStatus() - o1.getRecommendStatus();
			}
		});*/
		//sort by targetType
		dtos.sort(new Comparator<BusinessDTO>() {
			@Override
			public int compare(BusinessDTO o1, BusinessDTO o2) {
				return o2.getTargetType() - o1.getTargetType();
			}
		});
		return null;
	}

	private String parserUri(String uri,String ownerType, long ownerId){
		try {
			if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
				return contentServerService.parserUri(uri,ownerType,ownerId);

		} catch (Exception e) {
			LOGGER.error("Parser uri is error." + e.getMessage());
		}
		return null;

	}

	private List<String> getGeoHashCodeList(double latitude, double longitude){

		GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 4);
		GeoHash[] adjacents = geo.getAdjacent();
		List<String> geoHashCodes = new ArrayList<String>();
		geoHashCodes.add(geo.toBase32());
		for(GeoHash g : adjacents) {
			geoHashCodes.add(g.toBase32());
		}
		return geoHashCodes;
	}

	private List<Long> getFavoriteBizIds(long userId,long cmmtyId,long cityId,Integer namesapceId){
		List<Long> removeIds = new ArrayList<>();
		List<Long> userBizIds = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
				0, ScopeType.USER.getCode(), userId,namesapceId).stream()
				.filter(r ->{
					if(r.getDisplayFlag().byteValue() == ItemDisplayFlag.DISPLAY.getCode())
						return true;
					removeIds.add(Long.valueOf(r.getTargetId()));
					return false;
				}).map(r ->Long.valueOf(r.getTargetId())).collect(Collectors.toList());

		List<Long> cmmtyBizIds = null;
		if(cmmtyId!=0){
			cmmtyBizIds = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
					0, ScopeType.COMMUNITY.getCode(), cmmtyId,namesapceId).stream()
					.filter(r -> !removeIds.contains(r.getTargetId())).map(r -> Long.valueOf(r.getTargetId())).collect(Collectors.toList());
		}
		List<Long> cityBizIds = null;
		if(cityId!=0){
			cityBizIds = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
					0, ScopeType.CITY.getCode(), cityId,namesapceId).stream()
					.filter(r -> !removeIds.contains(r.getTargetId())).map(r ->Long.valueOf(r.getTargetId())).collect(Collectors.toList());
		}
		List<Long> countyBizIds = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
				0, ScopeType.ALL.getCode(), 0,namesapceId).stream()
				.filter(r -> !removeIds.contains(r.getTargetId())).map(r ->Long.valueOf(r.getTargetId())).collect(Collectors.toList());

		List<Long> favoriteBizIds = new ArrayList<Long>();
		if(userBizIds != null && !userBizIds.isEmpty())
			favoriteBizIds.addAll(userBizIds);
		if(cmmtyBizIds != null && !cmmtyBizIds.isEmpty())
			favoriteBizIds.addAll(cmmtyBizIds);
		if(cityBizIds != null && !cityBizIds.isEmpty())
			favoriteBizIds.addAll(cityBizIds);
		if(countyBizIds != null && !countyBizIds.isEmpty())
			favoriteBizIds.addAll(countyBizIds);
		return favoriteBizIds;
	}

	@Override
	public void updateBusiness(UpdateBusinessCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");
		Business business = this.businessProvider.findBusinessById(cmd.getId());
		if(business == null){
			LOGGER.error("Business is not exists.id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
					"Business is not exists.");
		}
		if(cmd.getAddress() != null && !cmd.getAddress().trim().equals(""))
			business.setAddress(cmd.getAddress());
		if(cmd.getContact() != null && !cmd.getContact().trim().equals(""))
			business.setContact(cmd.getContact());
		if(cmd.getDescription() != null && !cmd.getDescription().trim().equals(""))
			business.setDescription(cmd.getDescription());
		if(cmd.getDisplayName() != null && !cmd.getDisplayName().trim().equals(""))
			business.setDisplayName(cmd.getDisplayName());
		if(cmd.getLatitude() != null)
			business.setLatitude(cmd.getLatitude());
		if(cmd.getLongitude() != null)
			business.setLongitude(cmd.getLongitude());
		if(cmd.getLogoUri() != null && !cmd.getLogoUri().trim().equals(""))
			business.setLogoUri(cmd.getLogoUri());
		if(cmd.getName() != null && !cmd.getName().trim().equals(""))
			business.setName(cmd.getName());
		if(cmd.getPhone() != null && !cmd.getPhone().trim().equals(""))
			business.setPhone(cmd.getPhone());
		if(cmd.getTargetId() != null && !cmd.getTargetId().trim().equals(""))
			business.setTargetId(cmd.getTargetId());
		if(cmd.getTargetType() != null)
			business.setTargetType(cmd.getTargetType());
		if(cmd.getUrl() != null && cmd.getUrl().trim().equals(""))
			business.setUrl(cmd.getUrl());

		if(cmd.getLatitude() != null && cmd.getLongitude() != null){
			String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
			business.setGeohash(geohash);

		}
		this.dbProvider.execute((TransactionStatus status) -> {
			this.businessProvider.updateBusiness(business);
			if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
				this.businessProvider.deleteBusinessCategoryByBusiness(business);
				cmd.getCategroies().forEach(r ->{
					BusinessCategory category = new BusinessCategory();
					category.setCategoryId(r.longValue());
					category.setOwnerId(business.getId());
					Category c = categoryProvider.findCategoryById(r.longValue());
					if(c != null)
						category.setCategoryPath(c.getPath());
					this.businessProvider.createBusinessCategory(category);
				});
			}

			return null;
		});
	}

	@Override
	public void deleteBusiness(DeleteBusinessCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");
		Business business = this.businessProvider.findBusinessById(cmd.getId());
		if(business == null){
			LOGGER.error("Business is not exists.id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
					"Business is not exists.");
		}
		this.businessProvider.deleteBusiness(business);
		//删除服务市场item
		this.launchPadProvider.deleteLaunchPadItemByTargetTypeAndTargetId(ItemTargetType.BIZ.getCode(),business.getId());
	}

	@Override
	public BusinessDTO findBusinessById(Long id) {
		if(id == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");
		Business business = this.businessProvider.findBusinessById(id);
		if(business != null)
			return ConvertHelper.convert(business,BusinessDTO.class);
		return null;
	}

	@Override
	public List<BusinessDTO> getBusinessesByScope(GetBusinessesByScopeCommand cmd) {
		
		return null;
	}

	@Override
	public ListBusinessesByKeywordAdminCommandResponse listBusinessesByKeyword(ListBusinessesByKeywordAdminCommand cmd) {
		int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
		int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
				AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
		User user = UserContext.current().getUser();
		Integer namespaceId = user.getNamespaceId()==null?0:user.getNamespaceId();
		int offset = (int) PaginationHelper.offsetFromPageOffset((long)pageOffset, pageSize);
		List<BusinessAdminDTO> result = null;
		final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
		List<Business> businesses = this.businessProvider.listBusinessesByKeyword(cmd.getKeyword() , offset , pageSize);
		if(businesses != null && !businesses.isEmpty())
			result = businesses.stream().map(r -> {
				BusinessAdminDTO dto = ConvertHelper.convert(r, BusinessAdminDTO.class);
				dto.setLogoUrl(processLogoUrl(r,user.getId(),imageUrl));
				//set recommend status
				processRecommendStatus(dto);

				processPromoteFlag(dto,namespaceId);

				return dto;
			}
					).collect(Collectors.toList());


		ListBusinessesByKeywordAdminCommandResponse response = new ListBusinessesByKeywordAdminCommandResponse();
		response.setRequests(result);
		if(result != null && result.size() == pageSize)
			response.setNextPageOffset(pageOffset + 1);
		return response;
	}

	private void processPromoteFlag(BusinessAdminDTO dto,Integer namespaceId) {
		List<LaunchPadItem> countryItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
				dto.getId(), ScopeType.ALL.getCode(),0,namespaceId);
		List<BusinessPromoteScopeDTO> promoteScopes = new ArrayList<>();
		if(countryItems != null && !countryItems.isEmpty()){
			dto.setPromoteFlag((byte)1);
			BusinessPromoteScopeDTO scope = new BusinessPromoteScopeDTO();
			scope.setRegionName("全国");
			scope.setScopeCode(ScopeType.ALL.getCode());
			promoteScopes.add(scope);
			dto.setPromoteScopes(promoteScopes);
			return ;
		}
		List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
				dto.getId(), ScopeType.CITY.getCode(),0,namespaceId);
		if(cityItems != null && !cityItems.isEmpty()){
			dto.setPromoteFlag((byte)1);
			cityItems.forEach(c ->{
				Region region = this.regionProvider.findRegionById(c.getScopeId());
				BusinessPromoteScopeDTO scope = new BusinessPromoteScopeDTO();
				if(region != null){
					scope.setRegionName(region.getName());
					scope.setScopeCode(ScopeType.CITY.getCode());
					promoteScopes.add(scope);
				}
			});
			dto.setPromoteScopes(promoteScopes);
		}
		List<LaunchPadItem> cmmtyItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
				dto.getId(), ScopeType.COMMUNITY.getCode(),0,namespaceId);
		if(cmmtyItems != null && !cmmtyItems.isEmpty()){
			dto.setPromoteFlag((byte)1);
			cmmtyItems.forEach(c ->{
				Community community = this.communityProvider.findCommunityById(c.getScopeId());
				BusinessPromoteScopeDTO scope = new BusinessPromoteScopeDTO();
				if(community != null){
					scope.setRegionName(community.getName());
					scope.setScopeCode(ScopeType.COMMUNITY.getCode());
					promoteScopes.add(scope);
				}
			});
		}



	}

	private void processRecommendStatus(BusinessAdminDTO dto) {
		List<BusinessAssignedScope> assignedScopes = this.businessProvider.findBusinessAssignedScopesByBusinessId(dto.getId());
		if(assignedScopes != null && !assignedScopes.isEmpty()){
			dto.setRecommendStatus(BusinessRecommendStatus.RECOMMEND.getCode());
			List<BusinessAssignedScopeDTO> scopes = new ArrayList<>();
			assignedScopes.forEach(a ->{

				BusinessAssignedScopeDTO scopeDTO = ConvertHelper.convert(a,BusinessAssignedScopeDTO.class);
				if(a.getScopeCode().byteValue() == ScopeType.ALL.getCode())
					scopeDTO.setRegionName("全国");
				else if(a.getScopeCode().byteValue() == ScopeType.CITY.getCode()){
					Region region = this.regionProvider.findRegionById(scopeDTO.getScopeId());
					if(region != null)
						scopeDTO.setRegionName(region.getName());
				}
				else{
					Community community = this.communityProvider.findCommunityById(scopeDTO.getScopeId());
					if(community != null)
						scopeDTO.setRegionName(community.getName());
				}
				scopes.add(scopeDTO);
			});
			dto.setAssignedScopes(scopes);
		}

	}

	@Override
	public void recommendBusiness(RecommendBusinessesAdminCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,id is null");
		if(cmd.getRecommendStatus().byteValue() == BusinessRecommendStatus.RECOMMEND.getCode()
				&& cmd.getScopes() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter scopes null,scopes is null");
		}

		Business business = this.businessProvider.findBusinessById(cmd.getId());
		if(business == null){
			LOGGER.error("Business is not exits.id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
					"Business is not exits.id=" + cmd.getId());
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			this.businessProvider.deleteBusinessAssignedScopeByBusinessId(cmd.getId());

			if(cmd.getRecommendStatus().byteValue() == BusinessRecommendStatus.RECOMMEND.getCode()){
				cmd.getScopes().forEach(r ->{
					BusinessAssignedScope scope = ConvertHelper.convert(r,BusinessAssignedScope.class);
					scope.setOwnerId(cmd.getId());
					this.businessProvider.createBusinessAssignedScope(scope);
				});
			}
			return true;
		});
	}

	@Override
	public void syncDeleteBusiness(SyncDeleteBusinessCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");
		Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
		if(business == null){
			LOGGER.error("Business is not exists.id=" + cmd.getId());
			/*throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
                    "Business is not exists.");*/
		}
		User user = userProvider.findUserById(cmd.getUserId());
		if(user == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is not found");
		}
		this.dbProvider.execute((TransactionStatus status) -> {
			//this.userActivityService.cancelShop(user.getId());
			if(business != null){
				this.businessProvider.deleteBusiness(business.getId());
				//删除服务市场item
				this.launchPadProvider.deleteLaunchPadItemByTargetTypeAndTargetId(ItemTargetType.BIZ.getCode(),business.getId());
				//删除商家namespace
				List<BusinessAssignedNamespace> bizNamespacelist = businessProvider.listBusinessAssignedNamespaceByOwnerId(business.getId(),null);
				if(bizNamespacelist!=null&&!bizNamespacelist.isEmpty()){
					for(BusinessAssignedNamespace r:bizNamespacelist)
						businessProvider.deleteBusinessAssignedNamespace(r);
				}
			}
			return true;
		});
	}

	@Override
	public void createBusiness(CreateBusinessAdminCommand cmd) {
		if(cmd.getUrl() == null || cmd.getUrl().trim().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter url,url is null");
		}

		User user = UserContext.current().getUser();
		long userId = user.getId();
		this.dbProvider.execute((TransactionStatus status) -> {
			cmd.setLatitude(0D);
			cmd.setLongitude(0D);
			Business business = createBusiness(cmd, userId);

			if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
				createBusinessCategories(business, cmd.getCategroies());
			}
			return true;
		});
	}

	@Override
	public void syncUserFavorite(UserFavoriteCommand cmd) {
		isValiad(cmd);
		Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
		favoriteBusiness(cmd.getUserId(),cmd.getNamespaceId(), business.getId(),true, "default");

	}

	private boolean isValiad(UserFavoriteCommand cmd){
		if(cmd.getUserId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is not found");
		}
		User user = userProvider.findUserById(cmd.getUserId());
		if(user == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter userId,userId is not found");
		}
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");
		Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
		if(business == null){
			LOGGER.error("Business is not exists.id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
					"Business is not exists.");
		}
		return true;
	}

	@Override
	public void syncUserCancelFavorite(UserFavoriteCommand cmd) {
		isValiad(cmd);
		Business business = this.businessProvider.findBusinessByTargetId(cmd.getId()); 
		cancelFavoriteBusiness(cmd.getUserId(),cmd.getNamespaceId(), business.getId(),true, "default");

	}

	@Override
	public Byte findBusinessFavoriteStatus(UserFavoriteCommand cmd) {
		isValiad(cmd);
		User user = userProvider.findUserById(cmd.getUserId());
		Integer namespaceId = user.getNamespaceId()==null?0:user.getNamespaceId();
		Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
		List<LaunchPadItem> userItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), 
				business.getId(), ScopeType.USER.getCode(), user.getId(),namespaceId);
		if(userItems != null && !userItems.isEmpty()){
			return BusinessFavoriteStatus.FAVORITE.getCode();
		}
		return BusinessFavoriteStatus.NONE.getCode();
	}

	@Override
	public void favoriteBusiness(FavoriteBusinessCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id is null or empty");

		User user = UserContext.current().getUser();
		long userId = user.getId();
		favoriteBusiness(userId,user.getNamespaceId(),cmd.getId(),true, "default");
	}

	@Override
	public void favoriteBusinesses(FavoriteBusinessesCommand cmd) {
		if(cmd.getBizs() == null || cmd.getBizs().size() < 1)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter bizs is null or empty");

		User user = UserContext.current().getUser();
		long userId = user.getId();
		for(FavoriteBusinessDTO r:cmd.getBizs()){
			FavoriteFlagType flag = FavoriteFlagType.fromCode(r.getFavoriteFlag());
			if(flag == null){
				LOGGER.error("FavoriteFlag is error.bizId=" + r.getId()+",favoriteFlag="+r.getFavoriteFlag());
				continue ;
			}
			if(r.getId() == null){
				LOGGER.error("biz id is null.bizId=" + r.getId()+",favoriteFlag="+r.getFavoriteFlag());
				continue ;
			}
			if(r.getFavoriteFlag() == FavoriteFlagType.FAVORITE.getCode())
				favoriteBusiness(userId,user.getNamespaceId(), r.getId(),false, "default");
			else if(r.getFavoriteFlag() == FavoriteFlagType.CANCEL_FAVORITE.getCode())
				cancelFavoriteBusiness(userId,user.getNamespaceId(), r.getId(),false, "default");
		}
	}

	private void favoriteBusiness(long userId,Integer namespaceId, long businessId, boolean isException, String baseScene){
		Business business = this.businessProvider.findBusinessById(businessId);
		if(business == null){
			LOGGER.error("Business is not exists.id=" + businessId);
			if(isException)
				throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST,"Business is not exists.");
			else
				return ;
		}
		List<LaunchPadItem> list = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), businessId, ScopeType.USER.getCode(), userId,namespaceId);
		LaunchPadItem item = null;
		if(list != null && list.size() > 0){
			item = list.get(0);
		}
		//List<LaunchPadItem> bizItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), businessId, null, 0);
		boolean flag = isExistsUnUserItem(businessId,namespaceId);
		if(item != null){
			if(flag){
				item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
			}else{
				item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
			}
			item.setSceneType(baseScene);
			item.setScopeId(userId);
			item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
			this.launchPadProvider.updateLaunchPadItem(item);
		}else{
			List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
			item = new LaunchPadItem();
			item.setAppId(0L);
			item.setItemName(StringUtils.upperCase(business.getName()));
			item.setItemLabel(business.getDisplayName());
			item.setActionType(ActionType.THIRDPART_URL.getCode());
			item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
			item.setItemGroup(LaunchPadConstants.GROUP_BIZS);
			item.setItemLocation(LaunchPadConstants.ITEM_LOCATION);
			item.setIconUri(business.getLogoUri());
			item.setItemWidth(1);
			item.setItemHeight(1);
			item.setNamespaceId(namespaceId==null?0:namespaceId);
			item.setScopeCode(ScopeType.USER.getCode());
			item.setScopeId(userId);
			item.setDefaultOrder(0);
			item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
			item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
			item.setBgcolor(0);
			item.setTargetType(ItemTargetType.BIZ.getCode());
			item.setTargetId(businessId);
			item.setSceneType(baseScene);
			if(flag)
				item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
			items.add(item);
			this.launchPadProvider.createLaunchPadItems(items);
		}
	}

	private boolean isExistsUnUserItem(long businessId,Integer namespaceId){
		List<LaunchPadItem> result = getUnUserItems(businessId,namespaceId);
		if(result != null && !result.isEmpty())
			return true;
		return false;
	}

	private List<LaunchPadItem> getUnUserItems(long businessId,Integer namespaceId){
		List<LaunchPadItem> result = new ArrayList<LaunchPadItem>();
		List<LaunchPadItem> countyItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), businessId, ScopeType.ALL.getCode(), 0,namespaceId);
		List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), businessId, ScopeType.CITY.getCode(), 0,namespaceId);
		List<LaunchPadItem> cmmtyItems = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), businessId, ScopeType.COMMUNITY.getCode(), 0,namespaceId);
		if(countyItems != null && !countyItems.isEmpty())
			result.addAll(countyItems);
		if(cityItems != null && !cityItems.isEmpty())
			result.addAll(cityItems);
		if(cmmtyItems != null && !cmmtyItems.isEmpty())
			result.addAll(cmmtyItems);
		return result;
	}


	@Override
	public void cancelFavoriteBusiness(CancelFavoriteBusinessCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");
		User user = UserContext.current().getUser();
		long userId = user.getId();
		cancelFavoriteBusiness(userId,user.getNamespaceId(), cmd.getId(),true, "default");
	}

	private void cancelFavoriteBusiness(long userId,Integer namespaceId, long businessId,boolean isThrowExcept, String baseScene){
		Business business = this.businessProvider.findBusinessById(businessId);
		if(business == null){
			LOGGER.error("Business is not exists.id=" + businessId);
			if(isThrowExcept)
				throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
						"Business is not exists.");
			else
				return ;
		}

		List<LaunchPadItem> list = this.launchPadProvider.findLaunchPadItemByTargetAndScope(ItemTargetType.BIZ.getCode(), businessId, ScopeType.USER.getCode(), userId,namespaceId);
		LaunchPadItem item = null;
		if(list != null && list.size() > 0){
			item = list.get(0);
		}

		if(item != null){
			//存在非用户可见的item,则修改为 覆盖且不可见,否则直接删除
			if(isExistsUnUserItem(businessId,namespaceId)){
				item.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
				item.setScopeId(userId);
				item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
				item.setScopeCode(ScopeType.USER.getCode());
				this.launchPadProvider.updateLaunchPadItem(item);
			}else{
				this.launchPadProvider.deleteLaunchPadItem(item);
			}
		}else{
			List<LaunchPadItem> bizItems = getUnUserItems(businessId,namespaceId);
			if(bizItems != null && !bizItems.isEmpty()){
				item = bizItems.get(0);
				item.setId(0L);
				item.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
				item.setScopeId(userId);
				item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
				item.setScopeCode(ScopeType.USER.getCode());
				item.setNamespaceId(namespaceId==null?0:namespaceId);
				item.setSceneType(baseScene);
				this.launchPadProvider.createLaunchPadItem(item);
			}
		}
		//如果是用户自定义的直接删除,否则增加用户自定义项，并将displayFlag设为不可见
		//        if(item != null && (bizItems == null || bizItems.size() == 0)){
		//            this.launchPadProvider.deleteLaunchPadItem(item);
		//        }
		//        else{
		//            if(item != null){
		//                item.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
		//                item.setScopeId(userId);
		//                item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
		//                item.setScopeCode(ScopeType.USER.getCode());
		//                this.launchPadProvider.updateLaunchPadItem(item);
		//                return;
		//            }
		//            if(bizItems != null && bizItems.size() > 0){
		//                  item = bizItems.get(0);
		//            }
		//            item.setId(0L);
		//            item.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
		//            item.setScopeId(userId);
		//            item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
		//            item.setScopeCode(ScopeType.USER.getCode());
		//            this.launchPadProvider.createLaunchPadItem(item);
		//        }
	}

	@Override
	public void promoteBusiness(PromoteBusinessAdminCommand cmd) {
		if(cmd.getId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter id null,categoryId is null");

		Business business = this.businessProvider.findBusinessById(cmd.getId());
		if(business == null){
			LOGGER.error("Business is not exists.id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
					"Business is not exists.");
		}
		if(cmd.getItemScopes() == null || cmd.getItemScopes().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid item scopes paramter.");
		}
		//先删除用户自定义的
		this.launchPadProvider.deleteLaunchPadItemByScopeAndTargetId(ScopeType.USER.getCode(), null
				,ItemTargetType.BIZ.getCode(),cmd.getId());
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		cmd.getItemScopes().forEach((itemScope) ->{
			//重复推荐，先删除
			this.launchPadProvider.deleteLaunchPadItemByScopeAndTargetId(itemScope.getScopeCode(), itemScope.getScopeId()
					,ItemTargetType.BIZ.getCode(),cmd.getId());
			LaunchPadItem item = new LaunchPadItem();
			item.setAppId(0L);
			item.setItemName(StringUtils.upperCase(business.getName()));
			item.setItemLabel(business.getDisplayName());
			item.setActionType(ActionType.THIRDPART_URL.getCode());
			item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
			item.setItemGroup(LaunchPadConstants.GROUP_BIZS);
			item.setItemLocation(LaunchPadConstants.ITEM_LOCATION);
			item.setIconUri(business.getLogoUri());
			item.setItemWidth(1);
			item.setItemHeight(1);
			item.setNamespaceId(0);
			item.setScopeCode(itemScope.getScopeCode());
			item.setScopeId(itemScope.getScopeId());
			item.setDefaultOrder(0);
			item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
			item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
			item.setBgcolor(0);
			item.setTargetType(ItemTargetType.BIZ.getCode());
			item.setTargetId(cmd.getId());
			items.add(item);

		});

		this.launchPadProvider.createLaunchPadItems(items);
	}

	@Override
	public void deletePromoteBusiness(DeletePromoteBusinessAdminCommand cmd) {
		if(cmd.getIds() == null || !cmd.getIds().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter ids null.");
		}
		cmd.getIds().forEach(r ->{
			this.launchPadProvider.deleteLaunchPadItemByTargetTypeAndTargetId(ItemTargetType.BIZ.getCode(), r);
		});

	}

	/**
	 * 获取用户家庭地址
	 * @param userId
	 * @return
     */
	private List<FamilyAddressDTO> listUserFamilyAddresses(Long userId){
		List<FamilyAddressDTO> dtos = new ArrayList<>();
		List<UserGroup> userGroups = this.userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());
		if(userGroups != null && !userGroups.isEmpty()){
			for (UserGroup userGroup: userGroups) {
				Group group = groupProvider.findGroupById(userGroup.getGroupId());
				if(group == null){
					LOGGER.error("listUserFamilyAddresses-group=group is empty,groupId=" +userGroup.getGroupId());
					continue;
				}
				Address address = addressProvider.findAddressById(group.getIntegralTag1());
				if(address == null){
					LOGGER.error("listUserFamilyAddresses-address=address is empty,addressId=" +group.getIntegralTag1());
					continue;
				}
				FamilyAddressDTO dto = new FamilyAddressDTO();
				dto.setFamilyId(userGroup.getGroupId());
				dto.setAdddress(processAddressDTO(address));
				dtos.add(dto);
			}
		}
		return dtos;
	}

	/**
	 * 获取用户公司地址
	 * @param userId
	 * @return
     */
	private List<OrganizationAddressDTO> listUserOrganizationAddresses(Long userId){
		List<OrganizationAddressDTO> dtos = new ArrayList<>();
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(userId);
		for (OrganizationMember member: members) {
			Organization org = organizationProvider.findOrganizationById(member.getOrganizationId());
			if(null != org && OrganizationStatus.fromCode(org.getStatus()) == OrganizationStatus.ACTIVE && OrganizationGroupType.fromCode(member.getGroupType()) == OrganizationGroupType.ENTERPRISE){
				OrganizationAddressDTO dto = new OrganizationAddressDTO();
				dto.setOrganizastionId(org.getId());
				dto.setOrganizationName(org.getName());
				List<OrganizationAddress> orgAddresses = organizationProvider.findOrganizationAddressByOrganizationId(member.getOrganizationId());
				List<AddressDTO> adddresses = new ArrayList<>();
				if(null != orgAddresses && orgAddresses.size() > 0){
					for (OrganizationAddress orgAddress: orgAddresses) {
						Address address = addressProvider.findAddressById(orgAddress.getAddressId());
						if(address == null){
							LOGGER.error("listUserOrganizationAddresses-address=address is empty,addressId = {}", orgAddress.getAddressId());
							continue;
						}
						adddresses.add(processAddressDTO(address));
					}
				}
				dto.setAdddresses(adddresses);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	private AddressDTO processAddressDTO(Address address){
		AddressDTO dto = ConvertHelper.convert(address, AddressDTO.class);
		Region city = regionProvider.findRegionById(address.getCityId());
		if(null != city){
			dto.setCityName(city.getName());
		}

		Region area = regionProvider.findRegionById(address.getAreaId());
		if(null != area){
			dto.setAreaName(area.getName());
		}
		Community community = this.communityProvider.findCommunityById(address.getCommunityId());
		if(community != null){
			dto.setCommunityName(community.getName());
		}

		Region province = regionProvider.findRegionById(city.getParentId());
		if(null != province){
			dto.setProvinceName(province.getName());
		}
		return dto;
	}


	@Override
	public UserServiceAddressDTO getUserDefaultAddress(GetUserDefaultAddressCommand cmd) {
		//List<UserServiceAddressDTO> dtos = new ArrayList<UserServiceAddressDTO>();
		UserServiceAddressDTO dto = new UserServiceAddressDTO();
		Long userId = cmd.getUserId();
		LOGGER.error("getUserDefaultAddress-userId="+userId);
		List<UserServiceAddress> serviceAddresses = this.userActivityProvider.findUserRelateServiceAddresses(userId);
		LOGGER.error("getUserDefaultAddress-serviceAddresses="+StringHelper.toJsonString(serviceAddresses));
		if(serviceAddresses == null || serviceAddresses.isEmpty()){
			List<UserGroup> list = this.userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());
			if(list != null && !list.isEmpty()){
				for(int i=0;i<list.size();i++){
					UserGroup uGroup = list.get(i);
					Group group = groupProvider.findGroupById(uGroup.getGroupId());
					if(group == null){
						LOGGER.error("getUserDefaultAddress-group=group is empty,groupId=" +uGroup.getGroupId());
						continue;
					}
					Address addr = addressProvider.findAddressById(group.getIntegralTag1());
					if(addr == null){
						LOGGER.error("getUserDefaultAddress-address=address is empty,addressId=" +group.getIntegralTag1());
						continue;
					}
					Region city = this.regionProvider.findRegionById(addr.getCityId());
					if(city != null){
						Region province = this.regionProvider.findRegionById(city.getParentId());
						if(province != null)
							dto.setProvince(province.getName());
					}
					dto.setAddressType(AddressType.COMMUNITY_ADDRESS.getCode());
					dto.setId(addr.getId());
					dto.setCity(addr.getCityName());
					dto.setArea(addr.getAreaName());
					dto.setAddress(addr.getAddress());
					if(addr.getCommunityId() != null){
						Community com = this.communityProvider.findCommunityById(addr.getCommunityId());
						if(com != null){
							dto.setCommunityId(addr.getCommunityId());
							dto.setCommunityName(com.getName());
						}
					}
					User user = this.userProvider.findUserById(userId);
					LOGGER.error("getUserDefaultAddress-user=" +user.toString());
					if(user != null)
						dto.setUserName(user.getNickName());
					List<UserIdentifier> uIdentif = this.userProvider.listUserIdentifiersOfUser(userId);
					LOGGER.error("getUserDefaultAddress-uIdentif=" +StringHelper.toJsonString(uIdentif));
					if(uIdentif != null && !uIdentif.isEmpty()){
						dto.setCallPhone(uIdentif.get(0).getIdentifierToken());
					}
					break;
				}
			}
		}
		else{
			for(int i=0;i<serviceAddresses.size();i++){
				UserServiceAddress r = serviceAddresses.get(i);
				Address addr = this.addressProvider.findAddressById(r.getAddressId());
				if(addr == null){
					LOGGER.error("getUserDefaultAddress-error=Address is not found,addressId=" + r.getAddressId());
					continue;
				}
				Region city = this.regionProvider.findRegionById(addr.getCityId());
				if(city != null){
					Region province = this.regionProvider.findRegionById(city.getParentId());
					if(province != null)
						dto.setProvince(province.getName());
				}
				dto.setAddressType(AddressType.SERVICE_ADDRESS.getCode());
				dto.setId(addr.getId());
				dto.setCity(addr.getCityName());
				dto.setArea(addr.getAreaName());
				dto.setAddress(addr.getAddress());
				if(addr.getCommunityId() != null){
					Community com = this.communityProvider.findCommunityById(addr.getCommunityId());
					if(com != null){
						dto.setCommunityId(addr.getCommunityId());
						dto.setCommunityName(com.getName());
					}
				}
				dto.setUserName(r.getContactName());
				dto.setCallPhone(r.getContactToken());
				break;
			}
		}
		return dto;
	}

	@Override
	public UserAddressDTO getUserAddress(GetUserDefaultAddressCommand cmd) {
		if(null == cmd.getUserId()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter userId null.");
		}

		UserAddressDTO dto = new UserAddressDTO();
		Long userId = cmd.getUserId();
		List<UserServiceAddress> serviceAddresses = userActivityProvider.findUserRelateServiceAddresses(userId);
		List<UserServiceAddressDTO> userServiceAddressDTOs = new ArrayList<>();
		for (UserServiceAddress userServiceAddress: serviceAddresses) {
			UserServiceAddressDTO userServiceAddressDTO = ConvertHelper.convert(userServiceAddress, UserServiceAddressDTO.class);
			Address addr = this.addressProvider.findAddressById(userServiceAddress.getAddressId());
			if(addr == null){
				LOGGER.error("getUserAddress-error=Address is not found,addressId = {}", userServiceAddress.getAddressId());
				continue;
			}
			Region city = this.regionProvider.findRegionById(addr.getCityId());
			if(city != null){
				Region province = this.regionProvider.findRegionById(city.getParentId());
				if(province != null)
					userServiceAddressDTO.setProvince(province.getName());
			}
			userServiceAddressDTO.setAddressType(AddressType.SERVICE_ADDRESS.getCode());
			userServiceAddressDTO.setId(addr.getId());
			userServiceAddressDTO.setCity(addr.getCityName());
			userServiceAddressDTO.setArea(addr.getAreaName());
			userServiceAddressDTO.setAddress(addr.getAddress());
			if(addr.getCommunityId() != null){
				Community com = this.communityProvider.findCommunityById(addr.getCommunityId());
				if(com != null){
					userServiceAddressDTO.setCommunityId(addr.getCommunityId());
					userServiceAddressDTO.setCommunityName(com.getName());
				}
			}
			userServiceAddressDTO.setUserName(userServiceAddress.getContactName());
			userServiceAddressDTO.setCallPhone(userServiceAddress.getContactToken());
			userServiceAddressDTOs.add(userServiceAddressDTO);
		}
		dto.setServiceAddresses(userServiceAddressDTOs);
		List<FamilyAddressDTO> familyAddresses = listUserFamilyAddresses(userId);
		dto.setFamilyAddresses(familyAddresses);

		List<OrganizationAddressDTO> orgAddresses = listUserOrganizationAddresses(userId);
		dto.setOrganizationAddresses(orgAddresses);

		return dto;
	}

	@Override
	public List<OrganizationDTO> getUserOrganizations(GetUserDefaultAddressCommand cmd) {
		if(null == cmd.getUserId()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter userId null.");
		}
		List<OrganizationDTO> dtos = new ArrayList<>();
		List<UserOrganizations> userOrganizations = organizationProvider.listUserOrganizationByUserId(cmd.getUserId());
		for (UserOrganizations userOrganization: userOrganizations) {
			Organization organizaiton = organizationProvider.findOrganizationById(userOrganization.getOrganizationId());
			if(null != organizaiton && OrganizationStatus.fromCode(organizaiton.getStatus()) == OrganizationStatus.ACTIVE){
				dtos.add(ConvertHelper.convert(organizaiton, OrganizationDTO.class));
			}
		}
		return dtos;
	}

	@Override
	public CheckPaymentUserResponse checkPaymentUser(CheckPaymentUserCommand cmd) {
		CheckPaymentUserResponse res = businessProvider.checkoutPaymentUser(cmd.getUserId());
		if(res==null){
			res = new CheckPaymentUserResponse();
			// 使用新支付SDK，PayService被废弃，由于此段代码并不知道是否还在使用，故支付SDK对接时并没有对接，
	        // 若需要支持则需要另外安排时间对接 by lqs 20180613
//			PaymentUser paymentUser = payService.createPaymentUser(BusinessUserType.PERSONAL.getCode(), cmd.getOwnerType(), cmd.getUserId());
//			res.setCreateTime(paymentUser.getCreateTime());
//			res.setId(paymentUser.getId());
//			res.setOwnerId(paymentUser.getOwnerId());
//			res.setOwnerType(paymentUser.getOwnerType());
//			res.setPayment_user_id(paymentUser.getPaymentUserId());
//			res.setPaymentUserType(paymentUser.getPaymentUserType());
		}
		return res;
	}

	@Override
	public List<UserDtoForBiz> listUser(ListUserCommand cmd) {
		List<UserDtoForBiz> usersForBiz = new ArrayList<UserDtoForBiz>();
		/*if(StringUtils.isEmpty(cmd.getKeyword()))
			return usersForBiz;*/

		List<RoleAssignment> roleAssignments = this.aclProvider.getAllRoleAssignments();
		for(RoleAssignment r : roleAssignments){
			if(r.getTargetType().equals(EntityType.USER.getCode())){
				User user = this.userProvider.findUserById(r.getTargetId());
				if(user != null && user.getNickName() != null){
					if(StringUtils.isEmpty(cmd.getKeyword())){
						UserDtoForBiz userForBiz = new UserDtoForBiz();
						userForBiz.setId(user.getId());
						userForBiz.setName(user.getNickName());
						usersForBiz.add(userForBiz);
					}
					else if(user.getNickName().contains(cmd.getKeyword())){
						UserDtoForBiz userForBiz = new UserDtoForBiz();
						userForBiz.setId(user.getId());
						userForBiz.setName(user.getNickName());
						usersForBiz.add(userForBiz);
					}
				}
			}
		}

		if(usersForBiz != null && usersForBiz.size() > 0)
			for(UserDtoForBiz userBiz : usersForBiz){
				UserIdentifier iden = this.getUserIdentifierByUid(userBiz.getId());
				if(iden != null){
					SignupToken signUpToken = new SignupToken(userBiz.getId(),IdentifierType.fromCode(iden.getIdentifierType()),iden.getIdentifierToken());
					String token = WebTokenGenerator.getInstance().toWebToken(signUpToken);
					userBiz.setToken(token);
				}
				else{	
					LOGGER.info("listUser-UserIdentifier not find by UserId="+userBiz.getId());
				}
			}

		return usersForBiz;
	}

	private UserIdentifier getUserIdentifierByUid(Long userId) {
		List<UserIdentifier> idens = userProvider.listUserIdentifiersOfUser(userId);
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getUserIdentifierByUid-idens="+StringHelper.toJsonString(idens));

		if(idens != null && idens.size() > 0){
			return idens.get(0);
		}
		return null;
	}

	@Override
	public void updateBusinessDistance(UpdateBusinessDistanceCommand cmd) {
		this.checkBizIdIsNull(cmd.getId());
		this.checkBizDistanceIsNull(cmd.getVisibleDistance());
		Business biz = this.checkBusiness(cmd.getId(),true);
		biz.setVisibleDistance(cmd.getVisibleDistance());
		this.businessProvider.updateBusiness(biz);
	}

	private void checkBizDistanceIsNull(Double visibleDistance) {
		if(visibleDistance == null){
			LOGGER.error("Business visibleDistance is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Business visibleDistance is null or empty.");
		}
	}

	private Business checkBusiness(Long id,boolean isThrowExcept) {
		Business biz = this.businessProvider.findBusinessById(id);
		if(biz == null){
			LOGGER.error("Business is not exist.id="+id);
			if(isThrowExcept)
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Business is not exist.");
		}
		return biz;
	}

	private void checkBizIdIsNull(Long id) {
		if(id == null){
			LOGGER.error("Business id is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Business id is null or empty.");
		}
	}

	@Override
	public BusinessDTO findBusinessById(FindBusinessByIdCommand cmd) {
		this.checkBizIdIsNull(cmd.getId());

		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Integer namesapceId = user.getNamespaceId()==null?0:user.getNamespaceId();

		Business r = this.businessProvider.findBusinessById(cmd.getId());

		final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
		final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
		final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
		List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();

		BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
		List<CategoryDTO> categories = new ArrayList<>();
		dto.setLogoUrl(processLogoUrl(r, userId,imageUrl));
		dto.setUrl(processUrl(r,prefixUrl,businessDetailUrl));
		//店铺图标需要裁剪
		if(r.getTargetType().byteValue() == BusinessTargetType.ZUOLIN.getCode()){
			dto.setScaleType(ScaleType.TAILOR.getCode());
		}

		if(cmd.getCommunityId() != null){
			Community community = this.checkCommunity(cmd.getCommunityId(),true);
			List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
			if(points == null || points.isEmpty()){
				LOGGER.error("Community geo points is not exists,communityId=" + cmd.getCommunityId());
				throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST,"Community geo points is not exists.");
			}
			CommunityGeoPoint point = points.get(0);
			final double lat = point != null ? point.getLatitude() : 0;
			final double lon = point != null ? point.getLongitude() : 0;

			List<Long> recommendBizIds = this.businessProvider.listBusinessAssignedScopeByScope(community.getCityId(),cmd.getCommunityId(),null).stream()
					.map(r2->r2.getOwnerId()).collect(Collectors.toList());
			List<Long> favoriteBizIds = getFavoriteBizIds(userId, cmd.getCommunityId(), community.getCityId(),namesapceId);

			if(favoriteBizIds != null && favoriteBizIds.contains(r.getId()))
				dto.setFavoriteStatus(BusinessFavoriteStatus.FAVORITE.getCode());
			else
				dto.setFavoriteStatus(BusinessFavoriteStatus.NONE.getCode());

			if(recommendBizIds != null && recommendBizIds.contains(r.getId()))
				dto.setRecommendStatus(BusinessRecommendStatus.RECOMMEND.getCode());
			else
				dto.setRecommendStatus(BusinessRecommendStatus.NONE.getCode());

			if(lat != 0 || lon != 0)
				dto.setDistance((int)calculateDistance(r.getLatitude(),r.getLongitude(),lat, lon));
			else
				dto.setDistance(0);
		}
		return dto;
	}

	private Community checkCommunity(Long communityId, boolean isException) {
		Community com = this.communityProvider.findCommunityById(communityId);
		if(com == null){
			LOGGER.error("Community is not exists.id=" + communityId);
			if(isException)
				throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST,"Community is not exists.");
			else
				return null;
		}
		return com;
	}

	@Override
	public ListBusinessByKeywordCommandResponse listBusinessByKeyword(ListBusinessByKeywordCommand cmd) {
		ListBusinessByKeywordCommandResponse response = new ListBusinessByKeywordCommandResponse();
		List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();

		cmd.setPageOffset(cmd.getPageOffset() == null?1:cmd.getPageOffset());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset((long)cmd.getPageOffset(), (long)pageSize);
		List<Business> list = this.businessProvider.listBusinessesByKeyword(cmd.getKeyword(), (int)offset, pageSize+1);
		if(list != null && !list.isEmpty()){
			if(list.size()>pageSize){
				response.setNextPageOffset(cmd.getPageOffset()+1);
				list.remove(list.size()-1);
			}
			dtos = list.stream().map(r -> {
				BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
				return dto;
			}).collect(Collectors.toList());
		}
		response.setList(dtos);
		return response;
	}

	@Override
	public List<UserInfo> listUserByKeyword(ListUserByKeywordCommand cmd) {
		if(StringUtils.isEmpty(cmd.getKeyword())){
			LOGGER.error("Invalid paramter keyword,keyword is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter keyword,keyword is null");
		}
		List<UserInfo> users = userService.listUserByKeyword(cmd.getKeyword());
		return users;
	}

	@Override
	public List<UserInfo> listUserByIdentifier(ListUserByIdentifierCommand cmd) {
		if(StringUtils.isEmpty(cmd.getIdentifier())){
			LOGGER.error("Invalid paramter identifier,identifier is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter identifier,identifier is null");
		}
		return userService.listUserInfoByIdentifier(cmd.getIdentifier());
	}

	@Override
	public void openBusinessAssignedNamespace(BusinessAsignedNamespaceCommand cmd) {
		if(cmd.getNamespaceId()==null||StringUtils.isBlank(cmd.getTargetId())){
			LOGGER.error("namespaceId or targetId is null.namespaceId="+cmd.getNamespaceId()+",targetId="+cmd.getTargetId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"namespaceId or targetId is null.");
		}
		Business business = businessProvider.findBusinessByTargetId(cmd.getTargetId());
		if(business==null){
			LOGGER.error("business not found.targetId="+cmd.getTargetId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"business not found.");
		}
		createBusinessAssignedNamespace(business.getId(),cmd.getNamespaceId());
	}

	@Override
	public void closeBusinessAssignedNamespace(BusinessAsignedNamespaceCommand cmd) {
		if(cmd.getNamespaceId()==null||StringUtils.isBlank(cmd.getTargetId())){
			LOGGER.error("namespaceId or targetId is null.namespaceId="+cmd.getNamespaceId()+",targetId="+cmd.getTargetId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"namespaceId or targetId is null.");
		}
		Business business = businessProvider.findBusinessByTargetId(cmd.getTargetId());
		if(business==null){
			LOGGER.error("business not found.targetId="+cmd.getTargetId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"business not found.");
		}
		deleteBusinessAssignedNamespace(business.getId(),cmd.getNamespaceId());
	}

	private void deleteBusinessAssignedNamespace(Long businessId, Integer namespaceId) {
		BusinessAssignedNamespace bizNamespace = businessProvider.findBusinessAssignedNamespaceByNamespace(businessId,namespaceId,null);
		if(bizNamespace!=null){
			if(bizNamespace.getVisibleFlag().byteValue()!=BusinessAssignedNamespaceVisibleFlagType.HIDE.getCode()){
				bizNamespace.setVisibleFlag(BusinessAssignedNamespaceVisibleFlagType.HIDE.getCode());
				businessProvider.updateBusinessAssignedNamespace(bizNamespace);
			}
		}else{
			bizNamespace = new BusinessAssignedNamespace();
			bizNamespace.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			bizNamespace.setNamespaceId(namespaceId);
			bizNamespace.setOwnerId(businessId);
			bizNamespace.setVisibleFlag(BusinessAssignedNamespaceVisibleFlagType.HIDE.getCode());
			businessProvider.createBusinessAssignedNamespace(bizNamespace);
		}

	}

	@Override
	public void updateReceivedCouponCount(UpdateReceivedCouponCountCommand cmd) {
		if(cmd.getUserId()==null||cmd.getCount()==null){
			LOGGER.error("Invalid parameter,userId or count is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId or count is null");
		}
		userActivityService.updateUserProfile(cmd.getUserId(),UserProfileContstant.RECEIVED_COUPON_COUNT,String.valueOf(cmd.getCount()));
	}

	@Override
	public UserProfileDTO getReceivedCouponCount(GetReceivedCouponCountCommand cmd) {
		UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(cmd.getUserId(), UserProfileContstant.RECEIVED_COUPON_COUNT);
		if(profile!=null)
			return ConvertHelper.convert(profile, UserProfileDTO.class);
		return null;
	}
	
	@Override
	public List<BuildingDTO> listBuildingsByKeyword(ListBuildingByCommunityIdsCommand cmd) {
		if(cmd.getCommunityIds()==null||cmd.getCommunityIds().isEmpty()){
			LOGGER.error("Invalid parameter,commudityIds is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,commudityIds is null");
		}
		List<BuildingDTO> list = new ArrayList<BuildingDTO>();
		ListBuildingByKeywordCommand subcmd = new ListBuildingByKeywordCommand();
		subcmd.setKeyword(cmd.getKeyword());
		subcmd.setNamespaceId(cmd.getNamespaceId());
		for(Long r:cmd.getCommunityIds()){
			subcmd.setCommunityId(r);
			Tuple<Integer, List<BuildingDTO>> result = addressService.listBuildingsByKeywordForBusiness(subcmd);
			if(result.second()!=null&&!result.second().isEmpty()){
				List<BuildingDTO> tmpList = result.second().stream().map((r2) -> {
					r2.setCommunityId(r);
					return r2; 
				}).collect(Collectors.toList());
				list.addAll(tmpList);
			}
		}
		return list;
	}

	@Override
	public List<BuildingDTO> listBuildingsByKeywordAndNameSpace(ListBuildingsByKeywordAndNameSpaceCommand cmd) {
		List<Building> buildings = communityProvider.ListBuildingsBykeywordAndNameSpace(cmd.getNamespaceId(), cmd.getKeyword());
		return buildings.stream().map(r -> {
			BuildingDTO dto = new BuildingDTO();
			dto.setBuildingId(r.getId());
			dto.setBuildingName(r.getName());
			dto.setCommunityId(r.getCommunityId());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeyword(
			ListPropApartmentsByKeywordCommand cmd) {
		return addressService.listApartmentsByKeywordForBusiness(cmd);
	}

	@Override
	public List<CommunityDTO> getCommunitiesByNameAndCityId(
			GetCommunitiesByNameAndCityIdCommand cmd) {
		return communityService.getCommunitiesByNameAndCityId(cmd);
	}

	@Override
	public CommunityDTO getCommunityById(GetCommunityByIdCommand cmd) {
		return communityService.getCommunityById(cmd);
	}

	@Override
	public List<RegionDTO> listRegionByKeyword(ListRegionByKeywordCommand cmd) {
		Tuple<String, SortOrder> orderBy = null;
		if(cmd.getSortBy() == null)
			cmd.setSortBy("");
		if(cmd.getSortBy() != null)
			orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));

		Integer namespaceId = cmd.getNamespaceId()==null?Namespace.DEFAULT_NAMESPACE:cmd.getNamespaceId();

		List<Region> entityResultList = this.regionProvider.listRegionByKeyword(cmd.getParentId(), 
				RegionScope.fromCode(cmd.getScope()), 
				RegionAdminStatus.fromCode(cmd.getStatus()), orderBy, cmd.getKeyword(), namespaceId);

		List<RegionDTO> dtoResultList = entityResultList.stream() 
				.map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
				.collect(Collectors.toList());
		
		return dtoResultList;
	}

	@Override
	public void favoriteBusinessesByScene(FavoriteBusinessesBySceneCommand cmd, String baseScene) {

		if(cmd.getBizs() == null || cmd.getBizs().size() < 1)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid paramter bizs is null or empty");

		User user = UserContext.current().getUser();
		long userId = user.getId();
		for(FavoriteBusinessDTO r:cmd.getBizs()){
			FavoriteFlagType flag = FavoriteFlagType.fromCode(r.getFavoriteFlag());
			if(flag == null){
				LOGGER.error("FavoriteFlag is error.bizId=" + r.getId()+",favoriteFlag="+r.getFavoriteFlag());
				continue ;
			}
			if(r.getId() == null){
				LOGGER.error("biz id is null.bizId=" + r.getId()+",favoriteFlag="+r.getFavoriteFlag());
				continue ;
			}
			if(r.getFavoriteFlag() == FavoriteFlagType.FAVORITE.getCode())
				favoriteBusiness(userId,user.getNamespaceId(), r.getId(),false, baseScene);
			else if(r.getFavoriteFlag() == FavoriteFlagType.CANCEL_FAVORITE.getCode())
				cancelFavoriteBusiness(userId,user.getNamespaceId(), r.getId(),false, baseScene);
		}
	
	}

	@Override
	public Integer findUserCouponCount(UserCouponsCommand cmd) {
		if(cmd.getUserId()==null){
			LOGGER.error("Invalid parameter,userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId is null");
		}
		UserProfileDTO profile = userActivityService.findUserProfileBySpecialKey(cmd.getUserId(),UserProfileContstant.RECEIVED_COUPON_COUNT);
		if(profile==null)
			return 0;
		return NumberUtils.toInt(profile.getItemValue(), 0);
	}

	@Override
	public void updateUserCouponCount(UpdateUserCouponCountCommand cmd) {
		if(cmd.getUserId()==null||cmd.getCouponCount()==null){
			LOGGER.error("Invalid parameter,userId or coupon is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId or coupon is null");
		}
		userActivityService.updateUserProfile(cmd.getUserId(), UserProfileContstant.RECEIVED_COUPON_COUNT, cmd.getCouponCount()+"");
	}

	@Override
	public void addUserOrderCount(Long userId) {
		if(userId==null){
			LOGGER.error("Invalid parameter,userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId is null");
		}
		userActivityService.updateProfileIfNotExist(userId,UserProfileContstant.RECEIVED_ORDER_COUNT,1);
	}

	@Override
	public void reduceUserOrderCount(Long userId) {
		if(userId==null){
			LOGGER.error("Invalid parameter,userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId is null");
		}
		userActivityService.updateProfileIfNotExist(userId,UserProfileContstant.RECEIVED_ORDER_COUNT,-1);
	}

	@Override
	public Integer findUserOrderCount(UserCouponsCommand cmd) {
		if(cmd.getUserId()==null){
			LOGGER.error("Invalid parameter,userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId is null");
		}
		UserProfileDTO profile = userActivityService.findUserProfileBySpecialKey(cmd.getUserId(),UserProfileContstant.RECEIVED_ORDER_COUNT);
		if(profile==null)
			return 0;
		return NumberUtils.toInt(profile.getItemValue(), 0);
	}

	@Override
	public void updateUserOrderCount(UpdateUserOrderCountCommand cmd) {
		if(cmd.getUserId()==null||cmd.getOrderCount()==null){
			LOGGER.error("Invalid parameter,userId or orderCount is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid parameter,userId or orderCount is null");
		}
		userActivityService.updateUserProfile(cmd.getUserId(), UserProfileContstant.RECEIVED_ORDER_COUNT, cmd.getOrderCount()+"");
	}
	
	@Override
	public List<RegionDTO> listRegion(ListRegionCommand cmd) {
		if(null == cmd.getNamespaceId())
        	cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listRegionByParentId(
        		cmd.getNamespaceId(),
        		cmd.getParentId(),
        		RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()));
        
        return entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        
	}
	
	@Override
	public UserInfo validateUserPass(ValidateUserPassCommand cmd) {
		this.verifyVerifyUserPassCommand(cmd);
		ListUserByIdentifierCommand listUserCmd = new ListUserByIdentifierCommand();
		listUserCmd.setIdentifier(cmd.getUserIdentifier());
		List<UserInfo> users = listUserByIdentifier(listUserCmd);
		if(users != null && !users.isEmpty()) {
			for(UserInfo user : users) {
				if(user.getNamespaceId().compareTo(cmd.getNamespaceId()) == 0) {
					ValidatePassCommand passCmd = new ValidatePassCommand();
					passCmd.setUserId(user.getId());
					passCmd.setPassword(cmd.getPassword());
					Boolean validatePass = userService.validateUserPass(passCmd);
					if (validatePass) {
						return user;
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}
	
	private void verifyVerifyUserPassCommand(ValidateUserPassCommand cmd) {
		if(cmd.getNamespaceId() == null) {
			LOGGER.error("namespaceId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"namespaceId is null");
		}
		if(StringUtils.isEmpty(cmd.getUserIdentifier())) {
			LOGGER.error("userIdentitier is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"userIdentitier is null");
		}
		if(StringUtils.isEmpty(cmd.getPassword())) {
			LOGGER.error("password is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"password is null");
		}
	}

	@Override
	public CreateBusinessGroupResponse createBusinessGroup(CreateBusinessGroupCommand cmd) {
		if (cmd.getUserId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"userId cannot be null");
		}
		
		User user = userProvider.findUserById(cmd.getUserId());
		if (user == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist user");
		}
		
		UserContext userContext = UserContext.current();
		userContext.setUser(user);
		userContext.setNamespaceId(user.getNamespaceId());
		
		String groupName = cmd.getGroupName();
		if (StringUtils.isBlank(groupName)) {
			groupName = "group-"+UUID.randomUUID().toString();
		}
		
		return new CreateBusinessGroupResponse(groupService.createBusinessGroup(groupName));
	}

	@Override
	public void joinBusinessGroup(JoinBusinessGroupCommand cmd) {
		if (cmd.getUserId() == null || cmd.getGroupId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"userId or groupId cannot be null");
		}
		
		User user = userProvider.findUserById(cmd.getUserId());
		if (user == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist user");
		}
		
		UserContext userContext = UserContext.current();
		userContext.setUser(user);
		userContext.setNamespaceId(user.getNamespaceId());
		
		groupService.joinBusinessGroup(cmd.getGroupId());
	}

	@Override
	public Tuple<Integer, List<ApartmentFloorDTO>> listApartmentFloor(
			ListApartmentFloorCommand cmd) {
		return addressService.listApartmentFloorForBusiness(cmd);
	}

	private String source = "biz";// 电商运营数据获取来源 biz：从电商那里获取数据， db: 从数据库获取数据

    @Override
    public ListBusinessPromotionEntitiesReponse listBusinessPromotionEntities(ListBusinessPromotionEntitiesCommand cmd) {

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("list business promotion namespaceId={}", namespaceId);

        if ("biz".equals(source)) {// 从电商服务器获取数据
            return fetchBusinessPromotionEntitiesFromBiz(namespaceId, pageSize, cmd.getRecommendType());
        }
        // 从数据库获取数据
        else {
            ListBusinessPromotionEntitiesReponse reponse = new ListBusinessPromotionEntitiesReponse();

            List<BusinessPromotion> promotions = businessPromotionProvider.listBusinessPromotion(
                    namespaceId, pageSize, cmd.getPageAnchor());

            List<ModulePromotionEntityDTO> entities =
                    promotions.stream().map(this::toModulePromotionEntityDTO).collect(Collectors.toList());

            reponse.setEntities(entities);
            return reponse;
        }
    }

    private ListBusinessPromotionEntitiesReponse fetchBusinessPromotionEntitiesFromBiz(Integer namespaceId, Integer pageSize, Byte recommendType) {
        String bizApi = configurationProvider.getValue(ConfigConstants.BIZ_BUSINESS_PROMOTION_API, "zl-ec/rest/openapi/commodity/listRecommend");

        String bizServer = configurationProvider.getValue("stat.biz.server.url", "");

        if (StringUtils.isEmpty(bizApi)) {
            LOGGER.error("biz promotion api config are empty");
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BIZ_API_NOT_EXIST,
                    "biz promotion api config are empty");
        }

        Map<String, String> param = new HashMap<>();
        param.put("namespaceId", String.valueOf(namespaceId));
        param.put("pageSize", String.valueOf(pageSize));
        if (recommendType != null) {
            param.put("recommendType", String.valueOf(recommendType));
        }

        ListBusinessPromotionEntitiesReponse reponse = new ListBusinessPromotionEntitiesReponse();
        try {
            String jsonStr = HttpUtils.postJson((bizServer + bizApi), StringHelper.toJsonString(param), 10, "UTF-8");
            CommodityRespDTO resp = (CommodityRespDTO) StringHelper.fromJsonString(jsonStr, CommodityRespDTO.class);
            if (resp != null) {
                reponse.setEntities(resp.getResponse());
            }
            return reponse;
        } catch (Exception e) {
            LOGGER.error("biz server response error", e);
        }
        return reponse;
    }

    @Override
    public void createBusinessPromotion(CreateBusinessPromotionCommand cmd) {
        if (cmd.getId() != null) {
            BusinessPromotion promotion = businessPromotionProvider.findById(cmd.getId());
            if (cmd.getPrice() != null) {
                promotion.setPrice(cmd.getPrice());
            }
            if (cmd.getCommodityUrl() != null) {
                promotion.setCommodityUrl(cmd.getCommodityUrl());
            }
            if (cmd.getDefaultOrder() != null) {
                promotion.setDefaultOrder(cmd.getDefaultOrder());
            }
            if (cmd.getSubject() != null) {
                promotion.setSubject(cmd.getSubject());
            }
            if (cmd.getDescription() != null) {
                promotion.setDescription(cmd.getDescription());
            }
            if (cmd.getPosterUri() != null) {
                promotion.setPosterUri(cmd.getPosterUri());
            }
            businessPromotionProvider.updateBusinessPromotion(promotion);
        } else {
            BusinessPromotion promotion = ConvertHelper.convert(cmd, BusinessPromotion.class);
            promotion.setNamespaceId(UserContext.getCurrentNamespaceId());
            businessPromotionProvider.createBusinessPromotion(promotion);
        }
    }

    @Override
    public void testTransaction() {
        dbProvider.execute(s -> test());
    }

    private List<String> test() {
        for (int i = 0; i < 10; i++) {
            BusinessPromotion promotion = new BusinessPromotion();
            Integer namespaceId = UserContext.getCurrentNamespaceId();
            promotion.setNamespaceId(namespaceId);
            promotion.setSubject("comm:" + i);
            businessPromotionProvider.createBusinessPromotion(promotion);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("create business promotion {} {} {}", i, namespaceId, promotion);
            // try {
            //     Thread.sleep(5000);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
            if (i == 9) {
                throw RuntimeErrorException.errorWith("err", i, "error");
            }
        }
        return null;
    }

    @Override
    public void switchBusinessPromotionDataSource(SwitchBusinessPromotionDataSourceCommand cmd) {
        if (cmd.getSource() != null) {
            this.source = cmd.getSource();
        }
    }

    private ModulePromotionEntityDTO toModulePromotionEntityDTO(BusinessPromotion promotion) {
        ModulePromotionEntityDTO dto = new ModulePromotionEntityDTO();
        dto.setId(promotion.getId());
        dto.setSubject(promotion.getSubject());
        dto.setDescription(promotion.getDescription());
        dto.setPosterUrl(parserUri(promotion.getPosterUri(), EhBusinessPromotions.class.getSimpleName(), promotion.getId()));

        Map<String, String> metadata = new HashMap<>();
        metadata.put("url", promotion.getCommodityUrl());

        dto.setMetadata(StringHelper.toJsonString(metadata));

        ModulePromotionInfoDTO infoDTO = new ModulePromotionInfoDTO();
        infoDTO.setContent("¥" + promotion.getPrice().toString());
        infoDTO.setInfoType(ModulePromotionInfoType.TEXT.getCode());
        dto.setInfoList(Collections.singletonList(infoDTO));
        return dto;
    }

	@Override
	public SearchContentsBySceneReponse searchShops(SearchContentsBySceneCommand cmd) {
		//TODO 标准版要求没有场景，sceneTokenDTO固定为null，业务可能需要修改。有需要的话可以用 UserContext.current().getAppContext()的数据
		//SceneTokenDTO sceneTokenDto = WebTokenGenerator.getInstance().fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
		//Integer namespaceId = sceneTokenDto.getNamespaceId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		SearchTypes searchType =  userService.getSearchTypes(namespaceId, SearchContentType.SHOP.getCode());

		String bizApi = configurationProvider.getValue(ConfigConstants.BIZ_SEARCH_SHOPS_API, "");

        String bizServer = configurationProvider.getValue("stat.biz.server.url", "");

//        bizApi = "/zl-ec/rest/openapi/shop/listByKeyword";
//        bizServer = "https://biz-beta.zuolin.com";

        if (StringUtils.isEmpty(bizApi)) {
            LOGGER.error("biz promotion api config are empty");
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BIZ_API_NOT_EXIST,
                    "biz promotion api config are empty");
        }
    	
        Map<String, Object> param = new HashMap<>();
        param.put("namespaceId", namespaceId);
		param.put("buildingId", cmd.getBuildingId());
        param.put("keyword", cmd.getKeyword()==null?"":cmd.getKeyword());
//        param.put("shopNo", String.valueOf(searchShopsCommand.getShopNo()));
//        param.put("shopName", String.valueOf(searchShopsCommand.getShopName()));
        Integer pageNo = cmd.getPageAnchor() == null ? 1 : cmd.getPageAnchor().intValue();
        Integer pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
				AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        param.put("pageNo", pageNo);
        param.put("pageSize", pageSize);
        SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();
        try {
            String jsonStr = HttpUtils.postJson((bizServer + bizApi), StringHelper.toJsonString(param), 1000, "UTF-8");

            SearchShopsResponse searchShopsResponse = (SearchShopsResponse) StringHelper.fromJsonString(jsonStr, SearchShopsResponse.class);

            if(searchShopsResponse != null && searchShopsResponse.getResult() && searchShopsResponse.getBody() != null){
            	searchShopsResponse.getBody().getRows().forEach(r ->{
            		r.setSearchTypeId(searchType.getId());
        			r.setSearchTypeName(searchType.getName());
        			r.setContentType(searchType.getContentType());
            	});
            	response.setShopDTOs(searchShopsResponse.getBody().getRows());
            	
            	if(searchShopsResponse.getBody().getHasNext())
            	response.setNextPageAnchor(cmd.getPageAnchor()==null ? 2: cmd.getPageAnchor() + 1);
            }
//            if (resp != null) {
//                List<ModulePromotionEntityDTO> dtoList = new ArrayList<>();
//                for (Commodity commodity : resp.commodities) {
//                    ModulePromotionEntityDTO dto = new ModulePromotionEntityDTO();
//                    // dto.setId(commodity.id);
//                    dto.setSubject(commodity.commoName);
//                    dto.setPosterUrl(commodity.defaultPic);
//                    ModulePromotionInfoDTO infoDTO = new ModulePromotionInfoDTO(ModulePromotionInfoType.TEXT.getCode(), null, "¥" + commodity.price);
//                    dto.setInfoList(Collections.singletonList(infoDTO));
//
//                    dto.setMetadata(String.format("{\"url\":\"%s\"}", commodity.uri));
//
//                    dtoList.add(dto);
//                }
//                reponse.setEntities(dtoList);
//            }
            
            
            return response;
        } catch (Exception e) {
            // e.printStackTrace();
            LOGGER.error("biz server response error", e);
        }
        return response;
	}
    
}
