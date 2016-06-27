// @formatter:off
package com.everhomes.launchpad;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.business.Business;
import com.everhomes.business.BusinessProvider;
import com.everhomes.business.BusinessService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.business.BusinessTargetType;
import com.everhomes.rest.business.CancelFavoriteBusinessCommand;
import com.everhomes.rest.business.FavoriteBusinessDTO;
import com.everhomes.rest.business.FavoriteFlagType;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.launchpad.ApplyPolicy;
import com.everhomes.rest.launchpad.DeleteLaunchPadByIdCommand;
import com.everhomes.rest.launchpad.GetLaunchPadItemByIdCommand;
import com.everhomes.rest.launchpad.GetLaunchPadItemsByOrgCommand;
import com.everhomes.rest.launchpad.GetLaunchPadItemsCommand;
import com.everhomes.rest.launchpad.GetLaunchPadItemsCommandResponse;
import com.everhomes.rest.launchpad.GetLaunchPadLayoutByVersionCodeCommand;
import com.everhomes.rest.launchpad.GetLaunchPadLayoutCommand;
import com.everhomes.rest.launchpad.Item;
import com.everhomes.rest.launchpad.ItemDisplayFlag;
import com.everhomes.rest.launchpad.ItemKind;
import com.everhomes.rest.launchpad.ItemScope;
import com.everhomes.rest.launchpad.ItemTargetType;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.rest.launchpad.LaunchPadLayoutDTO;
import com.everhomes.rest.launchpad.LaunchPadLayoutStatus;
import com.everhomes.rest.launchpad.LaunchPadServiceErrorCode;
import com.everhomes.rest.launchpad.ListLaunchPadLayoutCommandResponse;
import com.everhomes.rest.launchpad.ScaleType;
import com.everhomes.rest.launchpad.UserDefinedLaunchPadCommand;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommand;
import com.everhomes.rest.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommandResponse;
import com.everhomes.rest.launchpad.admin.LaunchPadItemAdminDTO;
import com.everhomes.rest.launchpad.admin.ListLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.UpdateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.UpdateLaunchPadLayoutAdminCommand;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.organization.GetOrgDetailCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.ui.launchpad.CancelFavoriteBusinessBySceneCommand;
import com.everhomes.rest.ui.launchpad.FavoriteBusinessesBySceneCommand;
import com.everhomes.rest.ui.launchpad.GetLaunchPadItemsBySceneCommand;
import com.everhomes.rest.ui.launchpad.GetLaunchPadLayoutBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.scene.SceneService;
import com.everhomes.scene.SceneTypeInfo;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

@Component
public class LaunchPadServiceImpl implements LaunchPadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadServiceImpl.class);
	private static final String OFFICIAL_PHONE = "";
	//private static final String BUSINESS_HOME_URL = "business.home.url";
	private static final String BUSINESS_DETAIL_URL = "business.detail.url";
	//private static final String AUTHENTICATE_PREFIX_URL = "authenticate.prefix.url";
	private static final String PREFIX_URL = "prefix.url";
	private static final String BUSINESS_IMAGE_URL = "business.image.url";
	@Autowired
	private LaunchPadProvider launchPadProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private UserActivityProvider userActivityProvider;
	@Autowired
	private DbProvider dbProvider;
	@Autowired 
	private ContentServerService contentServerService;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private RegionProvider regionProvider;
	@Autowired
	private PropertyMgrService propertyMgrService;
	@Autowired
	private BusinessProvider businessProvider;
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;	 
	
	@Autowired 
	private FamilyProvider familyProvider;
	
	@Autowired
	private SceneService sceneService;

	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;
	
	@Override
	public GetLaunchPadItemsCommandResponse getLaunchPadItems(GetLaunchPadItemsCommand cmd, HttpServletRequest request){
		if(cmd.getItemLocation() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemLocation paramter,itemLocation is null");
		}
		if(cmd.getItemGroup() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemGroup paramter,itemGroup is null");
		}
//		Long communityId = cmd.getCommunityId();
//		Community community = null;
//		if(communityId != null) {
//		    community = communityProvider.findCommunityById(communityId);
//		}
//		if(cmd.getCommunityId() == null){
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid communityId paramter,communityId is null");
//		}
//
//		long communityId = cmd.getCommunityId();
//		Community community = communityProvider.findCommunityById(communityId);
//		if(community == null){
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
//		}

		long startTime = System.currentTimeMillis();
		GetLaunchPadItemsCommandResponse response = new GetLaunchPadItemsCommandResponse();
		List<LaunchPadItemDTO> result = null;
		//        if(cmd.getItemGroup().equals(ItemGroup.BIZS.getCode())){
		//            result = getBusinessItems(cmd,community);
		//        }else{
		//            result = getLaunchPadItems(cmd,community,request);
		//        }
		result = getLaunchPadItemsByCommunity(cmd, request);
		response.setLaunchPadItems(result);
		long endTime = System.currentTimeMillis();
		LOGGER.info("Query launch pad complete, cmd=" + cmd + ",esplse=" + (endTime - startTime));
		return response;

	}
	
   public GetLaunchPadItemsCommandResponse getLaunchPadItems(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request){
        if(cmd.getItemLocation() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid itemLocation paramter,itemLocation is null");
        }
        if(cmd.getItemGroup() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid itemGroup paramter,itemGroup is null");
        }
        
        long startTime = System.currentTimeMillis();
        GetLaunchPadItemsCommandResponse response = new GetLaunchPadItemsCommandResponse();
        List<LaunchPadItemDTO> result = null;
        //        if(cmd.getItemGroup().equals(ItemGroup.BIZS.getCode())){
        //            result = getBusinessItems(cmd,community);
        //        }else{
        //            result = getLaunchPadItems(cmd,community,request);
        //        }
        result = getLaunchPadItemsByOrg(cmd, request);
        response.setLaunchPadItems(result);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query launch pad complete, cmd=" + cmd + ",esplse=" + (endTime - startTime));
        return response;

    }
	
   // 场景需要同时支持小区和园区 by lqs 20160511
