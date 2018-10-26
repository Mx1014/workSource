package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.DECORATION_MODULE)
public class DecorationPortalPublishHandler implements PortalPublishHandler {

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        return instanceConfig;
    }

    @Override
    public String processInstanceConfig(Integer namespaceId,String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        JSONObject json = new JSONObject();
        if (!homeUrl.endsWith("/"))
            homeUrl += "/";
        String url = homeUrl + "decoration-management/build/index.html#/home?ns="+namespaceId+"?sign_suffix";
        json.put("url",url);
        return json.toJSONString();
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }
}
