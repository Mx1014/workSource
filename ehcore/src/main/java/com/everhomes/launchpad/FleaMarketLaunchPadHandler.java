// @formatter:off
package com.everhomes.launchpad;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everhomes.app.AppConstants;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_FLEAMARKET)
public class FleaMarketLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(long userId, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionUri(parserUri(userId,commnunityId,launchPadItem.getActionUri()));
        
        return launchPadItem;
    }
    
    private String parserUri(long userId, long commnunityId,String actionUri) {
        actionUri = LaunchPadUtils.addParameterToLink(actionUri, "userId", String.valueOf(userId));
        actionUri = LaunchPadUtils.addParameterToLink(actionUri, "commnunityId", String.valueOf(commnunityId));
        return actionUri;
        
    }
    


   
}
