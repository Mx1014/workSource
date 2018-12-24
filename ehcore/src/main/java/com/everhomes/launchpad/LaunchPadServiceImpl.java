// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.banner.BannerService;
import com.everhomes.bootstrap.PlatformContext;
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
import com.everhomes.http.HttpUtils;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleEntryProvider;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.portal.PortalItemGroup;
import com.everhomes.portal.PortalItemGroupProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersionUser;
import com.everhomes.portal.PortalVersionUserProvider;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.AddressType;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.business.BusinessDTO;


import com.everhomes.rest.business.BusinessTargetType;
import com.everhomes.rest.business.CancelFavoriteBusinessCommand;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.common.BizDetailActionData;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpad.UpdateUserAppsCommand;
import com.everhomes.rest.launchpad.admin.*;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Bulletins;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Card;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.OPPush;
import com.everhomes.rest.launchpadbase.indexconfigjson.Container;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.organization.GetOrgDetailCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsByOrganizationIdCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsByOrganizationIdResponse;
import com.everhomes.rest.statistics.transaction.SettlementErrorCode;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.rest.ui.launchpad.*;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.rest.widget.BannersInstanceConfig;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.scene.SceneService;
import com.everhomes.scene.SceneTypeInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.statistics.transaction.BizBusinessInfo;
import com.everhomes.statistics.transaction.ListBusinessInfoResponse;
import com.everhomes.statistics.transaction.ListModelInfoResponse;
import com.everhomes.statistics.transaction.StatTransactionConstant;
import com.everhomes.user.*;
import com.everhomes.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.rest.ui.user.SceneType.PARK_TOURIST;
import static com.everhomes.rest.ui.user.SceneType.PM_ADMIN;

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
	private OrganizationProvider organizationProvider;
	
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

	@Autowired
	private LaunchPadIndexProvider launchPadIndexProvider;

	@Autowired
	private ServiceModuleAppService serviceModuleAppService;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private BannerService bannerService;

	@Autowired
	private PortalService portalService;

    @Autowired
    private LaunchPadService launchPadService;

	@Autowired
    private PortalItemGroupProvider portalItemGroupProvider;

	@Autowired
    private PortalVersionUserProvider portalVersionUserProvider;
	
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
		result = getItemsByCommunity(cmd, request, ItemDisplayFlag.DISPLAY);

		// 更多或者全部不可删除，如果没有更多或者全部则其他icon不可以移除
		setDefaultEditFlag(result);

		response.setLaunchPadItems(result);
		long endTime = System.currentTimeMillis();
		LOGGER.info("Query launch pad complete, cmd=" + cmd + ",esplse=" + (endTime - startTime));
		return response;

	}


	/**
	 * 更多或者全部不可删除，如果没有更多或者全部则其他icon不可以移除
	 * @param dtos
	 */
	private void setDefaultEditFlag(List<LaunchPadItemDTO> dtos){
		if(dtos == null){
			return;
		}

		boolean deleteFlag = false;
		for (LaunchPadItemDTO dto: dtos){
			if(ActionType.fromCode(dto.getActionType()) == ActionType.MORE_BUTTON || ActionType.fromCode(dto.getActionType()) == ActionType.ALL_BUTTON){
				deleteFlag = true;
				dto.setDeleteFlag(DeleteFlagType.NO.getCode());
				dto.setEditFlag(EditFlagType.NO.getCode());
				break;
			}
		}
		if(!deleteFlag){
			for (LaunchPadItemDTO dto: dtos){
				dto.setDeleteFlag(DeleteFlagType.NO.getCode());
				dto.setEditFlag(EditFlagType.NO.getCode());
			}
		}
	}
	
	
	@Override
	public List<CategryItemDTO> getAllCategryItems(GetLaunchPadItemsCommand cmd, HttpServletRequest request){
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		if(cmd.getItemLocation() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemLocation paramter,itemLocation is null");
		}
		if(cmd.getItemGroup() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemGroup paramter,itemGroup is null");
		}
		long startTime = System.currentTimeMillis();
		List<CategryItemDTO> categryItemDTOs = new ArrayList<CategryItemDTO>();
		List<ItemServiceCategry> categries = getItemServiceCategriesByScopeType(namespaceId, cmd.getItemLocation(), cmd.getItemGroup(), cmd.getCurrentSceneType(), null, cmd.getCommunityId());
		for (ItemServiceCategry categry: categries) {
			CategryItemDTO categryItemDTO = new CategryItemDTO();
			categryItemDTO.setCategryId(categry.getId());
			categryItemDTO.setCategryName(categry.getLabel());
			categryItemDTO.setCategryIconUrl(parserUri(categry.getIconUri(),EntityType.USER.getCode(),userId));
			categryItemDTO.setCategryAlign(categry.getAlign());
			cmd.setCategryId(categry.getId());
			cmd.setCategryName(categry.getName());
			List<LaunchPadItemDTO> result = getItemsByCommunity(cmd, request, null);
			if(result != null && result.size() > 0){
				categryItemDTO.setLaunchPadItems(result);
				categryItemDTOs.add(categryItemDTO);
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("Query launch pad complete, cmd=" + cmd + ",esplse=" + (endTime - startTime));
		return categryItemDTOs;

	}

	private List<ItemServiceCategry> getItemServiceCategriesByScopeType(Integer namespaceId, String itemLocation,String itemGroup, String sceneType, Long organizationId, Long communityId){
		return launchPadProvider.listItemServiceCategries(namespaceId, itemLocation, itemGroup, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				ScopeType communityScopeType= ScopeType.COMMUNITY;
				SceneType communitySceneType= SceneType.PARK_TOURIST;
				if(SceneType.fromCode(sceneType) == SceneType.DEFAULT){
					communityScopeType = ScopeType.RESIDENTIAL;
					communitySceneType = SceneType.DEFAULT;
				}

				Condition condDefCommunity = Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_CODE.eq(communityScopeType.getCode());
				condDefCommunity = condDefCommunity.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_ID.eq(0L));
				condDefCommunity = condDefCommunity.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCENE_TYPE.eq(communitySceneType.getCode()));
				Condition cond = condDefCommunity;

				if(null != communityId) {
					Condition condCommunity = Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_CODE.eq(communityScopeType.getCode());
					condCommunity = condCommunity.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_ID.eq(communityId));
					condCommunity = condCommunity.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCENE_TYPE.eq(communitySceneType.getCode()));
					cond = cond.or(condCommunity);
				}

				if(null != organizationId){
					ScopeType orgScopeType = ScopeType.ORGANIZATION;

					if(SceneType.fromCode(sceneType) == PM_ADMIN){
						orgScopeType = ScopeType.PM;
					}

					Condition condDefOrg = Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_CODE.eq(orgScopeType.getCode());
					condDefOrg = condDefOrg.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_ID.eq(0L));
					condDefOrg = condDefOrg.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCENE_TYPE.eq(sceneType));
					cond = cond.or(condDefOrg);

					Condition condOrg = Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_CODE.eq(orgScopeType.getCode());
					condOrg = condOrg.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCOPE_ID.eq(organizationId));
					condOrg = condOrg.and(Tables.EH_ITEM_SERVICE_CATEGRIES.SCENE_TYPE.eq(sceneType));
					cond = cond.or(condOrg);
				}
				query.addConditions(cond);
				query.addGroupBy(Tables.EH_ITEM_SERVICE_CATEGRIES.NAME);
				query.addOrderBy(Tables.EH_ITEM_SERVICE_CATEGRIES.ORDER);
				return null;
			}
		});
	}

	@Override
	public List<CategryItemDTO> getAllCategryItems(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request){
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		if(cmd.getItemLocation() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemLocation paramter,itemLocation is null");
		}
		if(cmd.getItemGroup() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid itemGroup paramter,itemGroup is null");
		}
		long startTime = System.currentTimeMillis();
		OrganizationDTO org = organizationService.getOrganizationById(cmd.getOrganizationId());
		Long communityId = null;
		if(null != org)
			communityId = org.getCommunityId();
		List<CategryItemDTO> categryItemDTOs = new ArrayList<CategryItemDTO>();
		List<ItemServiceCategry> categries = getItemServiceCategriesByScopeType(namespaceId, cmd.getItemLocation(), cmd.getItemGroup(), cmd.getCurrentSceneType(), cmd.getOrganizationId(), communityId);
		for (ItemServiceCategry categry: categries) {
			CategryItemDTO categryItemDTO = new CategryItemDTO();
			categryItemDTO.setCategryId(categry.getId());
			categryItemDTO.setCategryName(categry.getLabel());
			categryItemDTO.setCategryIconUrl(parserUri(categry.getIconUri(),EntityType.USER.getCode(),userId));
			categryItemDTO.setCategryAlign(categry.getAlign());
			cmd.setCategryId(categry.getId());
			cmd.setCategryName(categry.getName());
			List<LaunchPadItemDTO> result = getItemsByOrg(cmd, request, null);
			if(result != null && result.size() > 0) {
				categryItemDTO.setLaunchPadItems(result);
				categryItemDTOs.add(categryItemDTO);
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("Query launch pad complete, cmd=" + cmd + ",esplse=" + (endTime - startTime));
		return categryItemDTOs;

	}

	@Override
	public GetLaunchPadItemsCommandResponse getMoreItems(GetLaunchPadItemsCommand cmd, HttpServletRequest request){
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
		List<LaunchPadItemDTO> result = getItemsByCommunity(cmd, request, ItemDisplayFlag.HIDE);
		response.setLaunchPadItems(result);
		long endTime = System.currentTimeMillis();
		LOGGER.info("Query launch pad complete, cmd=" + cmd + ",esplse=" + (endTime - startTime));
		return response;

	}
	
   public GetLaunchPadItemsCommandResponse getMoreItems(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request){
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
       List<LaunchPadItemDTO> result = getItemsByOrg(cmd, request,ItemDisplayFlag.HIDE);
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
		result = getItemsByOrg(cmd, request,ItemDisplayFlag.DISPLAY);

		// 更多或者全部不可删除，如果没有更多或者全部则其他icon不可以移除
		setDefaultEditFlag(result);

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
//            orgCmd.setCommunityId(sceneToken.getEntityId());
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

	   //检查游客是否能继续访问此场景 by sfyan 20161009
	   userService.checkUserScene(sceneType);
	   LOGGER.debug("Scene type is" + sceneType);

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

	   refreshActionData(cmdResponse.getLaunchPadItems());
       return cmdResponse;
   }
   
   @Override
   public GetLaunchPadItemsCommandResponse getMoreItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request) {
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
	   //检查游客是否能继续访问此场景 by sfyan 20161009
	   userService.checkUserScene(sceneType);
       switch(sceneType) {
       case DEFAULT:
       case PARK_TOURIST:
           community = communityProvider.findCommunityById(sceneToken.getEntityId());
           if(community != null) {
               getCmd.setCommunityId(community.getId());
           }
           
           cmdResponse = getMoreItems(getCmd, request);
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
           cmdResponse = getMoreItems(getCmd, request);
           break;
       case PM_ADMIN:// 无小区ID
       case ENTERPRISE: 
       case ENTERPRISE_NOAUTH:
           GetLaunchPadItemsByOrgCommand orgCmd = new GetLaunchPadItemsByOrgCommand();
           orgCmd.setItemGroup(cmd.getItemGroup());
           orgCmd.setItemLocation(cmd.getItemLocation());
           orgCmd.setNamespaceId(sceneToken.getNamespaceId());
           orgCmd.setSceneType(baseScene);
           orgCmd.setOrganizationId(sceneToken.getEntityId());
           cmdResponse = getMoreItems(orgCmd, request);
           break;
       default:
           LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
           break;
       }

	   refreshActionData(cmdResponse.getLaunchPadItems());
       
       return cmdResponse;
   }

	@Override
	public List<CategryItemDTO> getAllCategryItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request) {
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
		List<CategryItemDTO> categryItemDTOs = null;
		SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
		//检查游客是否能继续访问此场景 by sfyan 20161009
		userService.checkUserScene(sceneType);
		switch(sceneType) {
			case DEFAULT:
			case PARK_TOURIST:
				community = communityProvider.findCommunityById(sceneToken.getEntityId());
				if(community != null) {
					getCmd.setCommunityId(community.getId());
				}

				categryItemDTOs = getAllCategryItems(getCmd, request);
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
				categryItemDTOs = getAllCategryItems(getCmd, request);
				break;
			case PM_ADMIN:// 无小区ID
			case ENTERPRISE:
			case ENTERPRISE_NOAUTH:
				GetLaunchPadItemsByOrgCommand orgCmd = new GetLaunchPadItemsByOrgCommand();
				orgCmd.setItemGroup(cmd.getItemGroup());
				orgCmd.setItemLocation(cmd.getItemLocation());
				orgCmd.setNamespaceId(sceneToken.getNamespaceId());
				orgCmd.setSceneType(baseScene);
				orgCmd.setOrganizationId(sceneToken.getEntityId());
				categryItemDTOs = getAllCategryItems(orgCmd, request);
				break;
			default:
				LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
				break;
		}

		//刷新actionData
		if(categryItemDTOs != null && categryItemDTOs.size() > 0){
			List<LaunchPadItemDTO> dtos  = new ArrayList<>();
			categryItemDTOs.forEach(r ->
				dtos.addAll(r.getLaunchPadItems())
			);
			refreshActionData(dtos);
		}


		return categryItemDTOs;
	}
	
	@SuppressWarnings("unchecked")
	private List<LaunchPadItemDTO> getBusinessItems(GetLaunchPadItemsCommand cmd,Community community) {
		User user = UserContext.current().getUser();
		long userId = user.getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
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
	private List<LaunchPadItemDTO> getItemsByCommunity(GetLaunchPadItemsCommand cmd, HttpServletRequest request, ItemDisplayFlag itemDisplayFlag){
		User user = UserContext.current().getUser();
		long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
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

		ScopeType communityScopeType= ScopeType.COMMUNITY;
		SceneType communitySceneType= SceneType.PARK_TOURIST;
		if(SceneType.fromCode(sceneType) == SceneType.DEFAULT){
			communityScopeType = ScopeType.RESIDENTIAL;
			communitySceneType = SceneType.DEFAULT;
		}
        List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();
		
        //增加定制item流程 by sfyan 20160607
		List<LaunchPadItem> communityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, communitySceneType.getCode(), cmd.getItemLocation(),cmd.getItemGroup(),communityScopeType.getCode(),community.getId(),null, cmd.getCategryName());

		List<LaunchPadItem> customizedItems = new ArrayList<LaunchPadItem>();
		
		//筛选出小区定制的item sfyan 20160607
		for (LaunchPadItem launchPadItem : communityItems) {
			if(ApplyPolicy.fromCode(launchPadItem.getApplyPolicy()) == ApplyPolicy.CUSTOMIZED){
				customizedItems.add(launchPadItem);
			}
		}
		
		//判断此小区是否含有定制item，有则只需要定制的item sfyan 20160607
		if(customizedItems.size() > 0){
			allItems = customizedItems;
		}else{
//			List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.ALL.getCode(),0L,null);
//			//没有根据城市配置的item 暂时不用 add by sfyan 20170811
////			List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.CITY.getCode(),community.getCityId(),null);
//			List<LaunchPadItem> communityDefaultItems = null;
//			//获取场景获取园区或者小区默认的item add by sfyan 20170811
//			if(SceneType.PARK_TOURIST == SceneType.fromCode(sceneType))
//				communityDefaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(), ScopeType.COMMUNITY.getCode(), 0L, null);
//			else
//				communityDefaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(), ScopeType.RESIDENTIAL.getCode(), 0L, null);
//
//			allItems = overrideOrRevertItems(defaultItems, communityDefaultItems);
//			allItems = overrideOrRevertItems(defaultItems, communityItems);

			allItems = getLaunchPadItemsByScopeType(namespaceId, cmd.getItemLocation(), cmd.getItemGroup(), ApplyPolicy.DEFAULT.getCode(), sceneType, null, communityId, cmd.getCategryName());
		}
		
		
		if(allItems!=null&&!allItems.isEmpty()){
			List<UserLaunchPadItem> userLaunchPadItems = this.launchPadProvider.findUserLaunchPadItemByUserId(userId, sceneType, EntityType.COMMUNITY.getCode(), community.getId());

			allItems = overrideUserItems(allItems, userLaunchPadItems);

			if(null != itemDisplayFlag){
				allItems = allItems.stream().filter(r -> r.getDisplayFlag()==itemDisplayFlag.getCode()).collect(Collectors.toList());
			}

			// 根据类别过滤出item by sfyan 20161020
//			if(null != cmd.getCategryId()){
//				allItems = allItems.stream().filter(r -> null != r.getServiceCategryId() && r.getServiceCategryId().equals(cmd.getCategryId())).collect(Collectors.toList());
//			}
		}

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
//							itemDTO.setItemName(b.getName() == null ? itemDTO.getItemName() : b.getName()+"(店铺)");
//						else
//							itemDTO.setItemName(b.getName() == null ? itemDTO.getItemName() : b.getName());
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
		
		return processLaunchPadItems(token, userId, community.getId(), allItems, request,itemDisplayFlag);
	}
	
    private List<LaunchPadItemDTO> getItemsByOrg(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request, ItemDisplayFlag itemDisplayFlag){
        User user = UserContext.current().getUser();
        long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String sceneType = cmd.getCurrentSceneType();
        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        
        // 产品规则每个公司都有一个办公地点所在的园区/小区，故在公司场景也可以拿到小区ID
        // 把这个小区ID补回来，是为了物业相关的服务（报修、投诉建议等）在发帖时可以由服务器提供visible_region_type/id  by lqs 20160617
        Long communityId = null;
        OrganizationDTO org = organizationService.getOrganizationById(cmd.getOrganizationId());

        if(org != null) {
            communityId = org.getCommunityId();
        } else {
            LOGGER.error("Organization id not found, userId={}, cmd={}", userId, cmd);
        }
		ScopeType communityScopeType= ScopeType.COMMUNITY;
		SceneType communitySceneType= SceneType.PARK_TOURIST;
		if(SceneType.fromCode(sceneType) == SceneType.DEFAULT){
			communityScopeType = ScopeType.RESIDENTIAL;
			communitySceneType = SceneType.DEFAULT;
		}

		ScopeType orgScopeType = ScopeType.ORGANIZATION;

		if(SceneType.fromCode(sceneType) == PM_ADMIN){
			orgScopeType = ScopeType.PM;
		}

        List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();
        
        //增加定制item流程 by sfyan 20160607
      	List<LaunchPadItem> orgItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),orgScopeType.getCode(),cmd.getOrganizationId(),null, cmd.getCategryName());

        // 如果只定制scope为公司的，则只有当前公司才能查到，其它公司就查不到，故补充也按园区查询 by lqs 20160729
      	int orgItemSize = orgItems.size();
      	int cmntyItemSize = 0;

		List<LaunchPadItem> communityItems = null;
      	if(communityId != null) {
			communityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, communitySceneType.getCode(), cmd.getItemLocation(), cmd.getItemGroup(), communityScopeType.getCode(), communityId, null, cmd.getCategryName());
			if(0 == orgItemSize)
				orgItems = communityItems;

			cmntyItemSize = communityItems.size();
      	}
      	if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Check custom launchpad items, namespaceId={}, sceneType={}, organizationId={}, communityId={}, orgItemsize={}, cmntyItemSize={}", 
                namespaceId, sceneType, cmd.getOrganizationId(), communityId, orgItemSize, cmntyItemSize);
        }
