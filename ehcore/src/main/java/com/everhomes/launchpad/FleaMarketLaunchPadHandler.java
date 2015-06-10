// @formatter:off
package com.everhomes.launchpad;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_FLEAMARKET)
public class FleaMarketLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        
        launchPadItem.setJsonObj(parserJson(userToken, commnunityId, launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        
        return jsonObject.toJSONString();
    }
    


   
}
