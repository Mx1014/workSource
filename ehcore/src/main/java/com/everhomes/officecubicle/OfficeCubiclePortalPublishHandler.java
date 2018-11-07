package com.everhomes.officecubicle;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.HandlerGetAppInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.HandlerProcessInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerPublishCommand;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.OFFICE_CUBICLE)
public class OfficeCubiclePortalPublishHandler implements PortalPublishHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubiclePortalPublishHandler.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
        if(StringUtils.isNotEmpty(instanceConfig)){
            JSONObject jsonObj = (JSONObject) JSONObject.parse(instanceConfig);
            Byte currentProjectOnly = jsonObj.getByte("currentProjectOnly");
            if(null != currentProjectOnly){
                configurationProvider.setValue(namespaceId,"officecubicle.currentProjectOnly",String.valueOf(currentProjectOnly));
            }
        }
        return instanceConfig;
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
        JSONObject actionDataObj = new JSONObject();
        JSONObject instanceConfigObj = (JSONObject) JSONObject.parse(instanceConfig);
        if(StringUtils.isEmpty(instanceConfigObj.getString("url"))){
            LOGGER.info("OfficeCubicle portal fail.Cause url is null.");
            actionDataObj.put("url",configurationProvider.getValue(namespaceId,"officecubicle.default.url",""));
        }else{
            actionDataObj.put("url", instanceConfigObj.getString("url"));
        }
        return actionDataObj.toJSONString();
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return null;
    }
}
