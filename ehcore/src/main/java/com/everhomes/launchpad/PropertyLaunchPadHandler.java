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
    public LaunchPadItem accesProcessLaunchPadItem(long userId, long commnunityId, long itemId) {
        LaunchPadItem launchPadItem = this.launchPadProvider.findLaunchPadItemById(itemId);
        assert(launchPadItem != null);
        launchPadItem.setActionUri(parserUri(userId,commnunityId,itemId,launchPadItem.getActionUri()));
        
        return launchPadItem;
    }
    
    private String parserUri(long userId, long commnunityId, long itemId,String actionUri) {
        StringBuilder sb = new StringBuilder();
        sb.append(actionUri);
        sb.append("?");
        sb.append("userId=");
        sb.append(userId);
        sb.append("&");
        sb.append("communityId=");
        sb.append(commnunityId);
        sb.append("&");
        sb.append("itemId=");
        sb.append(itemId);
        return sb.toString();
        
    }

   
}
