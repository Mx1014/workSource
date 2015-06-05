// @formatter:off
package com.everhomes.launchpad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_PM)
public class PropertyLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(long userId, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        parserJson(userId,commnunityId);
        
        return launchPadItem;
    }
    
    private String parserJson(long userId, long commnunityId) {
        return null;
    }

   
}
