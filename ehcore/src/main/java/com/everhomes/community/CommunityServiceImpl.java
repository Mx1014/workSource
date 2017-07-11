// @formatter:off
package com.everhomes.community;

import com.everhomes.acl.*;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.configuration.Configurations;
import com.everhomes.configuration.ConfigurationsProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleAssignment;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.namespace.*;
import com.everhomes.organization.*;
import com.everhomes.point.UserLevel;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.community.*;
import com.everhomes.rest.community.admin.*;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;
import com.everhomes.rest.group.*;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.region.RegionServiceErrorCode;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.expansion.LeasePromotionFlag;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.UserWithoutConfAccountSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryProvider;
import com.everhomes.techpark.expansion.LeaseFormRequest;
import com.everhomes.user.*;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.*;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.version.VersionProvider;
import com.everhomes.version.VersionRealm;
import com.everhomes.version.VersionUpgradeRule;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommunityServiceImpl implements CommunityService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);
	private static final String COMMUNITY_NAME = "communityName";
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private RegionProvider regionProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private MessagingService messagingService;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private CommunitySearcher communitySearcher;
	@Autowired
	private ContentServerService contentServerService;

    @Autowired
    private UserGroupHistoryProvider userGroupHistoryProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private OrganizationService organizationService;
	

	@Autowired
	private GroupProvider groupProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;
	
	@Autowired
	private NamespacesProvider namespacesProvider;
	
	@Autowired
	private VersionProvider versionProvider;
	
	@Autowired
    private LocaleTemplateProvider localeTemplateProvider;
	
	@Autowired
	private ConfigurationsProvider configurationsProvider;
	
	@Autowired
	private CategoryProvider categoryProvider;
	
	@Autowired
	private ForumProvider forumProvider;
	
	@Autowired
	private AclProvider aclProvider;
	
	@Autowired
	private UserWithoutConfAccountSearcher userSearcher;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private  RolePrivilegeService rolePrivilegeService;
	@Autowired
	private GeneralFormService generalFormService;
	@Autowired
	private GeneralFormValProvider generalFormValProvider;
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;

	@Autowired
	private  ImportFileService importFileService;

	@Autowired
	private  UserActivityProvider userActivityProvider;

	@Override
	public ListCommunitesByStatusCommandResponse listCommunitiesByStatus(ListCommunitesByStatusCommand cmd) {

		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		if(cmd.getStatus() == null)
			cmd.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Community> communities = this.communityProvider.listCommunitiesByStatus(locator, pageSize + 1,
				(loc, query) -> {
					Condition c = Tables.EH_COMMUNITIES.STATUS.eq(cmd.getStatus());
					query.addConditions(c);
					return query;
				});

		Long nextPageAnchor = null;
		if(communities != null && communities.size() > pageSize) {
			communities.remove(communities.size() - 1);
			nextPageAnchor = communities.get(communities.size() -1).getId();
		}
		ListCommunitesByStatusCommandResponse response = new ListCommunitesByStatusCommandResponse();
		response.setNextPageAnchor(nextPageAnchor);

		List<CommunityDTO> communityDTOs = communities.stream().map((c) ->{
			List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(c.getId());
			List<CommunityGeoPointDTO> getPointDTOs = null;
			if(geoPoints != null && !geoPoints.isEmpty()){
				getPointDTOs = geoPoints.stream().map(r -> {
					return ConvertHelper.convert(r, CommunityGeoPointDTO.class);
				}).collect(Collectors.toList());
			}
			CommunityDTO dto = ConvertHelper.convert(c, CommunityDTO.class);
			dto.setGeoPointList(getPointDTOs);
			return dto;
		}).collect(Collectors.toList());

		response.setRequests(communityDTOs);
		return response;
	}


	@Override
	public void updateCommunity(UpdateCommunityAdminCommand cmd) {
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid communityId parameter");
		}
		
