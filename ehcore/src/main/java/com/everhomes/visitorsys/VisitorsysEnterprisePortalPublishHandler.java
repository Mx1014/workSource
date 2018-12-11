// @formatter:off
package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.module.ServiceModuleEntry;
import com.everhomes.module.ServiceModuleEntryProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.visitorsys.VisitorsysConstant;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/18 10:52
 * 企业访客
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + VisitorsysConstant.ENTERPRISE_MODULE_ID)
public class VisitorsysEnterprisePortalPublishHandler implements PortalPublishHandler{
    @Autowired
    private PortalVersionProvider portalVersionProvider;
    @Autowired
    private ServiceModuleAppProvider serviceModuleAppProvider;
    @Autowired
    private ServiceModuleEntryProvider serviceModuleEntryProvider;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
        PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
        List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, releaseVersion == null ? null : releaseVersion.getId(), VisitorsysConstant.COMMUNITY_MODULE_ID);
        if(serviceModuleApps!=null && serviceModuleApps.size()>0){
            return String.format(instanceConfig,namespaceId,serviceModuleApps.get(serviceModuleApps.size()-1).getOriginId());
        }
        return String.format(instanceConfig,namespaceId);
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
        JSONObject jsonObject = JSON.parseObject(instanceConfig);
        if (!StringUtils.isEmpty(jsonObject.getString("url")) && cmd.getModuleEntryId() != null) {
            String url = jsonObject.getString("url");
            String newUrl = "";
            ServiceModuleEntry serviceModuleEntry = this.serviceModuleEntryProvider.findById(cmd.getModuleEntryId());
            if (serviceModuleEntry != null) {
                String[] urls = url.split("[?]");
                if (urls.length > 1) {
                    jsonObject.remove("url");
                    newUrl = urls[0] + "?sceneType=" + serviceModuleEntry.getSceneType() + "&" + urls[1];
                    jsonObject.put("url",newUrl);
                    return jsonObject.toJSONString();
                }
            }
        }
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return actionData;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig, HandlerGetCustomTagCommand cmd) {
        return null;
    }

    @Override
    public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
        return null;
    }
}