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

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.launchpad.LaunchPadConstants;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.banner.BannerClickDTO;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerServiceErrorCode;
import com.everhomes.rest.banner.CreateBannerClickCommand;
import com.everhomes.rest.banner.GetBannerByIdCommand;
import com.everhomes.rest.banner.GetBannersByOrgCommand;
import com.everhomes.rest.banner.GetBannersCommand;
import com.everhomes.rest.banner.admin.CreateBannerAdminCommand;
import com.everhomes.rest.banner.admin.DeleteBannerAdminCommand;
import com.everhomes.rest.banner.admin.ListBannersAdminCommand;
import com.everhomes.rest.banner.admin.ListBannersAdminCommandResponse;
import com.everhomes.rest.banner.admin.UpdateBannerAdminCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.scene.SceneService;
import com.everhomes.scene.SceneTypeInfo;
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
        //String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        //query user relate banners
        List<Banner> countryBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(),
                ScopeType.ALL.getCode(), 0L);
        List<Banner> cityBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.CITY.getCode(), cityId);
        List<Banner> communityBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneType, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.COMMUNITY.getCode(), communityId);
        List<Banner> allBanners = new ArrayList<Banner>();
        if(countryBanners != null)
            allBanners.addAll(countryBanners);
        if(cityBanners != null)
            allBanners.addAll(cityBanners);
        if(communityBanners != null)
            allBanners.addAll(communityBanners);
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
        Integer namespaceId = (user.getNamespaceId() == null) ? 0 : user.getNamespaceId();
        String sceneTypeStr = cmd.getCurrentSceneType();
        //String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        //query user relate banners
        List<Banner> countryBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneTypeStr, cmd.getBannerLocation(), cmd.getBannerGroup(),
                ScopeType.ALL.getCode(), 0L);
        List<Banner> orgBanners = bannerProvider.findBannersByTagAndScope(namespaceId, sceneTypeStr, cmd.getBannerLocation(), cmd.getBannerGroup(), ScopeType.ORGANIZATION.getCode(), organizationId);
        List<Banner> allBanners = new ArrayList<Banner>();
        if(countryBanners != null)
            allBanners.addAll(countryBanners);
        if(orgBanners != null)
            allBanners.addAll(orgBanners);
        List<BannerDTO> result = allBanners.stream().map((Banner r) ->{
           BannerDTO dto = ConvertHelper.convert(r, BannerDTO.class);
           //third url add user token
//           if(dto.getActionType().byteValue() == ActionType.THIRDPART_URL.getCode()){
//               dto.setActionData(parserJson(communityId,dto));
//           }
           dto.setActionData(parserJson(userId, null,dto,request));
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
            LOGGER.error("Parser json is error,communityId=" + communityId,e.getMessage());
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
                
                banner.setSceneType(sceneType.getCode());
                bannerProvider.createBanner(banner);
            });
        }
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
        List<BannerDTO> result = bannerProvider.listBanners(cmd.getKeyword(),offset,pageSize).stream().map((Banner r) ->{
            BannerDTO dto = ConvertHelper.convert(r, BannerDTO.class); 
            dto.setPosterPath(parserUri(dto.getPosterPath(),EntityType.USER.getCode(),userId));
            return dto;
         }).collect(Collectors.toList());
        ListBannersAdminCommandResponse response = new ListBannersAdminCommandResponse();
        if(result != null && result.size() >= pageSize){
            response.setNextPageOffset((int)pageOffset + 1);
        }
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
}
