// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.user.IdentifierType;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component(LaunchPadItemActionDataHandler.LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX + LaunchPadItemActionDataHandler.DEFAULT)
public class LaunchPadItemActionDataDefaultHandler implements LaunchPadItemActionDataHandler {

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public String refreshActionData(String actionData, String sceneToken) {
        if(actionData == null || "".equals(actionData)){
            return actionData;
        }

        JSONObject jsonObject = (JSONObject) JSONValue.parse(actionData);
        if(jsonObject == null){
            return actionData;
        }

        //替换url中的{key}
        refeshActionDataKey(jsonObject);

        return jsonObject.toJSONString();
    }

    private void refeshActionDataKey(JSONObject jsonObject){
        String url = (String)jsonObject.get("url");

        if(url == null){
            return;
        }

        // 按指定模式在字符串查找
        String pattern = "\\{(.*)\\}";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(url);
        if (m.find( )) {
            String key = null;
            String value = null;
            for(int i = 0; i< m.groupCount(); i++){
                key = m.group(i);
                value = configProvider.getValue(key, "");
                if(value != null){
                    url.replaceAll("{" + key + "}", value);
                }

            }
        }

        jsonObject.put("url", url);
    }
}
