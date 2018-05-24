//@formatter:off
package com.everhomes.asset;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Wentian Wang on 2018/5/24.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.ASSET_MODULE)
public class AssetPortalPublishHandler implements PortalPublishHandler{
    private Logger LOGGER = LoggerFactory.getLogger(AssetPortalPublishHandler.class);
    // zhang jiang gao ke holds a different uri because in this namespace, the older asset UI is still in use
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        JSONObject config = new JSONObject();
        if(namespaceId == 999971){
            config.put("url", "${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix");
        }else{
            config.put("url", "${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix");
        }
        return config.toJSONString();
    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(instanceConfig);
            return (String)jsonObject.get("url");
        } catch (ParseException e) {
            LOGGER.error("failed to getItemActionData in AssetPortalHandler, instanceConfig is={}", instanceConfig, e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        JSONObject object = new JSONObject();
        String key = "url";
        String url = "";
        if(namespaceId == 999971){
            url = "${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix";
        }else{
            url = "${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix";
        }
        object.put(key, url);
        return object.toJSONString();
    }
}
