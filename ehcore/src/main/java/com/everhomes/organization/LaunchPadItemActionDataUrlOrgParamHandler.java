// @formatter:off
package com.everhomes.organization;

import com.everhomes.launchpad.LaunchPadItemActionDataHandler;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.user.UserService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(LaunchPadItemActionDataHandler.LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX + LaunchPadItemActionDataHandler.URL_ORG_PARAM)
public class LaunchPadItemActionDataUrlOrgParamHandler implements LaunchPadItemActionDataHandler {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UserService userService;

    @Override
    public String refreshActionData(String actionData, SceneTokenDTO sceneToken) {
        if(actionData == null || "".equals(actionData)){
            return actionData;
        }

        JSONObject jsonObject = (JSONObject) JSONValue.parse(actionData);
        if(jsonObject == null){
            return actionData;
        }

        if(sceneToken == null){
            return actionData;
        }

        String url = (String)jsonObject.get("url");

        if(url == null){
            return actionData;
        }

        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
        if(UserCurrentEntityType.ORGANIZATION.equals(entityType) ||UserCurrentEntityType.ENTERPRISE.equals(entityType) ){
            Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());

            if(organization != null && organization.getUnifiedSocialCreditCode() != null){
                if(url.contains("?")){
                    url = url + "&id=";
                }else {
                    url = url + "?id=";
                }
                url = url +  organization.getUnifiedSocialCreditCode();
                jsonObject.put("url", url);
            }
        }

        return jsonObject.toJSONString();
    }


}
