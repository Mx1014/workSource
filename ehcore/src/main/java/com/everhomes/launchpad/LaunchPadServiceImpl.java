// @formatter:off
package com.everhomes.launchpad;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.launchpad.admin.CreateLaunchPadItemAdminCommand;
import com.everhomes.launchpad.admin.CreateLaunchPadLayoutAdminCommand;
import com.everhomes.launchpad.admin.DeleteLaunchPadItemAdminCommand;
import com.everhomes.launchpad.admin.DeleteLaunchPadLayoutAdminCommand;
import com.everhomes.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommand;
import com.everhomes.launchpad.admin.ListLaunchPadLayoutAdminCommand;
import com.everhomes.launchpad.admin.UpdateLaunchPadItemAdminCommand;
import com.everhomes.launchpad.admin.UpdateLaunchPadLayoutAdminCommand;
import com.everhomes.organization.GetOrgDetailCommand;
import com.everhomes.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.organization.OrganizationDTO;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.OrganizationSimpleDTO;
import com.everhomes.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.organization.pm.PropCommunityContactDTO;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.region.RegionProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.visibility.VisibleRegionType;



@Component
public class LaunchPadServiceImpl implements LaunchPadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadServiceImpl.class);
    
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
        if(cmd.getCommunityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId paramter,communityId is null");
        }
        
        long communityId = cmd.getCommunityId();
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
        }
        long startTime = System.currentTimeMillis();
        GetLaunchPadItemsCommandResponse response = new GetLaunchPadItemsCommandResponse();
        List<LaunchPadItemDTO> result = null;
        if(cmd.getItemGroup().equals(ItemGroup.BIZS.getCode())){
            result = getBusinessItems(cmd,community);
        }else{
            result = getLaunchPadItems(cmd,community,request);
        }
        response.setLaunchPadItems(result);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Query launch pad complete,communityId=" + communityId 
                + ",itemLocation=" + cmd.getItemLocation() + ",itemGroup=" + cmd.getItemGroup() + ",esplse=" + (endTime - startTime));
        return response;
        
    }
    
    @SuppressWarnings("unchecked")
    private List<LaunchPadItemDTO> getBusinessItems(GetLaunchPadItemsCommand cmd,Community community) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
        List<Long> bizIds = userActivityProvider.findFavorite(userId).stream()
                .filter(r -> r.getTargetType().equalsIgnoreCase("biz")).map(r->r.getTargetId()).collect(Collectors.toList());
        //TODO get the businesses with the user create
        
        //TODO get the business with the user join in
        //get the business with the user favorite
        if(bizIds != null && !bizIds.isEmpty()){
            List<Business> businesses = businessProvider.findBusinessByIds(bizIds);
            if(businesses == null || businesses.isEmpty())
                return null;
            int index = 1;
            for(Business r : businesses){
                LaunchPadItemDTO dto = new LaunchPadItemDTO();
                dto.setIconUri(r.getLogoUri());
                dto.setIconUrl(parserUri(r.getLogoUri(),EntityType.USER.getCode(),userId));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(LaunchPadConstants.URL, r.getUrl());
                jsonObject.put(LaunchPadConstants.COMMUNITY_ID, community.getId());
                dto.setActionData(jsonObject.toJSONString());
                dto.setActionType(ActionType.BIZ_DETAILS.getCode());
                dto.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
                dto.setItemGroup(LaunchPadConstants.GROUP_BIZS);
                dto.setItemHeight(1);
                dto.setItemWidth(1);
                dto.setDefaultOrder(index ++);
                dto.setItemName(r.getName());
                dto.setItemLabel(r.getDisplayName() + "(商铺)");
                dto.setItemLocation(cmd.getItemLocation());
                result.add(dto);
            }
        }
       
        List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COUNTRY.getCode(),0L,null);
        defaultItems.forEach(r ->{
            LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
            result.add(itemDTO);
        });
        return result;
        
    }

    private List<LaunchPadItemDTO> getLaunchPadItems(GetLaunchPadItemsCommand cmd, Community community, HttpServletRequest request){
        User user = UserContext.current().getUser();
        long userId = user.getId();
        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
        List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COUNTRY.getCode(),0L,null);
        List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.CITY.getCode(),community.getCityId(),null);
        List<LaunchPadItem> communityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COMMUNITY.getCode(),community.getId(),null);
        List<LaunchPadItem> userItems = getUserItems(user.getId());
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
        //获取用户相关组织，如果用户加入组织，则获取相应的item（如某个item物业人员可见）
        ListUserRelatedOrganizationsCommand c = new ListUserRelatedOrganizationsCommand();
        List<OrganizationSimpleDTO> dtos = organizationService.listUserRelateOrgs(c);
        if(dtos != null && !dtos.isEmpty()){
            List<String> tags = new ArrayList<String>();
            dtos.forEach(r -> tags.add(r.getOrganizationType()));
            List<LaunchPadItem> adminItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COUNTRY.getCode(),0L,tags);
            if(adminItems != null && !adminItems.isEmpty())
                allItems.addAll(adminItems);
        }
        try{
            allItems.forEach(r ->{
                LaunchPadItemDTO itemDTO = ConvertHelper.convert(r, LaunchPadItemDTO.class);
                itemDTO.setActionData(parserJson(token,userId, community.getId(), r,request));
                itemDTO.setIconUrl(parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId));
                result.add(itemDTO);
               
            });
            if(result != null && !result.isEmpty())
                sortLaunchPadItems(result);
            
        }catch(Exception e){
            LOGGER.error("Process item aciton data is error.",e);
            return null;
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken,long userId, long communityId,LaunchPadItem launchPadItem, HttpServletRequest request) {
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
                    Community community = communityProvider.findCommunityById(communityId);
                    long cityId = community == null ? 0 : community.getCityId();
                    jsonObject.put(LaunchPadConstants.TOKEN, userToken);
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
            if(jsonObject != null)
                jsonObject.put(LaunchPadConstants.COMMUNITY_ID, communityId);
        }catch(Exception e){
            LOGGER.error("Parser json is error,userToken=" + userToken + ",communityId=" + communityId,e.getMessage());
        }
        
        return jsonObject.toJSONString();
    }

    @SuppressWarnings("unchecked")
    private JSONObject processPhoneCall(long communityId, JSONObject actionDataJson, LaunchPadItem launchPadItem){
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
    private JSONObject processPostNew(long communityId, JSONObject actionDataJson, LaunchPadItem launchPadItem) {
        actionDataJson.put(LaunchPadConstants.DISPLAY_NAME, launchPadItem.getItemLabel());
        String tag = launchPadItem.getTag();
        if(tag == null || tag.trim().equals("")){
            actionDataJson.put(LaunchPadConstants.REGION_TYPE, VisibleRegionType.COMMUNITY.getCode());
            actionDataJson.put(LaunchPadConstants.REGION_ID,communityId);
            actionDataJson.put(LaunchPadConstants.CREATOR_TAG, PostEntityTag.USER.getCode());
            actionDataJson.put(LaunchPadConstants.TARGET_TAG, tag);
            return actionDataJson;
        }
        
        GetOrgDetailCommand cmd = new GetOrgDetailCommand();
        cmd.setCommunityId(communityId);
        cmd.setOrganizationType(tag);
        OrganizationDTO organization = organizationService.getOrganizationByComunityidAndOrgType(cmd);
        if(organization == null){
            LOGGER.error("Organization is not exists,communityId=" + communityId);
            return actionDataJson;
        }
        actionDataJson.put(LaunchPadConstants.REGION_TYPE, VisibleRegionType.REGION.getCode());
        actionDataJson.put(LaunchPadConstants.REGION_ID,organization.getId());
        actionDataJson.put(LaunchPadConstants.CREATOR_TAG, tag);
        actionDataJson.put(LaunchPadConstants.TARGET_TAG, PostEntityTag.USER.getCode());
        return actionDataJson;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject processPostByCategory(long communityId, JSONObject actionDataJson, LaunchPadItem launchPadItem) {
        actionDataJson.put(LaunchPadConstants.DISPLAY_NAME, launchPadItem.getItemLabel());
        String tag = launchPadItem.getTag();
        if(tag == null || tag.trim().equals("")){
            actionDataJson.put(LaunchPadConstants.REGION_TYPE, VisibleRegionType.COMMUNITY.getCode());
            actionDataJson.put(LaunchPadConstants.REGION_ID,communityId);
            return actionDataJson;
        }
        
        GetOrgDetailCommand cmd = new GetOrgDetailCommand();
        cmd.setCommunityId(communityId);
        cmd.setOrganizationType(tag);
        OrganizationDTO organization = organizationService.getOrganizationByComunityidAndOrgType(cmd);
        if(organization == null){
            LOGGER.error("Organization is not exists,communityId=" + communityId);
            return actionDataJson;
        }
        actionDataJson.put(LaunchPadConstants.REGION_TYPE, VisibleRegionType.REGION.getCode());
        actionDataJson.put(LaunchPadConstants.REGION_ID,organization.getId());
        return actionDataJson;
    }
    
    private void sortLaunchPadItems(List<LaunchPadItemDTO> result){
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
                    allItems.add(o);
                }
                else if(o.getApplyPolicy()== ApplyPolicy.OVERRIDE.getCode() 
                        && (d.getId().longValue() == o.getId().longValue() || 
                        (d.getItemName().equals(o.getItemName()) && d.getItemGroup().equals(o.getItemGroup())))){
                    allItems.add(o);
                    flag = true;
                    break;
                }
            }
            if(!flag)
                allItems.add(d);
            //reset flag
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
            item.setNamespaceId(cmd.getNamespaceId() == null ? 0 : cmd.getNamespaceId());
            item.setScopeType(itemScope.getScopeType());
            item.setScopeId(itemScope.getScopeId());
            item.setDefaultOrder(itemScope.getDefaultOrder() == null ? 0 : itemScope.getDefaultOrder());
            item.setApplyPolicy(itemScope.getApplyPolicy());
            item.setDisplayFlag(cmd.getDisplayFlag() == null ? ItemDisplayFlag.DISPLAY.getCode() : cmd.getDisplayFlag());
            item.setDisplayLayout(cmd.getDisplayLayout());
            item.setBgcolor(cmd.getBgcolor() == null ? 0 : cmd.getBgcolor());
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
            List<UserProfile> userProfiles = this.userActivityProvider.findProfileByUid(userId);
            userProfiles.forEach((UserProfile p) ->{
                if(p.getItemKind().byteValue() == ItemKind.JSON.getCode())
                    this.userActivityProvider.deleteProfile(p);
            });
            
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
                launchPadItem.setScopeType(LaunchPadScopeType.USERDEFINED.getCode());
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
    public List<LaunchPadLayoutDTO> getLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd){
        if(cmd.getVersionCode() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid versionCode paramter.versionCode is null");
        }
        List<LaunchPadLayoutDTO> results = new ArrayList<LaunchPadLayoutDTO>();
        this.launchPadProvider.findLaunchPadItemsByVersionCode(cmd.getName(),cmd.getVersionCode()).stream().map((r) ->{;
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
        if(cmd.getScopeType() != null && !cmd.getScopeType().trim().equals("")){
            launchPadItem.setScopeType(cmd.getScopeType());
        }
        this.launchPadProvider.updateLaunchPadItem(launchPadItem);
    }

    @Override
    public GetLaunchPadItemsByKeywordCommandResponse getLaunchPadItemsByKeyword(GetLaunchPadItemsByKeywordAdminCommand cmd) {
        
        final int size = this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        final int pageOffset = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
        final int pageSize = cmd.getPageSize() == null ? size : cmd.getPageSize();
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        List<LaunchPadItem> launchPadItems = this.launchPadProvider.getLaunchPadItemsByKeyword(cmd.getKeyword(),offset,pageSize);
        if(launchPadItems != null && !launchPadItems.isEmpty()){
             launchPadItems.stream().map(r ->{
                 result.add(ConvertHelper.convert(r, LaunchPadItemDTO.class));
                 return null;
            }).collect(Collectors.toList());
        }
      
        GetLaunchPadItemsByKeywordCommandResponse response = new GetLaunchPadItemsByKeywordCommandResponse();
        if(result.size() == pageSize){
            response.setNextPageOffset(pageOffset + 1);
        }
        response.setLaunchPadItems(result);
        
        return response;
    }

    @Override
    public List<LaunchPadPostActionCategoryDTO> findLaunchPadPostActionCategories(FindLaunchPadPostActionItemCategoriesCommand cmd) {
        if(cmd.getItemLocation() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid itemLocation paramter,itemLocation is null");
        }
        if(cmd.getItemGroup() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid itemGroup paramter,itemGroup is null");
        }
        if(cmd.getCommunityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId paramter,communityId is null");
        }
        
        long communityId = cmd.getCommunityId();
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
        }
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        long userId = user.getId();
        List<LaunchPadPostActionCategoryDTO> result = new ArrayList<LaunchPadPostActionCategoryDTO>();
        List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COUNTRY.getCode(),0L,null);
        List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.CITY.getCode(),community.getCityId(),null);
        List<LaunchPadItem> communityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COMMUNITY.getCode(),communityId,null);
        List<LaunchPadItem> userItems = getUserItems(user.getId());
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
        allItems.forEach((r) ->{
            if(r.getActionData() == null)
                return;
            if(r.getActionData() != null && !r.getActionData().trim().equals("")){
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) JSONValue.parse(r.getActionData());
                if(jsonObject.get(LaunchPadConstants.CONTENT_CATEGORY) == null)
                    return;
            }
            LaunchPadPostActionCategoryDTO dto = (LaunchPadPostActionCategoryDTO) StringHelper.fromJsonString(r.getActionData(), LaunchPadPostActionCategoryDTO.class);
            
            dto.setItemLabel(r.getItemLabel());
            dto.setItemName(r.getItemName());
            result.add(dto);
        });
        long endTime = System.currentTimeMillis();
        
        LOGGER.info("Query launch pad complete,userId=" + userId + ",communityId=" + communityId 
                + ",itemLocation=" + cmd.getItemLocation() + ",itemGroup=" + cmd.getItemGroup() + ",esplse=" + (endTime - startTime));
        return result;
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
    
 
}
