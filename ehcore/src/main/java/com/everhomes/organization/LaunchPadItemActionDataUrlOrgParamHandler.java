// @formatter:off
package com.everhomes.organization;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.launchpad.LaunchPadItemActionDataHandler;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.user.UserTemporaryTokenDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.WebTokenGenerator;
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

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String refreshActionData(String actionData) {
        if(actionData == null || "".equals(actionData)){
            return actionData;
        }

        JSONObject jsonObject = (JSONObject) JSONValue.parse(actionData);
        if(jsonObject == null){
            return actionData;
        }

        String url = (String)jsonObject.get("url");

        if(url == null){
            return actionData;
        }

        AppContext appContext = UserContext.current().getAppContext();

        if(appContext != null && appContext.getOrganizationId() != null && UserContext.currentUserId() != null && UserContext.currentUserId().longValue() != 0){
            String unifiedSocialCreditCode = addUnifiedSocialCreditCode(jsonObject, appContext.getOrganizationId());

            addUserTemporaryToken(jsonObject, unifiedSocialCreditCode, UserContext.currentUserId(), UserContext.getCurrentNamespaceId());
        }

        return jsonObject.toJSONString();
    }

    private String addUnifiedSocialCreditCode(JSONObject jsonObject, Long orgId){
        Organization organization = organizationProvider.findOrganizationById(orgId);

        if(organization != null && organization.getUnifiedSocialCreditCode() != null){

            String url = (String)jsonObject.get("url");

            if(url.contains("?")){
                url = url + "&id=";
            }else {
                url = url + "?id=";
            }
            url = url +  organization.getUnifiedSocialCreditCode();

            jsonObject.put("url", url);

            return organization.getUnifiedSocialCreditCode();
        }
        return null;
    }

    private String addUserTemporaryToken(JSONObject jsonObject, String unifiedSocialCreditCode, Long userId, Integer namespaceId){

        if(unifiedSocialCreditCode == null){
            return null;
        }
        UserTemporaryTokenDTO userToken = new UserTemporaryTokenDTO();
        userToken.setUserId(userId);
        userToken.setNamespaceId(namespaceId);
        userToken.setStartTime(System.currentTimeMillis());

        Long interval = configurationProvider.getLongValue(namespaceId, "chengxinyuan.token.interval", 24 * 60 * 60 * 1000L);
        userToken.setInterval(interval);
        userToken.setInfo(unifiedSocialCreditCode);

        String userTokenString  = WebTokenGenerator.getInstance().toWebToken(userToken);

        String url = (String)jsonObject.get("url");
        if(url.contains("?")){
            url = url + "&userToken=";
        }else {
            url = url + "?userToken=";
        }
        url = url + userTokenString;
        jsonObject.put("url", url);
        return userTokenString;
    }

}