//	@Override
//	public GetLaunchPadItemsCommandResponse getLaunchPadItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request) {
//	    User user = UserContext.current().getUser();
//	    SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
//	    
//	    GetLaunchPadItemsCommand getCmd = new GetLaunchPadItemsCommand();
//	    getCmd.setItemGroup(cmd.getItemGroup());
//	    getCmd.setItemLocation(cmd.getItemLocation());
//	    getCmd.setNamespaceId(sceneToken.getNamespaceId());
//	    getCmd.setSceneType(sceneToken.getScene());
//	    
//	    Community community = null;
//	    GetLaunchPadItemsCommandResponse cmdResponse = null;
//        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
//        switch(entityType) {
//        case COMMUNITY_RESIDENTIAL:
//        case COMMUNITY_COMMERCIAL:
//        case COMMUNITY:
//            community = communityProvider.findCommunityById(sceneToken.getEntityId());
//            if(community != null) {
//                getCmd.setCommunityId(community.getId());
//            }
//            
//            cmdResponse = getLaunchPadItems(getCmd, request);
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
//            if(family != null) {
//                community = communityProvider.findCommunityById(family.getCommunityId());
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
//                }
//            }
//            if(community != null) {
//                getCmd.setCommunityId(community.getId());
//            }
//            
//            cmdResponse = getLaunchPadItems(getCmd, request);
//            break;
//        case ORGANIZATION:// 无小区ID
//            GetLaunchPadItemsByOrgCommand orgCmd = new GetLaunchPadItemsByOrgCommand();
//            orgCmd.setItemGroup(cmd.getItemGroup());
//            orgCmd.setItemLocation(cmd.getItemLocation());
//            orgCmd.setNamespaceId(sceneToken.getNamespaceId());
//            orgCmd.setSceneType(sceneToken.getScene());
//            orgCmd.setOrganizationId(sceneToken.getEntityId());
//            cmdResponse = getLaunchPadItems(orgCmd, request);
//            break;
//        default:
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
//            break;
//        }
//        
//        return cmdResponse;
//	}
   
   @Override
   public GetLaunchPadItemsCommandResponse getLaunchPadItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request) {
       User user = UserContext.current().getUser();
       SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
       
       GetLaunchPadItemsCommand getCmd = new GetLaunchPadItemsCommand();
       getCmd.setItemGroup(cmd.getItemGroup());
       getCmd.setItemLocation(cmd.getItemLocation());
       getCmd.setNamespaceId(sceneToken.getNamespaceId());
       
       SceneTypeInfo sceneInfo = sceneService.getBaseSceneTypeByName(sceneToken.getNamespaceId(), sceneToken.getScene());
       String baseScene = sceneToken.getScene();
       if(sceneInfo != null) {
           baseScene = sceneInfo.getName();
           if(LOGGER.isDebugEnabled()) {
               LOGGER.debug("Scene type is changed, sceneToken={}, newScene={}", sceneToken, sceneInfo.getName());
           }
       } else {
           LOGGER.error("Scene is not found, cmd={}, sceneToken={}", cmd, sceneToken);
       }
       getCmd.setSceneType(baseScene);
       
       Community community = null;
       GetLaunchPadItemsCommandResponse cmdResponse = null;
       SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
       switch(sceneType) {
       case DEFAULT:
       case PARK_TOURIST:
           community = communityProvider.findCommunityById(sceneToken.getEntityId());
           if(community != null) {
               getCmd.setCommunityId(community.getId());
           }
           
           cmdResponse = getLaunchPadItems(getCmd, request);
           break;
       case FAMILY:
           FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
           if(family != null) {
               community = communityProvider.findCommunityById(family.getCommunityId());
           } else {
               if(LOGGER.isWarnEnabled()) {
                   LOGGER.warn("Family not found, sceneToken=" + sceneToken);
               }
           }
           if(community != null) {
               getCmd.setCommunityId(community.getId());
           }
           
           cmdResponse = getLaunchPadItems(getCmd, request);
           break;
       case PM_ADMIN:// 无小区ID
       case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
       case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
           GetLaunchPadItemsByOrgCommand orgCmd = new GetLaunchPadItemsByOrgCommand();
           orgCmd.setItemGroup(cmd.getItemGroup());
           orgCmd.setItemLocation(cmd.getItemLocation());
           orgCmd.setNamespaceId(sceneToken.getNamespaceId());
           orgCmd.setSceneType(baseScene);
           orgCmd.setOrganizationId(sceneToken.getEntityId());
           cmdResponse = getLaunchPadItems(orgCmd, request);
           break;
       default:
           LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
           break;
       }
       
       return cmdResponse;
   }
	
	@SuppressWarnings("unchecked")
	private List<LaunchPadItemDTO> getBusinessItems(GetLaunchPadItemsCommand cmd,Community community) {
		User user = UserContext.current().getUser();
		long userId = user.getId();
		Integer namespaceId = (user.getNamespaceId() == null) ? 0 : user.getNamespaceId();
		// 对于老版本客户端，没有场景概念，此时它传过来的场景为null，但数据却已经有场景，需要根据小区类型来区分场景 by lqs 20160601
        // String sceneType = cmd.getCurrentSceneType();
        String sceneType = cmd.getSceneType();
        if(sceneType == null) {
            if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
                sceneType = SceneType.PARK_TOURIST.getCode();
            } else {
                sceneType = SceneType.DEFAULT.getCode();
            }
        }
		List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();

		//TODO get the businesses with the user create
		List<Business> businesses = this.businessProvider.findBusinessByCreatorId(userId);
		if(businesses != null && !businesses.isEmpty())
			businesses = businesses.stream().map(r -> {
				if(r.getCreatorUid().longValue() == userId)
					r.setDisplayName(r.getDisplayName() + "(商铺)");
				return r;
			}).collect(Collectors.toList());
		//get the business with the user favorite
		List<Long> bizIds = userActivityProvider.findFavorite(userId).stream()
				.filter(r -> r.getTargetType().equalsIgnoreCase("biz")).map(r->r.getTargetId()).collect(Collectors.toList());
		if(bizIds != null && !bizIds.isEmpty()){
			for(long bizId : bizIds){
				Business b = businessProvider.findBusinessById(bizId);
				if(b != null)
					businesses.add(b);
			}
		}

		if(businesses != null && !businesses.isEmpty()){
			int index = 1;
			//final String businessHomeUrl = configurationProvider.getValue(BUSINESS_HOME_URL, "");
			final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
			//final String authenticatePrefix = configurationProvider.getValue(AUTHENTICATE_PREFIX_URL, "");
			final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
			final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
			for(Business r : businesses){
				LaunchPadItemDTO dto = new LaunchPadItemDTO();
				dto.setIconUri(r.getLogoUri());
				dto.setIconUrl(processLogoUrl(r,userId,imageUrl));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(LaunchPadConstants.URL, processUrl(r, prefixUrl,businessDetailUrl));
				jsonObject.put(LaunchPadConstants.COMMUNITY_ID, community.getId());
				dto.setActionData(jsonObject.toJSONString());
				dto.setActionType(ActionType.BIZ_DETAILS.getCode());
				dto.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
				dto.setItemGroup(LaunchPadConstants.GROUP_BIZS);
				dto.setItemHeight(1);
				dto.setItemWidth(1);
				dto.setDefaultOrder(index ++);
				dto.setItemName(r.getName());
				dto.setItemLabel(r.getDisplayName());
				dto.setItemLocation(cmd.getItemLocation());
				dto.setScaleType(ScaleType.TAILOR.getCode());
				result.add(dto);
			}
		}

		List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.ALL.getCode(),0L,null);
		defaultItems.forEach(r ->{
			LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
			itemDTO.setIconUrl(parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId));
			result.add(itemDTO);
		});
		return result;

	}

	private String processLogoUrl(Business business, long userId,String imageUrl){
		if(business.getLogoUri() == null || business.getLogoUri().trim().equals(""))
			return null;
		if(business.getTargetType() != BusinessTargetType.ZUOLIN.getCode())
			return parserUri(business.getLogoUri(),EntityType.USER.getCode(),userId);
		return imageUrl.trim() + business.getLogoUri();
	}
	private String processUrl(Business business, String authenticatePrefix,String detailUrl){
		if(detailUrl.trim().equals(""))
			LOGGER.error("Buiness detail url is empty.");
		if(authenticatePrefix.trim().equals(""))
			LOGGER.error("Buiness authenticate prefix url is empty.");
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

	@SuppressWarnings("unchecked")
	private List<LaunchPadItemDTO> getLaunchPadItemsByCommunity(GetLaunchPadItemsCommand cmd, HttpServletRequest request){
		User user = UserContext.current().getUser();
		long userId = user.getId();
        Integer namespaceId = (user.getNamespaceId() == null) ? 0 : user.getNamespaceId();
        //String sceneType = cmd.getCurrentSceneType();
		String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
		
		Long communityId = cmd.getCommunityId();
        Community community = null;
        if(communityId != null) {
            community = communityProvider.findCommunityById(communityId);
        }
        
        if(community == null) {
        	community = new Community();
        	community.setId(0L);
        	community.setCityId(0L);
        }
        // 对于老版本客户端，没有场景概念，此时它传过来的场景为null，但数据却已经有场景，需要根据小区类型来区分场景 by lqs 20160601
        String sceneType = cmd.getSceneType();
        if(sceneType == null) {
            if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
                sceneType = SceneType.PARK_TOURIST.getCode();
            } else {
                sceneType = SceneType.DEFAULT.getCode();
            }
        }
		
		List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
		List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.ALL.getCode(),0L,null);
		List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.CITY.getCode(),community.getCityId(),null);
		List<LaunchPadItem> communityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.COMMUNITY.getCode(),community.getId(),null);
		
		List<LaunchPadItem> userItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(), cmd.getItemGroup(), ScopeType.USER.getCode(), userId, null);
		List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();

		if(defaultItems == null || defaultItems.isEmpty()){
			defaultItems = cityItems;
			if(defaultItems == null || defaultItems.isEmpty()){
				defaultItems = communityItems;
			}
		}
		if(defaultItems != null && !defaultItems.isEmpty()){
			allItems = defaultItems;
			if(cityItems != null && !cityItems.isEmpty()){
				allItems = overrideOrRevertItems(allItems,cityItems);
			}
			if(communityItems != null && !communityItems.isEmpty())
				allItems = overrideOrRevertItems(allItems, communityItems);
			if(userItems != null && !userItems.isEmpty())
				allItems = overrideOrRevertItems(allItems, userItems);
		}
		if(allItems!=null&&!allItems.isEmpty())
			allItems = allItems.stream().filter(r -> r.getDisplayFlag()==ItemDisplayFlag.DISPLAY.getCode()).collect(Collectors.toList());

		// 把对item的处理独立成一个新的方法，供公共调用 by lqs 20160324
