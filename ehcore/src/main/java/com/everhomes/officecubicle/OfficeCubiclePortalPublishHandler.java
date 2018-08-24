package com.everhomes.officecubicle;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.OFFICE_CUBICLE)
public class OfficeCubiclePortalPublishHandler implements PortalPublishHandler {
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
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
