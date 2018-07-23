// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.visitorsys.VisitorsysConstant;
import org.springframework.stereotype.Component;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/18 10:52
 * 企业访客
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + VisitorsysConstant.ENTERPRISE_MODULE_ID)
public class VisitorsysEnterprisePortalPublishHandler implements PortalPublishHandler{
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        return String.format(instanceConfig,namespaceId);
    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
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

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig) {
        return null;
    }

    @Override
    public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
        return null;
    }
}