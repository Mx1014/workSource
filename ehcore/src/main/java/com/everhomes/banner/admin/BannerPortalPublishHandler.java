// @formatter:off
package com.everhomes.banner.admin;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import org.springframework.stereotype.Component;

/**
 * create by yanlong.liang 20181026
 * 广告管理多应用发布
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.NEWS_MODULE)
public class BannerPortalPublishHandler implements PortalPublishHandler {
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
