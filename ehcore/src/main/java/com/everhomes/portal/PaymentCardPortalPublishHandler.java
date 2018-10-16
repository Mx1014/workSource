package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.PAYMENT_CARD_MODULE)
public class PaymentCardPortalPublishHandler implements PortalPublishHandler {
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        if (namespaceId == 999990)
            instanceConfig = "{\"vendorName\":\"taotaogu\"}";
        else if (namespaceId == 999955)
            instanceConfig = "{\"vendorName\":\"zhuzong\"}";
        return instanceConfig;
    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        JSONObject json = new JSONObject();
        if (!homeUrl.endsWith("/"))
            homeUrl += "/";
        String url = homeUrl + "wallet-docking/build/index.html?ns="+namespaceId+"#/home#sign_suffix";
        json.put("url",url);
        return json.toJSONString();
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }
}