//		if(cmd.getGeoPointList() == null || cmd.getGeoPointList().size() <= 0){
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//					"Invalid geoPointList parameter");
//		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		if(cmd.getAddress() != null && !cmd.getAddress().equals("")){
			community.setAddress(cmd.getAddress());
		}

		Region city = regionProvider.findRegionById(cmd.getCityId());
		if(city == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid cityId parameter,city is not found.");
		}

		Region area = regionProvider.findRegionById(cmd.getAreaId());
		if(area == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid areaId parameter,area is not found.");
		}
		community.setAreaId(cmd.getAreaId());
		community.setCityId(cmd.getCityId());
		community.setOperatorUid(userId);
		community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		community.setAreaName(area.getName());
		community.setCityName(city.getName());
		community.setAreaSize(cmd.getAreaSize());
		this.dbProvider.execute((TransactionStatus status) ->  {
			this.communityProvider.updateCommunity(community);

			List<CommunityGeoPointDTO> geoList = cmd.getGeoPointList();
			
			if(geoList != null && geoList.size() > 0){

				List<CommunityGeoPoint> geoPointList = this.communityProvider.listCommunityGeoPoints(cmd.getCommunityId());

				for(int i=0;i<geoList.size();i++){
					CommunityGeoPointDTO geoDto= geoList.get(i);
					if(geoDto.getLatitude() != null && geoDto.getLongitude() != null){
						if(geoDto.getId() != null && geoPointList != null){
							for(int j=0;j<geoPointList.size();j++){
								CommunityGeoPoint geo = geoPointList.get(j);
								if(geo.getId().compareTo(geoDto.getId()) == 0 && geo.getCommunityId().compareTo(geoDto.getCommunityId()) == 0){
									geo.setLatitude(geoDto.getLatitude());
									geo.setLongitude(geoDto.getLongitude());
									geo.setGeohash(GeoHashUtils.encode(geoDto.getLatitude(), geoDto.getLongitude()));
									this.communityProvider.updateCommunityGeoPoint(geo);
									geoPointList.remove(j);
									break;
								}
							}
						}
						else{
							CommunityGeoPoint point = new CommunityGeoPoint();
							point.setCommunityId(cmd.getCommunityId());
							point.setDescription(geoDto.getDescription());
							point.setLatitude(geoDto.getLatitude());
							point.setLongitude(geoDto.getLongitude());
							String geohash = GeoHashUtils.encode(geoDto.getLatitude(), geoDto.getLongitude());
							point.setGeohash(geohash);
							this.communityProvider.createCommunityGeoPoint(point);
						}
					}
				}
				if(geoPointList != null && geoPointList.size() > 0){
					geoPointList.forEach(r -> {
						this.communityProvider.deleteCommunityGeoPoint(r);
					});
				}
			}

			return null;
		});
	}
	@Override
	public void approveCommuniy(ApproveCommunityAdminCommand cmd){
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid communityId parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();
		
		if(community.getCityId() == null || community.getCityId().longValue() == 0){
		    LOGGER.error("Community missing infomation,cityId is null.communityId=" + cmd.getCommunityId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_CITY_NOT_EXIST, 
                    "Community missing infomation,cityId is null.");
		}
		List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
		if(geoPoints == null || geoPoints.isEmpty()){
		    LOGGER.error("Community missing infomation,community geopoint is null.communityId=" + cmd.getCommunityId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_GEOPOIN_NOT_EXIST, 
                    "Community missing infomation,community geopoint is null.");
		}
		
		community.setOperatorUid(userId);
		community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		this.communityProvider.updateCommunity(community);
		//create community index
		this.communitySearcher.feedDoc(community);

		sendNotificationForApproveCommunityByAdmin(userId,community.getCreatorUid(),community.getName());
	}

	private void sendNotificationForApproveCommunityByAdmin(long operatorId, long memberId, String communityName) {
		// send notification to the applicant
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(COMMUNITY_NAME, communityName);

			User user = userProvider.findUserById(memberId);
			String locale = user.getLocale();
			if(locale == null) locale = "zh_CN";

			// send notification member who applicant
			String scope = CommunityNotificationTemplateCode.SCOPE;
			int code = CommunityNotificationTemplateCode.COMMUNITY_ADD_APPROVE_FOR_APPLICANT;
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

			sendCommunityNotification(memberId, notifyTextForApplicant, null, null);

		} catch(Exception e) {
			LOGGER.error("Failed to send notification, operatorId=" + operatorId + ", memberId=" + memberId, e);
		}
	}
	private void sendCommunityNotification(long userId, String message,MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
		if(message != null && message.length() != 0) {
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(new MessageChannel("user", String.valueOf(userId)));
			messageDto.setMetaAppId(AppConstants.APPID_ADDRESS);
			messageDto.setBody(message);
			Map<String, String> metaMap = new HashMap<String, String>();
			messageDto.setMeta(metaMap);
			messageDto.getMeta().put("body-type", MessageBodyType.TEXT.getCode());
			if(metaObjectType != null && metaObject != null) {
				messageDto.getMeta().put("meta-object-type", metaObjectType.getCode());
				messageDto.getMeta().put("meta-object", StringHelper.toJsonString(metaObject));
			}
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, "user", 
					String.valueOf(userId), messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
		}
	}

	@Override
	public void rejectCommunity(RejectCommunityAdminCommand cmd){
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid communityId parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		community.setOperatorUid(userId);
		community.setStatus(CommunityAdminStatus.INACTIVE.getCode());
		community.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		communityProvider.updateCommunity(community);
		sendNotificationForRejectCommunityByAdmin(userId,community.getCreatorUid(),community.getName());
	}

	private void sendNotificationForRejectCommunityByAdmin(long operatorId, long memberId, String communityName) {
		// send notification to the applicant
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(COMMUNITY_NAME, communityName);

			User user = userProvider.findUserById(memberId);
			String locale = user.getLocale();
			if(locale == null) locale = "zh_CN";

			// send notification member who applicant
			String scope = CommunityNotificationTemplateCode.SCOPE;
			int code = CommunityNotificationTemplateCode.COMMUNITY_ADD_REJECT_FOR_APPLICANT;
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

			sendCommunityNotification(memberId, notifyTextForApplicant, null, null);

		} catch(Exception e) {
			LOGGER.error("Failed to send notification, operatorId=" + operatorId + ", memberId=" + memberId, e);
		}
	}


	@Override
	public List<CommunityDTO> getCommunitiesByNameAndCityId(GetCommunitiesByNameAndCityIdCommand cmd){
		if(cmd.getCityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid cityId parameter");
		}
		Region region = this.regionProvider.findRegionById(cmd.getCityId());
		if(region == null){
			throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST, 
					"City is not found");
		}
		
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<Community> communities = this.communityProvider.findCommunitiesByNameAndCityId(cmd.getName(),cmd.getCityId(), namespaceId);
		List<CommunityDTO> result = communities.stream().map((r) ->{
			return ConvertHelper.convert(r, CommunityDTO.class);
		}).collect(Collectors.toList());
		return result;

	}

	@Override
	public List<CommunityDTO> getCommunitiesByIds(GetCommunitiesByIdsCommand cmd){
		if(cmd.getIds() == null || cmd.getIds().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid ids parameter");
		}

		List<Community> communities = this.communityProvider.findCommunitiesByIds(cmd.getIds());
		List<CommunityDTO> result = communities.stream().map((r) ->{
			return ConvertHelper.convert(r, CommunityDTO.class);
		}).collect(Collectors.toList());
		return result;

	}

	@Override
	public void updateCommunityRequestStatus(UpdateCommunityRequestStatusCommand cmd){
		if(cmd.getId() == null || cmd.getRequestStatus() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid id or requestStatus parameter");
		}
		Community community = this.communityProvider.findCommunityById(cmd.getId());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found.");
		}

		community.setRequestStatus(cmd.getRequestStatus());
		this.communityProvider.updateCommunity(community);

	}

	@Override
	public List<CommunityDTO> getNearbyCommunityById(GetNearbyCommunitiesByIdCommand cmd){
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid id parameter");
		}
		Community community = this.communityProvider.findCommunityById(cmd.getId());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found."); 
		}
		List<CommunityDTO> result = this.communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), cmd.getId()).stream().map((r) ->{
			return ConvertHelper.convert(r, CommunityDTO.class);
		}).collect(Collectors.toList());
		return result;
	}


	@Override
	public CommunityDTO getCommunityById(GetCommunityByIdCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid id parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getId());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found.");
		}
		CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
		List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(cmd.getId());
		List<CommunityGeoPointDTO> getPointDTOs = null;
		if(geoPoints != null && !geoPoints.isEmpty()){
			getPointDTOs = geoPoints.stream().map(r -> {
				return ConvertHelper.convert(r, CommunityGeoPointDTO.class);
			}).collect(Collectors.toList());
			communityDTO.setGeoPointList(getPointDTOs);
		}

		return communityDTO;
	}


	@Override
	public CommunityDTO getCommunityByUuid(GetCommunityByUuidCommand cmd) {
		if(cmd.getUuid() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid id parameter");
		}

		Community community = this.communityProvider.findCommunityByUuid(cmd.getUuid());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
					"Community is not found.");
		}
		CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
		List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(communityDTO.getId());
		List<CommunityGeoPointDTO> getPointDTOs = null;
		if(geoPoints != null && !geoPoints.isEmpty()){
			getPointDTOs = geoPoints.stream().map(r -> {
				return ConvertHelper.convert(r, CommunityGeoPointDTO.class);
			}).collect(Collectors.toList());
			communityDTO.setGeoPointList(getPointDTOs);
		}

		return communityDTO;
	}


	@Override
	public ListCommunitiesByKeywordCommandResponse listCommunitiesByKeyword(
			ListComunitiesByKeywordAdminCommand cmd) {
		
		if(cmd.getPageAnchor()==null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Community> list = this.communityProvider.listCommunitiesByKeyWord(locator, pageSize+1,cmd.getKeyword());

		ListCommunitiesByKeywordCommandResponse response = new ListCommunitiesByKeywordCommandResponse();
		if(list != null && list.size() > pageSize){
			list.remove(list.size()-1);
			response.setNextPageAnchor(list.get(list.size()-1).getId());
		}
		if(list != null){
			List<CommunityDTO> resultList = list.stream().map((c) -> {
				return ConvertHelper.convert(c, CommunityDTO.class);
			}).collect(Collectors.toList());

			response.setRequests(resultList);
		}

		return response;
	}


	@Override
	public ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd) {
		
		Long communityId = cmd.getCommunityId();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
		List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, pageSize + 1,communityId, UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
		
		this.communityProvider.populateBuildingAttachments(buildings);
        
        Long nextPageAnchor = null;
        if(buildings.size() > pageSize) {
        	buildings.remove(buildings.size() - 1);
            nextPageAnchor = buildings.get(buildings.size() - 1).getId();
        }
        
        populateBuildings(buildings);
        
        List<BuildingDTO> dtoList = buildings.stream().map((r) -> {
        	
        	BuildingDTO dto = ConvertHelper.convert(r, BuildingDTO.class);  
        	
        	dto.setBuildingName(dto.getName());
        	
        	dto.setName(org.springframework.util.StringUtils.isEmpty(dto.getAliasName()) ? dto.getName() : dto.getAliasName());
        	
        	if(null != dto.getManagerUid()){
        		OrganizationMember member = organizationProvider.findOrganizationMemberById(dto.getManagerUid());
        		if(null != member){
        			dto.setManagerNickName(member.getContactName());
        			//modify by wh 2016-11-8 加了管理员电话新字段
        			//dto.setContact(member.getContactToken());
        			dto.setManagerContact(member.getContactToken());
        		}
        	}

			//TODO: set detail url
			processDetailUrl(dto);
        	populateBuildingDTO(dto);
        	
        	return dto;
        }).collect(Collectors.toList());
        
        
        return new ListBuildingCommandResponse(nextPageAnchor, dtoList);
	}

	private void processDetailUrl(BuildingDTO dto) {
		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configurationProvider.getValue(ConfigConstants.APPLY_ENTRY_BUILDING_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getId());

//            detailUrl = String.format(detailUrl, dto.getId(), URLEncoder.encode(name, "UTF-8"), RandomUtils.nextInt(2));
		dto.setDetailUrl(homeUrl + detailUrl);

	}

    private void populateBuildingDTO(BuildingDTO building) {
        if(building == null) {
            return;
        }
        
        String posterUrl = building.getPosterUri();
        if(posterUrl != null && posterUrl.length() > 0) {
            try{
                String url = contentServerService.parserUri(posterUrl, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                building.setPosterUrl(url);
            }catch(Exception e){
                LOGGER.error("Failed to parse poster uri of building, building=" + building, e);
            }
        }

		GetGeneralFormValuesCommand cmd = new GetGeneralFormValuesCommand();
		cmd.setSourceType(EntityType.BUILDING.getCode());
		cmd.setSourceId(building.getId());
		cmd.setOriginFieldFlag(NormalFlag.NEED.getCode());

		List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmd);
		building.setFormValues(formValues);

		if (LeasePromotionFlag.ENABLED.getCode() == building.getCustomFormFlag()) {
			LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(building.getNamespaceId(),
					building.getCommunityId(), EntityType.COMMUNITY.getCode(), EntityType.BUILDING.getCode());
			if (null != request) {
				building.setRequestFormId(request.getSourceId());
			}
		}
    }
	@Override
	public BuildingDTO getBuilding(GetBuildingCommand cmd) {
		
		Building building = communityProvider.findBuildingById(cmd.getBuildingId());
		if(building != null) {
            if(BuildingStatus.ACTIVE != BuildingStatus.fromCode(building.getStatus())) {
            	
        		LOGGER.error("Building already deleted");
        		throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE, 
        				BuildingServiceErrorCode.ERROR_BUILDING_DELETED, "Building already deleted");
            }
			this.communityProvider.populateBuildingAttachments(building);
	        populateBuilding(building);

			BuildingDTO dto = ConvertHelper.convert(building, BuildingDTO.class);
			populateFormInfo(dto);

			return dto;
		}else {
            LOGGER.error("Building not found");
            throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE, 
            		BuildingServiceErrorCode.ERROR_BUILDING_NOT_FOUND, "Building not found");
        }
	}
	
	private void populateBuildings(List<Building> buildingList) {
        if(buildingList == null || buildingList.size() == 0) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The building list is empty");
            }
            return;
        } else {
            for(Building building : buildingList) {
            	populateBuilding(building);
            }
        }
    }

	/**
	 * 填充楼栋信息
	 */
	 private void populateBuilding(Building building) {
		 if(building == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The building is null");
            }
		 } else {
		     String posterUri = building.getPosterUri();
             if(posterUri != null && posterUri.length() > 0) {
                 try{
                     String url = contentServerService.parserUri(posterUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                     building.setPosterUrl(url);
                 }catch(Exception e){
                     LOGGER.error("Failed to parse building poster uri, buildingId=" + building.getId() + ", posterUri=" + posterUri, e);
                 }
             } 
		     
			 populateBuildingCreatorInfo(building);
			 populateBuildingManagerInfo(building);
			 populateBuildingOperatorInfo(building);
             
			 populateBuildingAttachements(building, building.getAttachments());
	        
		 }
	 }
	 
	 
	 private void populateBuildingCreatorInfo(Building building) {
		 
		 String creatorNickName = building.getCreatorNickName();
         String creatorAvatar = building.getCreatorAvatar();
         
         User creator = userProvider.findUserById(building.getCreatorUid());
         if(creator != null) {
             if(creatorNickName == null || creatorNickName.trim().length() == 0) {
            	 building.setCreatorNickName(creator.getNickName());
             }
             if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
            	 building.setCreatorAvatar(creator.getAvatar());
             }
         }
         creatorAvatar = building.getCreatorAvatar();
         if(creatorAvatar != null && creatorAvatar.length() > 0) {
             String avatarUrl = contentServerService.parserUri(creatorAvatar, EntityType.USER.getCode(), UserContext.current().getUser().getId());
             building.setCreatorAvatarUrl(avatarUrl);
         }
		 
	 }
	 
	 private void populateBuildingManagerInfo(Building building) {
		 
		 String managerNickName = building.getManagerNickName();
         String managerAvatar = building.getManagerAvatar();
         if(null == building.getManagerUid()){
        	 return;
         }
         User manager = userProvider.findUserById(building.getManagerUid());
         if(manager != null) {
             if(managerNickName == null || managerNickName.trim().length() == 0) {
            	 building.setManagerNickName(manager.getNickName());
             }
             if(managerAvatar == null || managerAvatar.trim().length() == 0) {
            	 building.setManagerAvatar(manager.getAvatar());
             }
         }
         managerAvatar = building.getManagerAvatar();
         if(managerAvatar != null && managerAvatar.length() > 0) {
             String avatarUrl = contentServerService.parserUri(managerAvatar, EntityType.USER.getCode(), UserContext.current().getUser().getId());
             building.setManagerAvatarUrl(avatarUrl);
         }
	 }

	 private void populateBuildingOperatorInfo(Building building) {
	 
		 String operatorNickName = building.getOperateNickName();
         String operatorAvatar = building.getOperateAvatar();
         if(building.getOperatorUid() != null) {
        	 
	         User operator = userProvider.findUserById(building.getOperatorUid());
	         if(operator != null) {
	             if(operatorNickName == null || operatorNickName.trim().length() == 0) {
	            	 building.setOperateNickName(operator.getNickName());
	             }
	             if(operatorAvatar == null || operatorAvatar.trim().length() == 0) {
	            	 building.setOperateAvatar(operator.getAvatar());
	             }
	         }
	         operatorAvatar = building.getOperateAvatar();
	         if(operatorAvatar != null && operatorAvatar.length() > 0) {
	             String avatarUrl = contentServerService.parserUri(operatorAvatar, EntityType.USER.getCode(), UserContext.current().getUser().getId());
	             building.setOperateAvatarUrl(avatarUrl);
	         }
         }
	 }
	 
	 private void populateBuildingAttachements(Building building, List<BuildingAttachment> attachmentList) {
	 
		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The building attachment list is empty, buildingId=" + building.getId());
	            }
		 } else {
	            for(BuildingAttachment attachment : attachmentList) {
	                populateBuildingAttachement(building, attachment);
	            }
		 }
	 }
	 
	 private void populateBuildingAttachement(Building building, BuildingAttachment attachment) {
        
		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The building attachment is null, buildingId=" + building.getId());
			 }
		 } else {
			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					 attachment.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, buildingId=" + building.getId() + ", attachmentId=" + attachment.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
					 LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				 }
			 }
		 }
	 }


	@Override
	public BuildingDTO updateBuilding(UpdateBuildingAdminCommand cmd) {
		
		Building building = ConvertHelper.convert(cmd, Building.class);
		building.setAddress(cmd.getAddress());
		building.setAliasName(cmd.getAliasName());
		building.setAreaSize(cmd.getAreaSize());
		building.setCommunityId(cmd.getCommunityId());
		building.setContact(cmd.getContact());
		building.setDescription(cmd.getDescription());
		building.setManagerUid(cmd.getManagerUid());
		building.setName(cmd.getName());
		building.setPosterUri(cmd.getPosterUri());
		building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		building.setNamespaceId(null == cmd.getNamespaceId() ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId());
		if(StringUtils.isNotBlank(cmd.getGeoString())){
			String[] geoString = cmd.getGeoString().split(",");
			double longitude = Double.valueOf(geoString[0]);
			double latitude = Double.valueOf(geoString[1]);
			building.setLatitude(latitude);
			building.setLongitude(longitude);
			String geohash = GeoHashUtils.encode(latitude, longitude);
			building.setGeohash(geohash);
		}
		
		User user = UserContext.current().getUser();
		long userId = user.getId();

		dbProvider.execute((TransactionStatus status) -> {
			if (cmd.getId() == null) {

				LOGGER.info("add building");
				this.communityProvider.createBuilding(userId, building);
				addGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(), EntityType.BUILDING.getCode(),
						building.getId(), cmd.getCustomFormFlag());
			} else {
				LOGGER.info("update building");
				building.setId(cmd.getId());
				Building b = this.communityProvider.findBuildingById(cmd.getId());
				building.setCreatorUid(b.getCreatorUid());
				building.setCreateTime(b.getCreateTime());
				building.setNamespaceId(b.getNamespaceId());
				this.communityProvider.updateBuilding(building);

				generalFormValProvider.deleteGeneralFormVals(EntityType.BUILDING.getCode(), building.getId());
				addGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(), EntityType.BUILDING.getCode(),
						building.getId(), cmd.getCustomFormFlag());
			}
			return null;
		});
		processBuildingAttachments(userId, cmd.getAttachments(), building);
		
		populateBuilding(building);

		BuildingDTO dto = ConvertHelper.convert(building, BuildingDTO.class);

		populateFormInfo(dto);
		return dto;
		
	}

	private void populateFormInfo(BuildingDTO dto) {
		GetGeneralFormValuesCommand cmdValues = new GetGeneralFormValuesCommand();
		cmdValues.setSourceType(EntityType.BUILDING.getCode());
		cmdValues.setSourceId(dto.getId());
		cmdValues.setOriginFieldFlag(NormalFlag.NEED.getCode());
		List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmdValues);
		dto.setFormValues(formValues);

		if (LeasePromotionFlag.ENABLED.getCode() == dto.getCustomFormFlag()) {
			LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(dto.getNamespaceId(),
					dto.getCommunityId(), EntityType.COMMUNITY.getCode(), EntityType.BUILDING.getCode());
			if (null != request) {
				dto.setRequestFormId(request.getSourceId());
			}
		}
	}

	private void addGeneralFormInfo(Long generalFormId, List<PostApprovalFormItem> formValues, String sourceType,
		Long sourceId, Byte customFormFlag) {
		if (LeasePromotionFlag.ENABLED.getCode() == customFormFlag) {
			addGeneralFormValuesCommand cmd = new addGeneralFormValuesCommand();
			cmd.setGeneralFormId(generalFormId);
			cmd.setValues(formValues);
			cmd.setSourceId(sourceId);
			cmd.setSourceType(sourceType);
			generalFormService.addGeneralFormValues(cmd);
		}
	}
	@Override
	public void deleteBuilding(DeleteBuildingAdminCommand cmd) {
		
		Building building = this.communityProvider.findBuildingById(cmd.getBuildingId());
		
		this.communityProvider.deleteBuilding(building);
	}


	@Override
	public Boolean verifyBuildingName(VerifyBuildingNameAdminCommand cmd) {
		Boolean isValid = this.communityProvider.verifyBuildingName(cmd.getCommunityId(), cmd.getBuildingName());
		return isValid;
	}
	
    private void processBuildingAttachments(long userId, List<AttachmentDescriptor> attachmentList, Building building) {
        List<BuildingAttachment> results = null;
        
        this.communityProvider.deleteBuildingAttachmentsByBuildingId(building.getId());
        
        if(attachmentList != null) {
            results = new ArrayList<BuildingAttachment>();
            
            BuildingAttachment attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
                attachment = new BuildingAttachment();
                attachment.setCreatorUid(userId);
                attachment.setBuildingId(building.getId());
                attachment.setContentType(descriptor.getContentType());
                attachment.setContentUri(descriptor.getContentUri());
                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                
                // Make sure we can save as many attachments as possible even if any of them failed
                try {
                	this.communityProvider.createBuildingAttachment(attachment);
                    results.add(attachment);
                } catch(Exception e) {
                    LOGGER.error("Failed to save the attachment, userId=" + userId 
                        + ", attachment=" + attachment, e);
                }
            }
            
            building.setAttachments(results);
        }
    }


	@Override
	public List<CommunityManagerDTO> getCommunityManagers(
			ListCommunityManagersAdminCommand cmd) {
		
		List<OrganizationMember> member = organizationProvider.listOrganizationMembersByOrgId(cmd.getOrganizationId());
		
		List<CommunityManagerDTO> managers = member.stream().map(r -> {
			CommunityManagerDTO dto = new CommunityManagerDTO();
			dto.setManagerId(r.getTargetId());
			dto.setManagerName(r.getContactName());
			dto.setManagerPhone(r.getContactToken());
			return dto;
		}).collect(Collectors.toList());
		
		return managers;
	}


	@Override
	public List<UserCommunityDTO> getUserCommunities(
			ListUserCommunitiesCommand cmd) {
		
		List<OrganizationCommunity> communities = new ArrayList<OrganizationCommunity>();
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(cmd.getUserId());
		
		for(OrganizationMember member : members) {
			List<OrganizationCommunity> oc = organizationProvider.listOrganizationCommunities(member.getOrganizationId());
			communities.addAll(oc);
		}
		
		List<UserCommunityDTO> usercommunities = communities.stream().map(r -> {
			UserCommunityDTO dto = new UserCommunityDTO();
			dto.setCommunityId(r.getCommunityId());
			dto.setOrganizationId(r.getOrganizationId());
			
			Community community = communityProvider.findCommunityById(r.getCommunityId());
			dto.setCommunityName(community.getName());
			return dto;
		}).collect(Collectors.toList());
		
		return usercommunities;
	}


	@Override
	public void approveBuilding(VerifyBuildingAdminCommand cmd) {
		if(cmd.getBuildingId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid buildingId parameter");
		}

		Building building = this.communityProvider.findBuildingById(cmd.getBuildingId());
		if(building == null){
			LOGGER.error("Building is not found.buildingId=" + cmd.getBuildingId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_NOT_EXIST, 
					"Building is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();
		
		if(building.getCommunityId() == null || building.getCommunityId().longValue() == 0){
		    LOGGER.error("Building missing infomation,communityId is null.buildingId=" + cmd.getBuildingId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_COMMUNITY_NOT_EXIST, 
                    "Building missing infomation,communityId is null.");
		}
		
		
		building.setOperatorUid(userId);
		building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		this.communityProvider.updateBuilding(building);
	}


	@Override
	public void rejectBuilding(VerifyBuildingAdminCommand cmd) {
		
		if(cmd.getBuildingId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid buildingId parameter");
		}

		Building building = this.communityProvider.findBuildingById(cmd.getBuildingId());
		if(building == null){
			LOGGER.error("Building is not found.buildingId=" + cmd.getBuildingId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_NOT_EXIST, 
					"Building is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		building.setOperatorUid(userId);
		building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		building.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		this.communityProvider.updateBuilding(building);
	}


	@Override
	public ListBuildingsByStatusCommandResponse listBuildingsByStatus(
			listBuildingsByStatusCommand cmd) {

		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		if(cmd.getStatus() == null)
			cmd.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Building> buildings = this.communityProvider.listBuildingsByStatus(locator, pageSize + 1,
				(loc, query) -> {
					Condition c = Tables.EH_BUILDINGS.STATUS.eq(cmd.getStatus());
					query.addConditions(c);
					return query;
				});

		Long nextPageAnchor = null;
		if(buildings != null && buildings.size() > pageSize) {
			buildings.remove(buildings.size() - 1);
			nextPageAnchor = buildings.get(buildings.size() -1).getId();
		}
		ListBuildingsByStatusCommandResponse response = new ListBuildingsByStatusCommandResponse();
		response.setNextPageAnchor(nextPageAnchor);

		List<BuildingDTO> buildingDTOs = buildings.stream().map((c) ->{
			BuildingDTO dto = ConvertHelper.convert(c, BuildingDTO.class);
			return dto;
		}).collect(Collectors.toList());

		response.setBuildings(buildingDTOs);
		return response;
	}


	@Override
	public ImportFileTaskDTO importBuildingData(Long communityId, MultipartFile file) {
		Long userId = UserContext.current().getUser().getId();
		ImportFileTask task = new ImportFileTask();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
						"File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(communityId);
			task.setType(ImportFileTaskType.BUILDING.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
					ImportFileResponse response = new ImportFileResponse();
					List<ImportBuildingDataDTO> datas = handleImportBuildingData(resultList);
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportBuildingDataDTO>> results = importBuildingData(datas, userId, communityId);
					response.setTotalCount((long)datas.size());
					response.setFailCount((long)results.size());
					response.setLogs(results);
					return response;
			}, task);

		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return ConvertHelper.convert(task, ImportFileTaskDTO.class);
	}

	private List<ImportFileResultLog<ImportBuildingDataDTO>> importBuildingData(List<ImportBuildingDataDTO> datas,
			Long userId, Long communityId) {
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		List<OrganizationMember> orgMem = this.organizationProvider.listOrganizationMembersByOrgId(org.getId());
		Map<String, OrganizationMember> ct = new HashMap<String, OrganizationMember>();
		if(orgMem != null) {
			orgMem.stream().map(r -> {
				ct.put(r.getContactToken(), r);
				return null;
			});
		}
		List<ImportFileResultLog<ImportBuildingDataDTO>> list = new ArrayList<>();
		for (ImportBuildingDataDTO data : datas) {
			ImportFileResultLog<ImportBuildingDataDTO> log = checkData(data);
			if (log != null) {
				list.add(log);
				continue;
			}
			
			
			Building building = communityProvider.findBuildingByCommunityIdAndName(communityId, data.getName());
			if (building == null) {
				building = new Building();
				building.setName(data.getName());
				building.setAliasName(data.getAliasName());
				building.setAddress(data.getAddress());
				building.setContact(data.getPhone());
				if (StringUtils.isNotBlank(data.getAreaSize())) {
					building.setAreaSize(Double.valueOf(data.getAreaSize()));
				}
				String contactToken = data.getPhone();
				if(ct.get(contactToken) != null) {
					OrganizationMember om = ct.get(contactToken);
					building.setManagerUid(om.getTargetId());
				}else {
					///////////////////////////////////
				}
				building.setCommunityId(communityId);
				building.setDescription(data.getDescription());
				building.setTrafficDescription(data.getTrafficDescription());
				
				if (StringUtils.isNotEmpty(data.getLongitudeLatitude())) {
					String[] temp = data.getLongitudeLatitude().replace("，", ",").replace("、", ",").split(",");
					building.setLongitude(Double.parseDouble(temp[0]));
					building.setLatitude(Double.parseDouble(temp[1]));
				}
				
				building.setNamespaceId(org.getNamespaceId());
				building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
				
				communityProvider.createBuilding(userId, building);
			}else {
				building.setAliasName(data.getAliasName());
				building.setAddress(data.getAddress());
				building.setContact(data.getPhone());
				if (StringUtils.isNotBlank(data.getAreaSize())) {
					building.setAreaSize(Double.valueOf(data.getAreaSize()));
				}
				String contactToken = data.getPhone();
				if(ct.get(contactToken) != null) {
					OrganizationMember om = ct.get(contactToken);
					building.setManagerUid(om.getTargetId());
				}else {
					///////////////////////////////////
				}
				building.setDescription(data.getDescription());
				building.setTrafficDescription(data.getTrafficDescription());
				
				if (StringUtils.isNotEmpty(data.getLongitudeLatitude())) {
					String[] temp = data.getLongitudeLatitude().replace("，", ",").replace("、", ",").split(",");
					building.setLongitude(Double.parseDouble(temp[0]));
					building.setLatitude(Double.parseDouble(temp[1]));
				}
				
				building.setNamespaceId(org.getNamespaceId());
				building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
				
				communityProvider.updateBuilding(building);
			}
			
		}
		return list;
	}


	private ImportFileResultLog<ImportBuildingDataDTO> checkData(ImportBuildingDataDTO data) {
		ImportFileResultLog<ImportBuildingDataDTO> log = new ImportFileResultLog<>(CommunityServiceErrorCode.SCOPE);
		if (StringUtils.isEmpty(data.getName())) {
			log.setCode(CommunityServiceErrorCode.ERROR_BUILDING_NAME_EMPTY);
			log.setData(data);
			log.setErrorLog("building name cannot be empty");
			return log;
		}
		
		if (StringUtils.isEmpty(data.getAddress())) {
			log.setCode(CommunityServiceErrorCode.ERROR_ADDRESS_EMPTY);
			log.setData(data);
			log.setErrorLog("address cannot be empty");
			return log;
		}
		
		if (StringUtils.isEmpty(data.getContactor())) {
			log.setCode(CommunityServiceErrorCode.ERROR_CONTACTOR_EMPTY);
			log.setData(data);
			log.setErrorLog("contactor cannot be empty");
			return log;
		}
		
		if (StringUtils.isEmpty(data.getPhone())) {
			log.setCode(CommunityServiceErrorCode.ERROR_PHONE_EMPTY);
			log.setData(data);
			log.setErrorLog("phone cannot be empty");
			return log;
		}
		
		if (StringUtils.isNotEmpty(data.getLongitudeLatitude()) && !data.getLongitudeLatitude().replace("，", ",").replace("、", ",").contains(",")) {
			log.setCode(CommunityServiceErrorCode.ERROR_LATITUDE_LONGITUDE);
			log.setData(data);
			log.setErrorLog("latitude longitude error");
			return log;
		}
		
		return null;
	}


	private List<ImportBuildingDataDTO> handleImportBuildingData(List resultList) {
		List<ImportBuildingDataDTO> list = new ArrayList<>();
		for(int i = 1; i < resultList.size(); i++) {
			RowResult r = (RowResult) resultList.get(i);
			if (StringUtils.isNotBlank(r.getA()) || StringUtils.isNotBlank(r.getB()) || StringUtils.isNotBlank(r.getC()) || StringUtils.isNotBlank(r.getD()) || 
					StringUtils.isNotBlank(r.getE()) || StringUtils.isNotBlank(r.getF()) || StringUtils.isNotBlank(r.getG()) || StringUtils.isNotBlank(r.getH()) || 
					StringUtils.isNotBlank(r.getI())) {
				ImportBuildingDataDTO data = new ImportBuildingDataDTO();
				data.setName(trim(r.getA()));
				data.setAliasName(trim(r.getB()));
				data.setAddress(trim(r.getC()));
				data.setLongitudeLatitude(trim(r.getD()));
				data.setTrafficDescription(trim(r.getE()));
				data.setAreaSize(trim(r.getF()));
				data.setContactor(trim(r.getG()));
				data.setPhone(trim(r.getH()));
				data.setDescription(trim(r.getI()));
				list.add(data);
			}
		}
		return list;
	}
	
	private String trim(String string) {
		if (string != null) {
			return string.trim();
		}
		return "";
	}
	
	private Long getCurrentCommunityId(Long userId) {
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		if (org != null) {
			return org.getCommunityId();
		}
		return null;
	}


	@Override
	public ImportDataResponse importBuildingData(MultipartFile mfile,
			Long userId) {
		ImportDataResponse importDataResponse = new ImportDataResponse();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());
			
			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						"File content is empty");
			}
			LOGGER.debug("Start import data...,total:" + resultList.size());
			//导入数据，返回导入错误的日志数据集
			List<String> errorDataLogs = importBuilding(convertToStrList(resultList), userId);
			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if(null == errorDataLogs || errorDataLogs.isEmpty()){
				LOGGER.debug("Data import all success...");
			}else{
				//记录导入错误日志
				for (String log : errorDataLogs) {
					LOGGER.error(log);
				}
			}
			
			importDataResponse.setTotalCount((long)resultList.size()-1);
			importDataResponse.setFailCount((long)errorDataLogs.size());
			importDataResponse.setLogs(errorDataLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return importDataResponse;
	}

	private List<String> convertToStrList(List list) {
		List<String> result = new ArrayList<String>();
		boolean firstRow = true;
		for (Object o : list) {
			if(firstRow){
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult)o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			sb.append(r.getF()).append("||");
			sb.append(r.getG()).append("||");
			sb.append(r.getH());
			result.add(sb.toString());
		}
		return result;
	}
	
	private List<String> importBuilding(List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();
		//userID查orgid
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		List<OrganizationMember> orgMem = this.organizationProvider.listOrganizationMembersByOrgId(org.getId());
		Map<String, OrganizationMember> ct = new HashMap<String, OrganizationMember>();
		if(orgMem != null) {
			orgMem.stream().map(r -> {
				ct.put(r.getContactToken(), r);
				return null;
			});
		}
		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				Building building = new Building();
				building.setName(s[0]);
				building.setAliasName(s[1]);
				building.setAddress(s[2]);
				building.setContact(s[3]);
				building.setAreaSize(Double.valueOf(s[4]));
				String contactToken = s[6];
				
				if(ct.get(contactToken) != null) {
					OrganizationMember om = ct.get(contactToken);
					building.setManagerUid(om.getTargetId());
				}else {
					///////////////////////////////////
				}
				building.setCommunityId(org.getCommunityId());
				building.setDescription(s[7]);
				building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
				
				LOGGER.info("add building");
				this.communityProvider.createBuilding(userId, building);
				return null;
			});
		}
		return errorDataLogs;
		
	}
	
	@Override
	public CommunityAuthUserAddressResponse listCommunityAuthUserAddress(CommunityAuthUserAddressCommand cmd){
		// Long communityId = cmd.getCommunityId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<NamespaceResource> resourceList = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
        if (resourceList == null) {
            return new CommunityAuthUserAddressResponse();
        }
        List<Long> communityIds = resourceList.stream().map(NamespaceResource::getResourceId).collect(Collectors.toList());

        List<Group> groups = groupProvider.listGroupByCommunityIds(communityIds, (loc, query) -> {
            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });
		
		List<Long> groupIds = new ArrayList<>();
		for (Group group : groups) {
			groupIds.add(group.getId());
		}
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<GroupMember> groupMembers = null;

        if (cmd.getMemberStatus() != null && cmd.getMemberStatus().equals(GroupMemberStatus.REJECT.getCode())) {
            groupMembers = listCommunityRejectUserAddress(cmd.getUserInfoKeyword(), cmd.getCommunityKeyword(), communityIds, locator, pageSize);
        } else {
            groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds, locator, pageSize, (loc, query) -> {
                Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
                c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(cmd.getMemberStatus()));

                if (StringUtils.isNotBlank(cmd.getUserInfoKeyword())) {
                    String keyword = "%" + cmd.getUserInfoKeyword() + "%";
                    query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(Tables.EH_USERS.ID));
                    query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN, Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(Tables.EH_USERS.ID));
                    Condition condition = Tables.EH_USERS.NICK_NAME.like(keyword).or(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(keyword));
                    query.addConditions(condition);
                }
                if (StringUtils.isNotBlank(cmd.getCommunityKeyword())) {
                    String keyword = "%" + cmd.getCommunityKeyword() + "%";
                    query.addJoin(Tables.EH_GROUPS, JoinType.JOIN, Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID));
                    query.addJoin(Tables.EH_COMMUNITIES, JoinType.JOIN, Tables.EH_GROUPS.INTEGRAL_TAG2.eq(Tables.EH_COMMUNITIES.ID));
                    query.addConditions(Tables.EH_COMMUNITIES.NAME.like(keyword));
                }

                query.addConditions(c);
                if (null != locator.getAnchor()) {
                    query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.lt(locator.getAnchor()));
                }
                query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID.desc());
                return query;
            });
        }

        List<GroupMemberDTO> dtos = groupMembers.stream().map(r -> {
			GroupMemberDTO dto = ConvertHelper.convert(r, GroupMemberDTO.class);
			Group group = groupProvider.findGroupById(dto.getGroupId());
			if(null != group) {
				Address address = addressProvider.findAddressById(group.getFamilyAddressId());
				if (null != address) {
					dto.setAddressId(address.getId());
					dto.setApartmentName(address.getApartmentName());
					dto.setBuildingName(address.getBuildingName());
				}
                Long communityId = group.getFamilyCommunityId();
                Community community = communityProvider.findCommunityById(communityId);
                if (community != null) {
                    dto.setCityName(community.getCityName());
                    dto.setAreaName(community.getAreaName());
                    dto.setCommunityName(community.getName());
                }
            }

			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(dto.getMemberId(), IdentifierType.MOBILE.getCode());
			if(null != userIdentifier) {
				dto.setCellPhone(userIdentifier.getIdentifierToken());
				User user = userProvider.findUserById(userIdentifier.getOwnerUid());
				if(null != user) {
					dto.setInviterNickName(user.getNickName());
				}
			}
            if (r.getOperatorUid() != null) {
                userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(r.getOperatorUid(), IdentifierType.MOBILE.getCode());
                if(null != userIdentifier) {
                    dto.setOperatorPhone(userIdentifier.getIdentifierToken());
                    User user = userProvider.findUserById(userIdentifier.getOwnerUid());
                    if(null != user) {
                        dto.setOperatorName(user.getNickName());
                    }
                }
            }
            return dto;
		}).collect(Collectors.toList());
		
		CommunityAuthUserAddressResponse res = new CommunityAuthUserAddressResponse();
		res.setDtos(dtos);
		res.setNextPageAnchor(locator.getAnchor());
		
		return res;
	}
	
	private List<GroupMember> listCommunityRejectUserAddress(String userInfoKeyword, String communityKeyword, List<Long> communityIds, CrossShardListingLocator locator, int pageSize) {
		List<UserGroupHistory> histories = this.userGroupHistoryProvider.queryUserGroupHistoryByGroupIds(userInfoKeyword, communityKeyword, communityIds, locator, pageSize);
		return histories.stream().map(r -> {
            GroupMember member = ConvertHelper.convert(r, GroupMember.class);
            member.setMemberId(r.getOwnerUid());
            member.setApproveTime(r.getCreateTime());
            Address address = addressProvider.findAddressById(r.getAddressId());
            return member;
        }).collect(Collectors.toList());
	}


	@Override
	public CommunityUserAddressResponse listUserBycommunityId(ListCommunityUsersCommand cmd){
		Long communityId = cmd.getCommunityId();
		
		CommunityUserAddressResponse res = new CommunityUserAddressResponse();
		List<CommunityUserAddressDTO> dtos = new ArrayList<CommunityUserAddressDTO>();
		
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		
		List<Group> groups = groupProvider.listGroupByCommunityId(communityId, (loc, query) -> {
            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });
		
		List<Long> groupIds = new ArrayList<Long>(); 
		for (Group group : groups) {
			groupIds.add(group.getId());
		}
		
		if(StringUtils.isNotBlank(cmd.getKeywords())){
			UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId , cmd.getKeywords());
			
			if(null == identifier){
				return res;
			}
			
			List<UserGroup> userGroups = userProvider.listUserGroups(identifier.getOwnerUid(), GroupDiscriminator.FAMILY.getCode());
			
			if(null == userGroups || userGroups.size() == 0){
				return res;
			}
			
			for (UserGroup userGroup : userGroups) {
				if(groupIds.contains(userGroup.getGroupId())){
					User user = userProvider.findUserById(identifier.getOwnerUid());
					CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
					dto.setUserId(user.getId());
					dto.setUserName(user.getNickName());
					dto.setNikeName(user.getNickName());
					dto.setGender(user.getGender());
					dto.setPhone(identifier.getIdentifierToken());
					dto.setIsAuth(2);
					if(GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.ACTIVE)
						dto.setIsAuth(1);
					dtos.add(dto);
					break;
				}
			}
			res.setDtos(dtos);
			return res;
		}
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds,locator,pageSize,(loc, query) -> {
			Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
			if(cmd.getIsAuth() == 1){
				c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
			}else if(cmd.getIsAuth() == 2){
				Condition cond = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
				cond = cond.or(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));
				c = c.and(cond);
			}else{
				c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
			}
            query.addConditions(c);
            if(null != locator.getAnchor())
            	query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.lt(locator.getAnchor()));
            //query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID.desc());
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.desc());
            return query;
        });
		
		// 有重复的用户，需要过滤 by lqs 20160831
		List<Long> userIdList = new ArrayList<Long>();
		for (GroupMember member : groupMembers) {
			User user = userProvider.findUserById(member.getMemberId());
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(member.getMemberId(), IdentifierType.MOBILE.getCode());
			if(null != user && !userIdList.contains(user.getId())){
				CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
				dto.setUserId(user.getId());
				dto.setUserName(user.getNickName());
				dto.setNikeName(user.getNickName());
				dto.setGender(user.getGender());
				if(null != userIdentifier)
					dto.setPhone(userIdentifier.getIdentifierToken());
				dto.setIsAuth(2);
				if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.ACTIVE)
					dto.setIsAuth(1);
				
				userIdList.add(user.getId());
				dtos.add(dto);
			}
		}
		
		res.setDtos(dtos);
		res.setNextPageAnchor(locator.getAnchor());
		return res;
	}
	
	@Override
	public CommunityUserAddressResponse listOwnerBycommunityId(ListCommunityUsersCommand cmd){
		CommunityUserAddressResponse res = new CommunityUserAddressResponse();
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OrganizationOwner> owners = organizationProvider.listOrganizationOwnerByCommunityId(cmd.getCommunityId(),locator, cmd.getPageSize(),(loc, query) -> {
			if(org.springframework.util.StringUtils.isEmpty(cmd.getKeywords())){
				Condition cond = Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.like(cmd.getKeywords() + "%");
				cond = cond.or(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(cmd.getKeywords()));
				query.addConditions(cond);
			}
            return query;
        });
		
		List<CommunityUserAddressDTO> dtos = new ArrayList<CommunityUserAddressDTO>();
		for (OrganizationOwner organizationOwner : owners) {
			CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(organizationOwner.getNamespaceId(), organizationOwner.getContactToken());
			dto.setIsAuth(2);
			if(null != userIdentifier){
				dto.setUserId(userIdentifier.getOwnerUid());
				dto.setIsAuth(1);
			}
			
			dto.setUserName(organizationOwner.getContactName());
			dto.setNikeName(organizationOwner.getContactName());
			dto.setPhone(organizationOwner.getContactToken());
			dtos.add(dto);
		}
		res.setDtos(dtos);
		res.setNextPageAnchor(locator.getAnchor());
		return res;
	}
	
	@Override
	public CommunityUserAddressDTO qryCommunityUserAddressByUserId(QryCommunityUserAddressByUserIdCommand cmd){
		CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());
		if(null == userIdentifier){
			List<OrganizationOwner> owners = organizationProvider.findOrganizationOwnerByTokenOrNamespaceId(cmd.getContactToken(), namespaceId);
			List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();
			for (OrganizationOwner organizationOwner : owners) {
				Address address = addressProvider.findAddressById(organizationOwner.getAddressId());
				if(null != address)
					addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
			}
			return dto;
		}
		User user = userProvider.findUserById(userIdentifier.getOwnerUid());
		List<UserGroup> usreGroups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
		List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();
		if(null != usreGroups){
			for (UserGroup userGroup : usreGroups) {
				Group group = groupProvider.findGroupById(userGroup.getGroupId());
				if(null != group && group.getFamilyCommunityId().equals(cmd.getCommunityId())){
					Address address = addressProvider.findAddressById(group.getFamilyAddressId());
					if(null != address){
						address.setMemberStatus(userGroup.getMemberStatus());
						addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
					}
				}
			}
		}
		if(null != user){
			dto.setUserId(user.getId());
			dto.setUserName(user.getNickName());
			dto.setGender(user.getGender());
			dto.setPhone(null != userIdentifier ? userIdentifier.getIdentifierToken() : null);
			dto.setApplyTime(user.getCreateTime());
			dto.setAddressDtos(addressDtos);
		}
		
		return dto;
	}
	
	@Override
	public CommunityUserAddressDTO qryCommunityUserEnterpriseByUserId(QryCommunityUserAddressByUserIdCommand cmd){
		CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
		
		User user = userProvider.findUserById(cmd.getUserId());
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(cmd.getUserId(), IdentifierType.MOBILE.getCode());
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(user.getId());
		
		List<OrganizationDetailDTO> orgDtos = new ArrayList<OrganizationDetailDTO>();
		if(null != members){

			orgDtos.addAll(populateOrganizationDetails(members));
		}


		if(null != user){
			dto.setUserId(user.getId());
			dto.setUserName(user.getNickName());
			dto.setGender(user.getGender());
			dto.setPhone(null != userIdentifier ? userIdentifier.getIdentifierToken() : null);
			dto.setApplyTime(user.getCreateTime());
			dto.setOrgDtos(orgDtos); 
			dto.setCreateTime(user.getCreateTime() != null ? user.getCreateTime().getTime() : null);
			dto.setExecutiveFlag(user.getExecutiveTag());
			dto.setIdentityNumber(user.getIdentityNumberTag());
			dto.setPosition(user.getPositionTag());
		}
		
		List<OrganizationMemberLog> memberLogs = this.organizationProvider.listOrganizationMemberLogs(user.getId());
		dto.setMemberLogDTOs(new ArrayList<OrganizationMemberLogDTO>());
		if(null != memberLogs){
			for(OrganizationMemberLog log : memberLogs){
				OrganizationMemberLogDTO logDTO = ConvertHelper.convert(log, OrganizationMemberLogDTO.class);
				logDTO.setOperateTime(log.getOperateTime().getTime());
				Organization org = this.organizationProvider.findOrganizationById(log.getOrganizationId());
				if(null != org)
					logDTO.setOrganizationName(org.getName());
				User operator = this.userProvider.findUserById(log.getOperatorUid());
				if(null != operator)
					logDTO.setOperatorNickName(operator.getNickName());
				dto.getMemberLogDTOs().add(logDTO);
			}
		}
		return dto;
	}

	private Set<OrganizationDetailDTO> populateOrganizationDetails(List<OrganizationMember> members) {
		Set<OrganizationDetailDTO> set = new HashSet<>();

		for (OrganizationMember member : members) {
			OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(member.getOrganizationId());
			Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());

			// 通过SQL插入到eh_organization_details里面的数据有可能会漏填displayName，此时界面会显示为undefined，
			// 故需要对该情况补回该名字 by lqs 20170421
//			if(null == detail){
//				detail = new OrganizationDetail();
//				if(null != organization){
//					detail.setDisplayName(organization.getName());
//					detail.setOrganizationId(organization.getId());
//				}
//			}
			String displayName = organization.getName();
			Long organizationId = organization.getId();
			if(detail != null){
				if(detail.getDisplayName() != null){
					displayName = detail.getDisplayName();
				}
				if(detail.getOrganizationId() != null) {
					organizationId = detail.getOrganizationId();
				}
			} else {
				detail = new OrganizationDetail();
			}
			detail.setDisplayName(displayName);
			detail.setOrganizationId(organizationId);

			OrganizationDetailDTO detailDto = ConvertHelper.convert(detail, OrganizationDetailDTO.class);

			List<OrganizationAddress> orgAddresses = organizationProvider.findOrganizationAddressByOrganizationId(detailDto.getOrganizationId());

			List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();
			if(null != orgAddresses){
				for (OrganizationAddress organizationAddress : orgAddresses) {
					Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
					if(null != address)
						addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
				}
			}
			detailDto.setAddresses(addressDtos);

			if (null != organization && organization.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())
					&& OrganizationStatus.fromCode(organization.getStatus()) == OrganizationStatus.ACTIVE) {

				set.add(detailDto);
			}
		}

		return set;
	}

	private CommunityUserResponse listUserByOrganizationIdOrCommunityId(ListCommunityUsersCommand cmd){
		if(null == cmd.getOrganizationId() && null == cmd.getCommunityId()){
			LOGGER.error("organizationId and communityId All are empty");
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organizationId and communityId All are empty");
		}
		CommunityUserResponse res = new CommunityUserResponse(); 
		
		List<Long> organizationIds = this.getAllOrganizationIds(cmd.getCommunityId(), cmd.getOrganizationId());
		
		Condition cond = Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode());
		
		if(1 == cmd.getIsAuth()){
			cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
		}else if(2 == cmd.getIsAuth()){
			Condition condition = Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
			condition = condition.or(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode()));
			cond = cond.and(condition);
		}else{
			cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
		}
		
		if(StringUtils.isNotBlank(cmd.getKeywords())){
			Condition condition = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(cmd.getKeywords());
			condition = condition.or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.eq(cmd.getKeywords()));
			cond = cond.and(condition);
		}
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OrganizationMember> members = organizationProvider.listOrganizationMemberByOrganizationIds(locator, pageSize, cond, organizationIds);
		
		List<CommunityUserDto> dtos = new ArrayList<CommunityUserDto>();
		
		for (OrganizationMember organizationMember : members) {
			CommunityUserDto dto = new CommunityUserDto();
			dto.setUserId(organizationMember.getTargetId());
			dto.setUserName(organizationMember.getContactName());
			dto.setPhone(organizationMember.getContactToken());
			dto.setApplyTime(organizationMember.getCreateTime());
			if(OrganizationMemberStatus.fromCode(organizationMember.getStatus()) == OrganizationMemberStatus.ACTIVE){
				dto.setIsAuth(1);
			}else{
				dto.setIsAuth(2);
			}
			dtos.add(dto);
		}
		
		res.setNextPageAnchor(locator.getAnchor());
		res.setUserCommunities(dtos);
		return res;
	}
	
	private List<Long> getAllOrganizationIds(Long communityId, Long orgId){
		List<Long> communityIds = new ArrayList<Long>();
		List<Long> organizationIds = new ArrayList<Long>();
		if(null == communityId && null != orgId){
			List<CommunityDTO> communityDTOs = organizationService.listAllChildrenOrganizationCoummunities(orgId);
			for (CommunityDTO communityDTO : communityDTOs) {
				communityIds.add(communityDTO.getId());
			}
			organizationIds.add(orgId);
		}else{
			communityIds.add(communityId);
		}
		
		//查询园区下面的所有公司
		for (Long commId : communityIds) {
			List<OrganizationCommunityRequest> organizationCommunityRequests = organizationProvider.queryOrganizationCommunityRequestByCommunityId(new CrossShardListingLocator(), commId, 1000000, null);
			for (OrganizationCommunityRequest organizationCommunityRequest : organizationCommunityRequests) {
				organizationIds.add(organizationCommunityRequest.getMemberId());
			}
		}
		
		//查询所有公司下面所有的子公司
		List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypes.add(OrganizationGroupType.GROUP.getCode());
		
		List<Long> childOrganizationIds = new ArrayList<Long>();
		for (Long organizationId : organizationIds) {
			List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes("/" + organizationId + "/%", groupTypes);
			for (Organization organization : organizations) {
				childOrganizationIds.add(organization.getId());
			}
		}
		organizationIds.addAll(childOrganizationIds);
		
		return organizationIds;
	}
	
	@Override
	public CommunityUserResponse listUserCommunities(
			ListCommunityUsersCommand cmd) {
		
		CommunityUserResponse res = new CommunityUserResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		Long time = System.currentTimeMillis();
		List<UserOrganizations> users = organizationProvider.listUserOrganizations(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_USERS.STATUS.eq(UserStatus.ACTIVE.getCode()));
				query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
				query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));

				if(null != cmd.getOrganizationId()){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
				}

				if(null != cmd.getCommunityId()){
					query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(cmd.getCommunityId()));
				}

				if(!StringUtils.isEmpty(cmd.getKeywords())){
					Condition cond = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(cmd.getKeywords());
					cond = cond.or(Tables.EH_USERS.NICK_NAME.like("%" + cmd.getKeywords() + "%"));
					query.addConditions(cond);
				}

				if(null != UserGender.fromCode(cmd.getGender())){
					query.addConditions(Tables.EH_USERS.GENDER.eq(cmd.getGender()));
				}

				if(null != cmd.getExecutiveFlag()){
					query.addConditions(Tables.EH_USERS.EXECUTIVE_TAG.eq(cmd.getExecutiveFlag()));
				}

				if(AuthFlag.YES == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
				}

				if(AuthFlag.NO == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.ne(OrganizationMemberStatus.ACTIVE.getCode()));
				}
				query.addConditions();

				query.addGroupBy(Tables.EH_USERS.ID);
				return query;
			}
		});

		LOGGER.debug("Get user organization list time:{}", System.currentTimeMillis() - time);
		List<CommunityUserDto> userCommunities = new ArrayList<>();
		for(UserOrganizations r: users){
			CommunityUserDto dto = ConvertHelper.convert(r, CommunityUserDto.class);
			dto.setUserName(r.getNickName());
			dto.setPhone(r.getPhoneNumber());
			dto.setApplyTime(r.getRegisterTime());
			dto.setIdentityNumber(r.getIdentityNumberTag());

			//最新活跃时间 add by sfyan 20170620
			List<UserActivity> userActivities = userActivityProvider.listUserActivetys(r.getUserId(), 1);
			if(userActivities.size() > 0){
				dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
			}

			if(OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(r.getStatus())){
				dto.setIsAuth(AuthFlag.YES.getCode());
			}else{
				dto.setIsAuth(AuthFlag.NO.getCode());
			}

			if(null != r.getOrganizationId()){
				List<OrganizationMember> ms = new ArrayList<>();
				List<OrganizationMember> members = organizationProvider.listOrganizationMembers(r.getUserId());
				for (OrganizationMember member: members) {
					if(OrganizationMemberStatus.fromCode(member.getStatus()) == OrganizationMemberStatus.ACTIVE){
						dto.setOrganizationMemberName(member.getContactName());
						ms.add(member);
					}
				}
				List<OrganizationDetailDTO> organizations = new ArrayList<>();
				organizations.addAll(populateOrganizationDetails(ms));
				dto.setOrganizations(organizations);
			}
			userCommunities.add(dto);
		}
		LOGGER.debug("Get user detail list time:{}", System.currentTimeMillis() - time);
		res.setNextPageAnchor(locator.getAnchor());
		res.setUserCommunities(userCommunities);
		return res;
	}

	@Override
	public void exportCommunityUsers(ListCommunityUsersCommand cmd, HttpServletResponse response) {
		//暂时定成查询1000条记录，且总是从第一条开始导出 by 20170630
		cmd.setPageSize(1000);
		cmd.setPageAnchor(null);

		CommunityUserResponse resp = listUserCommunities(cmd);
		List<CommunityUserDto> dtos = resp.getUserCommunities();

		Workbook wb = new XSSFWorkbook();

		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		Sheet sheet = wb.createSheet("parkingRechargeOrders");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("姓名");
		row.createCell(1).setCellValue("性别");
		row.createCell(2).setCellValue("手机号");
		row.createCell(3).setCellValue("注册时间");
		row.createCell(4).setCellValue("认证状态");
		row.createCell(5).setCellValue("企业");
		row.createCell(6).setCellValue("是否高管");
		row.createCell(7).setCellValue("职位");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		int size = dtos.size();
		for(int i = 0; i < size; i++){
			Row tempRow = sheet.createRow(i + 1);
			CommunityUserDto dto = dtos.get(i);
			List<OrganizationDetailDTO> organizations = dto.getOrganizations();
			StringBuilder enterprises = new StringBuilder();

			for (int k = 0,l = organizations.size(); k < l; k++) {
				if (k == l-1)
					enterprises.append(organizations.get(k).getDisplayName());
				else
					enterprises.append(organizations.get(k).getDisplayName()).append(",");
			}

			tempRow.createCell(0).setCellValue(dto.getUserName());
			tempRow.createCell(1).setCellValue(UserGender.fromCode(dto.getGender()).getText());
			tempRow.createCell(2).setCellValue(dto.getPhone());
			tempRow.createCell(3).setCellValue(null != dto.getApplyTime() ? sdf.format(dto.getApplyTime()) : "");
			tempRow.createCell(4).setCellValue(dto.getIsAuth() == 1 ? "认证" : "非认证");
			tempRow.createCell(5).setCellValue(enterprises.toString());
			tempRow.createCell(6).setCellValue(null == dto.getExecutiveFlag() ? "否" : (dto.getExecutiveFlag() == 0 ? "否" : "是"));
			tempRow.createCell(7).setCellValue(null == dto.getPosition() ? "无" : dto.getPosition());

		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportParkingRechageOrders is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportParkingRechageOrders is fail.");
		}
	}


	@Override
	public CountCommunityUserResponse countCommunityUsers(
			CountCommunityUsersCommand cmd) {
		
		/**
		 * 园区用户统计
		 */
		int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		int communityUserCount  = 0;
		List<Long> orgIds  = new ArrayList<Long>();
		
		if(cmd.getCommunityId() != null) {
			Community community = communityProvider.findCommunityById(cmd.getCommunityId());
			
			/**
			 * 小区用户统计
			 */
			if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.RESIDENTIAL){
				List<Group> groups = groupProvider.listGroupByCommunityId(community.getId(), (loc, query) -> {
		            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
		            query.addConditions(c);
		            return query;
		        });
				
				List<Long> groupIds = new ArrayList<Long>(); 
				for (Group group : groups) {
					groupIds.add(group.getId());
				}
				
				CrossShardListingLocator locator = new CrossShardListingLocator();
				List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds,locator,null,(loc, query) -> {
					Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
					c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
		            query.addConditions(c);
		            query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
		            return query;
		        });
				
				int allCount = groupMembers.size();
				
				List<GroupMember> authMembers = new ArrayList<GroupMember>();
				for (GroupMember groupMember : groupMembers) {
					
					if(GroupMemberStatus.fromCode(groupMember.getMemberStatus()) == GroupMemberStatus.ACTIVE){
						authMembers.add(groupMember);
					}
				}
				
				int authCount = authMembers.size();
				
				CountCommunityUserResponse resp = new CountCommunityUserResponse();
				resp.setCommunityUsers(allCount);
				resp.setAuthUsers(authCount);
				resp.setNotAuthUsers(allCount - authCount);
				
				return resp;
			}
		}
		
		if(namespaceId == Namespace.DEFAULT_NAMESPACE){
			if(null == cmd.getOrganizationId() && null == cmd.getCommunityId()){
				LOGGER.error("organizationId and communityId All are empty");
				throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,
						"organizationId and communityId All are empty");
			}
			
			//获取园区或者机构所管辖的所有园区下的所有企业，包括自己的机构id
			orgIds = this.getAllOrganizationIds(cmd.getCommunityId(), cmd.getOrganizationId());
			
			//获取所有机构集的所有注册用户
			Condition cond = Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode());
			cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
			List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(orgIds, cond);
			
			List<Long> userIds = new ArrayList<Long>();
			if(null != members){
				for (OrganizationMember member : members) {
					if(!userIds.contains(member.getTargetId())){
						userIds.add(member.getTargetId());
					}
				}
			}
			
			communityUserCount = userIds.size();
			
		}else{
			// 如果是其他域的情况，则获取域下面所有的公司
			communityUserCount = userProvider.countUserByNamespaceId(namespaceId, null);
			List<Organization> orgs = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, null ,new CrossShardListingLocator(), 1000000);
			for (Organization organization : orgs) {
				orgIds.add(organization.getId());
			}
		}
		
		// 获取所有机构集的所有认证用户
		Condition cond = Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode());
		cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
		List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(orgIds, cond);
		List<Long> userIds = new ArrayList<Long>();
		if(null != members){
			for (OrganizationMember member : members) {
				if(!userIds.contains(member.getTargetId())){
					userIds.add(member.getTargetId());
				}
			}
		}
		
		int authUserCount = userIds.size();
		
		//总注册用户-认证用户 = 非认证用户
		int notAuthUsers = communityUserCount - authUserCount;
		
		CountCommunityUserResponse resp = new CountCommunityUserResponse();
		resp.setCommunityUsers(communityUserCount);
		resp.setAuthUsers(authUserCount);
		resp.setNotAuthUsers(notAuthUsers);

		return resp;
	}
	
	@Override
	public CommunityUserAddressResponse listUserByNotJoinedCommunity(
			ListCommunityUsersCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<CommunityUserAddressDTO> dtos = new ArrayList<CommunityUserAddressDTO>();
		CommunityUserAddressResponse res = new CommunityUserAddressResponse();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<User> users = userProvider.listUserByKeyword(cmd.getKeywords(), namespaceId, locator, Integer.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		for (User user : users) {
			List<UserGroup> groups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
			if(null == groups || groups.size() == 0){
				CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
				dto.setUserId(user.getId());
				dto.setUserName(user.getNickName());
				dto.setGender(user.getGender());
				dto.setIsAuth(2);
				dto.setPhone(user.getIdentifierToken());
				dto.setApplyTime(user.getCreateTime());
				dtos.add(dto);
				if(dtos.size() == pageSize){
					res.setDtos(dtos);
					res.setNextPageAnchor(user.getCreateTime().getTime());
					break;
				}
			}
		}
		
		return res;
	}
	
	@Override
	public List<CommunityDTO> listUnassignedCommunitiesByNamespaceId() {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
		List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
		for (NamespaceResource namespaceResource : resources) {
			List<OrganizationCommunityDTO> organizationCommunities = organizationProvider.findOrganizationCommunityByCommunityId(namespaceResource.getResourceId());
			if(null == organizationCommunities || organizationCommunities.size() == 0){
				Community community = communityProvider.findCommunityById(namespaceResource.getResourceId());
				dtos.add(ConvertHelper.convert(community, CommunityDTO.class));
			}
		}
		return dtos;
	}


	@Override
	public CreateCommunityResponse createCommunity(final CreateCommunityCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Integer namespaceId = cmd.getNamespaceId();
		checkCreateCommunityParameters(userId, cmd);
		
		CommunityDTO community = createCommunity(userId, namespaceId, cmd);
		
		CreateCommunityResponse response = new CreateCommunityResponse();
		response.setCommunityDTO(community);
		return response;
	}
	
	private CommunityDTO createCommunity(final Long userId, final Integer namespaceId, final CreateCommunityCommand cmd){
		List<CommunityDTO> communities = new ArrayList<>();
		List<Community> cs = new ArrayList<>();
		List<CommunityGeoPointDTO> points = new ArrayList<>();
		dbProvider.execute(s->{
			Long provinceId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName()), 0L);  //创建省
			Long cityId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName(), cmd.getCityName()), provinceId);  //创建市
			Long areaId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName(), cmd.getCityName(), cmd.getAreaName()), cityId);  //创建区县
			
			cmd.setProvinceId(provinceId);
			cmd.setCityId(cityId);
			cmd.setAreaId(areaId);
			Community community = createCommunity(userId, cmd);
			cs.add(community);
			
			CommunityGeoPoint point = createCommunityGeoPoint(community.getId(), cmd.getLatitude(), cmd.getLongitude());
			
			createNamespaceResource(namespaceId, community.getId());
			
			points.add(ConvertHelper.convert(point, CommunityGeoPointDTO.class));
			CommunityDTO cd = ConvertHelper.convert(community, CommunityDTO.class);
			cd.setGeoPointList(points);
			communities.add(cd);
			return true;
		});
		communitySearcher.feedDoc(cs.get(0));
		return communities.get(0);
	} 
	
	private Long createRegion(Long userId, Integer namespaceId, String path, Long parentId) {
		Region region = null;
		//查看此域空间是否存在该区域，若存在，则不添加
		region = regionProvider.findRegionByPath(namespaceId, path);
		if (region != null && region.getId() != null) {
			return region.getId();
		}
		//查看左邻域下是否存在该区域，若存在，则复制
		region = regionProvider.findRegionByPath(Namespace.DEFAULT_NAMESPACE, path);
		if (region != null && region.getId() != null) {
			region.setParentId(parentId);
			region.setId(null);
			region.setNamespaceId(namespaceId);
			regionProvider.createRegion(region);
			return region.getId();
		}
		//如果左邻域下也没有就抛出异常
		LOGGER.error(
				"Invalid region, operatorId=" + userId + ", path=" + path);
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
				"Invalid region");
	}

	private Community createCommunity(Long userId, CreateCommunityCommand cmd) {
		//创建社区
		Community community = ConvertHelper.convert(cmd, Community.class);
		community.setAptCount(0);
		community.setDefaultForumId(1L);
		community.setFeedbackForumId(2L);
		community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		communityProvider.createCommunity(userId, community);
		
		return community;
	}
	
	private CommunityGeoPoint createCommunityGeoPoint(Long communityId, Double latitude, Double longitude){
		//创建经纬度
		CommunityGeoPoint point = new CommunityGeoPoint();
		point.setCommunityId(communityId);
		point.setLatitude(latitude);
		point.setLongitude(longitude);
		point.setGeohash(GeoHashUtils.encode(latitude, longitude));
		communityProvider.createCommunityGeoPoint(point);
		return point;
	}

	private NamespaceResource createNamespaceResource(Integer namespaceId, Long resourceId) {
		NamespaceResource resource = new NamespaceResource();
		resource.setNamespaceId(namespaceId);
		resource.setResourceId(resourceId);
		resource.setResourceType(NamespaceResourceType.COMMUNITY.getCode());
		namespaceResourceProvider.createNamespaceResource(resource);
		return resource;
	}

	private void checkCreateCommunityParameters(final Long userId, final CreateCommunityCommand cmd) {
		if (cmd.getNamespaceId() == null || StringUtils.isBlank(cmd.getName()) || cmd.getCommunityType() == null 
				|| StringUtils.isBlank(cmd.getProvinceName()) || StringUtils.isBlank(cmd.getCityName())
				|| StringUtils.isBlank(cmd.getAreaName()) || CommunityType.fromCode(cmd.getCommunityType()) == null) {
			LOGGER.error(
					"Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
	}

	private String getPath(String... names) {
		StringBuilder sb = new StringBuilder();
		for (String name : names) {
			sb.append("/").append(name.trim());
		}
		return sb.toString();
	}

	@Override
	public void importCommunity(ImportCommunityCommand cmd, MultipartFile[] files) {
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getNamespaceId()==null) {
			LOGGER.error(
					"Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		List<CreateCommunityCommand> list = getCommunitiesFromExcel(userId, cmd, files);
		list.forEach(c->createCommunity(userId, cmd.getNamespaceId(), c));
	}


	@SuppressWarnings("unchecked")
	private List<CreateCommunityCommand> getCommunitiesFromExcel(Long userId, ImportCommunityCommand cmd, MultipartFile[] files) {
		List<RowResult> resultList = null;
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			LOGGER.error("process Excel error, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION, "process Excel error");
		}

		if (resultList != null && resultList.size() > 0) {
			final List<CreateCommunityCommand> list = new ArrayList<>();
			for (int i = 1, len = resultList.size(); i < len; i++) {
				RowResult result = resultList.get(i);
				
				CreateCommunityCommand ccmd = new CreateCommunityCommand();
				ccmd.setNamespaceId(cmd.getNamespaceId());
				ccmd.setName(RowResult.trimString(result.getA()));
				ccmd.setAliasName(RowResult.trimString(result.getB()));
				ccmd.setAddress(RowResult.trimString(result.getC()));
				ccmd.setDescription(RowResult.trimString(result.getD()));
				ccmd.setCommunityType(Byte.parseByte(RowResult.trimString(result.getE())));
				ccmd.setProvinceName(RowResult.trimString(result.getF()));
				ccmd.setCityName(RowResult.trimString(result.getG()));
				ccmd.setAreaName(RowResult.trimString(result.getH()));
				ccmd.setLatitude(Double.parseDouble(RowResult.trimString(result.getI())));
				ccmd.setLongitude(Double.parseDouble(RowResult.trimString(result.getJ())));
				
				checkCreateCommunityParameters(userId, ccmd);
				list.add(ccmd);
			}
			return list;
		}
		LOGGER.error("excel data format is not correct.rowCount=" + resultList.size());
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"excel data format is not correct");
	}


	@Override
	public ListCommunityByNamespaceIdResponse listCommunityByNamespaceId(ListCommunityByNamespaceIdCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null? 0 : cmd.getPageAnchor();
		
		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(pageAnchor);
		
		List<CommunityDTO> communities = communityProvider.listCommunitiesByNamespaceId(null, cmd.getNamespaceId(), locator, pageSize+1);
		
		ListCommunityByNamespaceIdResponse response = new ListCommunityByNamespaceIdResponse();
		if (communities != null && communities.size() > 0) {
			if (communities.size() > pageSize) {
				communities.remove(communities.size()-1);
				pageAnchor = communities.get(communities.size()-1).getId();
			}else {
				pageAnchor = null;
			}
			populateCommunityGeoPoint(communities);
			response.setCommunities(communities);
			response.setNextPageAnchor(pageAnchor);
		}
		
		return response;
	}

	private void populateCommunityGeoPoint(List<CommunityDTO> communities) {
		communities.forEach(c->c.setGeoPointList(
				communityProvider.listCommunityGeoPoints(c.getId()).stream().map(r->ConvertHelper.convert(r, CommunityGeoPointDTO.class))
				.collect(Collectors.toList()))
				);
	}


	@Override
	public void communityImportBaseConfig(CommunityImportBaseConfigCommand cmd) {
		if (StringUtils.isBlank(cmd.getNamespaceName()) || StringUtils.isBlank(cmd.getCommunityType()) 
				|| NamespaceCommunityType.fromCode(cmd.getCommunityType())==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		dbProvider.execute(s->{
			Integer namespaceId = createNamespace(cmd.getNamespaceName());
			createNamespaceDetail(namespaceId, cmd.getCommunityType());
			createVersion(namespaceId, cmd.getAndroidRealm(), cmd.getIosRealm(), cmd.getVersionDescription(), cmd.getVersionRange(), cmd.getTargetVersion(), cmd.getForceUpgrade());
			createSmsTemplate(namespaceId, cmd.getSmsTemplates());
			createAppAgreementsUrl(namespaceId, cmd.getAppAgreementsUrl());
			createHomeUrl(namespaceId, cmd.getHomeUrl());
			createPostTypes(namespaceId, cmd.getPostTypes());
			return true;
		});
	}


	private Integer createNamespace(String namespaceName) {
		Namespace namespace = new Namespace();
		namespace.setName(namespaceName);
		namespacesProvider.createNamespace(namespace);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create namespace success: namespaceId="+namespace.getId()+", namespaceName="+namespaceName);
		}
		return namespace.getId();
	}
	
	private void createNamespaceDetail(Integer namespaceId, String communityType) {
		NamespaceDetail namespaceDetail = new NamespaceDetail();
		namespaceDetail.setNamespaceId(namespaceId);
		namespaceDetail.setResourceType(communityType);
		namespacesProvider.createNamespaceDetail(namespaceDetail);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create namespace detail success: namespaceId="+namespaceId+", communityType="+communityType);
		}
	}

	private void createVersion(Integer namespaceId, String androidRealm, String iosRealm, String versionDescription,
			String versionRange, String targetVersion, Byte forceUpgrade) {
		createVersion(namespaceId, androidRealm, versionDescription, versionRange, targetVersion, forceUpgrade);
		createVersion(namespaceId, iosRealm, versionDescription, versionRange, targetVersion, forceUpgrade);
	}
	
	private void createVersion(Integer namespaceId, String realm, String description, String versionRange, String targetVersion, Byte forceUpgrade){
		if(StringUtils.isBlank(realm)){
			return ;
		}
		VersionRealm versionRealm = new VersionRealm();
		versionRealm.setRealm(realm);
		versionRealm.setDescription(description);
		//persist中无namespaceId，大师说不用管，add by tt， 20160817
		versionProvider.createVersionRealm(versionRealm);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create version realm success: namespaceId="+namespaceId+", realm="+realm);
		}
		
		if(StringUtils.isBlank(versionRange) || StringUtils.isBlank(targetVersion)){
			return;
		}
		VersionUpgradeRule rule = new VersionUpgradeRule();
        rule.setRealmId(versionRealm.getId());
        rule.setForceUpgrade(forceUpgrade);
        rule.setTargetVersion(targetVersion);
        rule.setOrder(0);
        VersionRange range = new VersionRange(versionRange);
        rule.setMatchingLowerBound(range.getLowerBound());
        rule.setMatchingUpperBound(range.getUpperBound());
        versionProvider.createVersionUpgradeRule(rule);
        if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create version upgrade rule success: namespaceId="+namespaceId+", targetVersion="+targetVersion+", versionRange="+versionRange);
		}
	}

	private void createSmsTemplate(Integer namespaceId, List<SmsTemplate> smsTemplates) {
		if(smsTemplates == null || smsTemplates.isEmpty()){
			return;
		}
		smsTemplates.forEach(s->{
			if(StringUtils.isNotBlank(s.getTitle())){
				LocaleTemplate localeTemplate = new LocaleTemplate();
				localeTemplate.setCode(s.getCode());
				localeTemplate.setDescription(s.getTitle());
				localeTemplate.setLocale("zh_CN");
				localeTemplate.setNamespaceId(namespaceId);
				localeTemplate.setScope("sms.default.yzx");
				localeTemplate.setText(s.getTemplateId());
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("create sms template success: namespaceId="+namespaceId+", title="+s.getTitle()+", templateId="+s.getTemplateId()+", code="+s.getCode());
				}
				localeTemplateProvider.createLocaleTemplate(localeTemplate);
			}
		});
	}


	private void createAppAgreementsUrl(Integer namespaceId, String appAgreementsUrl) {
		if (StringUtils.isBlank(appAgreementsUrl)) {
			return ;
		}
		Configurations configurations = new Configurations();
		configurations.setName("app.agreements.url");
		configurations.setValue(appAgreementsUrl);
		configurations.setNamespaceId(namespaceId);
		configurationsProvider.createConfiguration(configurations);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create app.agreements.url success: namespaceId="+namespaceId+", app.agreements.url="+appAgreementsUrl);
		}
	}


	private void createHomeUrl(Integer namespaceId, String homeUrl) {
		if (StringUtils.isBlank(homeUrl)) {
			return ;
		}
		Configurations configurations = new Configurations();
		configurations.setName("home.url");
		configurations.setValue(homeUrl);
		configurations.setNamespaceId(namespaceId);
		configurationsProvider.createConfiguration(configurations);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create home.url success: namespaceId="+namespaceId+", home.url="+homeUrl);
		}
	}


	private void createPostTypes(Integer namespaceId, List<String> postTypes) {
		if (postTypes == null || postTypes.isEmpty()) {
			return;
		}
		postTypes.forEach(p->{
			if(StringUtils.isNotBlank(p)){
				Category category = new Category();
				category.setParentId(1L);
				category.setLinkId(0L);
				category.setName(p);
				category.setPath("帖子/"+p);
				category.setDefaultOrder(0);
				category.setStatus((byte)0);
				category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				category.setNamespaceId(namespaceId);
				categoryProvider.createCategory(category);
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("create post type success: namespaceId="+namespaceId+", post type="+p);
				}
			}
		});
	}


	@Override
	public void communityImportOrganizationConfig(CommunityImportOrganizationConfigCommand cmd) {
		if (StringUtils.isBlank(cmd.getOrganizationName()) || StringUtils.isBlank(cmd.getAdminNickname()) 
				|| cmd.getNamespaceId() == null || cmd.getCommunityId() == null || StringUtils.isBlank(cmd.getAdminPhone())
				|| StringUtils.isBlank(cmd.getGroupName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		dbProvider.execute(s->{
			Organization organization = createOrganization(cmd.getNamespaceId(), cmd.getOrganizationName());
			createOrganizationCommunityRequest(organization.getId(), cmd.getCommunityId());
			Group group = createGroup(cmd.getNamespaceId(), organization, cmd.getGroupName(), cmd.getGroupDisplayName());
			createForum(cmd.getNamespaceId(), group);
			createOrganizationCommunities(cmd.getNamespaceId(), organization.getId());
			User user = createUser(cmd.getNamespaceId(), cmd.getAdminNickname(), cmd.getAdminPhone());
			createOrganizationMember(cmd.getNamespaceId(), organization.getId(), user.getId(), user.getNickName(), cmd.getAdminPhone());
			createAclRoleAssignment(organization.getId(), user.getId());
			return true;
		});
	}


	private Organization createOrganization(Integer namespaceId, String organizationName) {
		Organization organization = new Organization();
		organization.setParentId(0L);
		organization.setOrganizationType(OrganizationType.PM.getCode());
		organization.setName(organizationName);
		organization.setAddressId(0L);
		organization.setLevel(1);
		organization.setStatus(OrganizationStatus.ACTIVE.getCode());
		organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
		organization.setDirectlyEnterpriseId(0L);
		organization.setNamespaceId(namespaceId);
		organization.setShowFlag((byte) 1);
		organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organizationProvider.createOrganization(organization);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization success: namespaceId="+namespaceId+", organizationId="+organization.getId()+", organizationName"+organizationName);
		}
		return organization;
	}


	private void createOrganizationCommunityRequest(Long organizationId, Long communityId) {
		OrganizationCommunityRequest request = new OrganizationCommunityRequest();
		request.setCommunityId(communityId);
		request.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
		request.setMemberId(organizationId);
		request.setMemberStatus(OrganizationMemberStatus.ACTIVE.getCode());
		organizationProvider.createOrganizationCommunityRequest(request);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization community request success: organizationId="+organizationId+", communityId="+communityId);
		}
	}


	private Group createGroup(Integer namespaceId, Organization organization, String groupName,
			String groupDisplayName) {
		Group group = new Group();
		group.setNamespaceId(namespaceId);
		group.setName(groupName);
		group.setDisplayName(groupDisplayName);
		group.setCreatorUid(UserContext.current().getUser().getId());
		group.setPrivateFlag(PrivateFlag.PRIVATE.getCode());
		group.setJoinPolicy(GroupJoinPolicy.NEED_APPROVE.getCode());
		group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
		group.setStatus(GroupAdminStatus.ACTIVE.getCode());
		group.setMemberCount(0L);
		group.setShareCount(0L);
		group.setPostFlag(GroupPostFlag.ALL.getCode());
		group.setVisibleRegionType(VisibleRegionType.REGION.getCode());
		group.setVisibleRegionId(organization.getId());
		groupProvider.createGroup(group);
		
		//更新组织中的groupId
		organization.setGroupId(group.getId());
		organizationProvider.updateOrganization(organization);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create group success: namespaceId="+namespaceId+", groupName="+groupName);
		}
		
		return group;
	}


	private void createForum(Integer namespaceId, Group group) {
		Forum forum = new Forum();
		forum.setNamespaceId(namespaceId);
		forum.setAppId(AppConstants.APPID_FORUM);
		forum.setOwnerType(EntityType.GROUP.getCode());
		forum.setOwnerId(group.getId());
		forum.setName(EntityType.GROUP.getCode() + "-" + group.getId());
		forum.setPostCount(0L);
		forum.setModifySeq(0L);
		forum.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		forumProvider.createForum(forum);
		
		//更新group中圈论坛id
		group.setIntegralTag4(forum.getId());
		groupProvider.updateGroup(group);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create forum success: namespaceId="+namespaceId+", groupId="+group.getId());
		}
	}


	private void createOrganizationCommunities(Integer namespaceId, Long organizationId) {
		List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
		if(communities == null || communities.isEmpty()){
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("create organization communities error: namespaceId="+namespaceId+", organizationId="+organizationId+", no community!");
			}
			return ;
		}
		communities.forEach(c->{
			OrganizationCommunity organizationCommunity = new OrganizationCommunity();
			organizationCommunity.setCommunityId(c.getId());
			organizationCommunity.setOrganizationId(organizationId);
			organizationProvider.createOrganizationCommunity(organizationCommunity);
		});
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization communities success: namespaceId="+namespaceId+", organizationId="+organizationId+", communities count="+communities.size());
		}
	}


	private User createUser(Integer namespaceId, String adminNickname, String adminPhone) {
		User user = new User();
		user.setNickName(adminNickname);
		user.setStatus(UserStatus.ACTIVE.getCode());
		user.setPoints(0);
		user.setLevel(UserLevel.L1.getCode());
		user.setGender(UserGender.UNDISCLOSURED.getCode());
		user.setLocale("zh_CN");
		user.setNamespaceId(namespaceId);
		String salt = EncryptionUtils.createRandomSalt();
		user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "123456",salt)));
		user.setSalt(salt);
		userProvider.createUser(user);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create user success: namespaceId="+namespaceId+", adminNickname="+adminNickname+", adminPhone="+adminPhone);
		}
		
		UserIdentifier userIdentifier = new UserIdentifier();
		userIdentifier.setOwnerUid(user.getId());
		userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
		userIdentifier.setIdentifierToken(adminPhone);
		userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
		userIdentifier.setNamespaceId(namespaceId);
		userProvider.createIdentifier(userIdentifier);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create user identifier success: namespaceId="+namespaceId+", userId="+user.getId());
		}
		
		return user;
	}


	private void createOrganizationMember(Integer namespaceId, Long organizationId, Long userId,
			String nickName, String phone) {
		OrganizationMember organizationMember = new OrganizationMember();
		organizationMember.setOrganizationId(organizationId);
		organizationMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
		organizationMember.setTargetId(userId);
		organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		organizationMember.setContactName(nickName);
		organizationMember.setContactType(IdentifierType.MOBILE.getCode());
		organizationMember.setContactToken(phone);
		organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
		organizationMember.setGender(UserGender.UNDISCLOSURED.getCode());
		organizationMember.setNamespaceId(namespaceId);
		organizationMember.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organizationMember.setCreatorUid(UserContext.current().getUser().getId());
		organizationProvider.createOrganizationMember(organizationMember);
		userSearcher.feedDoc(organizationMember);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization member success: namespaceId="+namespaceId+", organizationId="+organizationId+", userId="+userId+", phone="+phone);
		}
	}


	private void createAclRoleAssignment(Long organizationId, Long userId) {
		RoleAssignment roleAssignment = new RoleAssignment();
		roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
		roleAssignment.setOwnerId(organizationId);
		roleAssignment.setTargetType(EntityType.USER.getCode());
		roleAssignment.setTargetId(userId);
		roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
		roleAssignment.setRoleId(1001L);
		aclProvider.createRoleAssignment(roleAssignment);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create acl role assignment success: organizationId="+organizationId+", userId="+userId);
		}
	}

	@Override
	public ListCommunityAuthPersonnelsResponse listCommunityAuthPersonnels(ListCommunityAuthPersonnelsCommand cmd) {
		// TODO Auto-generated method
        ListCommunityAuthPersonnelsResponse response = new ListCommunityAuthPersonnelsResponse();

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<NamespaceResource> resourceList = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
        if (resourceList == null) {
            return response;
        }
        List<Long> communityIds = resourceList.stream().map(NamespaceResource::getResourceId).collect(Collectors.toList());
        List<OrganizationCommunityRequest> orgs = this.organizationProvider.listOrganizationCommunityRequests(communityIds);
        if (null == orgs || orgs.size() == 0) {
            return response;
        }

		List<Long> orgIds = new ArrayList<>();
		for(OrganizationCommunityRequest org : orgs) {
			orgIds.add(org.getMemberId());
		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(
				cmd.getUserInfoKeyword(), cmd.getOrgNameKeyword(), orgIds, cmd.getStatus(), null, locator, pageSize);

		if(0 == organizationMembers.size()) {
			return response;
		}

		response.setNextPageAnchor(locator.getAnchor());

        List<ComOrganizationMemberDTO> dtoList = organizationMembers.stream()
            .map((c) -> {
                ComOrganizationMemberDTO dto = ConvertHelper.convert(c, ComOrganizationMemberDTO.class);
                if (c.getOperatorUid() != null) {
                    User operator = userProvider.findUserById(c.getOperatorUid());
                    UserIdentifier operatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(c.getOperatorUid(), IdentifierType.MOBILE.getCode());
                    dto.setOperatorName(operator.getNickName());
                    dto.setOperatorPhone(operatorIdentifier.getIdentifierToken());
                }
                return dto;
            }).collect(Collectors.toList());

        response.setMembers(dtoList);

		return response;
	}

	@Override
	public void updateCommunityUser(UpdateCommunityUserCommand cmd) { 
		if(null == cmd.getUserId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid userid parameter");
		User user = userProvider.findUserById(cmd.getUserId());

		if(null == user)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid userid parameter: no this user");
		user.setExecutiveTag(cmd.getExecutiveFlag());
		user.setIdentityNumberTag(cmd.getIdentityNumber());
		user.setPositionTag(cmd.getPosition());
		userProvider.updateUser(user); 
		
	}
	
	public void createResourceCategory(CreateResourceCategoryCommand cmd) {
		
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		String name = cmd.getName();
		checkResourceCategoryName(name);
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

		if(null == cmd.getType()){
			cmd.setType(ResourceCategoryType.CATEGORY.getCode());
		}

		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		Long parentId = cmd.getParentId();
		ResourceCategory category = null;
		ResourceCategory parentCategory = null;
		if(null == parentId || parentId == 0){
			
			category = communityProvider.findResourceCategoryByParentIdAndName(ownerId, ownerType, 0L, name, cmd.getType());
			checkResourceCategoryExsit(category);
			category = new ResourceCategory();
			category.setParentId(0L);
		}else{
			parentCategory = communityProvider.findResourceCategoryById(parentId);
			checkResourceCategoryIsNull(parentCategory);
			category = communityProvider.findResourceCategoryByParentIdAndName(ownerId, ownerType, parentId, name,cmd.getType());
			checkResourceCategoryExsit(category);
			category = new ResourceCategory();
			category.setPath(parentCategory.getPath());
			category.setParentId(parentId);
		}
		
		category.setCreateTime(new Timestamp(System.currentTimeMillis()));
		category.setCreatorUid(UserContext.current().getUser().getId());   
		category.setName(name);
		category.setNamespaceId(namespaceId);
		
		category.setStatus(ResourceCategoryStatus.ACTIVE.getCode());
		category.setOwnerType(ownerType);
		category.setOwnerId(ownerId);
		category.setType(ResourceCategoryType.CATEGORY.getCode());
		communityProvider.createResourceCategory(category);
		
	}


	@Override
	public void updateResourceCategory(UpdateResourceCategoryCommand cmd) {
		checkResourceCategoryId(cmd.getId());
		checkResourceCategoryName(cmd.getName());
		
		ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getId());
		checkResourceCategoryIsNull(category);
		
		category.setName(cmd.getName());
//		String path = category.getPath();
//		path = path.substring(0, path.lastIndexOf("/"));
//
//		category.setPath(path + "/" + cmd.getName());
		category.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		communityProvider.updateResourceCategory(category);
	}


	@Override
	public void deleteResourceCategory(DeleteResourceCategoryCommand cmd) {
		checkResourceCategoryId(cmd.getId());
		
		ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getId());
		checkResourceCategoryIsNull(category);
		
		category.setStatus(ResourceCategoryStatus.INACTIVE.getCode());
		communityProvider.updateResourceCategory(category);
		
	}


	@Override
	public void createResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd) {
		if(null == cmd.getResourceId()) {
        	LOGGER.error("ResourceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(cmd.getResourceType())) {
        	LOGGER.error("ResourceType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceType cannot be null.");
        }

		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		ResourceCategoryAssignment rca = communityProvider.findResourceCategoryAssignment(cmd.getResourceId(), cmd.getResourceType(), 
				namespaceId);
		if(null != rca) {
			if(null != cmd.getResourceCategoryId()) {
				ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getResourceCategoryId());
				checkResourceCategoryIsNull(category);
				rca.setResourceCategryId(category.getId());
				communityProvider.updateResourceCategoryAssignment(rca);
			}else{
				communityProvider.deleteResourceCategoryAssignmentById(rca.getId());
			}
		}else{
			if(null == cmd.getResourceCategoryId()) {
	        	LOGGER.error("CategoryId cannot be null.");
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	    				"CategoryId cannot be null.");
	        }
			ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getResourceCategoryId());
			checkResourceCategoryIsNull(category);
			rca = new ResourceCategoryAssignment();
			rca.setCreateTime(new Timestamp(System.currentTimeMillis()));
			rca.setCreatorUid(UserContext.current().getUser().getId());
			rca.setNamespaceId(namespaceId);
			rca.setResourceCategryId(category.getId());
			rca.setResourceId(cmd.getResourceId());
			rca.setResourceType(cmd.getResourceType());
			communityProvider.createResourceCategoryAssignment(rca);
		}
		
	}


	@Override
	public void deleteResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd) {
		if(null == cmd.getResourceId()) {
        	LOGGER.error("ResourceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(cmd.getResourceType())) {
        	LOGGER.error("ResourceType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceType cannot be null.");
        }
		
//    	ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getResourceCategoryId());
//		checkResourceCategoryIsNull(category);
		
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		ResourceCategoryAssignment rca = communityProvider.findResourceCategoryAssignment(cmd.getResourceId(), cmd.getResourceType(), 
				namespaceId);
		if(null != rca)
			communityProvider.deleteResourceCategoryAssignmentById(rca.getId());
		 
	}
	
	private void checkResourceCategoryId(Long id) {
		if(null == id) {
        	LOGGER.error("Id cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Id cannot be null.");
        }
	}
	
	private void checkResourceCategoryName(String name) {
		if(StringUtils.isBlank(name)) {
        	LOGGER.error("Name cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Name cannot be null.");
        }
	}
	
	private void checkResourceCategoryIsNull(ResourceCategory category) {
		if(null == category) {
			LOGGER.error("ResourceCategory not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ResourceCategory not found.");
		}
	}
	
	private void checkResourceCategoryExsit(ResourceCategory category) {
		if(null != category) {
			LOGGER.error("ResourceCategory have been in existing");
			throw RuntimeErrorException.errorWith(ResourceCategoryErrorCode.SCOPE, ResourceCategoryErrorCode.ERROR_RESOURCE_CATEGORY_EXIST,
					"ResourceCategory have been in existing");
		}
	}
	
	private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
		if(null == ownerId) {
        	LOGGER.error("OwnerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("OwnerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerType cannot be null.");
        }
	}


	@Override
	public ListCommunitiesByKeywordCommandResponse listCommunitiesByCategory(ListCommunitiesByCategoryCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int namespaceId =UserContext.getCurrentNamespaceId(null);

		List<Community> list = communityProvider.listCommunitiesByCategory(cmd.getCityId(), cmd.getAreaId(), 
				cmd.getCategoryId(), cmd.getKeywords(), cmd.getPageAnchor(), pageSize);

		ListCommunitiesByKeywordCommandResponse response = new ListCommunitiesByKeywordCommandResponse();
		
		if(list.size() > 0){
			List<CommunityDTO> resultList = list.stream().map((c) -> {
				CommunityDTO dto = ConvertHelper.convert(c, CommunityDTO.class);
				ResourceCategoryAssignment ra = communityProvider.findResourceCategoryAssignment(c.getId(), EntityType.COMMUNITY.getCode(), namespaceId);
				if(null != ra) {
					ResourceCategory category = communityProvider.findResourceCategoryById(ra.getResourceCategryId());
					dto.setCategoryId(category.getId());
					dto.setCategoryName(category.getName());
				}
				
				return dto;
			}).collect(Collectors.toList());
    		response.setRequests(resultList);
    		if(list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
		
		return response;
	}


	@Override
	public List<ResourceCategoryDTO> listResourceCategories(ListResourceCategoryCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		
//		List<ResourceCategoryDTO> result = new ArrayList<ResourceCategoryDTO>();
//		String path = null;
//		Long parentId = null == cmd.getParentId() ? 0L : cmd.getParentId();
//		if(null != cmd.getParentId()) {
//			ResourceCategory resourceCategory = communityProvider.findResourceCategoryById(cmd.getParentId());
//			checkResourceCategoryIsNull(resourceCategory);
//			path = resourceCategory.getPath();
//		}
		
		List<ResourceCategoryDTO> temp = communityProvider.listResourceCategory(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getParentId(), null, ResourceCategoryType.CATEGORY.getCode())
			.stream().map(r -> {
				ResourceCategoryDTO dto = ConvertHelper.convert(r, ResourceCategoryDTO.class);
				
				return dto;
			}).collect(Collectors.toList());
		
//		for(ResourceCategoryDTO s: temp) {
//			getChildCategories(temp, s);
//			if(s.getParentId() == parentId) {
//				result.add(s);
//			}
//		}
		
		return temp;
	}
	
	@Override
	public List<ResourceCategoryDTO> listTreeResourceCategoryAssignments(ListResourceCategoryCommand cmd) {
		if(null == cmd.getParentId())
			cmd.setParentId(0L);
		List<ResourceCategoryDTO> list = listTreeResourceCategories(cmd);
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();

		setresourceDTOs(list, namespaceId);
		
		return list;
	}
	
	private void setresourceDTOs(List<ResourceCategoryDTO> list, Integer namespaceId){
		if(null != list) {
			for(ResourceCategoryDTO r: list) {
				List<ResourceCategoryAssignment> resourceCategoryAssignments = communityProvider.listResourceCategoryAssignment(r.getId(), namespaceId);
				List<ResourceCategoryAssignmentDTO> resourceDTOs = resourceCategoryAssignments.stream().map(ra -> {
					ResourceCategoryAssignmentDTO dto = ConvertHelper.convert(ra, ResourceCategoryAssignmentDTO.class);
					Community community = communityProvider.findCommunityById(ra.getResourceId());
					dto.setResourceName(community.getName());
					return dto;
				}).collect(Collectors.toList());
				setresourceDTOs(r.getCategoryDTOs(), namespaceId);
				r.setResourceDTOs(resourceDTOs);
			}
		}
		
	}
	
	private ResourceCategoryDTO getChildCategories(List<ResourceCategoryDTO> list, ResourceCategoryDTO dto){
		
		List<ResourceCategoryDTO> childrens = new ArrayList<ResourceCategoryDTO>();
		
		for (ResourceCategoryDTO resourceCategoryDTO : list) {
			if(dto.getId().equals(resourceCategoryDTO.getParentId())){
				childrens.add(getChildCategories(list, resourceCategoryDTO));
			}
		}
		dto.setCategoryDTOs(childrens);
		
		return dto;
	}


	@Override
	public List<ResourceCategoryDTO> listTreeResourceCategories(ListResourceCategoryCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		
		List<ResourceCategoryDTO> result = new ArrayList<ResourceCategoryDTO>();
		String path = null;
		Long parentId = null == cmd.getParentId() ? 0L : cmd.getParentId();
		if(null != cmd.getParentId()) {
			ResourceCategory resourceCategory = communityProvider.findResourceCategoryById(cmd.getParentId());
			checkResourceCategoryIsNull(resourceCategory);
			path = resourceCategory.getPath();
		}
		
		List<ResourceCategoryDTO> temp = communityProvider.listResourceCategory(cmd.getOwnerId(), cmd.getOwnerType(), null, path, ResourceCategoryType.CATEGORY.getCode())
			.stream().map(r -> {
				ResourceCategoryDTO dto = ConvertHelper.convert(r, ResourceCategoryDTO.class);
				
				return dto;
			}).collect(Collectors.toList());
		
		for(ResourceCategoryDTO s: temp) {
			getChildCategories(temp, s);
			if(s.getParentId() == parentId) {
				result.add(s);
			}
		}
		
		return result;
	}

	@Override
	public List<ProjectDTO> listChildProjects(ListChildProjectCommand cmd){
		List<ProjectDTO> dtos = new ArrayList<>();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<ResourceCategory> categorys = communityProvider.listResourceCategory(cmd.getProjectId(), cmd.getProjectType(),0L, null, ResourceCategoryType.OBJECT.getCode());
		for (ResourceCategory category: categorys) {
			ProjectDTO dto = new ProjectDTO();
			dto.setProjectName(category.getName());
			dto.setProjectId(category.getId());
			dto.setProjectType(EntityType.RESOURCE_CATEGORY.getCode());
			List<ResourceCategoryAssignment> buildingCategorys = communityProvider.listResourceCategoryAssignment(category.getId(), namespaceId);
			List<ProjectDTO> buildingProjects = new ArrayList<>();
			for (ResourceCategoryAssignment buildingCategory: buildingCategorys) {
				if(EntityType.fromCode(buildingCategory.getResourceType()) == EntityType.BUILDING){
					Building building = communityProvider.findBuildingById(buildingCategory.getResourceId());
					if(null != building){
						ProjectDTO buildingProject = new ProjectDTO();
						buildingProject.setProjectId(building.getId());
						buildingProject.setProjectName(building.getName());
						buildingProject.setProjectType(EntityType.BUILDING.getCode());
						buildingProjects.add(buildingProject);
					}
				}
			}
			dto.setProjects(buildingProjects);
			dtos.add(dto);

		}

		return dtos;
	}

	@Override
	public void createChildProject(CreateChildProjectCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		ResourceCategory project = new ResourceCategory();
		project.setCreatorUid(user.getId());
		project.setNamespaceId(namespaceId);
		project.setStatus(ResourceCategoryStatus.ACTIVE.getCode());
		project.setName(cmd.getName());
		project.setOwnerId(cmd.getProjectId());
		project.setOwnerType(cmd.getProjectType());
		project.setParentId(0L);
		project.setType(ResourceCategoryType.OBJECT.getCode());

		this.dbProvider.execute((TransactionStatus status) ->  {
			communityProvider.createResourceCategory(project);
			if(null != cmd.getBuildingIds() && cmd.getBuildingIds().size() > 0){
				this.addResourceCategoryAssignment(cmd.getBuildingIds(), project.getId());
			}
			return null;
		});
	}

	@Override
	public void updateChildProject(UpdateChildProjectCommand cmd){
		ResourceCategory project = communityProvider.findResourceCategoryById(cmd.getId());
		if(null != project){
			this.dbProvider.execute((TransactionStatus status) ->  {
				project.setName(cmd.getName());
				project.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				communityProvider.updateResourceCategory(project);

				if(null != cmd.getBuildingIds() && cmd.getBuildingIds().size() > 0){
					this.addResourceCategoryAssignment(cmd.getBuildingIds(), project.getId());
				}
				return null;
			});
		}
	}

	private void addResourceCategoryAssignment(List<Long> buildingIds, Long categoryId){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<ResourceCategoryAssignment> bulidingCategorys = communityProvider.listResourceCategoryAssignment(categoryId,namespaceId);

		for (ResourceCategoryAssignment bulidingCategory: bulidingCategorys) {
			communityProvider.deleteResourceCategoryAssignmentById(bulidingCategory.getId());
		}

		List<ServiceModuleAssignment> smas = serviceModuleProvider.listServiceModuleAssignmentsByTargetIdAndOwnerId(EntityType.RESOURCE_CATEGORY.getCode(), categoryId, null, null, null);

		for (Long buildingId: buildingIds) {
			ResourceCategoryAssignment  projectAssignment = communityProvider.findResourceCategoryAssignment(buildingId, EntityType.BUILDING.getCode(), namespaceId);
			if(null != projectAssignment){
				communityProvider.deleteResourceCategoryAssignmentById(projectAssignment.getId());
			}
			projectAssignment = new ResourceCategoryAssignment();
			projectAssignment.setNamespaceId(namespaceId);
			projectAssignment.setCreatorUid(UserContext.current().getUser().getId());
			projectAssignment.setResourceCategryId(categoryId);
			projectAssignment.setResourceId(buildingId);
			projectAssignment.setResourceType(EntityType.BUILDING.getCode());
			communityProvider.createResourceCategoryAssignment(projectAssignment);

//			for (ServiceModuleAssignment sma: smas) {
//				rolePrivilegeService.assignmentPrivileges(projectAssignment.getResourceType(),projectAssignment.getResourceId(), sma.getTargetType(),sma.getTargetId(),"M" + sma.getModuleId() + "." + sma.getOwnerType() + sma.getOwnerId(), sma.getModuleId(),ServiceModulePrivilegeType.SUPER);
//			}

		}

	}

	@Override
	public void deleteChildProject(DeleteChildProjectCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		this.dbProvider.execute((TransactionStatus status) -> {
			communityProvider.deleteResourceCategoryById(cmd.getId());
			List<ResourceCategoryAssignment> bulidingCategorys = communityProvider.listResourceCategoryAssignment(cmd.getId(), namespaceId);
			for (ResourceCategoryAssignment bulidingCategory : bulidingCategorys) {
				communityProvider.deleteResourceCategoryAssignmentById(bulidingCategory.getId());
			}

			List<ServiceModuleAssignment> moduleAssignments = serviceModuleProvider.listResourceAssignmentGroupByTargets(EntityType.RESOURCE_CATEGORY.getCode(), cmd.getId(), null);
			for (ServiceModuleAssignment moduleAssignment: moduleAssignments) {
				AclRoleDescriptor aclRoleDescriptor = new AclRoleDescriptor(moduleAssignment.getTargetType(), moduleAssignment.getTargetId());
				List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.RESOURCE_CATEGORY.getCode(), cmd.getId(), aclRoleDescriptor);
				for (Acl acl: acls) {
					aclProvider.deleteAcl(acl.getId());
				}
			}
			return null;
		});
	}

	@Override
	public List<ProjectDTO> getTreeProjectCategories(GetTreeProjectCategoriesCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
		List<ProjectDTO> projects = new ArrayList<>();
		for (Community community: communities) {
			ProjectDTO project = new ProjectDTO();
			project.setProjectId(community.getId());
			project.setProjectType(EntityType.COMMUNITY.getCode());
			project.setProjectName(community.getName());
			projects.add(project);
		}
		return rolePrivilegeService.getTreeProjectCategories(namespaceId, projects);
	}

	@Override
	public void updateBuildingOrder(UpdateBuildingOrderCommand cmd) {
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		if (null == cmd.getExchangeId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid exchangeId parameter in the command");
		}
		Building building = communityProvider.findBuildingById(cmd.getId());
		Building exchangeBuilding = communityProvider.findBuildingById(cmd.getExchangeId());

		if (null == building) {
			LOGGER.error("Building not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Building not found");
		}
		if (null == exchangeBuilding) {
			LOGGER.error("Building not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Building not found");
		}

		Long order = building.getDefaultOrder();
		Long exchangeOrder = exchangeBuilding.getDefaultOrder();

		dbProvider.execute((TransactionStatus status) -> {
			building.setDefaultOrder(exchangeOrder);
			exchangeBuilding.setDefaultOrder(order);
			communityProvider.updateBuilding(building);
			communityProvider.updateBuilding(exchangeBuilding);
			return null;
		});
	}
}
