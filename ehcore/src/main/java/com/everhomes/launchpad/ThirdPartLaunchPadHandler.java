// @formatter:off
package com.everhomes.launchpad;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.launchpad.ActionType;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_THIRD_PART)
public class ThirdPartLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken,long userId, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionData(parserJson(userToken, userId,commnunityId, launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken,long userId, long commnunityId,LaunchPadItem launchPadItem) {
        
        JSONObject jsonObject = new JSONObject();
        if(launchPadItem.getActionData() != null && !launchPadItem.getActionData().trim().equals("")){
            jsonObject = (JSONObject) JSONValue.parse(launchPadItem.getActionData());
            if(launchPadItem.getActionType() == ActionType.THIRDPART_URL.getCode()){
              Community community = communityProvider.findCommunityById(commnunityId);
              long cityId = community == null ? 0 : community.getCityId();
              jsonObject.put(LaunchPadConstants.TOKEN, userToken);
              String url = (String) jsonObject.get(LaunchPadConstants.URL);
              if(url.indexOf(LaunchPadConstants.USER_REQUEST_LIST) != -1){
                  url = url + "&userId=" + userId + "&cityId=" + cityId;
              }
              jsonObject.put(LaunchPadConstants.URL, url);
            }
            
        }
        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        return jsonObject.toString();
    }
    
   
}
