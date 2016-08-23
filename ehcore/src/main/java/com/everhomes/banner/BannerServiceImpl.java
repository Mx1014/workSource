// @formatter:off
package com.everhomes.banner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogOperator;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.launchpad.LaunchPadConstants;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.banner.BannerClickDTO;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerOwnerType;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerServiceErrorCode;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.CreateBannerByOwnerCommand;
import com.everhomes.rest.banner.CreateBannerClickCommand;
import com.everhomes.rest.banner.DeleteBannerByOwnerCommand;
import com.everhomes.rest.banner.GetBannerByIdCommand;
import com.everhomes.rest.banner.GetBannersByOrgCommand;
import com.everhomes.rest.banner.GetBannersCommand;
import com.everhomes.rest.banner.ListBannersByOwnerCommand;
import com.everhomes.rest.banner.ListBannersByOwnerCommandResponse;
import com.everhomes.rest.banner.ReorderBannerByOwnerCommand;
import com.everhomes.rest.banner.UpdateBannerByOwnerCommand;
import com.everhomes.rest.banner.admin.CreateBannerAdminCommand;
import com.everhomes.rest.banner.admin.DeleteBannerAdminCommand;
import com.everhomes.rest.banner.admin.ListBannersAdminCommand;
import com.everhomes.rest.banner.admin.ListBannersAdminCommandResponse;
import com.everhomes.rest.banner.admin.UpdateBannerAdminCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.launchpad.ApplyPolicy;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.scene.SceneService;
import com.everhomes.scene.SceneTypeInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;

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
        
        List<Banner> allBanners = new ArrayList<Banner>();
        List<Banner> communityBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.COMMUNITY.getCode(), communityId);
        List<Banner> customizedBanners = new ArrayList<Banner>();
        
        for (Banner banner : communityBanners) {
			if(ApplyPolicy.fromCode(banner.getApplyPolicy()) == ApplyPolicy.CUSTOMIZED){
				customizedBanners.add(banner);
			}
		}
        
        if(customizedBanners.size() > 0){
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
        
        if(customizedBanners.size() > 0){
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
//            orgCmd.setOrganizationId(sceneToken.getEntityId());
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
        User user = UserContext.current().getUser();
        SceneTokenDTO sceneToken = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
        
        GetBannersCommand getCmd = new GetBannersCommand();
        getCmd.setBannerGroup(cmd.getBannerGroup());
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
            orgCmd.setBannerGroup(cmd.getBannerGroup());
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
            if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
                return contentServerService.parserUri(uri,ownerType,ownerId);
            
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
    public void createBanner(CreateBannerAdminCommand cmd){
        if(cmd.getScopes() == null || cmd.getScopes().isEmpty()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scopes paramter.");
        }
        if(cmd.getBannerLocation() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid bannerLocation paramter.");
        }
        if(cmd.getBannerGroup() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid bannerGroup paramter.");
        }
        if(cmd.getStartTime() != null && cmd.getEndTime() != null && cmd.getStartTime() > cmd.getEndTime()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid startTime and endTime paramter.");
        }
        
        // 增加场景信息，由于jooq生成的pojo不带默认值，场景类型是not null类型，故在没值时需要补充默认值 by lqs 2010505
        List<String> sceneTypeList = cmd.getSceneTypeList();
        if(sceneTypeList == null) {
            sceneTypeList = new ArrayList<String>();
            sceneTypeList.add(SceneType.DEFAULT.getCode());
        }
        
        User user = UserContext.current().getUser();
        long userId = user.getId();
        List<BannerScope> scopes = cmd.getScopes();
        for(String sceneStr : sceneTypeList) {
            SceneType sceneType = SceneType.fromCode(sceneStr);
            if(sceneType == null) {
                LOGGER.error("Invalid scene type, userId={}, sceneType={}, cmd={}", userId, sceneStr, cmd);
                continue;
            }
            scopes.forEach(scope ->{
                Banner banner = new Banner();
                banner.setActionType(cmd.getActionType());
                banner.setActionData(cmd.getActionData());
                banner.setAppid(cmd.getAppid());
                banner.setCreatorUid(userId);
                banner.setBannerLocation(cmd.getBannerLocation());
                banner.setBannerGroup(cmd.getBannerGroup());
                banner.setName(cmd.getName());
                banner.setNamespaceId(cmd.getNamespaceId());
                if(cmd.getStartTime() != null)
                    banner.setStartTime(new Timestamp(cmd.getStartTime()));
                if(cmd.getEndTime() != null)
                    banner.setEndTime(new Timestamp(cmd.getEndTime()));
                banner.setStatus(cmd.getStatus());
                banner.setPosterPath(cmd.getPosterPath());
                banner.setScopeCode(scope.getScopeCode());
                banner.setScopeId(scope.getScopeId());
                banner.setOrder(scope.getOrder());
                banner.setVendorTag(cmd.getVendorTag());
                
                // 在3.7.0版本，banner也需要能够为某指定园区配置不同的banner，故需要支持apply_policy  by yanshaofan
                ApplyPolicy applyPolicy = ApplyPolicy.fromCode(cmd.getApplyPolicy());
                if(applyPolicy == null) {
                    applyPolicy = ApplyPolicy.DEFAULT;
                }
                banner.setApplyPolicy(applyPolicy.getCode());
                
                banner.setSceneType(sceneType.getCode());
                bannerProvider.createBanner(banner);
            });
        }
    }
    
    @Override
	public void createBannerByOwner(CreateBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
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
        copyBannerToCustomized(cmd.getScope().getScopeCode(), cmd.getScope().getScopeId(), null);
        User user = UserContext.current().getUser();
        long userId = user.getId();
        for(String sceneStr : cmd.getSceneTypes()) {
            SceneType sceneType = SceneType.fromCode(sceneStr);
            if(sceneType == null) {
                LOGGER.error("Invalid scene type, userId={}, sceneType={}, cmd={}", userId, sceneStr, cmd);
                continue;
            }
            
            Banner banner = new Banner();
            banner.setActionType(cmd.getActionType());
            banner.setActionData(cmd.getActionData());
            banner.setCreatorUid(userId);
            banner.setBannerLocation(cmd.getBannerLocation());
            banner.setBannerGroup(cmd.getBannerGroup());
            banner.setName(cmd.getName());
            banner.setNamespaceId(user.getNamespaceId());
            banner.setStatus(cmd.getStatus());
            banner.setPosterPath(cmd.getPosterPath());
            banner.setScopeCode(cmd.getScope().getScopeCode());
            banner.setScopeId(cmd.getScope().getScopeId());
            banner.setOrder(cmd.getDefaultOrder());
            banner.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
            banner.setSceneType(sceneType.getCode());
            
            bannerProvider.createBanner(banner);
        }
	}

	@Override
	public void updateBannerByOwner(UpdateBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null){
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        if(ApplyPolicy.fromCode(banner.getApplyPolicy()) != ApplyPolicy.CUSTOMIZED) {
        	banner.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
        	Long newId = copyBannerToCustomized(cmd.getScope().getScopeCode(), cmd.getScope().getScopeId(), cmd.getId());
        	if(newId > 0) {
        		banner.setId(newId);
        	}
        }
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
        
        bannerProvider.updateBanner(banner);
	}

	private void checkUserNotInOrg(String ownerType, Long ownerId) {
		BannerOwnerType bot = BannerOwnerType.fromCode(ownerType);
		if(bot == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid ownerType parameter.");
		}
		User user = UserContext.current().getUser();
		OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), ownerId);
		if(member == null){
			LOGGER.error("User {} is not in the organization {}.", user.getId(), ownerId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"User is not in the organization.");
		}
	}

	/**
	 * 复制默认的banner为用户可见范围下customized的banner
	 */
	private Long copyBannerToCustomized(Byte scopeCode, Long scopeId, Long bannerId) {
		Long newId = 0L;
		List<Banner> banners = bannerProvider.listBannersByNamespeaceId(UserContext.getCurrentNamespaceId());
		for(Banner b : banners) {
			if(b.getApplyPolicy() == ApplyPolicy.CUSTOMIZED.getCode()) {
				continue;
			}
			b.setScopeCode(scopeCode);
			b.setScopeId(scopeId);
			b.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
			if(b.getId() == bannerId) {
				b.setId(null);
				newId = bannerProvider.createBanner(b);
			} else {
				b.setId(null);
				bannerProvider.createBanner(b);
			}
		}
		return newId;
	}

	@Override
    public void updateBanner(UpdateBannerAdminCommand cmd){
        if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null){
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        if(cmd.getStartTime() != null && cmd.getEndTime() != null && cmd.getStartTime() > cmd.getEndTime()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid startTime and endTime paramter.");
        }
        if(cmd.getActionType() != null)
            banner.setActionType(cmd.getActionType());
        if(cmd.getActionData() != null)
            banner.setActionData(cmd.getActionData());
        if(cmd.getStartTime() != null)
            banner.setStartTime(new Timestamp(cmd.getStartTime()));
        if(cmd.getEndTime() != null)
            banner.setEndTime(new Timestamp(cmd.getEndTime()));
        if(cmd.getOrder() != null)
            banner.setOrder(cmd.getOrder());
        if(cmd.getPosterPath() != null)
            banner.setPosterPath(cmd.getPosterPath());
        if(cmd.getScopeCode() != null)
            banner.setScopeCode(cmd.getScopeCode());
        if(cmd.getScopeId() != null)
            banner.setScopeId(cmd.getScopeId());
        if(cmd.getStatus() != null)
            banner.setStatus(cmd.getStatus());
        
        bannerProvider.updateBanner(banner);
    }
    
	@Override
	public void deleteBannerByOwner(DeleteBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		if(cmd.getId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null) {
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        if(ApplyPolicy.fromCode(banner.getApplyPolicy()) != ApplyPolicy.CUSTOMIZED) {
        	banner.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
        	Long newId = copyBannerToCustomized(cmd.getScope().getScopeCode(), cmd.getScope().getScopeId(), cmd.getId());
        	if(newId > 0) {
        		// 把需要删除的banner设置为复制过后新的banner的id
        		banner.setId(newId);
        	}
        }
        banner.setStatus(BannerStatus.DELETE.getCode());
        bannerProvider.updateBanner(banner);
        createDeleteOperLog(banner.getId());
	}
	
	private void createDeleteOperLog(Long bannerId) {
		User user = UserContext.current().getUser();
		AuditLog log = new AuditLog();
		log.setAppId(0L);
		log.setOperatorUid(user.getId());
		log.setOperationType(AuditLogOperator.DELETE.name());
		log.setResourceType(Tables.EH_BANNERS.getName());
		log.setResourceId(bannerId);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		auditLogProvider.createAuditLog(log);
	}

	@Override
    public void deleteBannerById(DeleteBannerAdminCommand cmd){
        if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null){
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
    public ListBannersAdminCommandResponse listBanners(ListBannersAdminCommand cmd){
        User user = UserContext.current().getUser();
        long userId = user.getId();
        if(cmd.getKeyword() == null)
            cmd.setKeyword("");
        final long pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        long pageOffset = cmd.getPageOffset() == null ? 1L : cmd.getPageOffset();
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        List<BannerDTO> result = bannerProvider.listBanners(cmd.getKeyword(), offset,pageSize).stream().map((Banner r) ->{
            BannerDTO dto = ConvertHelper.convert(r, BannerDTO.class); 
            dto.setPosterPath(parserUri(dto.getPosterPath(),EntityType.USER.getCode(),userId));
            return dto;
         }).collect(Collectors.toList());
        ListBannersAdminCommandResponse response = new ListBannersAdminCommandResponse();
        if(result != null && result.size() >= pageSize){
            response.setNextPageOffset((int)pageOffset + 1);
        }
        Collections.sort(result);
        response.setRequests(result);
        return response;
    }
    
    @Override
    public BannerClickDTO findBannerClickByToken(String token){
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
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		final Integer pageSize = cmd.getPageSize() != null ? cmd.getPageSize() 
				: this.configurationProvider.getIntValue("pagination.page.size", AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        Long pageAnchor = cmd.getPageAnchor();
        if(pageAnchor == null) {
        	pageAnchor = 0L;
        }
		List<BannerDTO> result = bannerProvider.listBannersByOwner(namespaceId, cmd.getCommunityId(), pageAnchor, 
        		pageSize + 1, ApplyPolicy.CUSTOMIZED);
        if(result == null || result.isEmpty()) {
        	result = bannerProvider.listBannersByOwner(namespaceId, cmd.getCommunityId(), pageAnchor, 
        			pageSize + 1, ApplyPolicy.DEFAULT);
        }
		Collections.sort(result);
		ListBannersByOwnerCommandResponse resp = new ListBannersByOwnerCommandResponse();
		resp.setBanners(result);
		if(result.size() > pageSize)
			resp.setNextPageAnchor(result.get(result.size() - 1).getId());
		return resp;
	}

	@Override
	public void reorderBannerByOwner(ReorderBannerByOwnerCommand cmd) {
		checkUserNotInOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		List<UpdateBannerByOwnerCommand> updateCmds = cmd.getBanners();
        if(updateCmds == null){
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid banners parameter.");
        }
        // 此步骤为复制默认的应用类型的banner为自定义的banner
        List<Banner> banners = bannerProvider.listBannersByNamespeaceId(UserContext.getCurrentNamespaceId());
        for(Banner b : banners) {
        	if(b.getApplyPolicy() == ApplyPolicy.CUSTOMIZED.getCode()) {
				continue;
			}
        	Long oldId = b.getId();
			b.setScopeCode(cmd.getScope().getScopeCode());
			b.setScopeId(cmd.getScope().getScopeId());
			b.setApplyPolicy(ApplyPolicy.CUSTOMIZED.getCode());
			b.setId(null);
			long newId = bannerProvider.createBanner(b);
			for(UpdateBannerByOwnerCommand ubc : updateCmds) {
				// 把复制后的新的id替换成旧的id
				if(ubc.getId() == oldId) {
					ubc.setId(newId);
					break;
				}
			}
        }
        updateCmds.forEach(uc -> {
        	Banner banner = bannerProvider.findBannerById(uc.getId());
            if(uc.getDefaultOrder() != null) {
            	banner.setOrder(uc.getDefaultOrder());
            	bannerProvider.updateBanner(banner);
            }
        });
	}
}
