// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.user.IdentifierType;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_PM)
public class PropertyLaunchPadHandler implements LaunchPadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLaunchPadHandler.class);
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Autowired
    private PropertyMgrService propertyMgrService;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken, long userId,long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionData(parserJson(userToken,commnunityId,launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        try{
            if(launchPadItem.getActionData() != null && !launchPadItem.getActionData().trim().equals("")){
                jsonObject = (JSONObject) JSONValue.parse(launchPadItem.getActionData());
                if(jsonObject.get("calllPhones") != null){
                    ListPropCommunityContactCommand cmd = new ListPropCommunityContactCommand();
                    cmd.setCommunityId(commnunityId);
                    List<String> contacts = new ArrayList<String>();
                    List<PropCommunityContactDTO> dtos = propertyMgrService.listPropertyCommunityContacts(cmd);
                    if(dtos != null && !dtos.isEmpty()){
                        dtos.forEach(r ->{
                            if(r.getContactType() == IdentifierType.MOBILE.getCode()){
                                contacts.add(r.getContactToken());
                            }
                        });
                    }
                    jsonObject.put("calllPhones",contacts);
                }
            }
            jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        }catch(Exception e){
            LOGGER.error("Parser json is error,userToken=" + userToken + ",communityId=" + commnunityId,e.getMessage());
        }
        
        return jsonObject.toJSONString();
    }

   
}
