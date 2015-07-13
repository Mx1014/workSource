// @formatter:off
package com.everhomes.launchpad;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + LaunchPadConstants.GROUP_GOVAGENCIES)
public class GovAgenciesLaunchPadHandler implements LaunchPadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GovAgenciesLaunchPadHandler.class);
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken,long userId, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionData(parserJson(userToken,commnunityId,launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        if(launchPadItem.getActionData() != null && !launchPadItem.getActionData().trim().equals("")){
            jsonObject = (JSONObject) JSONValue.parse(launchPadItem.getActionData());
        }

        jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        return jsonObject.toString();
    }

   
}
