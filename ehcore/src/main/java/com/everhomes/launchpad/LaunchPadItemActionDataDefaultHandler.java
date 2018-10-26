// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.StringTemplateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component(LaunchPadItemActionDataHandler.LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX + LaunchPadItemActionDataHandler.DEFAULT)
public class LaunchPadItemActionDataDefaultHandler implements LaunchPadItemActionDataHandler {

    @Autowired
    private ConfigurationProvider configProvider;


    @Override
    public String refreshActionData(String actionData) {
        if(actionData == null || "".equals(actionData)){
            return actionData;
        }

        JSONObject jsonObject = (JSONObject) JSONValue.parse(actionData);
        if(jsonObject == null){
            return actionData;
        }

        //替换url中的{key}
        refeshActionDataKey(jsonObject, UserContext.getCurrentNamespaceId());

        return jsonObject.toJSONString();
    }

    private void refeshActionDataKey(JSONObject jsonObject, Integer namespaceId){
        String url = (String)jsonObject.get("url");

        if(url == null){
            return;
        }

        // 按指定模式在字符串查找
        Set<String> keys = StringTemplateUtil.getTemplateKeys(url);
        if(keys == null || keys.size() == 0){
            return;
        }

        Map<String, String> model = getConfigsByKeys(keys, namespaceId);
        if(model == null || model.size() == 0){
            return;
        }

        try {

            //将model和url中的“.”替换成“_”，防止FreeMarker报错
            Map<String, String> newModel = new HashMap<>();
            for(String key: model.keySet()){
                String value = model.get(key);
                String newKey = key.replaceAll("\\.", "_");
                url = url.replaceAll( "\\$\\{" + key + "\\}","\\$\\{" + newKey + "\\}");
                newModel.put(key.replaceAll("\\.", "_"), value);
            }

            Template freeMarkerTemplate = new Template("url", url, null);
            url = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, newModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.put("url", url);
    }

    private Map<String, String> getConfigsByKeys(Set<String> keys, Integer namespaceId){
        Map<String, String> result = new HashMap<>();
        if(keys == null || keys.size() ==0){
            return result;
        }

        keys.forEach(r ->{
            String value = configProvider.getValue(namespaceId, r, "");
            result.put(r, value);
        });

        return result;
    }


}
