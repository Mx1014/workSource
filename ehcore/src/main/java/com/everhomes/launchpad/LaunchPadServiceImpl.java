// @formatter:off
package com.everhomes.launchpad;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProfile;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;




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
                        && (d.getId().longValue() == o.getId().longValue() || d.getItemName().equals(o.getItemName()))){
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
    
    public List<LaunchPadItem> getUserItems(long userId){
       List<LaunchPadItem> userItems = new ArrayList<LaunchPadItem>();
        List<UserProfile> userProfiles = this.userActivityProvider.findProfileByUid(userId);
        //UserProfile profile = this.userActivityProvider.findUserProfileBySpecialKey(userId, UserProfileContstant.LaunchPadName);
        if(userProfiles == null) return userItems;
        
        userProfiles.forEach((userProfile) ->{
            if(userProfile.getItemKind() == ItemKind.JSON.getCode()){
                String jsonString = userProfile.getItemValue();
                userItems.add((LaunchPadItem) StringHelper.fromJsonString(jsonString, LaunchPadItem.class));
            }
            
        });

        return userItems;
    }
    
    @Override
    public void createLaunchPadItem(CreateLaunchPadItemCommand cmd){
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
            items.add(item);
        });
        this.launchPadProvider.createLaunchPadItems(items);
    }
    
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
                
                UserProfile userProfile = new UserProfile();
                userProfile.setItemKind(ItemKind.JSON.getCode());
                userProfile.setOwnerId(userId);
                userProfile.setItemName(launchPadItem.getItemName());
                userProfile.setItemValue(StringHelper.toJsonString(launchPadItem));
                
                this.userActivityProvider.addUserProfile(userProfile);
            });
            return null;
        });
       
    }


    @Override
    public void deleteLaunchPadItem(DeleteLaunchPadItemCommand cmd) {
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
    public void createLaunchPadLayout(CreateLaunchPadLayoutCommand cmd){
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
    public LaunchPadLayoutDTO getLastLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd){
//        if(cmd.getVersionCode() == null){
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "Invalid versionCode paramter.versionCode is null");
//        }
        if(cmd.getVersionCode() == null)
            cmd.setVersionCode(0L);
        List<LaunchPadLayoutDTO> results = getLaunchPadLayoutByVersionCode(cmd);
        if(results != null && !results.isEmpty())
            return results.get(0);
        return null;
    }
    
    @Override
    public List<LaunchPadLayoutDTO> getLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd){
        if(cmd.getVersionCode() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid versionCode paramter.versionCode is null");
        }
        List<LaunchPadLayoutDTO> results = new ArrayList<LaunchPadLayoutDTO>();
        this.launchPadProvider.findLaunchPadItemsByVersionCode(cmd.getVersionCode()).stream().map((r) ->{;
            results.add(ConvertHelper.convert(r, LaunchPadLayoutDTO.class));
            return null;
        }).collect(Collectors.toList());
        return results;
    }
    
    @Override
    public GetLaunchPadItemsCommandResponse getLaunchPadItems(GetLaunchPadItemsCommand cmd){
        if(cmd.getItemLocation() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid itemLocation paramter,itemLocation is null");
        }
        if(cmd.getItemGroup() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid itemGroup paramter,itemGroup is null");
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
        String token = UserContext.current().getLogin().getLoginToken().getTokenString();
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
        List<LaunchPadItem> defaultItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COUNTRY.getCode(),0L);
        List<LaunchPadItem> cityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.CITY.getCode(),community.getCityId());
        List<LaunchPadItem> communityItems = this.launchPadProvider.findLaunchPadItemsByTagAndScope(cmd.getItemLocation(),cmd.getItemGroup(),LaunchPadScopeType.COMMUNITY.getCode(),communityId);
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
            LaunchPadHandler handler = PlatformContext.getComponent(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + r.getItemGroup());
            if(handler == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Unable to find launch pad handler.");
            LaunchPadItemDTO itemDTO = ConvertHelper.convert(handler.accesProcessLaunchPadItem(token, communityId, r), LaunchPadItemDTO.class);

            itemDTO.setIconUrl(parserUri(itemDTO.getIconUri(),EntityType.USER.getCode(),userId));
            result.add(itemDTO);
        });
        long endTime = System.currentTimeMillis();
        
        LOGGER.info("Query launch pad complete,userId=" + userId + ",communityId=" + communityId 
                + ",itemLocation=" + cmd.getItemLocation() + ",itemGroup=" + cmd.getItemGroup() + ",esplse=" + (endTime - startTime));
        GetLaunchPadItemsCommandResponse response = new GetLaunchPadItemsCommandResponse();
        response.setLaunchPadItems(result);
        
        return response;
    }
   
}
