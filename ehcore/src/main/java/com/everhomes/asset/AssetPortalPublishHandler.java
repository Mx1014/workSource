//@formatter:off
package com.everhomes.asset;

import com.alibaba.fastjson.parser.JSONLexerBase;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.user.UserContext;
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
    @Override
    private AssetService assetService;
    private Logger LOGGER = LoggerFactory.getLogger(AssetPortalPublishHandler.class);
    // zhang jiang gao ke holds a different uri because in this namespace, the older asset UI is still in use
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        if(instanceConfig == null){
            // new pushlish app
            JSONObject config = new JSONObject();
            String urlKey = "url";
            StringBuilder urlValue = new StringBuilder();
            if(namespaceId == 999971){
                urlValue.append("${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix");
            }else {
                urlValue.append("${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix");
            }
            config.put(urlKey, urlValue);
            // get categoryId from asset_category
            long categoryId = assetService.getNextCategoryId(namespaceId, UserContext.currentUserId(), instanceConfig);
            config.put("categoryId", categoryId);
            String ret = config.toJSONString();
            assetService.saveInstanceConfig(categoryId, ret);
            return ret;
        }
        return instanceConfig;

    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        try {
            JSONObject ret = new JSONObject();
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(instanceConfig);
            String url = (String)jsonObject.get("url");
            String categoryId = (String)jsonObject.get("categoryId");
            String urlValue = url + "?categoryId=" + categoryId;
            ret.put("url", urlValue);
            return ret.toJSONString();
        } catch (ParseException e) {
            LOGGER.error("failed to getItemActionData in AssetPortalHandler, instanceConfig is={}", instanceConfig, e);
            e.printStackTrace();
        }
        return null;
    }

    // todo do I have to implement this?
    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData){
        JSONObject object = new JSONObject();
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(actionData);
            String url = (String)jsonObject.get("url");
            String[] split = url.split("\\?");
            object.put("url", split[0]);
            object.put("categoryId", split[1].substring("categoryId=".length()));
            return object.toJSONString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
