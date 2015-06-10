// @formatter:off
package com.everhomes.launchpad;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_THIRD_SERVICE)
public class ThirdServiceLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionUri(parserUri(userToken,commnunityId,launchPadItem.getActionUri()));
        
        return launchPadItem;
    }
    
    private String parserUri(String userToken, long commnunityId,String actionUri) {
        actionUri = LaunchPadUtils.addParameterToLink(actionUri, LaunchPadConstants.TOKEN, userToken);
        actionUri = LaunchPadUtils.addParameterToLink(actionUri, LaunchPadConstants.COMMUNITY_ID, String.valueOf(commnunityId));
        return actionUri;
        
    }
    


   
}
