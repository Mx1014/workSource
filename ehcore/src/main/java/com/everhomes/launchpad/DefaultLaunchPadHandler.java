// @formatter:off
package com.everhomes.launchpad;


import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.app.AppConstants;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_DEFAULT)
public class DefaultLaunchPadHandler implements LaunchPadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLaunchPadHandler.class);
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken,long userId, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionData(parserJson(userToken, commnunityId, launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        return jsonObject.toJSONString();
    }

}
