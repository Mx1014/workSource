// @formatter:off
package com.everhomes.community;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.address.CommunityAdminStatus;
import com.everhomes.address.CommunityDTO;
import com.everhomes.app.AppConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.MetaObjectType;
import com.everhomes.messaging.QuestionMetaObject;
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

import freemarker.core.ReturnInstruction.Return;


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
    public void updateCommunity(UpdateCommunityCommand cmd) {
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
       community.setAddress(cmd.getAddress());
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
           if(cmd.getLatitude() != null && cmd.getLongitude() != null) {
               List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
               if(geoPoints == null || geoPoints.isEmpty()){
                   CommunityGeoPoint point = new CommunityGeoPoint();
                   point.setCommunityId(community.getId());
                   point.setDescription("central");
                   point.setLatitude(cmd.getLatitude());
                   point.setLongitude(cmd.getLongitude());
                   String geoHash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
                   point.setGeohash(geoHash);
                   this.communityProvider.createCommunityGeoPoint(point);
               }
               else{
                   CommunityGeoPoint point = geoPoints.get(0);
                   point.setLatitude(cmd.getLatitude());
                   point.setLongitude(cmd.getLongitude());
                   String geoHash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
                   point.setGeohash(geoHash);
                   this.communityProvider.updateCommunityGeoPoint(point);
               }
           }
           return null;
       });
    }
    @Override
    public void approveCommuniy(ApproveCommunityCommand cmd){
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
    public void rejectCommunity(RejectCommunityCommand cmd){
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

}
