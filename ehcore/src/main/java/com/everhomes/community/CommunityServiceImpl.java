// @formatter:off
package com.everhomes.community;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.hibernate.mapping.Collection;
import org.hibernate.validator.internal.xml.GroupsType;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationOwners;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.BuildingServiceErrorCode;
import com.everhomes.rest.community.BuildingStatus;
import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.rest.community.CommunityNotificationTemplateCode;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.GetBuildingCommand;
import com.everhomes.rest.community.GetCommunitiesByIdsCommand;
import com.everhomes.rest.community.GetCommunitiesByNameAndCityIdCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.community.GetCommunityByUuidCommand;
import com.everhomes.rest.community.GetNearbyCommunitiesByIdCommand;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.community.ListCommunitesByStatusCommand;
import com.everhomes.rest.community.ListCommunitesByStatusCommandResponse;
import com.everhomes.rest.community.ListCommunitiesByKeywordCommandResponse;
import com.everhomes.rest.community.UpdateCommunityRequestStatusCommand;
import com.everhomes.rest.community.admin.ApproveCommunityAdminCommand;
import com.everhomes.rest.community.admin.CommunityAuthUserAddressCommand;
import com.everhomes.rest.community.admin.CommunityAuthUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityManagerDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUsersCommand;
import com.everhomes.rest.community.admin.CreateCommunityCommand;
import com.everhomes.rest.community.admin.CreateCommunityResponse;
import com.everhomes.rest.community.admin.DeleteBuildingAdminCommand;
import com.everhomes.rest.community.admin.ImportCommunityCommand;
import com.everhomes.rest.community.admin.ListBuildingsByStatusCommandResponse;
import com.everhomes.rest.community.admin.ListCommunityByNamespaceIdCommand;
import com.everhomes.rest.community.admin.ListCommunityByNamespaceIdResponse;
import com.everhomes.rest.community.admin.ListCommunityManagersAdminCommand;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.community.admin.ListComunitiesByKeywordAdminCommand;
import com.everhomes.rest.community.admin.ListUserCommunitiesCommand;
import com.everhomes.rest.community.admin.QryCommunityUserAddressByUserIdCommand;
import com.everhomes.rest.community.admin.RejectCommunityAdminCommand;
import com.everhomes.rest.community.admin.UpdateBuildingAdminCommand;
import com.everhomes.rest.community.admin.UpdateCommunityAdminCommand;
import com.everhomes.rest.community.admin.UserCommunityDTO;
import com.everhomes.rest.community.admin.VerifyBuildingAdminCommand;
import com.everhomes.rest.community.admin.VerifyBuildingNameAdminCommand;
import com.everhomes.rest.community.admin.listBuildingsByStatusCommand;
import com.everhomes.rest.enterprise.EnterpriseContactStatus;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.group.GroupMemberDTO;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.QuestionMetaObject;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.region.RegionServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.mysql.jdbc.StringUtils;


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
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private EnterpriseContactProvider enterpriseContactProvider;
	
	@Autowired
	private GroupProvider groupProvider;
	
	@Autowired
	private EnterpriseProvider enterpriseProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;

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
		community.setStatus(CommunityAdminStatus.CONFIRMING.getCode());
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
        			dto.setContact(member.getContactToken());
        		}
        	}
        	
        	populateBuildingDTO(dto);
        	
        	return dto;
        }).collect(Collectors.toList());
        
        
        return new ListBuildingCommandResponse(nextPageAnchor, dtoList);
	}

    private void populateBuildingDTO( BuildingDTO building) {
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
		
		Building building = new Building();
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
		if(!StringUtils.isNullOrEmpty(cmd.getGeoString())){
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
		if(cmd.getId() == null) {
			
			LOGGER.info("add building");
			this.communityProvider.createBuilding(userId, building);
		} else {
			LOGGER.info("update building");
			building.setId(cmd.getId());
			Building b = this.communityProvider.findBuildingById(cmd.getId());
			building.setCreatorUid(b.getCreatorUid());
			building.setCreateTime(b.getCreateTime());
			building.setNamespaceId(b.getNamespaceId());
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
		Long communityId = cmd.getCommunityId();
		
		List<Group> groups = groupProvider.listGroupByCommunityId(communityId, (loc, query) -> {
            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });
		
		List<Long> groupIds = new ArrayList<Long>(); 
		for (Group group : groups) {
			groupIds.add(group.getId());
		}
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds,locator,pageSize,(loc, query) -> {
			Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
			c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(cmd.getMemberStatus()));
            query.addConditions(c);
            if(null != locator.getAnchor())
            	query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.lt(locator.getAnchor()));
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID.desc());
            return query;
        });
		
		
		List<GroupMemberDTO> dtos = groupMembers.stream().map(r->{
			GroupMemberDTO dto = ConvertHelper.convert(r, GroupMemberDTO.class);
			Group group = groupProvider.findGroupById(dto.getGroupId());
			if(null != group){
				Address address = addressProvider.findAddressById(group.getFamilyAddressId());
				if(null != address){
					dto.setAddressId(address.getId());
					dto.setApartmentName(address.getApartmentName());
					dto.setBuildingName(address.getBuildingName());
				}
			}
			
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(dto.getMemberId(), IdentifierType.MOBILE.getCode());
			if(null != userIdentifier){
				dto.setCellPhone(userIdentifier.getIdentifierToken());
				User user = userProvider.findUserById(userIdentifier.getOwnerUid());
				if(null != user){
					dto.setInviterNickName(user.getNickName());
				}
			}
			return dto;
		}).collect(Collectors.toList());
		
		CommunityAuthUserAddressResponse res = new CommunityAuthUserAddressResponse();
		res.setDtos(dtos);
		res.setNextPageAnchor(locator.getAnchor());
		
		return res;
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
		
		if(!StringUtils.isNullOrEmpty(cmd.getKeywords())){
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
		
		
		for (GroupMember member : groupMembers) {
			User user = userProvider.findUserById(member.getMemberId());
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(member.getMemberId(), IdentifierType.MOBILE.getCode());
			if(null != user){
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
		List<OrganizationOwners> owners = organizationProvider.listOrganizationOwnerByCommunityId(cmd.getCommunityId(),locator, cmd.getPageSize(),(loc, query) -> {
			if(org.springframework.util.StringUtils.isEmpty(cmd.getKeywords())){
				Condition cond = Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.like(cmd.getKeywords() + "%");
				cond = cond.or(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(cmd.getKeywords()));
				query.addConditions(cond);
			}
            return query;
        });
		
		List<CommunityUserAddressDTO> dtos = new ArrayList<CommunityUserAddressDTO>();
		for (OrganizationOwners organizationOwners : owners) {
			CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(organizationOwners.getNamespaceId(), organizationOwners.getContactToken());
			dto.setIsAuth(2);
			if(null != userIdentifier){
				dto.setUserId(userIdentifier.getOwnerUid());
				dto.setIsAuth(1);
			}
			
			dto.setUserName(organizationOwners.getContactName());
			dto.setNikeName(organizationOwners.getContactName());
			dto.setPhone(organizationOwners.getContactToken());
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
			List<OrganizationOwners> owners = organizationProvider.findOrganizationOwnerByTokenOrNamespaceId(cmd.getContactToken(), namespaceId);
			List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();
			for (OrganizationOwners organizationOwners : owners) {
				Address address = addressProvider.findAddressById(organizationOwners.getAddressId());
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
					address.setMemberStatus(userGroup.getMemberStatus());
					if(null != address)
						addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
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
			for (OrganizationMember member : members) {
				OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(member.getOrganizationId());
				if(null == detail){
					detail = new OrganizationDetail();
					Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());
					if(null != organization){
						detail.setDisplayName(organization.getName());
						detail.setOrganizationId(organization.getId());
					}
				}
				
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
				
				orgDtos.add(detailDto);
			}
			
		}
		
		if(null != user){
			dto.setUserId(user.getId());
			dto.setUserName(user.getNickName());
			dto.setGender(user.getGender());
			dto.setPhone(null != userIdentifier ? userIdentifier.getIdentifierToken() : null);
			dto.setApplyTime(user.getCreateTime());
			dto.setOrgDtos(orgDtos);
		}
		
		return dto;
	}
	
	private CommunityUserResponse listUserByOrganizationIdOrCommunityId(ListCommunityUsersCommand cmd){
		if(null == cmd.getOrganizationId() && null == cmd.getCommunityId()){
			LOGGER.error("organizationId and communityId All are empty");
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organizationId and communityId All are empty");
		}
		List<Long> communityIds = new ArrayList<Long>();
		CommunityUserResponse res = new CommunityUserResponse(); 
		List<Long> organizationIds = new ArrayList<Long>();
		if(null == cmd.getCommunityId() && null != cmd.getOrganizationId()){
			List<CommunityDTO> communityDTOs = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());
			for (CommunityDTO communityDTO : communityDTOs) {
				communityIds.add(communityDTO.getId());
			}
			organizationIds.add(cmd.getOrganizationId());
		}else{
			communityIds.add(cmd.getCommunityId());
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
		
		if(!StringUtils.isNullOrEmpty(cmd.getKeywords())){
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
	
	@Override
	public CommunityUserResponse listUserCommunities(
			ListCommunityUsersCommand cmd) {
		
		CommunityUserResponse res = new CommunityUserResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<CommunityUserDto> dtos = new ArrayList<CommunityUserDto>();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		//当是左邻域下的某个园区查询
		if(namespaceId == Namespace.DEFAULT_NAMESPACE){
			res = this.listUserByOrganizationIdOrCommunityId(cmd);
			return res;
		}
		List<User> users = null;
		
		int index = 100;
		
		if(cmd.getIsAuth() == 1){
			index = 10000;
		}
		
		users = userProvider.listUserByKeyword(cmd.getKeywords(), namespaceId, locator, index);
		
		if(null == users) 
			return res;
		
		for (User user : users) {
			CommunityUserDto dto = new CommunityUserDto();
			dto.setUserId(user.getId());
			dto.setUserName(user.getNickName());
			dto.setPhone(user.getIdentifierToken());
			List<OrganizationMember> members = organizationProvider.listOrganizationMembers(user.getId());
			dto.setApplyTime(user.getCreateTime());
			dto.setIsAuth(2);
			if(null != members){
				for (OrganizationMember member : members) {
					if(OrganizationMemberStatus.ACTIVE.getCode() == member.getStatus()){
						dto.setIsAuth(1);
						break;
					}
				}
//				if(null != m){
//					Organization org = organizationProvider.findOrganizationById(m.getOrganizationId());
//					if(null != org)
//						dto.setEnterpriseName(org.getName());
//					
//					List<OrganizationAddress> addresses = organizationProvider.findOrganizationAddressByOrganizationId(m.getOrganizationId());
//					if(null != addresses && addresses.size() > 0){
//						Address address = addressProvider.findAddressById(addresses.get(0).getAddressId());
//						dto.setAddressName(address.getAddress());
//						dto.setAddressId(address.getId());
//						dto.setBuildingName(address.getBuildingName());
//					}
//				}
			}
			
			if(0 != cmd.getIsAuth()){
				if(cmd.getIsAuth().equals(dto.getIsAuth())){
					dtos.add(dto);
				}
			}else{
				dtos.add(dto);
			}
		}
		
		res.setNextPageAnchor(null);
		if(dtos.size() > pageSize){
			dtos = dtos.subList(0, pageSize);
			res.setNextPageAnchor(dtos.get(pageSize-1).getApplyTime().getTime());
		}
		res.setUserCommunities(dtos);
		return res;
	}

	
	

	@Override
	public CountCommunityUserResponse countCommunityUsers(
			CountCommunityUsersCommand cmd) {
		
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
		
		/**
		 * 园区用户统计
		 */
		int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		
		int communityUserCount = userProvider.countUserByNamespaceId(namespaceId, null);
		
		List<Organization> orgs = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, null ,new CrossShardListingLocator(), 1000000);
		List<Long> orgIds  = new ArrayList<Long>();
		for (Organization organization : orgs) {
			orgIds.add(organization.getId());
		}
		List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(orgIds, OrganizationMemberStatus.ACTIVE);
		List<Long> userIds = new ArrayList<Long>();
		if(null != members){
			for (OrganizationMember member : members) {
				if(!userIds.contains(member.getTargetId())){
					userIds.add(member.getTargetId());
				}
			}
		}
		
		
		int authUserCount = userIds.size();
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
		List<CommunityGeoPointDTO> points = new ArrayList<>();
		dbProvider.execute(s->{
			Long provinceId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName()), 0L);  //创建省
			Long cityId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName(), cmd.getCityName()), provinceId);  //创建市
			Long areaId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName(), cmd.getCityName(), cmd.getAreaName()), cityId);  //创建区县
			
			cmd.setProvinceId(provinceId);
			cmd.setCityId(cityId);
			cmd.setAreaId(areaId);
			Community community = createCommunity(userId, cmd);
			
			CommunityGeoPoint point = createCommunityGeoPoint(community.getId(), cmd.getLatitude(), cmd.getLongitude());
			
			createNamespaceResource(namespaceId, community.getId());
			
			points.add(ConvertHelper.convert(point, CommunityGeoPointDTO.class));
			CommunityDTO cd = ConvertHelper.convert(community, CommunityDTO.class);
			cd.setGeoPointList(points);
			communities.add(cd);
			return true;
		});
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
		if (cmd.getNamespaceId() == null || StringUtils.isEmptyOrWhitespaceOnly(cmd.getName()) || cmd.getCommunityType() == null 
				|| StringUtils.isEmptyOrWhitespaceOnly(cmd.getProvinceName()) || StringUtils.isEmptyOrWhitespaceOnly(cmd.getCityName())
				|| StringUtils.isEmptyOrWhitespaceOnly(cmd.getAreaName()) || CommunityType.fromCode(cmd.getCommunityType()) == null) {
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
}
