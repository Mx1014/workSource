// @formatter:off
package com.everhomes.launchpad;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_COUPON)
public class CouponLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setJsonObj(parserJson(userToken, commnunityId, launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        
        Community community = this.communityProvider.findCommunityById(commnunityId);
        assert(community != null);
        
        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        jsonObject.put(LaunchPadConstants.CITY_ID, community.getCityId());
        return jsonObject.toJSONString();
    }
    


   
}
