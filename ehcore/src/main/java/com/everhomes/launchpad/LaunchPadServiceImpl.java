// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;






import com.everhomes.acl.PortalRoleResolver;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProfile;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;import com.everhomes.util.StringHelper;


@Component
public class LaunchPadServiceImpl implements LaunchPadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadServiceImpl.class);
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private UserActivityProvider userActivityProvider;

    @Override
    public ListLaunchPadByCommunityIdCommandResponse listLaunchPadByCommunityId(ListLaunchPadByCommunityIdCommand cmd) {
        
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
        User user = UserContext.current().getUser();
        List<LaunchPadItem> defaultItems = launchPadProvider.listLaunchPadItemsByScopeTypeAndScopeId(LaunchPadScopeType.COUNTRY.getCode(), 0L);
        List<LaunchPadItem> cityItems = launchPadProvider.listLaunchPadItemsByScopeTypeAndScopeId(LaunchPadScopeType.CITY.getCode(), community.getCityId());
        List<LaunchPadItem> communityItems = launchPadProvider.listLaunchPadItemsByScopeTypeAndScopeId(LaunchPadScopeType.COMMUNITY.getCode(), communityId);
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
                allItems = overrideItems(allItems,cityItems);
            }
            if(communityItems != null && !communityItems.isEmpty())
                allItems = overrideItems(allItems, communityItems);
            if(userItems != null && !userItems.isEmpty())
                allItems = overrideItems(allItems, userItems);
        }
        
        List<LaunchPadItemDTO> result = new ArrayList<LaunchPadItemDTO>();
        allItems.forEach((r) ->{
            LaunchPadHandler handler = PlatformContext.getComponent(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + r.getAppId());
            if(handler == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Unable to find launch pad handler.");
            
            result.add(ConvertHelper.convert(handler.accesProcessLaunchPadItem(user.getId(), communityId, r.getId()), LaunchPadItemDTO.class));
        });
        long endTime = System.currentTimeMillis();
        
        LOGGER.info("Query launch pad,esplse=" + (endTime - startTime));
        ListLaunchPadByCommunityIdCommandResponse response = new ListLaunchPadByCommunityIdCommandResponse();
        response.setLaunchPadItems(result);
        
        return response;
    }

    private List<LaunchPadItem> overrideItems(List<LaunchPadItem> defalultItems, List<LaunchPadItem> overrideItems) {
        
        if(defalultItems == null || overrideItems == null) return null;
        List<LaunchPadItem> allItems = new ArrayList<LaunchPadItem>();
        for(LaunchPadItem d : defalultItems){
            for(LaunchPadItem o : overrideItems){
                if(o.getApplyPolicy()== 1 && d.getItemName().equals(o.getItemName()) 
                        && d.getItemGroup().equals(o.getItemGroup())){
                    allItems.add(o);
                    break;
                }
            }
            allItems.add(d);
        }
        return allItems;
    }
    public List<LaunchPadItem> getUserItems(long userId){
       List<LaunchPadItem> userItems = new ArrayList<LaunchPadItem>();
        List<UserProfile> userProfiles = this.userActivityProvider.findProfileByUid(userId);
        userProfiles.forEach((userProfile) ->{
            if(userProfile.getItemKind() == ItemKind.JSON.getCode()){
                String jsonString = userProfile.getItemValue();
                userItems.add((LaunchPadItem) StringHelper.fromJsonString(jsonString, LaunchPadItem.class));
            }
            
        });

        return userItems;
    }
   
}