//		try{ 
//			List<LaunchPadItemDTO> distinctDto = new ArrayList<LaunchPadItemDTO>();
//			final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
//			final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
//			final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
//			allItems.forEach(r ->{
//				LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
//				itemDTO.setActionData(parserJson(token,userId, community.getId(), r,request));
//				itemDTO.setScaleType(ScaleType.TAILOR.getCode());
//				if(r.getTargetType() != null && r.getTargetType().equalsIgnoreCase(ItemTargetType.BIZ.getCode())){
//					Business b = this.businessProvider.findBusinessById(r.getTargetId());
//					if(b != null){
//						itemDTO.setIconUrl(processLogoUrl(b,userId,imageUrl));
//						JSONObject jsonObject = new JSONObject();
//						jsonObject.put(LaunchPadConstants.URL, processUrl(b, prefixUrl,businessDetailUrl));
//						jsonObject.put(LaunchPadConstants.COMMUNITY_ID, community.getId());
//						itemDTO.setActionData(jsonObject.toJSONString());
//						if(b.getCreatorUid().longValue() == userId)
//							itemDTO.setItemLabel(b.getName() == null ? itemDTO.getItemLabel() : b.getName()+"(店铺)");
//						else
//							itemDTO.setItemLabel(b.getName() == null ? itemDTO.getItemLabel() : b.getName());
//					}
//				}else{
//					itemDTO.setIconUrl(parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId));
//				}
//				
//				distinctDto.add(itemDTO);
//			});
//			if(distinctDto != null && !distinctDto.isEmpty()){
//				distinctDto.forEach(r ->{
//					if(!result.contains(r)){
//						result.add(r);
//					}
//				});
//			}
//			if(result != null && !result.isEmpty())
//				sortLaunchPadItems(result);
//
//		}catch(Exception e){
//			LOGGER.error("Process item aciton data is error.",e);
//			return null;
//		}		
//		return result;
		
		return processLaunchPadItems(token, userId, community.getId(), allItems, request);
	}
	
    private List<LaunchPadItemDTO> getLaunchPadItemsByOrg(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request){
        User user = UserContext.current().getUser();
        long userId = user.getId();
        Integer namespaceId = (user.getNamespaceId() == null) ? 0 : user.getNamespaceId();
        String sceneType = cmd.getCurrentSceneType();
        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        
        List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.ALL.getCode(),0L,null);
        List<LaunchPadItem> orgItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.ORGANIZATION.getCode(),cmd.getOrganizationId(),null);
        List<LaunchPadItem> userItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(), cmd.getItemGroup(), ScopeType.USER.getCode(), userId, null);
        List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();

        if(defaultItems == null || defaultItems.isEmpty()){
            defaultItems = orgItems;
        }
        if(defaultItems != null && !defaultItems.isEmpty()){
            allItems = defaultItems;
            if(orgItems != null && !orgItems.isEmpty())
                allItems = overrideOrRevertItems(allItems, orgItems);
            if(userItems != null && !userItems.isEmpty())
                allItems = overrideOrRevertItems(allItems, userItems);
        }
        if(allItems!=null&&!allItems.isEmpty())
            allItems = allItems.stream().filter(r -> r.getDisplayFlag()==ItemDisplayFlag.DISPLAY.getCode()).collect(Collectors.toList());
        
        // 产品规则每个公司都有一个办公地点所在的园区/小区，故在公司场景也可以拿到小区ID
        // 把这个小区ID补回来，是为了物业相关的服务（报修、投诉建议等）在发帖时可以由服务器提供visible_region_type/id  by lqs 20160617
        Long communityId = null;
        OrganizationDTO org = organizationService.getOrganizationById(cmd.getOrganizationId());
        if(org != null) {
            communityId = org.getCommunityId();
        } else {
            LOGGER.error("Organization id not found, userId={}, cmd={}", userId, cmd);
        }
        
        return processLaunchPadItems(token, userId, communityId, allItems, request);
    }
	
	private List<LaunchPadItemDTO> processLaunchPadItems(String token, Long userId, Long communityId, List<LaunchPadItem> allItems, HttpServletRequest request) {
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
        
	    try{ 
            List<LaunchPadItemDTO> distinctDto = new ArrayList<LaunchPadItemDTO>();
            final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
            final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
            final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
            allItems.forEach(r ->{
                LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
                itemDTO.setActionData(parserJson(token, userId, communityId, r, request));
                itemDTO.setScaleType(ScaleType.TAILOR.getCode());
                if(r.getTargetType() != null && r.getTargetType().equalsIgnoreCase(ItemTargetType.BIZ.getCode())){
                    Business b = this.businessProvider.findBusinessById(r.getTargetId());
                    if(b != null){
                        itemDTO.setIconUrl(processLogoUrl(b,userId,imageUrl));
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(LaunchPadConstants.URL, processUrl(b, prefixUrl,businessDetailUrl));
                        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, communityId);
                        itemDTO.setActionData(jsonObject.toJSONString());
                        if(b.getCreatorUid().longValue() == userId)
                            itemDTO.setItemLabel(b.getName() == null ? itemDTO.getItemLabel() : b.getName()+"(店铺)");
                        else
                            itemDTO.setItemLabel(b.getName() == null ? itemDTO.getItemLabel() : b.getName());
                    }
                }else{
                    String url = parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId);
                    itemDTO.setIconUrl(url);
//                    if(LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("Parse uri while processing launchpad items, item=" + itemDTO);
//                    }
                }
                
                distinctDto.add(itemDTO);
            });
            if(distinctDto != null && !distinctDto.isEmpty()){
                distinctDto.forEach(r ->{
                    if(!result.contains(r)){
                        result.add(r);
                    }
                });
            }
            if(result != null && !result.isEmpty())
                sortLaunchPadItems(result);

        }catch(Exception e){
            LOGGER.error("Process item aciton data is error.",e);
            return null;
        }
        return result;
	}

	@SuppressWarnings("unchecked")
	private String parserJson(String userToken,long userId, Long communityId,LaunchPadItem launchPadItem, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try{
			//use handler instead of??
			if(launchPadItem.getActionData() != null && !launchPadItem.getActionData().trim().equals("")){
				jsonObject = (JSONObject) JSONValue.parse(launchPadItem.getActionData());
				//处理phoneCall actionData
				if(launchPadItem.getActionType() == ActionType.PHONE_CALL.getCode() &&
						launchPadItem.getItemGroup().equals(LaunchPadConstants.GROUP_CALLPHONES)){ 
					jsonObject = processPhoneCall(communityId, jsonObject, launchPadItem);
				}

				else if(launchPadItem.getActionType() == ActionType.THIRDPART_URL.getCode()){
					Community community = null;
					if(communityId != null) {
					    community = communityProvider.findCommunityById(communityId);
					}
					long cityId = community == null ? 0 : community.getCityId();
					String url = (String) jsonObject.get(LaunchPadConstants.URL);
					//处理收集地址url
					if(url.indexOf(LaunchPadConstants.USER_REQUEST_LIST) != -1){
						url = url + "&userId=" + userId + "&cityId=" + cityId;
					}
					jsonObject.put(LaunchPadConstants.URL, url);
				}
				else if(launchPadItem.getActionType() == ActionType.LAUNCH_APP.getCode()){
					jsonObject = processLaunchApp(jsonObject,request);
				}
				else if(launchPadItem.getActionType() == ActionType.POST_NEW.getCode()){
					jsonObject = processPostNew(communityId,jsonObject,launchPadItem);
				}
				else if(launchPadItem.getActionType() == ActionType.POST_BY_CATEGORY.getCode()){
					jsonObject = processPostByCategory(communityId,jsonObject,launchPadItem);
				}
			}
			//            if(jsonObject != null)
			//                jsonObject.put(LaunchPadConstants.COMMUNITY_ID, communityId);
		}catch(Exception e){
			LOGGER.error("Parser json is error,communityId=" + communityId, e);
		}

		return jsonObject.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private JSONObject processPhoneCall(Long communityId, JSONObject actionDataJson, LaunchPadItem launchPadItem){
		if(actionDataJson.get(LaunchPadConstants.CALLPHONES) != null ){

			String location = launchPadItem.getItemLocation();
			if(location.lastIndexOf("/") == -1)
				return actionDataJson;
			String organizationType = location.substring(location.lastIndexOf("/") + 1).toUpperCase();
			ListPropCommunityContactCommand cmd = new ListPropCommunityContactCommand();
			cmd.setCommunityId(communityId);
			cmd.setOrganizationType(organizationType);
			List<String> contacts = new ArrayList<String>();
			List<PropCommunityContactDTO> dtos = propertyMgrService.listPropertyCommunityContacts(cmd);
			if(dtos != null && !dtos.isEmpty()){
				dtos.forEach(r ->{
					if(r.getContactType() == IdentifierType.MOBILE.getCode()){
						contacts.add(r.getContactToken());
					}
				});
			}
			if(contacts.isEmpty())
				contacts.add(OFFICIAL_PHONE);
			actionDataJson.put(LaunchPadConstants.CALLPHONES,contacts);
		}
		return actionDataJson;
	}

	private JSONObject processLaunchApp(JSONObject actionDataJson, HttpServletRequest request){
		//区分访问平台，根据相应平台返回相应的数据
		String header = request.getHeader("user-agent");
		//"androidEmbedded_json":{"package":"mqq:open","download":"www.xx.com"}
		if(header.contains("Android")){
			JSONObject androidJson =  (JSONObject) actionDataJson.get(LaunchPadConstants.ANDROID_EMBEDDED);
			if(androidJson != null)
				return  androidJson;
		}else if(header.contains("iOS")){
			JSONObject iosJson =  (JSONObject)actionDataJson.get(LaunchPadConstants.IOS_EMBEDDED);
			if(iosJson != null)
				return iosJson;
		}
		return actionDataJson;
	}


	@SuppressWarnings("unchecked")
	private JSONObject processPostNew(Long communityId, JSONObject actionDataJson, LaunchPadItem launchPadItem) {
		actionDataJson.put(LaunchPadConstants.DISPLAY_NAME, launchPadItem.getItemLabel());
		String targetEntityTag = (String) actionDataJson.get(LaunchPadConstants.TARGET_TAG);
		targetEntityTag = targetEntityTag == null || targetEntityTag.trim().equals("") ? PostEntityTag.USER.getCode() : targetEntityTag;

		String tag = launchPadItem.getTag();
		if(tag == null || tag.trim().equals("")){
			actionDataJson.put(LaunchPadConstants.CREATOR_TAG, PostEntityTag.USER.getCode());
		}else{
			actionDataJson.put(LaunchPadConstants.CREATOR_TAG, tag);
		}

		Byte visibleRegionType = null;
		Object regionType = actionDataJson.get(LaunchPadConstants.VISIBLE_REGION_TYPE);
		if(regionType != null) {
		    visibleRegionType = Byte.valueOf(regionType.toString());
		}

		//给GANC（居委）或GAPS（公安）发帖，visibleRegionType填片区、visibleRegionId填居委或公安所管理的片区ID
		if(VisibleRegionType.fromCode(visibleRegionType) == VisibleRegionType.REGION){
			GetOrgDetailCommand cmd = new GetOrgDetailCommand();
			cmd.setCommunityId(communityId);
			cmd.setOrganizationType(targetEntityTag);
			OrganizationDTO organization = organizationService.getOrganizationByComunityidAndOrgType(cmd);
			if(organization == null){
				LOGGER.error("Organization is not exists,communityId=" + communityId+",targetEntityTag="+targetEntityTag);
				return actionDataJson;
			}
			actionDataJson.put(LaunchPadConstants.VISIBLE_REGION_TYPE, VisibleRegionType.REGION.getCode());
			actionDataJson.put(LaunchPadConstants.VISIBLE_REGIONID,organization.getId());
		}else{
			actionDataJson.put(LaunchPadConstants.VISIBLE_REGION_TYPE, VisibleRegionType.COMMUNITY.getCode());
			actionDataJson.put(LaunchPadConstants.VISIBLE_REGIONID,communityId);
		}
		return actionDataJson;
	}

	@SuppressWarnings("unchecked")
	private JSONObject processPostByCategory(Long communityId, JSONObject actionDataJson, LaunchPadItem launchPadItem) {
		actionDataJson.put(LaunchPadConstants.DISPLAY_NAME, launchPadItem.getItemLabel());
		String tag = launchPadItem.getTag();
		if(tag == null || tag.trim().equals("")){
			actionDataJson.put(LaunchPadConstants.COMMUNITY_ID, communityId);
			return actionDataJson;
		}

		GetOrgDetailCommand cmd = new GetOrgDetailCommand();
		cmd.setCommunityId(communityId);
		cmd.setOrganizationType(tag);
		OrganizationDTO organization = organizationService.getOrganizationByComunityidAndOrgType(cmd);
		if(organization == null){
			LOGGER.error("Organization is not exists,communityId=" + communityId+",tag="+tag);
			return actionDataJson;
		}
		actionDataJson.put(LaunchPadConstants.ORGANIZATION_ID, organization.getId());
		return actionDataJson;
	}

	private void sortLaunchPadItems(List<LaunchPadItemDTO> result){

		Collections.sort(result, new Comparator<LaunchPadItemDTO>(){
			@Override
			public int compare(LaunchPadItemDTO o1, LaunchPadItemDTO o2){
				return o1.getId().intValue() - o2.getId().intValue();
			}
		});
		//优先根据defaultOrder排序显示
		Collections.sort(result, new Comparator<LaunchPadItemDTO>(){
			@Override
			public int compare(LaunchPadItemDTO o1, LaunchPadItemDTO o2){
				return o1.getDefaultOrder().intValue() - o2.getDefaultOrder().intValue();
			}
		});
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

	/**
	 * 1、applyPolicy=1(覆盖)，小范围覆盖大范围，
	 * 用户自定义的，直接根据itemId比较，系统配置的覆盖，根据itemName进行比较
	 * 2、applyPolicy=2(恢复)，直接忽略即可
	 * @param defalultItems
	 * @param overrideItems
	 * @return
	 */
	private List<LaunchPadItem> overrideOrRevertItems(List<LaunchPadItem> defalultItems, List<LaunchPadItem> overrideItems) {

		if(defalultItems == null || overrideItems == null) return null;
		boolean flag = false;
		List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();
		for(LaunchPadItem d : defalultItems){
			for(LaunchPadItem o : overrideItems){
				//非覆盖
				if(o.getApplyPolicy() == ApplyPolicy.DEFAULT.getCode() && !allItems.contains(o)){
					if(o.getDisplayFlag()==ItemDisplayFlag.DISPLAY.getCode())
						allItems.add(o);
				}
				else if(!allItems.contains(o)&&o.getApplyPolicy()== ApplyPolicy.OVERRIDE.getCode()&&d.getItemLabel().equals(o.getItemLabel()) && d.getItemGroup().equals(o.getItemGroup())){
					if(o.getDisplayFlag() == ItemDisplayFlag.DISPLAY.getCode())
						allItems.add(o);
					flag = true;
					break;
				}
			}
			if(!flag&&d.getDisplayFlag()==ItemDisplayFlag.DISPLAY.getCode())
				allItems.add(d);
			flag = false;
		}
		return allItems;
	}

	@SuppressWarnings("unchecked")
	public List<LaunchPadItem> getUserItems(long userId){
		List<LaunchPadItem> userItems = new ArrayList<LaunchPadItem>();

		//List<UserProfile> userProfiles = this.userActivityProvider.findProfileByUid(userId);
		UserProfile profile = this.userActivityProvider.findUserProfileBySpecialKey(userId, UserProfileContstant.LaunchPadName);
		if(profile == null) return userItems;
		if(profile.getItemKind() == ItemKind.JSON.getCode()){
			String jsonString = profile.getItemValue();
			JSONArray jsonArray = new JSONArray();
			jsonArray = (JSONArray) JSONValue.parse(jsonString);
			jsonArray.forEach(r ->{
				userItems.add((LaunchPadItem) StringHelper.fromJsonString(r.toString(), LaunchPadItem.class));
			});

		}
		//        userProfiles.forEach((userProfile) ->{
		//            if(userProfile.getItemKind() == ItemKind.JSON.getCode()){
		//                String jsonString = userProfile.getItemValue();
		//                userItems.add((LaunchPadItem) StringHelper.fromJsonString(jsonString, LaunchPadItem.class));
		//            }
		//            
		//        });

		return userItems;
	}

	@Override
	public void createLaunchPadItem(CreateLaunchPadItemAdminCommand cmd){
		if(cmd.getItemScopes() == null || cmd.getItemScopes().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid item scopes paramter.");
		}
		if(cmd.getActionType() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid actionType paramter.");
		}
		if(cmd.getItemGroup() == null || cmd.getItemGroup().trim().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemGroup paramter.");
		}
		if(cmd.getItemLabel() == null || cmd.getItemLabel().trim().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemLabel paramter.");
		}
		if(cmd.getItemLocation() == null || cmd.getItemLocation().trim().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemLocation paramter.");
		}
		List<ItemScope> itemScopes = cmd.getItemScopes();
		LOGGER.info("Item scope size is " + itemScopes.size());
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		itemScopes.forEach((itemScope) ->{
			LaunchPadItem item = new LaunchPadItem();
			item.setAppId(cmd.getAppId() == null ? 0 : cmd.getAppId());
			item.setItemLabel(cmd.getItemLabel());
			item.setItemName(StringUtils.upperCase(cmd.getItemName()));
			item.setActionType(cmd.getActionType());
			item.setActionData(cmd.getActionData());
			item.setItemGroup(cmd.getItemGroup());
			item.setItemLocation(cmd.getItemLocation());
			item.setIconUri(cmd.getIconUri());
			item.setItemWidth(cmd.getItemWidth() == null ? 1 : cmd.getItemWidth());
			item.setItemHeight(cmd.getItemHeight() == null ? 1 : cmd.getItemHeight());
			item.setNamespaceId(cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId());
			item.setScopeCode(itemScope.getScopeCode());
			item.setScopeId(itemScope.getScopeId());
			item.setDefaultOrder(itemScope.getDefaultOrder() == null ? 0 : itemScope.getDefaultOrder());
			item.setApplyPolicy(itemScope.getApplyPolicy());
			item.setDisplayFlag(cmd.getDisplayFlag() == null ? ItemDisplayFlag.DISPLAY.getCode() : cmd.getDisplayFlag());
			item.setDisplayLayout(cmd.getDisplayLayout());
			item.setBgcolor(cmd.getBgcolor() == null ? 0 : cmd.getBgcolor());
			item.setTag(cmd.getTag());
			items.add(item);
		});
		this.launchPadProvider.createLaunchPadItems(items);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void userDefinedLaunchPad(UserDefinedLaunchPadCommand cmd){
		if(cmd.getItems() == null || cmd.getItems().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid items paramter.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();
		if(cmd.getItems().isEmpty()){
			LOGGER.warn("Items is empty.userId=" + userId);
			return;
		}
		List<Item> items = cmd.getItems();
		this.dbProvider.execute((TransactionStatus status) -> {

			//delete before
			UserProfile profile = this.userActivityProvider.findUserProfileBySpecialKey(userId, UserProfileContstant.LaunchPadName);
			this.userActivityProvider.deleteProfile(profile);

			JSONArray array = new JSONArray();
			items.forEach((Item item) ->{
				Long id = item.getId();
				if (id == null) return ;
				LaunchPadItem launchPadItem = this.launchPadProvider.findLaunchPadItemById(id);
				if(launchPadItem == null){
					LOGGER.error("Item is not found,itemId=" + item.getId());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid items paramter.item is not found");
				}
				launchPadItem.setDefaultOrder(item.getOrderIndex());
				launchPadItem.setApplyPolicy(item.getApplyPolicy());
				launchPadItem.setScopeCode(ScopeType.USER.getCode());
				launchPadItem.setScopeId(userId);
				launchPadItem.setDisplayFlag(item.getDisplayFlag());
				array.add(launchPadItem);

			});
			UserProfile userProfile = new UserProfile();
			userProfile.setItemKind(ItemKind.JSON.getCode());
			userProfile.setOwnerId(userId);
			userProfile.setItemName(UserProfileContstant.LaunchPadName);
			userProfile.setItemValue(array.toJSONString());

			this.userActivityProvider.addUserProfile(userProfile);
			return null;
		});

	}


	@Override
	public void deleteLaunchPadItem(DeleteLaunchPadItemAdminCommand cmd) {
		if(cmd.getItemIds() == null || cmd.getItemIds().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemIds paramter.itemId is null");
		}
		this.dbProvider.execute((TransactionStatus status) -> {
			List<Long> itemIds = cmd.getItemIds();
			itemIds.forEach((itemId) ->{
				this.launchPadProvider.deleteLaunchPadItem(itemId);
			});
			return null; 
		});

	}

	@Override
	public void createLaunchPadLayout(CreateLaunchPadLayoutAdminCommand cmd){
		if(cmd.getLayoutJson() == null || cmd.getLayoutJson().trim().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid layoutJson paramter.layoutJson is null");
		}
		if(cmd.getName() == null || cmd.getName().trim().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name paramter.name is null");
		}
		LaunchPadLayout layout = new LaunchPadLayout();
		layout.setLayoutJson(cmd.getLayoutJson());
		layout.setName(cmd.getName());
		layout.setNamespaceId(cmd.getNamespaceId());
		layout.setStatus(cmd.getStatus());
		layout.setMinVersionCode(cmd.getMinVersionCode());
		layout.setVersionCode(cmd.getVersionCode());
		layout.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		this.launchPadProvider.createLaunchPadLayout(layout);
	}

	@Override
	public void deleteLaunchPadLayout(DeleteLaunchPadLayoutAdminCommand cmd){
		if(cmd.getIds() == null || cmd.getIds().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ids paramter.ids is null");
		}
		cmd.getIds().forEach(id ->{
			LaunchPadLayout layout = this.launchPadProvider.findLaunchPadLayoutById(id);
			if(layout == null){
				LOGGER.error("LaunchPad is not exits,id=" + id);
				return;
			}
			layout.setStatus(LaunchPadLayoutStatus.INACTIVE.getCode());
			this.launchPadProvider.updateLaunchPadLayout(layout);
		});
	}


	@Override
	public void updateLaunchPadLayout(UpdateLaunchPadLayoutAdminCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id paramter.id is null");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();
		LaunchPadLayout layout = this.launchPadProvider.findLaunchPadLayoutById(cmd.getId());
		if(layout == null){
			throw RuntimeErrorException.errorWith(LaunchPadServiceErrorCode.SCOPE, LaunchPadServiceErrorCode.ERROR_LAUNCHPAD_LAYOUT_NOT_EXISTS,
					"LaunchPad layout is not exists,id=" + cmd.getId() + ",userId=" + userId);
		}
		if(cmd.getNamespaceId() != null){
			layout.setNamespaceId(cmd.getNamespaceId());
		}
		if(cmd.getLayoutJson() != null && !cmd.getLayoutJson().trim().equals("")){
			layout.setLayoutJson(cmd.getLayoutJson());
		}
		if(cmd.getMinVersionCode() != null){
			layout.setMinVersionCode(cmd.getMinVersionCode());
		}
		if(cmd.getName() != null && !cmd.getName().trim().equals("")){
			layout.setName(cmd.getName());
		}
		if(cmd.getVersionCode() != null){
			layout.setVersionCode(cmd.getVersionCode());
		}
		if(cmd.getStatus() != null){
			layout.setStatus(cmd.getStatus());
		}
		this.launchPadProvider.updateLaunchPadLayout(layout);

	}

	@Override
	public LaunchPadLayoutDTO getLaunchPadLayout(GetLaunchPadLayoutCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id paramter.id is null");
		}
		LaunchPadLayout launchPadLayout = this.launchPadProvider.findLaunchPadLayoutById(cmd.getId());
		if(launchPadLayout == null){
			LOGGER.error("LaunchPad layout is not exits,id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(LaunchPadServiceErrorCode.SCOPE, LaunchPadServiceErrorCode.ERROR_LAUNCHPAD_LAYOUT_NOT_EXISTS,
					"LaunchPad layout is not exists,id=" + cmd.getId());
		}
		return ConvertHelper.convert(launchPadLayout, LaunchPadLayoutDTO.class);
	}
	@Override
	public LaunchPadLayoutDTO getLastLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd){
		if(cmd.getName() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name paramter.name is null");
		}
		if(cmd.getVersionCode() == null)
			cmd.setVersionCode(0L);
		List<LaunchPadLayoutDTO> results = getLaunchPadLayoutByVersionCode(cmd);
		if(results != null && !results.isEmpty()){
			LaunchPadLayoutDTO dto =  results.get(0);
			return dto;
		}
		return null;
	}
	
	@Override
    public LaunchPadLayoutDTO getLastLaunchPadLayoutByScene(@Valid GetLaunchPadLayoutBySceneCommand cmd) {
	    User user = UserContext.current().getUser();
        SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
        
        // 当场景有继承时，使用base场景对应的layout by lqs 20160513
        SceneTypeInfo sceneInfo = sceneService.getBaseSceneTypeByName(sceneToken.getNamespaceId(), sceneToken.getScene());
        String baseScene = sceneToken.getScene();
        if(sceneInfo != null) {
            baseScene = sceneInfo.getName();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Scene type is changed, sceneToken={}, newScene={}", sceneToken, sceneInfo.getName());
            }
        } else {
            LOGGER.error("Scene is not found, cmd={}, sceneToken={}", cmd, sceneToken);
        }
        
        GetLaunchPadLayoutByVersionCodeCommand getCmd = new GetLaunchPadLayoutByVersionCodeCommand();
        getCmd.setVersionCode(cmd.getVersionCode());
        getCmd.setName(cmd.getName());
        getCmd.setNamespaceId(sceneToken.getNamespaceId());
        getCmd.setSceneType(baseScene);
        
        return getLastLaunchPadLayoutByVersionCode(getCmd);
	}

	@Override
	public List<LaunchPadLayoutDTO> getLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd){
		if(cmd.getVersionCode() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid versionCode paramter.versionCode is null");
		}

		User user = UserContext.current().getUser();
		Integer namespaceId = (cmd.getNamespaceId() == null) ? 0 : cmd.getNamespaceId(); 
		// 对于老版本客户端，没有场景概念，此时它传过来的场景为null，但数据却已经有场景，需要根据小区类型来区分场景，
		// 由于该接口客户端并没有传小区信息过来，只能通过域空间配置的资源类型来定 by lqs 20160601
		// String sceneType = cmd.getCurrentSceneType();
		String sceneType = cmd.getSceneType();
		if(sceneType == null) {
		    NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(namespaceId);
		    if(namespaceDetail != null 
		        && NamespaceCommunityType.fromCode(namespaceDetail.getResourceType()) == NamespaceCommunityType.COMMUNITY_COMMERCIAL) {
		        sceneType = SceneType.PARK_TOURIST.getCode();
            } else {
                sceneType = SceneType.DEFAULT.getCode();
            }
		}
		
		List<LaunchPadLayoutDTO> results = new ArrayList<LaunchPadLayoutDTO>();
		this.launchPadProvider.findLaunchPadItemsByVersionCode(namespaceId, sceneType, cmd.getName(),cmd.getVersionCode()).stream().map((r) ->{;
		results.add(ConvertHelper.convert(r, LaunchPadLayoutDTO.class));
		return null;
		}).collect(Collectors.toList());
		return results;
	}

	@Override
	public LaunchPadItemDTO getLaunchPadItemById(GetLaunchPadItemByIdCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id paramter.id is null");
		}
		LaunchPadItem launchPadItem = this.launchPadProvider.findLaunchPadItemById(cmd.getId());
		if(launchPadItem == null){
			LOGGER.error("LaunchPad item is not exists,id=" + cmd.getId());
			throw RuntimeErrorException.errorWith(LaunchPadServiceErrorCode.SCOPE, LaunchPadServiceErrorCode.ERROR_LAUNCHPAD_ITEM_NOT_EXISTS,
					"LaunchPad item is not exists.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();
		LaunchPadItemDTO itemDTO = ConvertHelper.convert(launchPadItem, LaunchPadItemDTO.class);
		itemDTO.setIconUrl(parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId));

		return itemDTO;
	}

	@Override
	public void updateLaunchPadItem(UpdateLaunchPadItemAdminCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id paramter.id is null");
		}
		LaunchPadItem launchPadItem = this.launchPadProvider.findLaunchPadItemById(cmd.getId());
		if(launchPadItem == null){
			throw RuntimeErrorException.errorWith(LaunchPadServiceErrorCode.SCOPE, LaunchPadServiceErrorCode.ERROR_LAUNCHPAD_ITEM_NOT_EXISTS,
					"LaunchPad item is not exists,id=" + cmd.getId());
		}
		if(cmd.getActionData() != null && !cmd.getActionData().trim().equals("")){
			launchPadItem.setActionData(cmd.getActionData());
		}
		if(cmd.getActionType() != null){
			launchPadItem.setActionType(cmd.getActionType());
		}
		if(cmd.getAppId() != null){
			launchPadItem.setAppId(cmd.getAppId());
		}
		if(cmd.getApplyPolicy() != null){
			launchPadItem.setApplyPolicy(cmd.getApplyPolicy());
		}
		if(cmd.getDefaultOrder() != null){
			launchPadItem.setDefaultOrder(cmd.getDefaultOrder());
		}
		if(cmd.getDisplayFlag() != null){
			launchPadItem.setDisplayFlag(cmd.getDisplayFlag());
		}
		if(cmd.getDisplayLayout() != null && !cmd.getDisplayLayout().trim().equals("")){
			launchPadItem.setDisplayLayout(cmd.getDisplayLayout());
		}
		if(cmd.getIconUri() != null && !cmd.getIconUri().trim().equals("")){
			launchPadItem.setIconUri(cmd.getIconUri());
		}
		if(cmd.getItemGroup() != null && !cmd.getItemGroup().trim().equals("")){
			launchPadItem.setItemGroup(cmd.getItemGroup());
		}
		if(cmd.getItemHeight() != null){
			launchPadItem.setItemHeight(cmd.getItemHeight());
		}
		if(cmd.getItemLabel() != null && !cmd.getItemLabel().trim().equals("")){
			launchPadItem.setItemLabel(cmd.getItemLabel());
		}
		if(cmd.getItemLocation() != null && ! cmd.getItemLocation().trim().equals("")){
			launchPadItem.setItemLocation(cmd.getItemLocation());
		}
		if(cmd.getItemName() != null && ! cmd.getItemName().trim().equals("")){
			launchPadItem.setItemName(cmd.getItemName());
		}
		if(cmd.getItemWidth() != null){
			launchPadItem.setItemWidth(cmd.getItemWidth());
		}
		if(cmd.getNamespaceId() != null){
			launchPadItem.setNamespaceId(cmd.getNamespaceId());
		}
		if(cmd.getScopeId() != null){
			launchPadItem.setScopeId(cmd.getScopeId());
		}
		if(cmd.getScopeCode() != null){
			launchPadItem.setScopeCode(cmd.getScopeCode());
		}
		if(cmd.getTag() != null && !cmd.getTag().trim().equals("")){
			launchPadItem.setTag(cmd.getTag());
		}
		this.launchPadProvider.updateLaunchPadItem(launchPadItem);
	}

	@Override
	public GetLaunchPadItemsByKeywordAdminCommandResponse getLaunchPadItemsByKeyword(GetLaunchPadItemsByKeywordAdminCommand cmd) {

		final int size = this.configurationProvider.getIntValue("pagination.page.size", 
				AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
		final int pageOffset = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
		final int pageSize = cmd.getPageSize() == null ? size : cmd.getPageSize();
		List<LaunchPadItemAdminDTO> result = new ArrayList<LaunchPadItemAdminDTO>();
		int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
		List<LaunchPadItem> launchPadItems = this.launchPadProvider.getLaunchPadItemsByKeyword(cmd.getKeyword(),offset,pageSize);
		if(launchPadItems != null && !launchPadItems.isEmpty()){
			launchPadItems.stream().map(r ->{
				result.add(ConvertHelper.convert(r, LaunchPadItemAdminDTO.class));
				return null;
			}).collect(Collectors.toList());
		}

		GetLaunchPadItemsByKeywordAdminCommandResponse response = new GetLaunchPadItemsByKeywordAdminCommandResponse();
		if(result.size() == pageSize){
			response.setNextPageOffset(pageOffset + 1);
		}
		response.setLaunchPadItems(result);

		return response;
	}


	@Override
	public ListLaunchPadLayoutCommandResponse listLaunchPadLayoutByKeyword(ListLaunchPadLayoutAdminCommand cmd) {
		long pageOffset = cmd.getPageOffset() == null?1L:cmd.getPageOffset();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);

		List<LaunchPadLayoutDTO> list = this.launchPadProvider.listLaunchPadLayoutByKeyword(pageSize,offset,cmd.getKeyword());

		ListLaunchPadLayoutCommandResponse response = new ListLaunchPadLayoutCommandResponse();
		if(list != null && !list.isEmpty()){
			response.setRequests(list);
			if(list.size() == pageSize){
				response.setNextPageOffset(pageOffset+1);
			}
		}

		return response;
	}

	@Override
	public void deleteLaunchPadById(DeleteLaunchPadByIdCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		this.checkIdIsNull(cmd.getId());
		LaunchPadItem item = this.checkLaunchPadItem(cmd.getId(),true);
		this.deleteLaunchPadItem(item,userId,user.getNamespaceId());
	}

	private void deleteLaunchPadItem(LaunchPadItem item,Long userId,Integer namespaceId) {
		if(item.getScopeCode() == ScopeType.USER.getCode()){
			if(isExistsUnUserItem(item,namespaceId)){
				item.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
				item.setScopeId(userId);
				item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
				item.setScopeCode(ScopeType.USER.getCode());
				item.setNamespaceId(namespaceId==null?0:namespaceId);
				this.launchPadProvider.updateLaunchPadItem(item);
			}
			else{
				this.launchPadProvider.deleteLaunchPadItem(item);
			}
		}else{
			item.setDisplayFlag(ItemDisplayFlag.HIDE.getCode());
			item.setScopeId(userId);
			item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
			item.setScopeCode(ScopeType.USER.getCode());
			item.setId(0L);
			item.setNamespaceId(namespaceId==null?0:namespaceId);
			this.launchPadProvider.createLaunchPadItem(item);
		}
	}

	private boolean isExistsUnUserItem(LaunchPadItem item,Integer namespaceId) {
		List<LaunchPadItem> result = this.getUnUserItems(item,namespaceId);
		if(result != null && !result.isEmpty())
			return true;
		return false;
	}

	private List<LaunchPadItem> getUnUserItems(LaunchPadItem item,Integer namespaceId) {
		List<LaunchPadItem> result = new ArrayList<LaunchPadItem>();
		List<LaunchPadItem> countyItems = this.launchPadProvider.findLaunchPadItem(namespaceId,item.getItemGroup(),item.getItemName(), ScopeType.ALL.getCode(), 0);
		List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItem(namespaceId,item.getItemGroup(),item.getItemName(), ScopeType.CITY.getCode(), 0);
		List<LaunchPadItem> cmmtyItems = this.launchPadProvider.findLaunchPadItem(namespaceId,item.getItemGroup(),item.getItemName(), ScopeType.COMMUNITY.getCode(), 0);
		if(countyItems != null && !countyItems.isEmpty())
			result.addAll(countyItems);
		if(cityItems != null && !cityItems.isEmpty())
			result.addAll(cityItems);
		if(cmmtyItems != null && !cmmtyItems.isEmpty())
			result.addAll(cmmtyItems);
		return result;
	}

	private LaunchPadItem checkLaunchPadItem(Long id,boolean isThrowExcept) {
		LaunchPadItem item = this.launchPadProvider.findLaunchPadItemById(id);
		if(item == null){
			LOGGER.error("can not found launchPadItem by id.id="+id);
			if(isThrowExcept)
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"can not found launchPadItem by id.");
		}
		return item;
	}

	private void checkIdIsNull(Long id) {
		if(id == null){
			LOGGER.error("propterty id paramter can not be null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty id paramter can not be null or empty.");
		}
	}

	@Override
	public void favoriteBusinessesByScene(FavoriteBusinessesBySceneCommand cmd) {
		User user = UserContext.current().getUser();
		long userId = user.getId();
		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
		
		SceneTypeInfo sceneInfo = sceneService.getBaseSceneTypeByName(sceneToken.getNamespaceId(), sceneToken.getScene());
        String baseScene = sceneToken.getScene();
        if(sceneInfo != null) {
            baseScene = sceneInfo.getName();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Scene type is changed, sceneToken={}, newScene={}", sceneToken, sceneInfo.getName());
            }
        } else {
            LOGGER.error("Scene is not found, cmd={}, sceneToken={}", cmd, sceneToken);
        }
        
        this.businessService.favoriteBusinessesByScene(cmd, baseScene);
	}

	@Override
	public void cancelFavoriteBusinessByScene(
			CancelFavoriteBusinessBySceneCommand cmd) {
		User user = UserContext.current().getUser();
		SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
		
		CancelFavoriteBusinessCommand command = new CancelFavoriteBusinessCommand();
		command.setId(cmd.getId());
		this.businessService.cancelFavoriteBusiness(command);
	}


}
