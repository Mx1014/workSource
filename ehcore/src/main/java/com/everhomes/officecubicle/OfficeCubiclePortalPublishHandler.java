package com.everhomes.officecubicle;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
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
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        if(StringUtils.isNotEmpty(instanceConfig)){
            JSONObject jsonObj = (JSONObject) JSONObject.parse(instanceConfig);
            Byte currentProjectOnly = jsonObj.getByte("officecubicle.currentProjectOnly");
            if(null != currentProjectOnly){
                configurationProvider.setValue(namespaceId,"officecubicle.currentProjectOnly",String.valueOf(currentProjectOnly));
            }
        }
        return null;
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig) {
        return null;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return null;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return null;
    }
}
