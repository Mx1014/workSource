// @formatter:off
package com.everhomes.banner;

import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogOperator;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.launchpad.LaunchPadConstants;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.portal.PortalItemGroup;
import com.everhomes.portal.PortalItemGroupProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.banner.*;
import com.everhomes.rest.banner.BannerOrder;
import com.everhomes.rest.banner.admin.CreateBannerCommand;
import com.everhomes.rest.banner.admin.DeleteBannerCommand;
import com.everhomes.rest.banner.admin.*;
import com.everhomes.rest.banner.admin.UpdateBannerCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.launchpad.ApplyPolicy;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.widget.BannersInstanceConfig;
import com.everhomes.scene.SceneService;
import com.everhomes.scene.SceneTypeInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.rest.banner.BannerStatus.ACTIVE;
import static com.everhomes.rest.banner.BannerStatus.CLOSE;
import static com.everhomes.rest.ui.user.SceneType.PARK_TOURIST;

@Component
public class BannerServiceImpl implements BannerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerServiceImpl.class);
    
    @Autowired
    private BannerProvider bannerProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private PropertyMgrService propertyMgrService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private SceneService sceneService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private AuditLogProvider auditLogProvider;
    
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private LaunchPadService launchPadService;

    @Autowired
    private PortalItemGroupProvider portalItemGroupProvider;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;
    
    @Override
    public List<BannerDTO> getBanners(GetBannersCommand cmd, HttpServletRequest request){
        if(cmd.getCommunityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
        }
        
        long communityId = cmd.getCommunityId();
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
        }
        long startTime = System.currentTimeMillis();
        long cityId = community.getCityId();
        User user = UserContext.current().getUser();
        long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("banner namespaceId = {}", namespaceId);
        }
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
        
        List<Banner> allBanners = new ArrayList<Banner>();

        // 看是否有自定义banner        add by xq.tian  2016/11/01
        // 如果有, 则说明该场景下只需要自定义的banner了, 不需要默认的banner了
        Banner customizedBanner = bannerProvider.findAnyCustomizedBanner(namespaceId, ScopeType.COMMUNITY.getCode(), communityId, sceneType);

        List<Banner> communityBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.COMMUNITY.getCode(), communityId);
        List<Banner> customizedBanners = new ArrayList<Banner>();
        
        for (Banner banner : communityBanners) {
			if(ApplyPolicy.fromCode(banner.getApplyPolicy()) == ApplyPolicy.CUSTOMIZED){
				customizedBanners.add(banner);
			}
		}

        if(customizedBanner != null || customizedBanners.size() > 0){
        	allBanners = customizedBanners;
        }else{
        	//String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
            //query user relate banners
            List<Banner> countryBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(),
                    ScopeType.ALL.getCode(), 0L);
            
            List<Banner> cityBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.CITY.getCode(), cityId);
            
            List<Banner> communityDefBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.COMMUNITY.getCode(), 0L);
            
            if(countryBanners != null)
                allBanners.addAll(countryBanners);
            if(cityBanners != null)
                allBanners = overrideOrRevertBanners(allBanners, cityBanners);
            if(communityDefBanners != null)
                allBanners = overrideOrRevertBanners(allBanners, communityDefBanners);
            if(communityBanners != null)
            	allBanners = overrideOrRevertBanners(allBanners, communityBanners);
        }
        
        List<BannerDTO> result = allBanners.stream().map((Banner r) ->{
           BannerDTO dto = ConvertHelper.convert(r, BannerDTO.class);
           //third url add user token
//           if(dto.getActionType().byteValue() == ActionType.THIRDPART_URL.getCode()){
//               dto.setActionData(parserJson(communityId,dto));
//           }
           dto.setActionData(parserJson(userId,communityId,dto,request));
           dto.setPosterPath(parserUri(dto.getPosterPath(),EntityType.USER.getCode(),userId));
           return dto;
        }).collect(Collectors.toList());
        if(result != null && !result.isEmpty())
            sortBanner(result);
        long endTime = System.currentTimeMillis();
        int size = result == null ? 0 : result.size();
        LOGGER.info("Query banner by communityId complete,communityId=" + communityId + ",size=" + size + ",esplse=" + (endTime - startTime));
        return result;
    }
    
    public List<BannerDTO> getBannersByOrg(GetBannersByOrgCommand cmd, HttpServletRequest request){
        Long organizationId = cmd.getOrganizationId();
//        Community community = communityProvider.findCommunityById(communityId);
//        if(community == null){
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
//        }
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("banner namespaceId = {}", namespaceId);
        }

        String sceneTypeStr = cmd.getCurrentSceneType();
        
        Long communityId = null;
        OrganizationCommunityRequest  organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getOrganizationId());
		if(null != organizationCommunityRequest){
			communityId = organizationCommunityRequest.getCommunityId();
		}
		
		Long commId = communityId;
		
		List<Banner> allBanners = new ArrayList<Banner>();
		
        List<Banner> orgBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneTypeStr, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.ORGANIZATION.getCode(), organizationId);
        // 如果只定制scope为公司的，则只有当前公司才能查到，其它公司就查不到，故补充也按园区查询 by lqs 20160730
        int orgBannerSize = (orgBanners == null) ? 0 : orgBanners.size();
        int cmntyBannerSize = 0;
        if((orgBanners == null || orgBanners.size() == 0) && communityId != null) {
            orgBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneTypeStr, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.COMMUNITY.getCode(), communityId);
            cmntyBannerSize = (orgBanners == null) ? 0 : orgBanners.size();
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Check custom banners, namespaceId={}, sceneType={}, organizationId={}, communityId={}, orgBannersize={}, cmntyBannerSize={}", 
                namespaceId, sceneTypeStr, organizationId, communityId, orgBannerSize, cmntyBannerSize);
        }
		
        List<Banner> customizedBanners = new ArrayList<Banner>();
        
        for (Banner banner : orgBanners) {
			if(ApplyPolicy.fromCode(banner.getApplyPolicy()) == ApplyPolicy.CUSTOMIZED){
				customizedBanners.add(banner);
			}
		}
        // 看是否有自定义banner        add by xq.tian  2016/11/01
        // 如果有, 则说明该场景下只需要自定义的banner了, 不需要默认的banner了
        Banner customizedBanner = bannerProvider.findAnyCustomizedBanner(namespaceId, ScopeType.COMMUNITY.getCode(), communityId, sceneTypeStr);

        if(customizedBanner != null || customizedBanners.size() > 0){
        	allBanners = customizedBanners;
        }else{
        	//String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
            //query user relate banners
            List<Banner> countryBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneTypeStr, cmd.getBannerLocation(), cmd.getBannerGroup(),
                    ScopeType.ALL.getCode(), 0L);
            List<Banner> orgDefBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneTypeStr, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.ORGANIZATION.getCode(), 0L);

            if(countryBanners != null)
                allBanners.addAll(countryBanners);
            if(orgDefBanners != null)
            	allBanners = this.overrideOrRevertBanners(allBanners, orgDefBanners);
            if(orgBanners != null)
            	allBanners = this.overrideOrRevertBanners(allBanners, orgBanners);
        }
        
        List<BannerDTO> result = allBanners.stream().map((Banner r) ->{
            BannerDTO dto = ConvertHelper.convert(r, BannerDTO.class);
            //third url add user token
//            if(dto.getActionType().byteValue() == ActionType.THIRDPART_URL.getCode()){
//                dto.setActionData(parserJson(communityId,dto));
//            }
            if(null != commId){
                // 不加上小区ID会在解释JSON中抛空指针异常 by lqs 20160727
         	   //dto.setActionData(parserJson(userId, null,dto,request));
         	   dto.setActionData(parserJson(userId, commId,dto,request));
            }else{
         	   LOGGER.error("communityId is null. organizationId = {}", cmd.getOrganizationId());
            }
            dto.setPosterPath(parserUri(dto.getPosterPath(),EntityType.USER.getCode(),userId));
            return dto;
         }).collect(Collectors.toList());
         if(result != null && !result.isEmpty())
             sortBanner(result);
         long endTime = System.currentTimeMillis();
         int size = result == null ? 0 : result.size();
         LOGGER.info("Query banner by communityId complete,cmd=" + cmd + ",size=" + size + ",esplse=" + (endTime - startTime));
         return result;
    }
    
    
    /**
	 * 1、applyPolicy=1(覆盖)，小范围覆盖大范围，
	 * 用户自定义的，直接根据itemId比较，系统配置的覆盖，根据itemName进行比较
	 * 2、applyPolicy=2(恢复)，直接忽略即可
	 * @param defalultBanners
	 * @param overrideBanners
	 * @return
	 */
	private List<Banner> overrideOrRevertBanners(List<Banner> defalultBanners, List<Banner> overrideBanners) {

		if(defalultBanners == null || overrideBanners == null) return null;
		boolean flag = false;
		List<Banner> allBanners = new ArrayList<Banner>();
		for(Banner b : defalultBanners){
			for(Banner o : overrideBanners){
				//非覆盖
				if(o.getApplyPolicy() == ApplyPolicy.DEFAULT.getCode() && !allBanners.contains(o)){
					allBanners.add(o);
				}
			}
			if(!flag)
				allBanners.add(b);
			flag = false;
		}
		return allBanners;
	}
    
    // 场景需要同时支持小区和园区 by lqs 20160511