//		ScopeType copeType = ScopeType.ORGANIZATION;
//
//		if(SceneType.fromCode(sceneType) == SceneType.PM_ADMIN){
//			copeType = ScopeType.PM;
//		}
      	List<LaunchPadItem> customizedItems = new ArrayList<LaunchPadItem>();
      		
      	//筛选出小区定制的item sfyan 20160607
      	for (LaunchPadItem launchPadItem : orgItems) {
      		if(ApplyPolicy.fromCode(launchPadItem.getApplyPolicy()) == ApplyPolicy.CUSTOMIZED){
      			customizedItems.add(launchPadItem);
      		}
      	}
        
        //判断此小区是否含有定制item，有则只需要定制的item sfyan 20160607
      	if(customizedItems.size() > 0){
      		allItems = customizedItems;
      	}else{

//      		List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),ScopeType.ALL.getCode(),0L,null);
//			List<LaunchPadItem> communityDefaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, SceneType.PARK_TOURIST.getCode(), cmd.getItemLocation(),cmd.getItemGroup(),copeType.getCode(), 0L ,null);
//			List<LaunchPadItem> orgDefaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(namespaceId, sceneType, cmd.getItemLocation(),cmd.getItemGroup(),copeType.getCode(), 0L ,null);
//			allItems = overrideOrRevertItems(defaultItems, communityDefaultItems);
//			allItems = overrideOrRevertItems(allItems, orgDefaultItems);
//			allItems = overrideOrRevertItems(allItems, communityItems);
//			allItems = overrideOrRevertItems(allItems, orgItems);

			allItems = getLaunchPadItemsByScopeType(namespaceId, cmd.getItemLocation(), cmd.getItemGroup(),ApplyPolicy.DEFAULT.getCode(), sceneType, cmd.getOrganizationId(), communityId, cmd.getCategryName());
      	}

		if(allItems!=null&&!allItems.isEmpty()){
			List<UserLaunchPadItem> userLaunchPadItems = this.launchPadProvider.findUserLaunchPadItemByUserId(userId, sceneType, EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId());
			allItems = overrideUserItems(allItems, userLaunchPadItems);

			if(null != itemDisplayFlag){
				allItems = allItems.stream().filter(r -> r.getDisplayFlag()==itemDisplayFlag.getCode()).collect(Collectors.toList());
			}
			// 根据类别过滤出item by sfyan 20161020
//				if(null != cmd.getCategryId()){
//					allItems = allItems.stream().filter(r -> null != r.getServiceCategryId() && r.getServiceCategryId().equals(cmd.getCategryId())).collect(Collectors.toList());
//				}
		}



        return processLaunchPadItems(token, userId, communityId, allItems, request, itemDisplayFlag);
    }

	private List<LaunchPadItem> getLaunchPadItemsByScopeType(Integer namespaceId, String itemLocation, String itemGroup, String sceneType, Long organizationId, Long communityId){
		return getLaunchPadItemsByScopeType(namespaceId, itemLocation, itemGroup, ApplyPolicy.DEFAULT.getCode(), sceneType, organizationId, communityId, null);
	}

	private List<LaunchPadItem> getLaunchPadItemsByScopeType(Integer namespaceId, String itemLocation,String itemGroup, Byte applyPolicy, String sceneType, Long organizationId, Long communityId, String categryName){
		return launchPadProvider.listLaunchPadItemsByScopeType(namespaceId, itemLocation, itemGroup, applyPolicy, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				Condition cond = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(ScopeType.ALL.getCode());
				cond = cond.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(0L));
				cond = cond.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));


				ScopeType communityScopeType= ScopeType.COMMUNITY;
				SceneType communitySceneType= SceneType.PARK_TOURIST;
				if(SceneType.fromCode(sceneType) == SceneType.DEFAULT){
					communityScopeType = ScopeType.RESIDENTIAL;
					communitySceneType = SceneType.DEFAULT;
				}

				Condition condDefCommunity = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(communityScopeType.getCode());
				condDefCommunity = condDefCommunity.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(0L));
				condDefCommunity = condDefCommunity.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(communitySceneType.getCode()));
				cond = cond.or(condDefCommunity);

				if(null != communityId) {
					Condition condCommunity = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(communityScopeType.getCode());
					condCommunity = condCommunity.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(communityId));
					condCommunity = condCommunity.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(communitySceneType.getCode()));
					cond = cond.or(condCommunity);
				}

				if(null != organizationId){
					ScopeType orgScopeType = ScopeType.ORGANIZATION;

					if(SceneType.fromCode(sceneType) == PM_ADMIN){
						orgScopeType = ScopeType.PM;
					}

					Condition condDefOrg = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(orgScopeType.getCode());
					condDefOrg = condDefOrg.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(0L));
					condDefOrg = condDefOrg.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));
					cond = cond.or(condDefOrg);

					Condition condOrg = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(orgScopeType.getCode());
					condOrg = condOrg.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(organizationId));
					condOrg = condOrg.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));
					cond = cond.or(condOrg);
				}

				if(!StringUtils.isEmpty(categryName))
					cond = cond.and(Tables.EH_LAUNCH_PAD_ITEMS.CATEGRY_NAME.eq(categryName));

				//增加版本功能，默认找正式版本，有特别标识的找该版本功能
				cond = cond.and(launchPadProvider.getPreviewPortalVersionCondition(Tables.EH_LAUNCH_PAD_ITEMS.getName()));

				query.addConditions(cond);
				query.addGroupBy(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_NAME);
				query.addOrderBy(Tables.EH_LAUNCH_PAD_ITEMS.DEFAULT_ORDER);
				return query;
			}
		});
	}


	private List<LaunchPadItemDTO> processLaunchPadItems(String token, Long userId, Long communityId, List<LaunchPadItem> allItems, HttpServletRequest request,ItemDisplayFlag itemDisplayFlag) {
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
	    try{
            List<LaunchPadItemDTO> distinctDto = new ArrayList<LaunchPadItemDTO>();
            final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
            final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
            final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
            List<Long> categoryIds = new ArrayList<Long>();
            categoryIds.add(CategoryConstants.CATEGORY_ID_BUSINESS_AROUND);
            categoryIds.add(CategoryConstants.CATEGORY_ID_BUSINESS_NEXTDOOR);
            List<BusinessDTO> businessDTOs = businessService.getBusinesses(categoryIds, communityId);
            List<Long> businessIds = new ArrayList<Long>();
            if(null != businessDTOs && businessDTOs.size() > 0){
            	businessIds = businessDTOs.stream().map( r->r.getId()).collect(Collectors.toList());
        	}
            LOGGER.debug("need business item size = {} id = {},", businessIds.size(), businessIds);
            List<Long> bizIds = businessIds;

            allItems.forEach(r ->{
                LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
                if(null != request){
                	itemDTO.setActionData(parserJson(token, userId, communityId, r, request));
                }
                if(null == itemDTO.getScaleType()){
                	itemDTO.setScaleType(ScaleType.TAILOR.getCode());
                }


				if(null != ItemTargetType.fromCode(r.getTargetType())){
					if(ItemTargetType.BIZ == ItemTargetType.fromCode(r.getTargetType())){
						Long businessId = Long.valueOf(r.getTargetId());
						Business b = this.businessProvider.findBusinessById(businessId);
						if(b != null){
							if( ItemDisplayFlag.fromCode(r.getDisplayFlag()) == ItemDisplayFlag.DISPLAY
									|| BusinessTargetType.fromCode(b.getTargetType()) != BusinessTargetType.ZUOLIN
									|| (bizIds.contains(businessId) && ItemDisplayFlag.fromCode(r.getDisplayFlag()) == ItemDisplayFlag.HIDE)){
								itemDTO.setIconUrl(processLogoUrl(b,userId,imageUrl));
								JSONObject jsonObject = new JSONObject();
								jsonObject.put(LaunchPadConstants.URL, processUrl(b, prefixUrl,businessDetailUrl));
								jsonObject.put(LaunchPadConstants.COMMUNITY_ID, communityId);
								itemDTO.setActionData(jsonObject.toJSONString());
								if(b.getCreatorUid().longValue() == userId)
									itemDTO.setItemLabel(b.getName() == null ? itemDTO.getItemLabel() : b.getName()+"(店铺)");
								else
									itemDTO.setItemLabel(b.getName() == null ? itemDTO.getItemLabel() : b.getName());

								itemDTO.setEditFlag(r.getDeleteFlag());
								distinctDto.add(itemDTO);
							}
						}
					}else if(ItemTargetType.ZUOLIN_SHOP == ItemTargetType.fromCode(r.getTargetType())){
						List<String> businessNos = new ArrayList<>();
						businessNos.add(r.getTargetId());
						List<BusinessDTO> businesses = this.getBusinessesInfo(businessNos);
						if(businesses.size() > 0){
							BusinessDTO business = businesses.get(0);
							itemDTO.setIconUrl(business.getLogoUrl());
							BizDetailActionData actionData = (BizDetailActionData)StringHelper.fromJsonString(r.getActionData(), BizDetailActionData.class);
							JSONObject jsonObject = new JSONObject();
							if(null != actionData){
								jsonObject.put(LaunchPadConstants.URL, actionData.getUrl());
							}else{
								LOGGER.error("item build data error. itemId = {}, actionData= {}", r.getId(), r.getActionData());
							}
							jsonObject.put(LaunchPadConstants.COMMUNITY_ID, communityId);
							itemDTO.setActionData(jsonObject.toJSONString());
							itemDTO.setItemLabel(business.getDisplayName());
							itemDTO.setEditFlag(r.getDeleteFlag());
							distinctDto.add(itemDTO);
						}
					}else{
						String url = parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId);
						itemDTO.setIconUrl(url);
						if (StringUtils.isNotBlank(itemDTO.getSelectedIconUri())) {
							String selectedUrl = parserUri(itemDTO.getSelectedIconUri(),EntityType.USER.getCode(),userId);
							itemDTO.setSelectedIconUrl(selectedUrl);
						}else {
							itemDTO.setSelectedIconUrl(itemDTO.getIconUrl());
						}
						itemDTO.setEditFlag(r.getDeleteFlag());
						distinctDto.add(itemDTO);
					}
				}else{
					String url = parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId);
					itemDTO.setIconUrl(url);
					if (StringUtils.isNotBlank(itemDTO.getSelectedIconUri())) {
						String selectedUrl = parserUri(itemDTO.getSelectedIconUri(),EntityType.USER.getCode(),userId);
						itemDTO.setSelectedIconUrl(selectedUrl);
					}else {
						itemDTO.setSelectedIconUrl(itemDTO.getIconUrl());
					}
					
//                    if(LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("Parse uri while processing launchpad items, item=" + itemDTO);
//                    }
					itemDTO.setEditFlag(r.getDeleteFlag());
					distinctDto.add(itemDTO);
				}
            });
            if(distinctDto != null && !distinctDto.isEmpty()){
                distinctDto.forEach(r ->{
                    if(!result.contains(r)){
                        result.add(r);
                    }
                });
            }

			//一般按item的defaultOrder排序 by sfyan 20170323
			if(null != itemDisplayFlag){
				if(result != null && !result.isEmpty())
					sortLaunchPadItems(result);
			}else{
				//查询全部的item的时候按moreOrder排序 by sfyan 20170323
				if(result != null && !result.isEmpty())
					Collections.sort(result, new Comparator<LaunchPadItemDTO>(){
						@Override
						public int compare(LaunchPadItemDTO o1, LaunchPadItemDTO o2){
							return o1.getMoreOrder().intValue() - o2.getMoreOrder().intValue();
						}
					});
			}

        }catch(Exception e){
            LOGGER.error("Process item aciton data is error.",e);
            return null;
        }
        return result;
	}

	private void refreshActionData(List<LaunchPadItemDTO> dtos){
		if(dtos != null && dtos.size() > 0){
			dtos.forEach(r -> {
				if(r.getActionData() != null && !"".equals(r.getActionData().trim())){
					//调用各个业务的handler处理action
                    String newActionData = refreshActionData(r.getActionData());
                    r.setActionData(newActionData);
                }
			});
		}
	}

	@Override
    public String refreshActionData(String actionData) {
		if(actionData == null){
			return null;
		}
        JSONObject jsonObject = (JSONObject) JSONValue.parse(actionData);

		if(jsonObject == null){
			return null;
		}
        if(jsonObject.get("handler") != null) {
            LaunchPadItemActionDataHandler handler = PlatformContextNoWarnning.getComponent(
                    LaunchPadItemActionDataHandler.LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX+ String.valueOf(jsonObject.get("handler")));
            if (handler != null) {
                actionData = handler.refreshActionData(actionData);
            }
        }

        //调用默认的default_host handler处理url，将{key}等转换成实际的host
        LaunchPadItemActionDataHandler handler = PlatformContext.getComponent(
                LaunchPadItemActionDataHandler.LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX+ LaunchPadItemActionDataHandler.DEFAULT);
        return handler.refreshActionData(actionData);
    }

    private List<BusinessDTO> getBusinessesInfo(List<String> businessIds){
		List<BusinessDTO> businesses = new ArrayList<>();
		String serverURL = configurationProvider.getValue(StatTransactionConstant.BIZ_SERVER_URL, "");
		if(org.springframework.util.StringUtils.isEmpty(serverURL)){
			LOGGER.error("biz serverURL not configured, param = {}", StatTransactionConstant.BIZ_SERVER_URL);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz serverURL not configured.");
		}

		String paidOrderApi = configurationProvider.getValue(StatTransactionConstant.BIZ_BUSINESSES_INFO_API, "");

		if(org.springframework.util.StringUtils.isEmpty(paidOrderApi)){
			LOGGER.error("biz paid ware info api not configured, param = {}", StatTransactionConstant.BIZ_WARE_INFO_API);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz paid  ware info api not configured.");
		}

		try {
			String appKey = configurationProvider.getValue(StatTransactionConstant.BIZ_APPKEY, "");
			String secretKey = configurationProvider.getValue(StatTransactionConstant.BIZ_SECRET_KEY, "");
			Integer nonce = (int)(Math.random()*1000);
			Long timestamp = System.currentTimeMillis();
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("nonce", nonce);
			params.put("timestamp", timestamp);
			params.put("appKey", appKey);
			params.put("shopNos", businessIds);
			Map<String, String> mapForSignature = new HashMap<String, String>();
			for(Map.Entry<String, Object> entry : params.entrySet()) {
				if(!entry.getKey().equals("shopNos")) {
					mapForSignature.put(entry.getKey(), entry.getValue().toString());
				}
			}
			mapForSignature.put("modelIds", org.apache.commons.lang.StringUtils.join(businessIds, ","));
			String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
			params.put("signature", URLEncoder.encode(signature,"UTF-8"));
			String url = serverURL + paidOrderApi;
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("request url = {}, params = {}", url, params);
			}

			String result = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, HTTP.UTF_8);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("response result = {}", result);
			}
			ListBusinessInfoResponse response= (ListBusinessInfoResponse)StringHelper.fromJsonString(result, ListModelInfoResponse.class);

			if(null != response){
				List<BizBusinessInfo> bizBusinessInfos = response.getResponse();
				if(null != bizBusinessInfos){
					for (BizBusinessInfo bizBusinessInfo: bizBusinessInfos) {
						BusinessDTO business = new BusinessDTO();
						business.setName(bizBusinessInfo.getShopName());
						business.setAddress(bizBusinessInfo.getShopAddress());
						business.setPhone(bizBusinessInfo.getPhone());
						business.setLogoUrl(bizBusinessInfo.getShopLogo());
						business.setDisplayName(bizBusinessInfo.getShopName());
						businesses.add(business);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Get business info error e = {}");
		}

		return businesses;
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
			//增加异常日志信息 by sfyan 20160712
			LOGGER.error("Parser json is error {} ,communityId = {}, itemId = {}, actionData = {}", e , communityId, launchPadItem.getId() ,launchPadItem.getActionData());
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

		// 按id排序 可以去掉 by sfyan
//		Collections.sort(result, new Comparator<LaunchPadItemDTO>(){
//			@Override
//			public int compareTo(LaunchPadItemDTO o1, LaunchPadItemDTO o2){
//				return o1.getId().intValue() - o2.getId().intValue();
//			}
//		});
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

		if(null == defalultItems || defalultItems.size() == 0){
			defalultItems = overrideItems;
			return defalultItems;
		}

		if(null == overrideItems || overrideItems.size() == 0){
			return defalultItems;
		}

		boolean flag = false;
		List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();
		for(LaunchPadItem d : defalultItems){
			for(LaunchPadItem o : overrideItems){
				//非覆盖
				if(o.getApplyPolicy() == ApplyPolicy.DEFAULT.getCode() && !allItems.contains(o)){
					if(d.getItemName().equals(o.getItemName()) && d.getItemGroup().equals(o.getItemGroup())){
						 continue;
					}
					allItems.add(o);
				}else if(o.getApplyPolicy()== ApplyPolicy.OVERRIDE.getCode() && !allItems.contains(o)){
					if(d.getItemName().equals(o.getItemName()) && d.getItemGroup().equals(o.getItemGroup())){
						o.setId(d.getId());
						allItems.add(o);
						flag = true;
					}
				}
			}
			if(!flag)
				allItems.add(d);
			flag = false;
		}
		return allItems;
	}
	
	/**
	 * 1、applyPolicy=1(覆盖)，小范围覆盖大范围，
	 * 用户自定义的，直接根据itemId比较，系统配置的覆盖，根据itemName进行比较
	 * 2、applyPolicy=2(恢复)，直接忽略即可
	 * @param defalultLayouts
	 * @param overrideLayouts
	 * @return
	 */
	private List<LaunchPadLayout> overrideOrRevertLayouts(List<LaunchPadLayout> defalultLayouts, List<LaunchPadLayout> overrideLayouts) {

		if(defalultLayouts == null || overrideLayouts == null) return null;
		boolean flag = false;
		List<LaunchPadLayout> allLayouts = new ArrayList<LaunchPadLayout>();
		for(LaunchPadLayout d : defalultLayouts){
			for(LaunchPadLayout o : overrideLayouts){
				//非覆盖
				if(o.getApplyPolicy() == ApplyPolicy.DEFAULT.getCode() && !allLayouts.contains(o)){
					allLayouts.add(o);
				}else if(!allLayouts.contains(o)&&o.getApplyPolicy()== ApplyPolicy.OVERRIDE.getCode()&&d.getName().equals(o.getName()) && d.getNamespaceId().equals(o.getNamespaceId())){
					o.setId(d.getId());
					allLayouts.add(o);
					flag = true;
					break;
				}
			}
			if(!flag)
				allLayouts.add(d);
			flag = false;
		}
		return allLayouts;
	}
	
	/**
	 * 1、applyPolicy=1(覆盖)，小范围覆盖大范围，
	 * 用户自定义的，直接根据itemId比较，系统配置的覆盖，根据itemName进行比较
	 * 2、applyPolicy=2(恢复)，直接忽略即可
	 * @param defalultItems
	 * @param userItems
	 * @return
	 */
	private List<LaunchPadItem> overrideUserItems(List<LaunchPadItem> defalultItems, List<UserLaunchPadItem> userItems) {

		if(defalultItems == null || userItems == null) return null;
		for(LaunchPadItem d : defalultItems){
			for(UserLaunchPadItem o : userItems){
				if(d.getItemName().equals(o.getItemName())){
					if(ApplyPolicy.fromCode(o.getApplyPolicy()) == ApplyPolicy.OVERRIDE){
						d.setDisplayFlag(o.getDisplayFlag());

						if(ActionType.fromCode(d.getActionType()) == ActionType.MORE_BUTTON || ActionType.fromCode(d.getActionType()) == ActionType.ALL_BUTTON){
							d.setDefaultOrder(10000);
						}else {
							d.setDefaultOrder(o.getDefaultOrder());
						}

					}
				}
			}
		}
		return defalultItems;
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
    public LaunchPadLayoutDTO getLaunchPadBaseLayout(GetLaunchPadLayoutCommand cmd) {
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
        LaunchPadLayoutDTO launchPadLayoutDTO = ConvertHelper.convert(launchPadLayout, LaunchPadLayoutDTO.class);
        if (launchPadLayoutDTO != null) {
            LayoutJsonDTO layoutJsonDTO = (LayoutJsonDTO)StringHelper.fromJsonString(launchPadLayoutDTO.getLayoutJson(), LayoutJsonDTO.class);
            if (layoutJsonDTO != null && !CollectionUtils.isEmpty(layoutJsonDTO.getGroups())) {
                for (ItemGroupDTO itemGroupDTO: layoutJsonDTO.getGroups()) {
                    if (!StringUtils.isBlank(itemGroupDTO.getTitleUri())) {
                        String url = contentServerService.parserUri(itemGroupDTO.getTitleUri());
                        itemGroupDTO.setTitleUrl(url);
                    }
                }
            }
            launchPadLayoutDTO.setLayoutJson(StringHelper.toJsonString(layoutJsonDTO));
        }
        return launchPadLayoutDTO;

    }

    @Override
	public LaunchPadLayoutDTO getLastLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd, ScopeType scopeType, Long scopeId){
		if(cmd.getName() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name paramter.name is null");
		}
		if(cmd.getVersionCode() == null)
			cmd.setVersionCode(0L);
		List<LaunchPadLayoutDTO> results = getLaunchPadLayoutByVersionCode(cmd, scopeType,scopeId);
		if(results != null && !results.isEmpty()){
			LaunchPadLayoutDTO dto =  results.get(0);
			populateLaunchPadLayoutDTO(dto);
			return dto;
		}
		return null;
	}

	private void populateLaunchPadLayoutDTO(LaunchPadLayoutDTO dto){
		if(dto == null || StringUtils.isEmpty(dto.getBgImageUri()) || dto.getId() == null){
			return;
		}

		String url = contentServerService.parserUri(dto.getBgImageUri(), LaunchPadLayoutDTO.class.getSimpleName(), dto.getId());

		dto.setBgImageUrl(url);
	}

	@Override
    public LaunchPadLayoutDTO getLastLaunchPadLayoutByScene(@Valid GetLaunchPadLayoutBySceneCommand cmd) {
//	    User user = UserContext.current().getUser();
//        SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
//
//        SceneTypeInfo sceneInfo = sceneService.getBaseSceneTypeByName(sceneToken.getNamespaceId(), sceneToken.getScene());
//        String baseScene = sceneToken.getScene();
//        if(sceneInfo != null) {
//            baseScene = sceneInfo.getName();
//            if(LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Scene type is changed, sceneToken={}, newScene={}", sceneToken, sceneInfo.getName());
//            }
//        } else {
//            LOGGER.error("Scene is not found, cmd={}, sceneToken={}", cmd, sceneToken);
//        }
//        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
//        Community community = null;
//        ScopeType scopeType = null;
//        Long scopeId = null;
//		//检查游客是否能继续访问此场景 by sfyan 20161009
//		userService.checkUserScene(sceneType);
//        switch(sceneType) {
//        case DEFAULT:
//        case PARK_TOURIST:
//            community = communityProvider.findCommunityById(sceneToken.getEntityId());
//            if(community != null) {
//            	scopeId = sceneToken.getEntityId();
//            	scopeType = ScopeType.COMMUNITY;
//            }else{
//            	LOGGER.warn("community not found, sceneToken=" + sceneToken);
//            }
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
//            	scopeId = community.getId();
//            	scopeType = ScopeType.COMMUNITY;
//            }else{
//            	LOGGER.warn("community not found, sceneToken=" + sceneToken);
//            }
//            break;
//        case PM_ADMIN:// 无小区ID
//        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
//        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
//        	scopeId = sceneToken.getEntityId();
//        	scopeType = ScopeType.ORGANIZATION;
//            break;
//        default:
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
//            break;
//        }
//


        ScopeType scopeType = null;
        Long scopeId = null;
		String baseScene = SceneType.PARK_TOURIST.getCode();

		//实际上只存在两种场景，一是公司场景，一是园区场景。家庭场景最后会转换成园区场景。
        AppContext appContext = UserContext.current().getAppContext();
        if(appContext.getOrganizationId() != null){
        	scopeType = ScopeType.ORGANIZATION;
        	scopeId = appContext.getOrganizationId();
		}else if(appContext.getCommunityId() != null){
			scopeId = appContext.getCommunityId();
			scopeType = ScopeType.COMMUNITY;
		}else if(appContext.getFamilyId() != null){
			FamilyDTO family = familyProvider.getFamilyById(appContext.getFamilyId());
			if(family != null) {
				Community community = communityProvider.findCommunityById(family.getCommunityId());
				if(community != null) {
					scopeId = community.getId();
					scopeType = ScopeType.COMMUNITY;
				}else {
					LOGGER.error("community not found, family.id =" + appContext.getFamilyId());
				}
			} else {
				LOGGER.error("family not found, family.id =" + appContext.getFamilyId());
			}
		}


        GetLaunchPadLayoutByVersionCodeCommand getCmd = new GetLaunchPadLayoutByVersionCodeCommand();
        getCmd.setVersionCode(cmd.getVersionCode());
        getCmd.setName(cmd.getName());
        getCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        getCmd.setSceneType(baseScene);
        
        return getLastLaunchPadLayoutByVersionCode(getCmd, scopeType, scopeId);
	}

	@Override
	public List<LaunchPadLayoutDTO> getLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd, ScopeType scopeType, Long scopeId){
		if(cmd.getVersionCode() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid versionCode paramter.versionCode is null");
		}

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
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
		
		//先看社区或者机构是否有定制的layout，没有则查询All的layout，然后看社区或者机构的场景下是否有覆盖新增的layout，再看特定的园区和社区有覆盖新增的layout，合并起来。 by sfyan 20160628
		List<LaunchPadLayoutDTO> results = new ArrayList<LaunchPadLayoutDTO>();
		List<LaunchPadLayout> launchPadLayouts = this.launchPadProvider.findLaunchPadItemsByVersionCode(namespaceId, sceneType, cmd.getName(),cmd.getVersionCode(), scopeType, scopeId);
		
		// 对于机构实体，其有可能是为机构定制，也有可能是为机构所在园区定制，以机构定制优先 by lqs 20160730
        int orgLayoutsize = (launchPadLayouts == null) ? 0 : launchPadLayouts.size();
        int cmntyLayoutSize = 0;
        Long communityId = null;
		if((launchPadLayouts == null || launchPadLayouts.size() == 0) && scopeType == ScopeType.ORGANIZATION) {
		    OrganizationDTO organization = organizationService.getOrganizationById(scopeId);
		    communityId = organization.getCommunityId();
		    if(organization != null && communityId != null) {
		        launchPadLayouts = this.launchPadProvider.findLaunchPadItemsByVersionCode(namespaceId, sceneType, cmd.getName(),cmd.getVersionCode(), ScopeType.COMMUNITY, communityId);
		        cmntyLayoutSize = (launchPadLayouts == null) ? 0 : launchPadLayouts.size();
		    }
		}
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Check custom launchpad layouts, namespaceId={}, sceneType={}, organizationId={}, communityId={}, orgLayoutsize={}, cmntyLayoutSize={}", 
                namespaceId, sceneType, scopeId, communityId, orgLayoutsize, cmntyLayoutSize);
        }
		
		for (LaunchPadLayout launchPadLayout : launchPadLayouts) {
			if(ApplyPolicy.fromCode(launchPadLayout.getApplyPolicy()) == ApplyPolicy.CUSTOMIZED){
				results.add(ConvertHelper.convert(launchPadLayout, LaunchPadLayoutDTO.class));
			}
		}
		if(results.size() == 0){
			List<LaunchPadLayout> allLaunchPadLayouts = this.launchPadProvider.findLaunchPadItemsByVersionCode(namespaceId, sceneType, cmd.getName(),cmd.getVersionCode(), ScopeType.ALL, 0L);
			List<LaunchPadLayout> defLaunchPadLayouts = this.launchPadProvider.findLaunchPadItemsByVersionCode(namespaceId, sceneType, cmd.getName(),cmd.getVersionCode(), scopeType, 0L);
			if(defLaunchPadLayouts.size() > 0)
				allLaunchPadLayouts = overrideOrRevertLayouts(allLaunchPadLayouts, defLaunchPadLayouts);
			
			if(launchPadLayouts.size() > 0)
				allLaunchPadLayouts = overrideOrRevertLayouts(allLaunchPadLayouts, launchPadLayouts);
			
			for (LaunchPadLayout launchPadLayout : allLaunchPadLayouts) {
				results.add(ConvertHelper.convert(launchPadLayout, LaunchPadLayoutDTO.class));
			}
		}
		
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
	
	

	@Override
	public void reorderLaunchPadItemByScene(ReorderLaunchPadItemBySceneCommand cmd, ItemDisplayFlag itemDisplayFlag){
		User user = UserContext.current().getUser();
		Long userId = user.getId();
	    SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
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
	    Community community = null;
	    SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
	       switch(sceneType) {
	       case DEFAULT:
	       case PARK_TOURIST:
	           community = communityProvider.findCommunityById(sceneToken.getEntityId());
	           if(community == null) {
	        	   	LOGGER.error("community not found, sceneToken=" + sceneToken);
	   				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	   					"community not found.");
	           }
	           
	           this.reorderLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, cmd.getSorts(), itemDisplayFlag);
	           break;
	       case FAMILY:
	           FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
	           if(family != null) {
	               	community = communityProvider.findCommunityById(family.getCommunityId());
	           } else {
	        	   	LOGGER.error("Family not found, sceneToken=" + sceneToken);
	   			 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	   					"Family not found.");
	           }
	           if(community == null) {
	        	   LOGGER.error("community not found, sceneToken=" + sceneToken);
	           }

	           this.reorderLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, cmd.getSorts(), itemDisplayFlag);
	           break;
	       case PM_ADMIN:
	       case ENTERPRISE: 
	       case ENTERPRISE_NOAUTH: 
	    	   this.reorderLaunchPadItem(userId, EntityType.ORGANIZATIONS.getCode(), sceneToken.getEntityId(), baseScene, cmd.getSorts(), itemDisplayFlag);
	           break;
	       default:
	           LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
	           break;
	       }
	}

	@Override
	public void editLaunchPadItemByScene(EditLaunchPadItemBySceneCommand cmd){
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
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

		if(null == cmd.getDelItemIds()){
			LOGGER.debug("del launch pad item list is null.");
			cmd.setDelItemIds(new ArrayList<Long>());
		}

		if(null == cmd.getAddItemIds()){
			LOGGER.debug("add launch pad item list is null.");
			cmd.setAddItemIds(new ArrayList<Long>());
		}
		GetLaunchPadItemsCommand command = new GetLaunchPadItemsCommand();
		command.setNamespaceId(UserContext.getCurrentNamespaceId());
		command.setItemGroup(ItemGroup.BIZS.getCode());
		command.setItemLocation("/home");
		GetLaunchPadItemsCommandResponse response = null;
		List<LaunchPadItemDTO> launchPadItemDTOs = null;

		Community community = null;
		SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
		switch(sceneType) {
			case DEFAULT:
			case PARK_TOURIST:
				community = communityProvider.findCommunityById(sceneToken.getEntityId());
				if(community == null) {
					LOGGER.error("community not found, sceneToken=" + sceneToken);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"community not found.");
				}
				command.setCommunityId(community.getId());
				command.setSceneType(baseScene);
				response = this.getLaunchPadItems(command, null);
				launchPadItemDTOs = response.getLaunchPadItems();
				this.editLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, cmd.getDelItemIds(), cmd.getAddItemIds(), launchPadItemDTOs);
				break;
			case FAMILY:
				FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
				if(family != null) {
					community = communityProvider.findCommunityById(family.getCommunityId());
				} else {
					LOGGER.error("Family not found, sceneToken=" + sceneToken);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Family not found.");
				}
				if(community == null) {
					LOGGER.error("community not found, sceneToken=" + sceneToken);
				}
				command.setCommunityId(community.getId());
				command.setSceneType(baseScene);
				response = this.getLaunchPadItems(command, null);
				launchPadItemDTOs = response.getLaunchPadItems();
				this.editLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, cmd.getDelItemIds(), cmd.getAddItemIds(), launchPadItemDTOs);
				break;
			case PM_ADMIN:
			case ENTERPRISE:
			case ENTERPRISE_NOAUTH:
				GetLaunchPadItemsByOrgCommand orgCommand = new GetLaunchPadItemsByOrgCommand();
				orgCommand.setItemGroup(ItemGroup.BIZS.getCode());
				orgCommand.setItemLocation("/home");
				orgCommand.setNamespaceId(sceneToken.getNamespaceId());
				orgCommand.setSceneType(baseScene);
				orgCommand.setOrganizationId(sceneToken.getEntityId());
				response = this.getMoreItems(orgCommand, null);
				launchPadItemDTOs = response.getLaunchPadItems();
				this.editLaunchPadItem(userId, EntityType.ORGANIZATIONS.getCode(), sceneToken.getEntityId(), baseScene, cmd.getDelItemIds(), cmd.getAddItemIds(), launchPadItemDTOs);
				break;
			default:
				LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
				break;
		}
	}

    @Override
	public UserLaunchPadItemDTO deleteLaunchPadItemByScene(DeleteLaunchPadItemBySceneCommand cmd){
		User user = UserContext.current().getUser();
		Long userId = user.getId();
	    SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
	    Community community = null;
	    GetLaunchPadItemsCommand command = new GetLaunchPadItemsCommand();
	    command.setNamespaceId(UserContext.getCurrentNamespaceId());
	    command.setItemGroup(ItemGroup.BIZS.getCode());
        command.setItemLocation("/home");
	    GetLaunchPadItemsCommandResponse response = null;
	    List<LaunchPadItemDTO> launchPadItemDTOs = null;
	    UserLaunchPadItem userItem = null;
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
	    SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
	       switch(sceneType) {
	       case DEFAULT:
	       case PARK_TOURIST:
	           community = communityProvider.findCommunityById(sceneToken.getEntityId());
	           if(community == null) {
	        	   	LOGGER.error("community not found, sceneToken=" + sceneToken);
	   				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	   					"community not found.");
	           }
	           command.setCommunityId(community.getId());
	           command.setSceneType(baseScene);
	           response = this.getMoreItems(command, null);
	           launchPadItemDTOs = response.getLaunchPadItems();
	           userItem = this.updateUserLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, this.maxOrder(launchPadItemDTOs), cmd.getId(), ItemDisplayFlag.HIDE);
	           break;
	       case FAMILY:
	           FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
	           if(family != null) {
	               	community = communityProvider.findCommunityById(family.getCommunityId());
	           } else {
	        	   	LOGGER.error("Family not found, sceneToken=" + sceneToken);
	   			 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	   					"Family not found.");
	           }
	           if(community == null) {
	        	   LOGGER.error("community not found, sceneToken=" + sceneToken);
	           }
	           command.setCommunityId(community.getId());
	           command.setSceneType(baseScene);
	           response = this.getMoreItems(command, null);
	           launchPadItemDTOs = response.getLaunchPadItems();
	           userItem = this.updateUserLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, this.maxOrder(launchPadItemDTOs), cmd.getId(), ItemDisplayFlag.HIDE);
	           break;
	       case PM_ADMIN:
	       case ENTERPRISE: 
	       case ENTERPRISE_NOAUTH: 
	    	   GetLaunchPadItemsByOrgCommand orgCommand = new GetLaunchPadItemsByOrgCommand();
	    	   orgCommand.setItemGroup(ItemGroup.BIZS.getCode());
	    	   orgCommand.setItemLocation("/home");
	    	   orgCommand.setNamespaceId(sceneToken.getNamespaceId());
	    	   orgCommand.setSceneType(baseScene);
	    	   orgCommand.setOrganizationId(sceneToken.getEntityId());
	    	   response = this.getMoreItems(orgCommand, null);
	    	   launchPadItemDTOs = response.getLaunchPadItems();
	           this.updateUserLaunchPadItem(userId, EntityType.ORGANIZATIONS.getCode(), sceneToken.getEntityId(), baseScene, this.maxOrder(launchPadItemDTOs), cmd.getId(), ItemDisplayFlag.HIDE);
	           break;
	       default:
	           LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
	           break;
	       }
	       
	       return ConvertHelper.convert(userItem, UserLaunchPadItemDTO.class);
	}
	
	@Override
	public UserLaunchPadItemDTO addLaunchPadItemByScene(AddLaunchPadItemBySceneCommand cmd){
		User user = UserContext.current().getUser();
		Long userId = user.getId();
	    SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
	    Community community = null;
	    GetLaunchPadItemsCommand command = new GetLaunchPadItemsCommand();
	    command.setNamespaceId(UserContext.getCurrentNamespaceId());
	    command.setItemGroup(ItemGroup.BIZS.getCode());
        command.setItemLocation("/home");
	    GetLaunchPadItemsCommandResponse response = null;
	    List<LaunchPadItemDTO> launchPadItemDTOs = null;
	    UserLaunchPadItem userItem = null;
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
	    SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
	       switch(sceneType) {
	       case DEFAULT:
	       case PARK_TOURIST:
	           community = communityProvider.findCommunityById(sceneToken.getEntityId());
	           if(community == null) {
	        	   	LOGGER.error("community not found, sceneToken=" + sceneToken);
	   				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	   					"community not found.");
	           }
	           
	           command.setCommunityId(community.getId());
	           command.setSceneType(baseScene);
	           response = this.getLaunchPadItems(command, null);
	           launchPadItemDTOs = response.getLaunchPadItems();
	           this.updateUserLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, this.maxOrder(launchPadItemDTOs), cmd.getId(), ItemDisplayFlag.DISPLAY);
	           break;
	       case FAMILY:
	           FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
	           if(family != null) {
	               	community = communityProvider.findCommunityById(family.getCommunityId());
	           } else {
	        	   	LOGGER.error("Family not found, sceneToken=" + sceneToken);
	   			 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	   					"Family not found.");
	           }
	           if(community == null) {
	        	   LOGGER.error("community not found, sceneToken=" + sceneToken);
	           }
	           command.setCommunityId(community.getId());
	           command.setSceneType(baseScene);
	           response = this.getLaunchPadItems(command, null);
	           launchPadItemDTOs = response.getLaunchPadItems();
	          
	           userItem = this.updateUserLaunchPadItem(userId, EntityType.COMMUNITY.getCode(), community.getId(), baseScene, this.maxOrder(launchPadItemDTOs), cmd.getId(), ItemDisplayFlag.DISPLAY);
	           break;
	       case PM_ADMIN:
	       case ENTERPRISE: 
	       case ENTERPRISE_NOAUTH: 
	    	   GetLaunchPadItemsByOrgCommand orgCommand = new GetLaunchPadItemsByOrgCommand();
	    	   orgCommand.setItemGroup(ItemGroup.BIZS.getCode());
	    	   orgCommand.setItemLocation("/home");
	    	   orgCommand.setNamespaceId(sceneToken.getNamespaceId());
	    	   orgCommand.setSceneType(baseScene);
	    	   orgCommand.setOrganizationId(sceneToken.getEntityId());
	    	   response = this.getLaunchPadItems(orgCommand, null);
	    	   launchPadItemDTOs = response.getLaunchPadItems();
	           userItem = this.updateUserLaunchPadItem(userId, EntityType.ORGANIZATIONS.getCode(), sceneToken.getEntityId(), baseScene,  this.maxOrder(launchPadItemDTOs), cmd.getId(), ItemDisplayFlag.DISPLAY);
	           break;
	       default:
	           LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
	           break;
	       }

	       return ConvertHelper.convert(userItem, UserLaunchPadItemDTO.class);
	}

	@Override
	public List<ItemServiceCategryDTO> listItemServiceCategries(){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long userId = UserContext.current().getUser().getId();
		List<ItemServiceCategry> itemServiceCategries = new ArrayList<>();
		return itemServiceCategries.stream().map( r -> {
			ItemServiceCategryDTO dto = ConvertHelper.convert(r, ItemServiceCategryDTO.class);
			dto.setIconUrl(parserUri(r.getIconUri(), EntityType.USER.getCode(), userId));
			return dto;
		}).collect(Collectors.toList());
	}

	private Integer maxOrder(List<LaunchPadItemDTO> launchPadItemDTOs){
		Integer order = 0;
		if(null != launchPadItemDTOs && launchPadItemDTOs.size() > 0){
     	   LaunchPadItemDTO launchPadItemDTO = launchPadItemDTOs.get(launchPadItemDTOs.size() - 1);
     	   if(ActionType.fromCode(launchPadItemDTO.getActionType()) == ActionType.MORE_BUTTON || ActionType.fromCode(launchPadItemDTO.getActionType()) == ActionType.ALL_BUTTON){
     		   if(launchPadItemDTOs.size() > 1){
     			   order = launchPadItemDTOs.get(launchPadItemDTOs.size() - 2).getDefaultOrder();
     		   }
     	   }else{
     		   order = launchPadItemDTOs.get(launchPadItemDTOs.size() - 1).getDefaultOrder();
     	   }
        }
        if(null != order){
     	   order = order + 1;
        }else{
     	   order = 1;
        }
        return order;
	}
	
	private UserLaunchPadItem updateUserLaunchPadItem(Long userId, String ownerType, Long ownerId, String sceneType, Integer order, Long itemId, ItemDisplayFlag itemDisplayFlag){
		LaunchPadItem padItem = checkLaunchPadItem(itemId, true);
		UserLaunchPadItem userItem = launchPadProvider.getUserLaunchPadItemByOwner(userId, sceneType, ownerType, ownerId, padItem.getItemName());
		if(userItem == null){
			userItem = new UserLaunchPadItem();
			userItem.setItemName(padItem.getItemName());
			userItem.setItemId(itemId);
			userItem.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
			userItem.setDefaultOrder(order);
			userItem.setDisplayFlag(itemDisplayFlag.getCode());
			userItem.setOwnerId(ownerId);
			userItem.setOwnerType(ownerType);
			userItem.setSceneType(sceneType);
			userItem.setUserId(userId);
			userItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			userItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			launchPadProvider.createUserLaunchPadItem(userItem);
		}else{
			if(null != order){
				userItem.setDefaultOrder(order);
			}
			userItem.setDisplayFlag(itemDisplayFlag.getCode());
			userItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			launchPadProvider.updateUserLaunchPadItemById(userItem);
		}
		
		return userItem;
	}
	
	private void reorderLaunchPadItem(Long userId, String ownerType, Long ownerId, String sceneType, List<LaunchPadItemSort> sorts, ItemDisplayFlag itemDisplayFlag){

		if(null == sorts){
			LOGGER.debug("LaunchPadItemSort list is null");
			return;
		}


		dbProvider.execute((TransactionStatus status) ->{
			/**
			 * 重新添加用户排序
			 */
			for (LaunchPadItemSort launchPadItemSort : sorts) {
				this.updateUserLaunchPadItem(userId, ownerType, ownerId, sceneType, launchPadItemSort.getDefaultOrder(), launchPadItemSort.getId(), itemDisplayFlag);
			}

			return null;
		});
	}

	private void editLaunchPadItem(Long userId, String ownerType, Long ownerId, String sceneType, List<Long> delItemIds, List<Long> addItemIds, List<LaunchPadItemDTO> launchPadItemDTOs){

		List<Long> items = new ArrayList<Long>();

		if(null == launchPadItemDTOs){
			launchPadItemDTOs = new ArrayList<LaunchPadItemDTO>();
			LOGGER.debug("lanch pad item list is null");
		}

		for (LaunchPadItemDTO dto: launchPadItemDTOs) {
			if(delItemIds.contains(dto.getId()) || ActionType.fromCode(dto.getActionType()) == ActionType.MORE_BUTTON || ActionType.fromCode(dto.getActionType()) == ActionType.ALL_BUTTON ){
			}else{
				items.add(dto.getId());
			}
		}
		items.addAll(addItemIds);
		dbProvider.execute((TransactionStatus status) ->{

			Integer order = 1;

			/**
			 * 删除服务广场的item
			 */
			for (Long delItemId : delItemIds) {
				this.updateUserLaunchPadItem(userId, ownerType, ownerId, sceneType, null, delItemId, ItemDisplayFlag.HIDE);
			}

			/**
			 * 添加服务广场的item
			 */
			for (Long itemId : items) {
				this.updateUserLaunchPadItem(userId, ownerType, ownerId, sceneType, order ++, itemId, ItemDisplayFlag.DISPLAY);
			}
			return null;
		});
	}


	@Override
	public SearchContentsBySceneReponse searchLaunchPadItemByScene(SearchContentsBySceneCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();

		//TODO 标准版要求没有场景，sceneTokenDTO固定为null，业务可能需要修改。有需要的话可以用 UserContext.current().getAppContext()的数据
		AppContext appContext = UserContext.current().getAppContext();
		//SceneTokenDTO sceneTokenDto = WebTokenGenerator.getInstance().fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
//		Integer namespaceId = sceneTokenDto.getNamespaceId();
//		String sceneType = sceneTokenDto.getScene();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		SearchTypes searchType = userService.getSearchTypes(namespaceId, SearchContentType.LAUNCHPADITEM.getCode());

		//根据场景获取应用scope：配置为all和user的固定选择，配置为organization和community的根据场景sceneType获取 
		//switch内的逻辑根据this.getLaunchPadItemsByScene方法改编
		//add by yanjun 20170419
		Map<Byte, Long> scopeMap = new HashMap<Byte, Long>();		
		scopeMap.put(ScopeType.USER.getCode(), userId);

		//园区的item包括当前园区和0园区的，不能使用一个map  add by yanjun
		Map<Byte, Long> defaultScopeMap = new HashMap<Byte, Long>();
		defaultScopeMap.put(ScopeType.ALL.getCode(), 0L);
		defaultScopeMap.put(ScopeType.COMMUNITY.getCode(), 0L);
		defaultScopeMap.put(ScopeType.RESIDENTIAL.getCode(), 0L);
//
//		if(SceneType.fromCode(sceneType) != null){
//			switch(SceneType.fromCode(sceneType)) {
//			case DEFAULT:
//			case PARK_TOURIST:
		//TODO 标准版要求没有场景，sceneTokenDTO固定为null，业务可能需要修改。有需要的话可以用 UserContext.current().getAppContext()的数据
				scopeMap.put(ScopeType.COMMUNITY.getCode(), appContext.getCommunityId());
//				break;
//			case FAMILY:
//				FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
//				if(family != null) {
//					scopeMap.put(ScopeType.COMMUNITY.getCode(), family.getCommunityId());
//				}
//				break;
//			case PM_ADMIN:// 无小区ID
//			case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
//			case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
//				scopeMap.put(ScopeType.ORGANIZATION.getCode(), sceneTokenDto.getEntityId());
//				OrganizationDTO org = organizationService.getOrganizationById(sceneTokenDto.getEntityId());
//				if(org != null) {
//					scopeMap.put(ScopeType.COMMUNITY.getCode(), org.getCommunityId());
//				}
//				break;
//			}
//		}
		
		//SearchTypes searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.LAUNCHPADITEM.getCode());

		//TODO 标准版要求没有场景，sceneTokenDTO固定为null，业务可能需要修改。有需要的话可以用 UserContext.current().getAppContext()的数据
		String sceneType = PARK_TOURIST.getCode();
		SceneTypeInfo sceneInfo = sceneService.getBaseSceneTypeByName(namespaceId, sceneType);
		if(sceneInfo != null) {
			sceneType = sceneInfo.getName();
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Scene type is changed, appContext={}, newScene={}", appContext, sceneInfo.getName());
			}
		} else {
			LOGGER.error("Scene is not found, cmd={}, appContext={}", cmd, appContext);
		}

		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();
		Integer offset = pageSize * Integer.valueOf(pageAnchor.intValue());
		List<LaunchPadItem> launchPadItems= this.launchPadProvider.searchLaunchPadItemsByKeyword(namespaceId, sceneType, scopeMap, defaultScopeMap, cmd.getKeyword(), offset, pageSize + 1);

		//TODO 标准版要求没有场景，sceneTokenDTO固定为null，业务可能需要修改。有需要的话可以用 UserContext.current().getAppContext()的数据
		//如果没有PM_ADMIN定制的，同样的范围查询PARK_TOURIST
		//if((launchPadItems == null || launchPadItems.size() == 0) && SceneType.fromCode(sceneType) == PM_ADMIN){
		if(launchPadItems == null || launchPadItems.size() == 0){
			launchPadItems= this.launchPadProvider.searchLaunchPadItemsByKeyword(namespaceId, PARK_TOURIST.getCode(), scopeMap, defaultScopeMap, cmd.getKeyword(), offset, pageSize + 1);
		}

		// 处理分页
		Long nextPageAnchor = null;
		if (launchPadItems.size() > pageSize) {
			launchPadItems.remove(launchPadItems.size() - 1);
			nextPageAnchor = pageAnchor + 1;
		}
		
		List<LaunchPadItemDTO> dtos = new ArrayList<LaunchPadItemDTO>();
		launchPadItems.forEach(r ->{
			//如果item有别名AliasIconUri，则使用别名替换IconUri，得到统一的圆形风格图标
			LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
			if(itemDTO.getAliasIconUri() != null && !itemDTO.getAliasIconUri().equals("")){
				itemDTO.setIconUri(itemDTO.getAliasIconUri());
			}
			itemDTO.setIconUrl(parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId));
			itemDTO.setAliasIconUrl(parserUri(itemDTO.getAliasIconUri(),EntityType.USER.getCode(),userId));
			
			itemDTO.setSearchTypeId(searchType.getId());
			itemDTO.setSearchTypeName(searchType.getName());
			itemDTO.setContentType(searchType.getContentType());
			dtos.add(itemDTO);
		});

		refreshActionData(dtos);

		response.setLaunchPadItemDtos(dtos);
		response.setNextPageAnchor(nextPageAnchor);
		return response;
	}

	@Override
	public List<IndexDTO> listIndexDtos(Integer namespaceId, Long userId) {

		CrossShardListingLocator locator = new CrossShardListingLocator();

		//先查预览版本，如果预览版本没有，则使用正式版本
        PortalVersionUser portalVersionUser = this.portalVersionUserProvider.findPortalVersionUserByUserId(userId);
		List<LaunchPadIndex> launchPadIndices = new ArrayList<>();
        if (portalVersionUser != null) {
            launchPadIndices = launchPadIndexProvider.queryLaunchPadIndexs(locator, 100, (locator1, query) -> {
                query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.PREVIEW_VERSION_ID.eq(portalVersionUser.getVersionId()));
                query.addOrderBy(Tables.EH_LAUNCH_PAD_INDEXS.DEFAULT_ORDER);
                return query;
            });
        }
        if (CollectionUtils.isEmpty(launchPadIndices)) {
            launchPadIndices = launchPadIndexProvider.queryLaunchPadIndexs(locator, 100, (locator1, query) -> {
                query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.STATUS.isNotNull());
                query.addOrderBy(Tables.EH_LAUNCH_PAD_INDEXS.DEFAULT_ORDER);
                return query;
            });
        }

		List<IndexDTO> dtos = new ArrayList<>();
		for (LaunchPadIndex index: launchPadIndices){
			if(IndexType.fromCode(index.getType()) == IndexType.CONTAINER){
				Container container = ConvertHelper.convert(index.getConfigJson(), Container.class);

				//工作台特殊逻辑
				if(LayoutType.fromCode(container.getLayoutType()) == LayoutType.WORKPLATFORM){
					ListAddressUsersCommand cmd = new ListAddressUsersCommand();
					cmd.setStatus(GroupMemberStatus.ACTIVE.getCode());
					cmd.setType(AddressUserType.ORGANIZATION.getCode());
					cmd.setWorkPlatformFlag(TrueOrFalseFlag.TRUE.getCode());
					ListAddressUsersResponse response = userService.listAddressUsers(cmd);
					//不显示工作台
					if(response == null || response.getDtos() == null || response.getDtos().size() == 0){
						continue;
					}
				}
			}

			//TODO
			if(IndexType.fromCode(index.getType()) == IndexType.APPLICATION){

			}

			IndexDTO dto = processIndexDto(index);

			dtos.add(dto);
		}
		return dtos;
	}

	private IndexDTO processIndexDto(LaunchPadIndex index){

		IndexDTO dto = ConvertHelper.convert(index, IndexDTO.class);

		if(index.getIconUri() != null){
			String inconUrl = contentServerService.parserUri(index.getIconUri(), IndexDTO.class.getSimpleName(), dto.getId());
			dto.setIconUrl(inconUrl);

		}

		if(index.getSelectedIconUri() != null){
			String selectInconUrl = contentServerService.parserUri(index.getSelectedIconUri(), IndexDTO.class.getSimpleName(), dto.getId());
			dto.setSelectIconUrl(selectInconUrl);
		}

		return dto;

	}

	@Override
	public ListBannersResponse listBanners(ListBannersCommand cmd) {
        ListBannersResponse response = new ListBannersResponse();

		//String sceneToken = getSceneTokenByCommunityId(cmd.getContext().getCommunityId());
		if(UserContext.current().getAppContext() == null){
			UserContext.current().setAppContext(cmd.getContext());
		}

		GetBannersBySceneCommand bannerCmd = new GetBannersBySceneCommand();
		BannersInstanceConfig config = (BannersInstanceConfig) StringHelper.fromJsonString(cmd.getInstanceConfig(), BannersInstanceConfig.class);
		if (config != null) {
            bannerCmd.setCategoryId(config.getCategoryId());
            if (config.getMoreAppId() != null) {
                ServiceModuleApp serviceModuleApp = this.serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getMoreAppId());
                if (serviceModuleApp != null) {
                    ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModuleApp.getModuleId());

                    Byte clientHandlerType = 0;
                    String host = "";
                    if(module != null){
                        clientHandlerType = module.getClientHandlerType();
                        host = module.getHost();
                    }

                    String appConfig = launchPadService.refreshActionData(serviceModuleApp.getInstanceConfig());
                    RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(serviceModuleApp.getModuleId(), serviceModuleApp.getOriginId(), serviceModuleApp.getName(), appConfig, null, null, null, clientHandlerType);

                    if(StringUtils.isEmpty(host)){
                        host  = "default";
                    }

                    String router = "zl://" + host + routerInfo.getPath() + "?" + routerInfo.getQuery();
                    response.setMoreRouter(router);
                }
            }
		}
		//bannerCmd.setSceneToken(sceneToken);
		List<BannerDTO> bannerDTOS =  bannerService.getBannersBySceneNew(bannerCmd);

		response.setDtos(bannerDTOS);
		return response;
	}

	@Override
	public ListOPPushCardsResponse listOPPushCards(ListOPPushCardsCommand cmd) {
		UserContext.current().setAppContext(cmd.getContext());

		ListOPPushCardsResponse response = new ListOPPushCardsResponse();

		OPPush oppush = (OPPush)StringHelper.fromJsonString(cmd.getInstanceConfig(), OPPush.class);

		if(oppush.getAppId() == null){
			return response;
		}

		ServiceModuleApp serviceModuleApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(oppush.getAppId());

		if(serviceModuleApp == null || serviceModuleApp.getModuleId() == null){
			return response;
		}
		response.setAppId(oppush.getAppId());
		response.setActionType(serviceModuleApp.getActionType());

		//处理方式
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(serviceModuleApp.getModuleId());
		response.setModuleId(serviceModule.getId());
		response.setClientHandlerType(serviceModule.getClientHandlerType());

		OPPushHandler opPushHandler = getOPPushHandler(serviceModuleApp.getModuleId());
		if(opPushHandler != null){
			List<OPPushCard> opPushCards = opPushHandler.listOPPushCard(cmd.getLayoutId(), cmd.getInstanceConfig(), cmd.getContext());
			response.setCards(opPushCards);

			String newConfig = opPushHandler.refreshInstanceConfig(serviceModuleApp.getInstanceConfig());

			serviceModuleApp.setInstanceConfig(newConfig);

			PortalPublishHandler portalPublishHandler = portalService.getPortalPublishHandler(serviceModuleApp.getModuleId());
			String itemActionData = serviceModuleApp.getInstanceConfig();
			if(portalPublishHandler != null){
				HandlerGetItemActionDataCommand handlerCmd = new HandlerGetItemActionDataCommand();
				handlerCmd.setAppId(serviceModuleApp.getId());
				handlerCmd.setAppOriginId(serviceModuleApp.getOriginId());
				itemActionData = portalPublishHandler.getItemActionData(serviceModuleApp.getNamespaceId(), serviceModuleApp.getInstanceConfig(), handlerCmd);
			}


			itemActionData = refreshActionData(itemActionData);
			response.setInstanceConfig(itemActionData);

			RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(serviceModuleApp.getModuleId(), oppush.getAppId(), serviceModuleApp.getName(),itemActionData, null, null, null, serviceModule.getClientHandlerType());
			response.setRouterPath(routerInfo.getPath());
			response.setRouterQuery(routerInfo.getQuery());

			String host = serviceModule.getHost();
            if(org.springframework.util.StringUtils.isEmpty(host)){
                host  = "default";
            }

            String router = "zl://" + host + response.getRouterPath() + "?" + response.getRouterQuery();
            response.setRouter(router);

		}

		return response;
	}




	@Override
	public OPPushHandler getOPPushHandler(Long moduleId) {
		OPPushHandler handler = null;

		if(moduleId != null) {
			String handlerPrefix = OPPushHandler.OPPUSH_ITEMGROUP_TYPE;
			try {
				handler = PlatformContext.getComponent(handlerPrefix + moduleId);
			}catch (Exception ex){
				LOGGER.info("OPPushHandler not exist moduleId = {}", moduleId);
			}

		}

		return handler;
	}


	@Override
	public BulletinsHandler getBulletinsHandler(Long moduleId) {
		BulletinsHandler handler = null;

		if(moduleId != null) {
			String handlerPrefix = BulletinsHandler.BULLETINS_HANDLER_TYPE;
			try {
				handler = PlatformContext.getComponent(handlerPrefix + moduleId);
			}catch (Exception ex){
				LOGGER.info("OPPushHandler not exist moduleId = {}", moduleId);
			}

		}

		return handler;
	}

	@Override
	public String getSceneTokenByCommunityId(Long communityId){
		Community community = communityProvider.findCommunityById(communityId);
		SceneTokenDTO sceneToken = new SceneTokenDTO();
		sceneToken.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());
		sceneToken.setScene(PARK_TOURIST.getCode());
		sceneToken.setEntityId(community.getId());
		sceneToken.setNamespaceId(community.getNamespaceId());
		sceneToken.setUserId(UserContext.currentUserId());

		return WebTokenGenerator.getInstance().toWebToken(sceneToken);
	}


	@Override
	public ListBulletinsCardsResponse listBulletinsCards(ListBulletinsCardsCommand cmd) {

		ListBulletinsCardsResponse response = new ListBulletinsCardsResponse();

		Bulletins bulletins = (Bulletins)StringHelper.fromJsonString(cmd.getInstanceConfig(), Bulletins.class);

		//历史是园区公告，默认使用园区公告
		if(bulletins.getModuleId() == null){
			bulletins.setModuleId(10300L);
		}

		//处理方式
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(bulletins.getModuleId());
		response.setModuleId(serviceModule.getId());
		response.setClientHandlerType(serviceModule.getClientHandlerType());

		BulletinsHandler bulletinsHandler = getBulletinsHandler(bulletins.getModuleId());
		if(bulletinsHandler != null){

			//处理方式rowCount、noticeCount是用于客户端显示的，都不是条数。现在直接查询所有的。
			List<BulletinsCard> cards = bulletinsHandler.listBulletinsCards(bulletins.getAppId(), cmd.getContext(), 1000);
			response.setCards(cards);

			String itemActionData = "{}";
			String title = serviceModule.getName();
			if(bulletins.getAppId() != null){
				ServiceModuleApp serviceModuleApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(bulletins.getAppId());
				itemActionData = serviceModuleApp.getInstanceConfig();
				title = serviceModuleApp.getName();
				if(serviceModuleApp != null){
					PortalPublishHandler portalPublishHandler = portalService.getPortalPublishHandler(serviceModuleApp.getModuleId());
					if(portalPublishHandler != null){
						HandlerGetItemActionDataCommand handlerCmd = new HandlerGetItemActionDataCommand();
						handlerCmd.setAppOriginId(serviceModuleApp.getOriginId());
						handlerCmd.setAppId(serviceModuleApp.getId());

						itemActionData = portalPublishHandler.getItemActionData(serviceModuleApp.getNamespaceId(), serviceModuleApp.getInstanceConfig(), handlerCmd);
					}
				}
			}

			itemActionData = refreshActionData(itemActionData);
			response.setInstanceConfig(itemActionData);

			RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(bulletins.getModuleId(), bulletins.getAppId(), title, itemActionData, null, null, null, serviceModule.getClientHandlerType());
			response.setRouterPath(routerInfo.getPath());
			response.setRouterQuery(routerInfo.getQuery());

			String host = serviceModule.getHost();
            if(org.springframework.util.StringUtils.isEmpty(host)){
                host  = "default";
            }

            String router = "zl://" + host + response.getRouterPath() + "?" + response.getRouterQuery();
            response.setRouter(router);
        }


		return response;
	}


	@Override
	public ListAllAppsResponse listAllApps(ListAllAppsCommand cmd) {
		ListAllAppsResponse response = new ListAllAppsResponse();


		List<Tuple<Byte, Long>> scopes = getScope(cmd.getContext());
		String ownerType = getOwnerType(cmd.getContext());
		Long ownerId = getOwnerId(cmd.getContext());

        List<LaunchPadCategoryDTO> categoryDtos = new ArrayList<>();

		List<ItemServiceCategry> categories = launchPadProvider.listItemServiceCategryByGroupId(cmd.getGroupId(), scopes);

		//1、有分类的--“全部”，按照分类查询。
        //2、没有分类--“更多”，把所有的查询出来，有客户端判断。
		if(categories != null && categories.size() > 0){
			for (ItemServiceCategry categry: categories){

				List<LaunchPadItem> launchPadItems = launchPadProvider.listLaunchPadItemsByGroupId(cmd.getGroupId(), scopes, categry.getName(), null, TrueOrFalseFlag.TRUE.getCode());
				List<AppDTO> appDtos = itemToAppDto(launchPadItems);
				LaunchPadCategoryDTO categoryDto = new LaunchPadCategoryDTO();
                categoryDto.setAppDtos(appDtos);
                categoryDto.setName(categry.getLabel());
				if(categry.getIconUri() != null){
					String url = contentServerService.parserUri(categry.getIconUri(), ItemServiceCategry.class.getSimpleName(), categry.getId());
                    categoryDto.setIconUrl(url);
				}

                categoryDtos.add(categoryDto);
			}

		}else {
			List<LaunchPadItem> launchPadItems = launchPadProvider.listLaunchPadItemsByGroupId(cmd.getGroupId(), scopes, null, null,TrueOrFalseFlag.FALSE.getCode());
			List<AppDTO> appDtos = itemToAppDto(launchPadItems);
			LaunchPadCategoryDTO categoryDto = new LaunchPadCategoryDTO();
            categoryDto.setAppDtos(appDtos);
            categoryDtos.add(categoryDto);
		}

		Long userId = UserContext.currentUserId();

		List<AppDTO> appDtos = new ArrayList<>();

		if(userId != null || userId != 0){
			//查询用户自己编辑的广场信息
			List<UserLaunchPadItem> userItems = launchPadProvider.listUserLaunchPadItemByUserId(userId, cmd.getGroupId(), ownerType, ownerId);
			appDtos = userItemToAppDto(userItems);

		}

		if(appDtos == null || appDtos.size() == 0){
			//默认的
			List<LaunchPadItem> defaultItems = launchPadProvider.listLaunchPadItemsByGroupId(cmd.getGroupId(), scopes, null, ItemDisplayFlag.DISPLAY.getCode(), TrueOrFalseFlag.FALSE.getCode());
			appDtos = itemToAppDto(defaultItems);
		}

		response.setCategoryDtos(categoryDtos);
		response.setDefaultDtos(appDtos);

		return response;
	}


	private List<Tuple<Byte, Long>> getScope(AppContext context){


		List<Tuple<Byte, Long>> list = new ArrayList<>();

		if(context == null){
			return list;
		}

		//communityId必传
		if(context.getOrganizationId() != null){
			list.add(new Tuple<>(ScopeType.ORGANIZATION.getCode(), context.getOrganizationId()));
			list.add(new Tuple<>(ScopeType.ORGANIZATION.getCode(), 0L));
			list.add(new Tuple<>(ScopeType.COMMUNITY.getCode(), context.getCommunityId()));
			list.add(new Tuple<>(ScopeType.COMMUNITY.getCode(), 0L));
			list.add(new Tuple<>(ScopeType.ALL.getCode(), 0L));
		}else if(context.getFamilyId() != null){
			list.add(new Tuple<>(ScopeType.COMMUNITY.getCode(), context.getCommunityId()));
			list.add(new Tuple<>(ScopeType.COMMUNITY.getCode(), 0L));
			list.add(new Tuple<>(ScopeType.RESIDENTIAL.getCode(), context.getCommunityId()));
			list.add(new Tuple<>(ScopeType.RESIDENTIAL.getCode(), 0L));
			list.add(new Tuple<>(ScopeType.ALL.getCode(), 0L));
		}else{
			list.add(new Tuple<>(ScopeType.COMMUNITY.getCode(), context.getCommunityId()));
			list.add(new Tuple<>(ScopeType.COMMUNITY.getCode(), 0L));
			list.add(new Tuple<>(ScopeType.ALL.getCode(), 0L));
		}

		return list;
	}


	private String getOwnerType(AppContext context){
		String ownerType = null;

		if(context == null){
			return null;
		}

		//communityId必传
		if(context.getOrganizationId() != null){
			ownerType = EntityType.ORGANIZATIONS.getCode();
		}else if(context.getFamilyId() != null){
			ownerType =  EntityType.COMMUNITY.getCode();
		}else {
			ownerType =  EntityType.COMMUNITY.getCode();
		}

		return ownerType;
	}

	private Long getOwnerId(AppContext context){
		Long ownerId = 0L;

		if(context == null){
			return ownerId;
		}

		if(context.getOrganizationId() != null){
			ownerId = context.getOrganizationId();
		}else if(context.getFamilyId() != null){
			ownerId = context.getCommunityId();
		}else {
			ownerId = context.getCommunityId();
		}

		return ownerId;
	}

	private List<AppDTO> itemToAppDto(List<LaunchPadItem> items){

		if(items == null || items.size() == 0){
			return null;
		}

		Set<Long> appIds = new HashSet<>();
		for(LaunchPadItem item: items){
			appIds.add(item.getAppId());
		}

		Map<Long, ServiceModuleApp> appMap = getAppMap(new ArrayList<>(appIds));
		Map<Long, ServiceModule> moduleMap = getModuleMap(new ArrayList<>(appIds));


		List<AppDTO> dtos = new ArrayList<>();

		for(LaunchPadItem item: items){
			AppDTO dto = ConvertHelper.convert(item, AppDTO.class);
			ServiceModuleApp app = appMap.get(item.getAppId());

			//“全部/更多”
			if(ActionType.fromCode(item.getActionType()) == ActionType.MORE_BUTTON || ActionType.fromCode(item.getActionType()) == ActionType.ALL_BUTTON){
				app = new ServiceModuleApp();
				app.setModuleId(-10000L);
				app.setOriginId(0L);
			}else if(ActionType.fromCode(item.getActionType()) == ActionType.ROUTER || ActionType.fromCode(item.getActionType()) == ActionType.NAVIGATION){
				//路由 actionType=60、二级门户actionType=2
				app = new ServiceModuleApp();
				app.setModuleId(250000L);
				app.setOriginId(0L);
			}


			if(app != null){
				dto.setModuleId(app.getModuleId());
				ServiceModule serviceModule = moduleMap.get(app.getModuleId());
				Byte clientHandlerType = 0;
				String host = "default";
				if(serviceModule != null){
					dto.setClientHandlerType(serviceModule.getClientHandlerType());

					clientHandlerType = serviceModule.getClientHandlerType();
					host = serviceModule.getHost();

				}

				String actionData = refreshActionData(item.getActionData());

				String path = "/index";
				if(ActionType.fromCode(item.getActionType()) == ActionType.MORE_BUTTON){
					host = "app-management";
					path = "/more";
					dto.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
				}else if(ActionType.fromCode(item.getActionType()) == ActionType.ALL_BUTTON){
					host = "app-management";
					path = "/all";
					dto.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
				}else if(ActionType.fromCode(item.getActionType()) == ActionType.NAVIGATION || ActionType.fromCode(item.getActionType()) == ActionType.ROUTER){
					host = "container";
					path = "/index";
					dto.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
				}

				//填充路由信息
				RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(app.getModuleId(), app.getOriginId(), item.getItemLabel(), actionData, path, null, null, clientHandlerType);
				dto.setRouterPath(routerInfo.getPath());
				dto.setRouterQuery(routerInfo.getQuery());

				if(org.springframework.util.StringUtils.isEmpty(host)){
					host  = "default";
				}

				String router = "zl://" + host + dto.getRouterPath() + "?" + dto.getRouterQuery();
				dto.setRouter(router);

			}

			dto.setName(item.getItemLabel());
			dto.setItemId(item.getId());

			if(!org.springframework.util.StringUtils.isEmpty(item.getIconUri())){
				String url = contentServerService.parserUri(item.getIconUri(), LaunchPadItem.class.getSimpleName(), item.getId());
				dto.setIconUrl(url);
			}

			dtos.add(dto);
		}

		return dtos;
	}


	private List<AppDTO> userItemToAppDto(List<UserLaunchPadItem> items){
		if(items == null || items.size() == 0){
			return null;
		}

		List<Long> itemIds = new ArrayList<>();
		for(UserLaunchPadItem item: items){
			itemIds.add(item.getItemId());
		}

		List<LaunchPadItem> launchPadItems = launchPadProvider.listLaunchPadItemsByIds(itemIds);

		if(launchPadItems == null){
			return null;
		}

		//按照UserLaunchPadItem的顺序对LaunchPadItem重新排序
		List<LaunchPadItem> orderLaunchPadItems = new ArrayList<>();
		for(Long itemId: itemIds){
			for(LaunchPadItem launchPadItem: launchPadItems){
				if(itemId.equals(launchPadItem.getId())){
					orderLaunchPadItems.add(launchPadItem);
					break;
				}
			}
		}


		List<AppDTO> dtos = itemToAppDto(orderLaunchPadItems);

		return dtos;
	}


	private Map<Long, ServiceModuleApp> getAppMap(List<Long> appIds){

		Map<Long, ServiceModuleApp> appMap = new HashMap<>();

		if(appIds == null || appIds.size() == 0){
			return appMap;
		}

		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleAppsByOriginIds(UserContext.getCurrentNamespaceId(), appIds);

		if(apps == null || apps.size() == 0){
			return appMap;
		}

		for(ServiceModuleApp app: apps){
			appMap.put(app.getOriginId(), app);
		}

		return appMap;
	}


	private Map<Long, ServiceModule> getModuleMap(List<Long> appIds){

		Map<Long, ServiceModule> moduleMap = new HashMap<>();

		if(appIds == null || appIds.size() == 0){
			return moduleMap;
		}

		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleAppsByOriginIds(UserContext.getCurrentNamespaceId(), appIds);

		if(apps == null || apps.size() == 0){
			return moduleMap;
		}

		Set<Long> moduleIds = new HashSet<>();

		for(ServiceModuleApp app: apps){
			moduleIds.add(app.getModuleId());
		}

		if(moduleIds.size() == 0){
			return moduleMap;
		}

		List<ServiceModule> serviceModules = serviceModuleProvider.listServiceModule(new ArrayList<>(moduleIds));


		if(serviceModules == null || serviceModules.size() == 0){
			return moduleMap;
		}

		for(ServiceModule module: serviceModules){
			moduleMap.put(module.getId(), module);
		}

		return moduleMap;
	}

	@Override
	public void updateUserApps(UpdateUserAppsCommand cmd) {

		Long userId = UserContext.currentUserId();

		if(userId == null || userId == 0){
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_UNAUTHENTITICATION, "Authentication is required");
		}

		if(cmd.getGroupId() == null || cmd.getItemIds() == null || cmd.getItemIds().size() == 0){
			LOGGER.error("Invalid paramter, cmd = ", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter, cmd = " + cmd);
		}

		String ownerType = getOwnerType(cmd.getContext());
		Long ownerId = getOwnerId(cmd.getContext());


		dbProvider.execute((transactionStatus -> {

			//删除已有的自定义配置
			launchPadProvider.deleteUserLaunchPadItemByUserId(userId, cmd.getGroupId(), ownerType, ownerId);

			List<LaunchPadItem> launchPadItems = launchPadProvider.listLaunchPadItemsByIds(cmd.getItemIds());

			if(launchPadItems == null || launchPadItems.size() == 0){
				LOGGER.error("launchPadItem not found.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"launchPadItem not found.");
			}

			for (LaunchPadItem item: launchPadItems){

				if(!cmd.getGroupId().equals(item.getGroupId())){
					LOGGER.error("launchPadItem not match. item.groupId=" + item.getGroupId() + " cmd.groupId=" + cmd.getGroupId());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"launchPadItem not match. item.groupId=" + item.getGroupId() + " cmd.groupId=" + cmd.getGroupId());
				}

				UserLaunchPadItem userItem = new UserLaunchPadItem();
				userItem.setGroupId(item.getGroupId());
				userItem.setItemName(item.getItemName());
				userItem.setItemId(item.getId());
				userItem.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());

				Integer order = cmd.getItemIds().indexOf(item.getId());

				userItem.setDefaultOrder(order);
				userItem.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
				userItem.setOwnerId(ownerId);
				userItem.setOwnerType(ownerType);
				userItem.setSceneType(item.getSceneType());
				userItem.setUserId(userId);
				userItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				userItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				launchPadProvider.createUserLaunchPadItem(userItem);
			}
			return null;
		}));


	}
}
