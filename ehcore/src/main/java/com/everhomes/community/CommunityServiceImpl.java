// @formatter:off
package com.everhomes.community;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.hibernate.mapping.Collection;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.address.CommunityAdminStatus;
import com.everhomes.address.CommunityDTO;
import com.everhomes.app.AppConstants;
import com.everhomes.community.admin.ApproveCommunityAdminCommand;
import com.everhomes.community.admin.CommunityManagerDTO;
import com.everhomes.community.admin.DeleteBuildingAdminCommand;
import com.everhomes.community.admin.ListBuildingsByStatusCommandResponse;
import com.everhomes.community.admin.ListCommunityManagersAdminCommand;
import com.everhomes.community.admin.ListComunitiesByKeywordAdminCommand;
import com.everhomes.community.admin.ListUserCommunitiesCommand;
import com.everhomes.community.admin.RejectCommunityAdminCommand;
import com.everhomes.community.admin.UpdateBuildingAdminCommand;
import com.everhomes.community.admin.UpdateCommunityAdminCommand;
import com.everhomes.community.admin.UserCommunityDTO;
import com.everhomes.community.admin.VerifyBuildingAdminCommand;
import com.everhomes.community.admin.VerifyBuildingNameAdminCommand;
import com.everhomes.community.admin.listBuildingsByStatusCommand;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.AttachmentDescriptor;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.MetaObjectType;
import com.everhomes.messaging.QuestionMetaObject;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionServiceErrorCode;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;


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
	private OrganizationProvider organizationProvider;

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
		if(cmd.getGeoPointList() == null || cmd.getGeoPointList().size() <= 0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid geoPointList parameter");
		}

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
		community.setStatus(CommunityAdminStatus.CONFIRMING.getCode());
		community.setAreaName(area.getName());
		community.setCityName(city.getName());
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
		List<Community> communities = this.communityProvider.findCommunitiesByNameAndCityId(cmd.getName(),cmd.getCityId());
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
		List<CommunityDTO> result = this.communityProvider.findNearyByCommunityById(cmd.getId()).stream().map((r) ->{
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
		if(cmd.getKeyword() == null || cmd.getKeyword().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid keyword parameter");
		}
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
		List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, pageSize + 1,communityId);
		
		this.communityProvider.populateBuildingAttachments(buildings);
        
        Long nextPageAnchor = null;
        if(buildings.size() > pageSize) {
        	buildings.remove(buildings.size() - 1);
            nextPageAnchor = buildings.get(buildings.size() - 1).getId();
        }
        
        populateBuildings(buildings);
        
        List<BuildingDTO> dtoList = buildings.stream().map((r) -> {
          return ConvertHelper.convert(r, BuildingDTO.class);  
        }).collect(Collectors.toList());
        
        
        return new ListBuildingCommandResponse(nextPageAnchor, dtoList);
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
	        return ConvertHelper.convert(building, BuildingDTO.class);
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
             String avatarUrl = contentServerService.parserUri(creatorAvatar, EntityType.USER.getCode(), building.getCreatorUid());
             building.setCreatorAvatarUrl(avatarUrl);
         }
		 
	 }
	 
	 private void populateBuildingManagerInfo(Building building) {
		 
		 String managerNickName = building.getManagerNickName();
         String managerAvatar = building.getManagerAvatar();
         
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
             String avatarUrl = contentServerService.parserUri(managerAvatar, EntityType.USER.getCode(), building.getManagerUid());
             building.setManagerAvatarUrl(avatarUrl);
         }
	 }

	 private void populateBuildingOperatorInfo(Building building) {
	 
		 String operatorNickName = building.getOperateNickName();
         String operatorAvatar = building.getOperateAvatar();
         
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
             String avatarUrl = contentServerService.parserUri(operatorAvatar, EntityType.USER.getCode(), building.getOperatorUid());
             building.setOperateAvatarUrl(avatarUrl);
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
                  
					 String url = contentServerService.parserUri(contentUri, EntityType.BUILDING.getCode(), building.getId());
                    
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
		
		String[] geoString = cmd.getGeoString().split(",");
		double longitude = Double.valueOf(geoString[0]);
		double latitude = Double.valueOf(geoString[1]);
		Building building = new Building();
		building.setAddress(cmd.getAddress());
		building.setAliasName(cmd.getAliasName());
		building.setAreaSize(cmd.getAreaSize());
		building.setCommunityId(cmd.getCommunityId());
		building.setContact(cmd.getContact());
		building.setDescription(cmd.getDescription());
		building.setLatitude(latitude);
		building.setLongitude(longitude);
		building.setManagerUid(cmd.getManagerUid());
		building.setName(cmd.getName());
		building.setPosterUri(cmd.getPosterUri());
		building.setStatus(CommunityAdminStatus.CONFIRMING.getCode());
		String geohash = GeoHashUtils.encode(latitude, longitude);
		building.setGeohash(geohash);
		
		User user = UserContext.current().getUser();
		long userId = user.getId();
		if(cmd.getId() == null) {
			
			LOGGER.info("add building");
			this.communityProvider.createBuilding(userId, building);
		} else {
			LOGGER.info("update building");
			building.setId(cmd.getId());
			this.communityProvider.updateBuilding(building);
		}
		
		processBuildingAttachments(userId, cmd.getAttachments(), building);
		
		populateBuilding(building);
		
		BuildingDTO dto = ConvertHelper.convert(building, BuildingDTO.class);
		return dto;
		
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

}
