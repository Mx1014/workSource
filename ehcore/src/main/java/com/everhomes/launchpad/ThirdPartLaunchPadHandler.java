// @formatter:off
package com.everhomes.launchpad;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + "/System/bizs")
public class ThirdPartLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionData(parserJson(userToken, commnunityId, launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        
        JSONObject jsonObject = new JSONObject();
        if(launchPadItem.getActionData() != null && !launchPadItem.getActionData().trim().equals("")){
            jsonObject = (JSONObject) JSONValue.parse(launchPadItem.getActionData());
        }
        jsonObject.put(LaunchPadConstants.TOKEN, userToken);
        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        return jsonObject.toString();
    }
    
   
}