//    @Override
//    public List<BannerDTO> getBannersByScene(GetBannersBySceneCommand cmd, HttpServletRequest request) {
//        User user = UserContext.current().getUser();
//        SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
//        
//        GetBannersCommand getCmd = new GetBannersCommand();
//        getCmd.setBannerGroup(cmd.getBannerGroup());
//        getCmd.setBannerLocation(cmd.getBannerLocation());
//        getCmd.setNamespaceId(sceneToken.getNamespaceId());
//        getCmd.setSceneType(sceneToken.getScene());
//        
//        Community community = null;
//        List<BannerDTO> bannerList = null;
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
//            bannerList = getBanners(getCmd, request);
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
//            bannerList = getBanners(getCmd, request);
//            
//            break;
//        case ORGANIZATION:// 无小区ID
//            GetBannersByOrgCommand orgCmd = new GetBannersByOrgCommand();
//            getCmd.setBannerGroup(cmd.getBannerGroup());
//            getCmd.setBannerLocation(cmd.getBannerLocation());
//            getCmd.setNamespaceId(sceneToken.getNamespaceId());
//            getCmd.setSceneType(sceneToken.getScene());
//            orgCmd.setCommunityId(sceneToken.getEntityId());
//            bannerList = getBannersByOrg(orgCmd, request);
//            break;
//        default:
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
//            break;
//        }
//        
//        return bannerList;
//    }
    
    @Override
    public List<BannerDTO> getBannersByScene(GetBannersBySceneCommand cmd, HttpServletRequest request) {
        SceneTokenDTO sceneToken = userService.checkSceneToken(UserContext.currentUserId(), cmd.getSceneToken());
        
        GetBannersCommand getCmd = new GetBannersCommand();

        //先注释掉bannerGroup，原因是运营后台配置是多入口的，但是园区后台发布banner是单入口的，他们的bannerGroup字段不一致会导致查询失败
        //当前所有域空间的banner都是单入口的，因此此处临时去除bannerGroup，待园区后台实现多入口后可开放   add by yanjun 20171116
        //getCmd.setBannerGroup(cmd.getBannerGroup());

        getCmd.setBannerLocation(cmd.getBannerLocation());
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
        List<BannerDTO> bannerList = null;
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
            
            bannerList = getBanners(getCmd, request);
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
            
            bannerList = getBanners(getCmd, request);
            break;
        case PM_ADMIN:// 无小区ID
        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
            GetBannersByOrgCommand orgCmd = new GetBannersByOrgCommand();

            //先注释掉bannerGroup，原因是运营后台配置是多入口的，但是园区后台发布banner是单入口的，他们的bannerGroup字段不一致会导致查询失败
            //当前所有域空间的banner都是单入口的，因此此处临时去除bannerGroup，待园区后台实现多入口后可开放   add by yanjun 20171116
            //orgCmd.setBannerGroup(cmd.getBannerGroup());

            orgCmd.setBannerLocation(cmd.getBannerLocation());
            orgCmd.setNamespaceId(sceneToken.getNamespaceId());
            orgCmd.setSceneType(baseScene);
            orgCmd.setOrganizationId(sceneToken.getEntityId());
            bannerList = getBannersByOrg(orgCmd, request);
            break;
        default:
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
            break;
        }
        
        return bannerList;
    }

    @Override
    public List<BannerDTO> getBannersBySceneNew(GetBannersBySceneCommand cmd) {
        //SceneTokenDTO sceneToken = userService.checkSceneToken(UserContext.currentUserId(), cmd.getSceneToken());

        AppContext appContext = UserContext.current().getAppContext();
        //Long communityId = parseCommunityIdFromSceneToken(sceneToken);

        //旧版本的公司场景没有communityId跪了
        if(appContext != null && appContext.getCommunityId() == null && appContext.getOrganizationId() != null){
            OrganizationCommunityRequest communityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(appContext.getOrganizationId());
            if(communityRequest != null){
                appContext.setCommunityId(communityRequest.getCommunityId());
            }
        }


        if (appContext != null && appContext.getCommunityId() != null) {
            List<Banner> bannerList = bannerProvider.listBannersByCommunityId(UserContext.getCurrentNamespaceId(), appContext.getCommunityId(),cmd.getCategoryId());
            return bannerList.stream().map(r -> {
                BannerDTO dto = toBannerDTO(r);
                // 本来posterPath是 cs:// 开头的
                // 但是客户端用来下载图片的字段是posterPath, 所以在这里要特殊处理一下
                dto.setPosterPath(dto.getPosterUrl());
                dto.setPosterUrl(null);

                if (dto.getActionData() != null) {
                    try {
                        dto.setActionData(launchPadService.refreshActionData(dto.getActionData()));
                    } catch (Exception e) {
                        LOGGER.error("Refresh actionData error, bannerDTO = " + dto, e);
                    }
                }
                // like actionType=30
                if (dto.getActionData() == null) {
                    dto.setActionData("{}");
                }

                // 应用类型的跳转需要把名称设置为应用名称，用于客户端在跳转后的界面上显示标题
                if (BannerTargetType.fromCode(r.getTargetType()) == BannerTargetType.APP) {
                    dto.setName(r.getVendorTag());
                }

                BannerTargetType targetType = BannerTargetType.fromCode(dto.getTargetType());
                if(targetType == null) {
                    return dto;
                }
                BannerTargetHandler handler = PlatformContext.getComponent(
                        BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + targetType.getCode());
                if(handler == null) {
                    return dto;
                }

                try {
                    RouterInfo routerInfo = handler.getRouterInfo(dto.getTargetData());

                    if(routerInfo != null){
                        dto.setModuleId(routerInfo.getModuleId());
                        dto.setRouterPath(routerInfo.getPath());
                        dto.setRouterQuery(routerInfo.getQuery());

                        String host = "default";
                        String router = "zl://" + host + dto.getRouterPath() + "?moduleId=10400&clientHandlerType=0&" + dto.getRouterQuery();
                        dto.setRouter(router);
                    }

                    Byte clientHandlerType = handler.getClientHandlerType(dto.getTargetData());
                    dto.setClientHandlerType(clientHandlerType);

                } catch (Exception e) {
                    throw RuntimeErrorException.errorWith(e,
                            BannerServiceErrorCode.SCOPE,
                            BannerServiceErrorCode.ERROR_TARGET_HANDLE_ERROR,
                            "Parse targetData error, targetData: %s.", dto.getActionType());
                }

                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<BannerDTO> listBannerByCommunityId(Long communityId){
        Community community = communityProvider.findCommunityById(communityId);


        SceneTokenDTO sceneToken = new SceneTokenDTO();
        sceneToken.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());
        sceneToken.setScene(PARK_TOURIST.getCode());
        sceneToken.setEntityId(community.getId());
        sceneToken.setNamespaceId(community.getNamespaceId());
        sceneToken.setUserId(UserContext.currentUserId());

        String sceneTokenStr = WebTokenGenerator.getInstance().toWebToken(sceneToken);

        GetBannersBySceneCommand cmd = new GetBannersBySceneCommand();
        cmd.setSceneToken(sceneTokenStr);
        return getBannersBySceneNew(cmd);

    }



    @Override
    public CountEnabledBannersByScopeResponse countEnabledBannersByScope(CountEnabledBannersByScopeCommand cmd) {
	    ValidatorUtil.validate(cmd);

        Map<Long, Integer> communityIdTOEnabledCountMap = bannerProvider.countEnabledBannersByScope(cmd.getNamespaceId());

        List<EnabledBannersDTO> list = new ArrayList<>();
        communityIdTOEnabledCountMap.forEach((k, v) -> list.add(new EnabledBannersDTO(k, v)));

        CountEnabledBannersByScopeResponse response = new CountEnabledBannersByScopeResponse();
        response.setList(list);
        return response;
    }

    @Override
    public void reorderBanners(ReorderBannersCommand cmd) {
        ValidatorUtil.validate(cmd);

        Banner banner1 = bannerProvider.findBannerById(cmd.getId());
        if (banner1 == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        Banner banner2 = bannerProvider.findBannerById(cmd.getExchangeId());
        if (banner2 == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }

        Integer order1 = banner1.getOrder();
        Integer order2 = banner2.getOrder();

        banner1.setOrder(order2);
        banner2.setOrder(order1);

        dbProvider.execute(status -> {
            bannerProvider.updateBanner(banner1);
            bannerProvider.updateBanner(banner2);
            return true;
        });
    }

    @Override
    public BannerDTO updateBannerStatus(UpdateBannerStatusCommand cmd) {
	    ValidatorUtil.validate(cmd);
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if (banner == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }

        if (!Objects.equals(banner.getStatus(), cmd.getStatus())) {
            if (Objects.equals(BannerStatus.ACTIVE.getCode(), cmd.getStatus())) {
                checkQuantityExceeded(banner);
                setupOrderToBanner(banner, cmd.getStatus());
            } else if (Objects.equals(BannerStatus.CLOSE.getCode(), cmd.getStatus())) {
                setupOrderToBanner(banner, cmd.getStatus());
            }
            banner.setStatus(cmd.getStatus());
            bannerProvider.updateBanner(banner);
        }
        return toBannerDTO(banner);
    }

    @Override
    public BannerInstanconfigDTO getBannerInstanconfig(GetBannerInstanconfigCommand cmd) {
        BannerInstanconfigDTO dto = new BannerInstanconfigDTO();
        String instanceConfig = "%\"appId\":\""+cmd.getOriginId()+"\"%";
        List<PortalItemGroup> list = this.portalItemGroupProvider.listBannerItemGroupByAppId(instanceConfig);
        if (!CollectionUtils.isEmpty(list)) {
            PortalItemGroup portalItemGroup = list.get(0);
            if (portalItemGroup.getStyle().equals("Shape")) {
                dto.setWidthRatio(22L);
                dto.setHeightRatio(10L);
            }else {
                BannersInstanceConfig config = (BannersInstanceConfig)StringHelper.fromJsonString(portalItemGroup.getInstanceConfig(), BannersInstanceConfig.class);
                if (config != null) {
                    dto.setWidthRatio(config.getWidthRatio());
                    dto.setHeightRatio(config.getHeightRatio());
                }
            }
        }
        return dto;
    }

    private void setupOrderToBanner(Banner banner, Byte status) {
        if (Objects.equals(BannerStatus.ACTIVE.getCode(), status)) {
            Integer maxOrder = bannerProvider.getMaxOrderByCommunityId(banner.getNamespaceId(), banner.getScopeId());
            if (maxOrder != null) {
                banner.setOrder(maxOrder + 1);
            }
        } else if (Objects.equals(BannerStatus.CLOSE.getCode(), status)) {
            Integer minOrder = bannerProvider.getMinOrderByCommunityId(banner.getNamespaceId(), banner.getScopeId());
            if (minOrder != null) {
                banner.setOrder(minOrder - 1);
            }
        }
    }

    // 检查激活上限
    private void checkQuantityExceeded(Banner banner) {
        CountEnabledBannersByScopeCommand cmd = new CountEnabledBannersByScopeCommand();
        cmd.setNamespaceId(banner.getNamespaceId());

        CountEnabledBannersByScopeResponse enabledBanners = countEnabledBannersByScope(cmd);

        int maxBannerCount = this.configurationProvider.getIntValue("banner.max.active.count", AppConstants.DEFAULT_MAX_BANNER_CAN_ACTIVE);
        enabledBanners.getList().stream()
                .filter(r -> r.getScope().equals(banner.getScopeId()))
                .findFirst()
                .ifPresent(r -> {
            if (r.getCount() >= maxBannerCount) {
                throw RuntimeErrorException.errorWith(
                        BannerServiceErrorCode.SCOPE,
                        BannerServiceErrorCode.ERROR_BANNER_MAX_ACTIVE,
                        "Active banner quantity exceeded.");
            }
        });
    }

    private Long parseCommunityIdFromSceneToken(SceneTokenDTO sceneToken) {
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if (sceneType == null) {
            return null;
        }
        Long communityId = null;
        switch (sceneType) {
            case DEFAULT:
            case PARK_TOURIST: {
                Community community = communityProvider.findCommunityById(sceneToken.getEntityId());
                if (community != null) {
                    communityId = community.getId();
                }
                break;
            }
            case FAMILY: {
                FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
                if (family != null) {
                    Community community = communityProvider.findCommunityById(family.getCommunityId());
                    if (community != null) {
                        communityId = community.getId();
                    }
                }
                break;
            }
            case PM_ADMIN:// 无小区ID
            case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
            case ENTERPRISE_NOAUTH: { // 增加两场景，与园区企业保持一致 by lqs 20160517
                OrganizationCommunityRequest organizationCommunityRequest =
                        organizationProvider.getOrganizationCommunityRequestByOrganizationId(sceneToken.getEntityId());
                if (organizationCommunityRequest != null) {
                    communityId = organizationCommunityRequest.getCommunityId();
                }
                break;
            }
            default: {
                LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
                break;
            }
        }
        return communityId;
    }

    private BannerDTO toBannerDTO(Banner banner) {
        BannerDTO dto = ConvertHelper.convert(banner, BannerDTO.class);
        dto.setPosterUrl(parserUri(banner.getPosterPath(), Banner.class.getSimpleName(), banner.getId()));
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(long userId, Long communityId,BannerDTO banner, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try{
            //use handler instead of??
            if(banner.getActionData() != null && !banner.getActionData().trim().equals("")){
                jsonObject = (JSONObject) JSONValue.parse(banner.getActionData());
                //处理phoneCall actionData
                if(banner.getActionType() == ActionType.PHONE_CALL.getCode() &&
                        banner.getBannerGroup().equals(LaunchPadConstants.GROUP_CALLPHONES)){ 
                    jsonObject = processPhoneCall(communityId, jsonObject, banner);
                }
                else if(banner.getActionType() == ActionType.THIRDPART_URL.getCode()){
                    Community community = communityProvider.findCommunityById(communityId);
                    long cityId = community == null ? 0 : community.getCityId();
                    String url = (String) jsonObject.get(LaunchPadConstants.URL);
                    //处理收集地址url
                    if(url.indexOf(LaunchPadConstants.USER_REQUEST_LIST) != -1){
                        url = url + "&userId=" + userId + "&cityId=" + cityId;
                    }
                    jsonObject.put(LaunchPadConstants.URL, url);
                }
                else if(banner.getActionType() == ActionType.LAUNCH_APP.getCode()){
                    jsonObject = processLaunchApp(jsonObject,request);
                }
                else if(banner.getActionType() == ActionType.POST_BY_CATEGORY.getCode()){
                    jsonObject = processPostByCategory(communityId,jsonObject,banner);
                }
            }
        }catch(Exception e){
            LOGGER.error("Parser json is error, communityId={}, banner={}", communityId, banner, e);
        }
        
        return jsonObject.toJSONString();
    }

    @SuppressWarnings("unchecked")
    private JSONObject processPhoneCall(Long communityId, JSONObject actionDataJson, BannerDTO banner){
        if(actionDataJson.get(LaunchPadConstants.CALLPHONES) != null ){
            
            String location = banner.getBannerLocation();
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
    private JSONObject processPostByCategory(Long communityId, JSONObject actionDataJson,BannerDTO banner) {
        actionDataJson.put(LaunchPadConstants.DISPLAY_NAME, banner.getName());
        actionDataJson.put(LaunchPadConstants.COMMUNITY_ID, communityId);
        return actionDataJson;
    }
    
    //sort banner with banner order asc
    private void sortBanner(List<BannerDTO> result){
        Collections.sort(result, new Comparator<BannerDTO>(){
            @Override
            public int compare(BannerDTO o1, BannerDTO o2){
               return o1.getOrder().intValue() - o2.getOrder().intValue();
            }
        });
    }
    private String parserUri(String uri,String ownerType, long ownerId){
        try {
            if(!org.apache.commons.lang.StringUtils.isEmpty(uri)) {
                return contentServerService.parserUri(uri, ownerType, ownerId);
            }
            
        } catch (Exception e) {
            LOGGER.error("Parser uri is error." + e.getMessage());
        }
        return null;

    }
    
//    private String parserJson(long commnunityId,BannerDTO banner) {
//        
//        JSONObject jsonObject = new JSONObject();
//        if(banner.getActionData() != null && !banner.getActionData().trim().equals("")){
//            jsonObject = (JSONObject) JSONValue.parse(banner.getActionData());
//        }
//        
//        return jsonObject.toString();
//    }
    
    @Override
    public void createBanner(CreateBannerCommand cmd) {
        ValidatorUtil.validate(cmd);

        BannerTargetType targetType = BannerTargetType.fromCode(cmd.getTargetType());
        if (targetType == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter targetType: %s.", cmd.getTargetType());
        }
        Long userId = UserContext.currentUserId();

        for (Long communityId : cmd.getScopes()) {
            Banner banner = ConvertHelper.convert(cmd, Banner.class);

            BannerTargetHandler handler = PlatformContext.getComponent(
                    BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + targetType.getCode());

            BannerTargetHandleResult result = null;
            try {
                result = handler.evaluate(cmd.getTargetData());
            } catch (Exception e) {
                throw RuntimeErrorException.errorWith(e,
                        BannerServiceErrorCode.SCOPE,
                        BannerServiceErrorCode.ERROR_TARGET_HANDLE_ERROR,
                        "Parse targetData error, targetData: %s.", cmd.getTargetType());
            }

            banner.setActionType(result.getActionType());
            banner.setActionData(result.getActionData());
            banner.setVendorTag(result.getAppName());

            banner.setAppId(result.getAppId());
            banner.setCreatorUid(userId);
            banner.setBannerGroup("Default");
            banner.setStatus(BannerStatus.ACTIVE.getCode());
            banner.setScopeCode(ScopeType.COMMUNITY.getCode());
            banner.setScopeId(communityId);
            banner.setCategoryId(cmd.getCategoryId());
            Integer minOrder = bannerProvider.getMinOrderByCommunityId(UserContext.getCurrentNamespaceId(), communityId);
            banner.setOrder(minOrder == null ? 10 : minOrder - 1);
            bannerProvider.createBanner(banner);
        }
    }
    
	@Override
    public BannerDTO updateBanner(UpdateBannerCommand cmd) {
	    ValidatorUtil.validate(cmd);

        BannerTargetType targetType = BannerTargetType.fromCode(cmd.getTargetType());
        if (targetType == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter targetType: %s.", cmd.getTargetType());
        }

        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if (banner == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        banner.setName(cmd.getName());
        banner.setPosterPath(cmd.getPosterPath());

        if (!Objects.equals(banner.getTargetType(), cmd.getTargetType())
                || !Objects.equals(banner.getTargetData(), cmd.getTargetData())) {
            BannerTargetHandler handler = PlatformContext.getComponent(
                    BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + targetType.getCode());

            BannerTargetHandleResult result = null;
            try {
                result = handler.evaluate(cmd.getTargetData());
            } catch (Exception e) {
                throw RuntimeErrorException.errorWith(e,
                        BannerServiceErrorCode.SCOPE,
                        BannerServiceErrorCode.ERROR_TARGET_HANDLE_ERROR,
                        "Parse targetData error, targetData: %s.", cmd.getTargetType());
            }

            banner.setActionType(result.getActionType());
            banner.setActionData(result.getActionData());
            banner.setVendorTag(result.getAppName());

            banner.setTargetType(cmd.getTargetType());
            banner.setTargetData(cmd.getTargetData());
        }
        bannerProvider.updateBanner(banner);
        return toBannerDTO(banner);
    }
	
	@Override
    public void deleteBannerById(DeleteBannerCommand cmd) {
        if(cmd.getId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        
        bannerProvider.deleteBanner(banner);
    }
	
    @Override
    public String createOrUpdateBannerClick(CreateBannerClickCommand cmd){
        if(cmd.getBannerId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid bannerId paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getBannerId());
        if(banner == null){
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        BannerClick bannerClick = bannerProvider.findBannerClickByBannerIdAndUserId(cmd.getBannerId(),userId);
        if(bannerClick == null){
            bannerClick = new BannerClick();
            bannerClick.setBannerId(cmd.getBannerId());
            if(cmd.getFamilyId() != null)
                bannerClick.setFamilyId(cmd.getFamilyId());
            bannerClick.setClickCount(1L);
            bannerClick.setUid(userId);
            bannerClick.setUuid(UUID.randomUUID().toString());
            bannerClick.setLastClickTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            bannerClick.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            bannerProvider.createBannerClick(bannerClick);
            
        }else {
            bannerClick.setClickCount(bannerClick.getClickCount().longValue() + 1);
            bannerClick.setLastClickTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            bannerProvider.updateBannerClick(bannerClick);
        }
        return bannerClick.getUuid();
    }
    
    @Override
    public ListBannersResponse listBanners(ListBannersCommand cmd) {
	    ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<Banner> bannerList = bannerProvider.listBannersByCommunityId(
                cmd.getNamespaceId(), cmd.getScope(), cmd.getCategoryId(), pageSize, locator);

        List<BannerDTO> dtoList = bannerList.stream().map(this::toBannerDTO).collect(Collectors.toList());

        ListBannersResponse response = new ListBannersResponse();
        response.setBanners(dtoList);
        response.setNextPageAnchor(locator.getAnchor());

        return response;
    }
    
    @Override
    public BannerClickDTO findBannerClickByToken(String token) {
        BannerClick bannerClick = this.bannerProvider.findBannerClickByToken(token);
        BannerClickDTO dto = ConvertHelper.convert(bannerClick, BannerClickDTO.class);
        return dto;
    }
    
    @Override
    public BannerDTO getBannerById(GetBannerByIdCommand cmd){
        
        User user = UserContext.current().getUser();
        long userId = user.getId();
        if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null){
            LOGGER.error("Banner is not exists,id=" + cmd.getId() + ",userId=" + userId);
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        BannerDTO dto = ConvertHelper.convert(banner, BannerDTO.class); 
        dto.setPosterPath(parserUri(dto.getPosterPath(),EntityType.USER.getCode(),userId));
        return dto;
        
    }

	@Override
	public ListBannersByOwnerCommandResponse listBannersByOwner(ListBannersByOwnerCommand cmd) {
		/*checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		if(cmd.getScope() == null || cmd.getScope().getScopeCode() == null || cmd.getScope().getScopeId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scope parameter.");
		}
		Integer namespaceId = UserContext.getCurrentNamespaceId();

        Integer pageSize = cmd.getPageSize() != null ? cmd.getPageSize()
				: this.configurationProvider.getIntValue("pagination.page.size", AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);

        // 看是否有自定义banner
        // 如果有, 则说明该场景下只需要自定义的banner了, 不需要默认的banner了
        Banner customizedBanner = bannerProvider.findAnyCustomizedBanner(namespaceId, cmd.getScope().getScopeCode(), cmd.getScope().getScopeId(), cmd.getSceneType());
        List<BannerDTO> bannerList;
        if (customizedBanner != null) {
            bannerList = bannerProvider.listBannersByOwner(namespaceId, cmd.getScope(), cmd.getSceneType(), cmd.getPageAnchor(),
                    pageSize + 1, ApplyPolicy.CUSTOMIZED);
        } else {
            bannerList = bannerProvider.listBannersByOwner(namespaceId, null, cmd.getSceneType(), cmd.getPageAnchor(), pageSize + 1,
                    ApplyPolicy.DEFAULT);
        }

        for(BannerDTO dto : bannerList) {
        	dto.setPosterUrl(parserUri(dto.getPosterPath(), cmd.getOwnerType(), cmd.getOwnerId()));
        }
        
		bannerDTOSort(bannerList);
		
		ListBannersByOwnerCommandResponse resp = new ListBannersByOwnerCommandResponse();
		resp.setBanners(bannerList);
		if(bannerList.size() > pageSize) {
			resp.setNextPageAnchor(bannerList.get(bannerList.size() - 1).getCreateTime().getTime());
			bannerList.remove(bannerList.size() - 1);
		}
		return resp;*/
        return null;
	}

	private void bannerDTOSort(List<BannerDTO> result) {
		Collections.sort(result, (b1, b2) -> {
			if (b1 == null || b2 == null) {
				return 0;
			}
			if (b1.getStatus() == ACTIVE.getCode() && b2.getStatus() == ACTIVE.getCode()) {
				return b1.getOrder().compareTo(b2.getOrder());
			}
			if (b1.getStatus() == CLOSE.getCode() && b2.getStatus() == CLOSE.getCode()) {
				return b2.getUpdateTime().compareTo(b1.getUpdateTime());
			}
			return b1.getStatus().compareTo(b2.getStatus());
		});
	}

	@Override
	public void reorderBannerByOwner(ReorderBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		List<BannerOrder> bannerOrders = cmd.getBanners();
        if(bannerOrders == null){
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid banners parameter.");
        }
        if(cmd.getScope() == null || cmd.getScope().getScopeCode() == null || cmd.getScope().getScopeId() == null) {
         	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                     ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scope parameter.");
        }
        
    	List<BannerDTO> customizedBanners = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                cmd.getScope(), null, null, null, ApplyPolicy.CUSTOMIZED);
        if(customizedBanners == null || customizedBanners.isEmpty()) {// 如果没有自定义的banner，则复制默认banner并重排序
         	List<BannerDTO> defaultBanners = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                    null, null, null, null, ApplyPolicy.DEFAULT);
         	dbProvider.execute(status -> {
         		bannerOrders.forEach(bo -> {
         			Banner banner = null;
         			for(BannerDTO dto : defaultBanners) {
         				if(Objects.equals(dto.getId(), bo.getId())) {
         					banner = ConvertHelper.convert(dto, Banner.class);
         					break;
         				}
         			}
         			// Banner banner = bannerProvider.findBannerById(bo.getId());
                    if(banner != null && bo.getDefaultOrder() != null) {
                    	banner.setOrder(bo.getDefaultOrder());
                    	banner.setId(null);
                    	banner.setScopeCode(cmd.getScope().getScopeCode());
                    	banner.setScopeId(cmd.getScope().getScopeId());
                    	banner.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
                     	bannerProvider.createBanner(banner);
                    }
                });
         		return status;
         	});
        } else {// 有自定义的banner，直接重排序
        	dbProvider.execute(status -> {
	        	bannerOrders.forEach(bo -> {
	     			 Banner banner = bannerProvider.findBannerById(bo.getId());
	                 if(banner != null && bo.getDefaultOrder() != null) {
	                 	banner.setOrder(bo.getDefaultOrder());
	                 	bannerProvider.updateBanner(banner);
	                 }
	            });
	        	return status;
         	});
        }
	}

    /**
	 * 复制默认的banner为用户可见范围下customized的banner
	 * 
	 * @param scope 复制过后新的banner的可见范围
	 */
	private void copyDefaultToCustomized(BannerScope scope) {
        List<BannerDTO> customizedBanners = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                scope, null, null, null, ApplyPolicy.CUSTOMIZED);
        if(customizedBanners == null || customizedBanners.isEmpty()) {
        	List<BannerDTO> defaultBanners = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                    null, null, null, null, ApplyPolicy.DEFAULT);
        	for(BannerDTO dto : defaultBanners) {
        		Banner b = ConvertHelper.convert(dto, Banner.class);
        		b.setScopeCode(scope.getScopeCode());
        		b.setScopeId(scope.getScopeId());
        		b.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
        		b.setId(null);
        		bannerProvider.createBanner(b);
        	}
        }
	}
	
	@Override
	public void deleteBannerByOwner(DeleteBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		if(cmd.getId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
		if(cmd.getScope() == null || cmd.getScope().getScopeCode() == null || cmd.getScope().getScopeId() == null) {
         	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                     ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scope parameter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        if(ApplyPolicy.fromCode(banner.getApplyPolicy()) == ApplyPolicy.DEFAULT) {// 如果当前要删除的banner为默认的banner，则需要复制并设置删除状态
        	List<BannerDTO> defaultBanners = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                    null, null, null, null, ApplyPolicy.DEFAULT);
        	dbProvider.execute(status -> {
        		for(BannerDTO dto : defaultBanners) {
     				Banner b = ConvertHelper.convert(dto, Banner.class);
     				if(Objects.equals(b.getId(), cmd.getId())) {
     					b.setStatus(BannerStatus.DELETE.getCode());
     					createDeleteOperLog(b.getId());
     				}
     				b.setId(null);
     				b.setScopeCode(cmd.getScope().getScopeCode());
     				b.setScopeId(cmd.getScope().getScopeId());
                	b.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
     				bannerProvider.createBanner(b);
     			}
        		return status;
        	});
        } else {// 直接设置为删除状态
        	banner.setStatus(BannerStatus.DELETE.getCode());
            bannerProvider.updateBanner(banner);
            createDeleteOperLog(banner.getId());
        }
	}
	
	private void createDeleteOperLog(Long bannerId) {
		try {
			User user = UserContext.current().getUser();
			AuditLog log = new AuditLog();
			log.setAppId(0L);
			log.setOperatorUid(user.getId());
			log.setOperationType(AuditLogOperator.DELETE.name());
			log.setResourceType(Tables.EH_BANNERS.getName());
			log.setResourceId(bannerId);
			log.setCreateTime(new Timestamp(System.currentTimeMillis()));
			auditLogProvider.createAuditLog(log);
		} catch (Exception e) {
			LOGGER.error("Create delete banner operator log error, bannerId = " + bannerId);
			e.printStackTrace();
		}
	}
	

    @Override
	public void createBannerByOwner(CreateBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		checkBannerActivationCount(cmd.getScope(), cmd.getSceneTypes());
		
		if(cmd.getScope() == null || cmd.getScope().getScopeCode() == null || cmd.getScope().getScopeId() == null) {
         	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                     ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scope parameter.");
        }
        if(cmd.getBannerLocation() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid bannerLocation parameter.");
        }
        if(cmd.getBannerGroup() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid bannerGroup parameter.");
        }
        if(cmd.getSceneTypes() == null || cmd.getSceneTypes().isEmpty()) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scentTypes parameter.");
        }
        
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        dbProvider.execute(status -> {
        	 copyDefaultToCustomized(cmd.getScope());
             
             for(String sceneStr : cmd.getSceneTypes()) {
                 SceneType sceneType = SceneType.fromCode(sceneStr);
                 if(sceneType == null) {
                     LOGGER.error("Invalid scene type, userId={}, sceneType={}, cmd={}", userId, sceneStr, cmd);
                     throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                             ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid sceneTypes parameter.");
                 }
                 Banner banner = new Banner();
                 banner.setActionType(cmd.getActionType());
                 banner.setActionData(cmd.getActionData());
                 banner.setCreatorUid(userId);
                 banner.setBannerLocation(cmd.getBannerLocation());
                 banner.setBannerGroup(cmd.getBannerGroup());
                 banner.setName(cmd.getName());
                 Integer namespaceId = UserContext.getCurrentNamespaceId();
                 banner.setNamespaceId(namespaceId);
                 banner.setStatus(cmd.getStatus());
                 banner.setPosterPath(cmd.getPosterPath());
                 banner.setScopeCode(cmd.getScope().getScopeCode());
                 banner.setScopeId(cmd.getScope().getScopeId());
                 banner.setOrder(cmd.getDefaultOrder());
                 // 设置最大的order值
                 List<BannerDTO> bannerDTOList = bannerProvider.listBannersByOwner(namespaceId,
                         cmd.getScope(), sceneStr, null, null, ApplyPolicy.CUSTOMIZED);

                 bannerDTOList.stream()
                         .mapToInt(BannerDTO::getOrder)
                         .distinct()
                         .reduce(Math::max)
                         .ifPresent(r -> banner.setOrder(r + 1));

                 banner.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
                 banner.setSceneType(sceneType.getCode());
                 
                 bannerProvider.createBanner(banner);
             }
        	return status;
        });
	}

    /**
     * 分不同的场景检查当前作用范围下banner的激活数量是否超限
     * @param scope 当前作用范围
     * @param sceneTypes 需要检查的场景列表
     */
	private void checkBannerActivationCount(BannerScope scope, List<String> sceneTypes) {
		if(sceneTypes != null && !sceneTypes.isEmpty()) {
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			int maxBannerCount = this.configurationProvider.getIntValue("banner.max.active.count", AppConstants.DEFAULT_MAX_BANNER_CAN_ACTIVE);
			Map<String, Integer> sceneTypeCountMap = bannerProvider.selectCountGroupBySceneType(namespaceId, scope, BannerStatus.ACTIVE);
			
			sceneTypes.forEach(scene -> {
				Integer activeCount = sceneTypeCountMap.get(scene);
				if(activeCount != null && activeCount >= maxBannerCount) {
					throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
							BannerServiceErrorCode.ERROR_BANNER_MAX_ACTIVE, "Banner with maximum number %s of activations", maxBannerCount);
				}
			});
		}
	}

	@Override
	public void updateBannerByOwner(UpdateBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		if(cmd.getId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id parameter.");
        }
		if(cmd.getScope() == null || cmd.getScope().getScopeCode() == null || cmd.getScope().getScopeId() == null) {
         	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                     ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scope parameter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        if(banner.getStatus() == BannerStatus.CLOSE.getCode()
                && cmd.getStatus() == BannerStatus.ACTIVE.getCode()) {
        	// 检查最大banner的激活数量是否超限
			checkBannerActivationCount(cmd.getScope(), Collections.singletonList(banner.getSceneType()));
		}
        // 目前只考虑banner的应用类型为default和customized这两种情况 
        if(ApplyPolicy.fromCode(banner.getApplyPolicy()) == ApplyPolicy.DEFAULT) {// 要更新的banner为默认的，复制并更新
        	List<BannerDTO> defaultBanners = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                    null, null, null, null, ApplyPolicy.DEFAULT);
        	dbProvider.execute(status -> {
        		for(BannerDTO dto : defaultBanners) {
     				Banner b = ConvertHelper.convert(dto, Banner.class);
     				if(Objects.equals(b.getId(), cmd.getId())) {
     					setUpdateDataToBanner(cmd, b);
     				}
     				b.setId(null);
     				b.setScopeCode(cmd.getScope().getScopeCode());
     				b.setScopeId(cmd.getScope().getScopeId());
                	b.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
     				bannerProvider.createBanner(b);
     			}
        		return status;
        	});
        } else {// 直接更新
        	setUpdateDataToBanner(cmd, banner);
            bannerProvider.updateBanner(banner);
        }
    }

	private void setUpdateDataToBanner(UpdateBannerByOwnerCommand cmd, Banner banner) {
		if(cmd.getActionType() != null)
            banner.setActionType(cmd.getActionType());
        if(cmd.getActionData() != null)
            banner.setActionData(cmd.getActionData());
        if(cmd.getDefaultOrder() != null)
            banner.setOrder(cmd.getDefaultOrder());
        if(cmd.getPosterPath() != null)
            banner.setPosterPath(cmd.getPosterPath());
        if(cmd.getScope().getScopeId() != null)
            banner.setScopeId(cmd.getScope().getScopeId());
        if(cmd.getStatus() != null)
            banner.setStatus(cmd.getStatus());
        if(cmd.getScope().getScopeCode() != null)
        	banner.setScopeCode(cmd.getScope().getScopeCode());
        if(cmd.getName() != null)
        	banner.setName(cmd.getName());
        if(banner.getStatus() == BannerStatus.CLOSE.getCode()
                && cmd.getStatus() == BannerStatus.ACTIVE.getCode()) {
            // 设置最大的order值
            List<BannerDTO> bannerDTOList = bannerProvider.listBannersByOwner(UserContext.getCurrentNamespaceId(),
                    cmd.getScope(), banner.getSceneType(), null, null, ApplyPolicy.CUSTOMIZED);
            bannerDTOList.stream().mapToInt(BannerDTO::getOrder).distinct().reduce(Math::max).ifPresent(r -> banner.setOrder(r + 1));
        }
	}

	private void checkUserNotInOrg(String ownerType, Long ownerId) {
		if(ownerType == null || ownerId == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid ownerType or ownerId parameter.");
		}
		BannerOwnerType bot = BannerOwnerType.fromCode(ownerType);
		if(bot == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid ownerType parameter.");
		}
		/*User user = UserContext.current().getUser();
		OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), ownerId);
		if(member == null){
			LOGGER.error("User {} is not in the organization {}.", user.getId(), ownerId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"User is not in the organization.");
		}*/
	}
}
