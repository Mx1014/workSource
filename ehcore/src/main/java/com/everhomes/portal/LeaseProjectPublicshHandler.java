package com.everhomes.portal;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.portal.LeaseProjectInstanceConfig;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.techpark.expansion.LeaseProject;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/12.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.LEASE_PROJECT_MODULE)
public class LeaseProjectPublicshHandler implements PortalPublishHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaseProjectPublicshHandler.class);

    @Autowired
    private PortalService portalService;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        LeaseProjectInstanceConfig leaseProjectInstanceConfig = (LeaseProjectInstanceConfig) StringHelper.fromJsonString(instanceConfig,LeaseProjectInstanceConfig.class);

        if(leaseProjectInstanceConfig == null){
            leaseProjectInstanceConfig = new LeaseProjectInstanceConfig();
        }

        if (leaseProjectInstanceConfig.getCategoryId()!=null){
            return instanceConfig;
        }else{
            ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
            listServiceModuleAppsCommand.setNamespaceId(namespaceId);
            listServiceModuleAppsCommand.setModuleId(ServiceModuleConstants.LEASE_PROJECT_MODULE);
            ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
            Byte max = 0;
            if (apps!=null && apps.getServiceModuleApps().size()>0){
                for (ServiceModuleAppDTO r :apps.getServiceModuleApps()) {
                    LeaseProjectInstanceConfig config = (LeaseProjectInstanceConfig) StringHelper.fromJsonString(r.getInstanceConfig(), LeaseProjectInstanceConfig.class);
                    if (config!=null && config.getCategoryId() != null)
                        max = config.getCategoryId() > max ? config.getCategoryId() : max;
                }
            }
            max ++;
            leaseProjectInstanceConfig.setCategoryId(max);
            return StringHelper.toJsonString(leaseProjectInstanceConfig);
        }
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
        LeaseProjectInstanceConfig leaseProjectInstanceConfig = (LeaseProjectInstanceConfig) StringHelper.fromJsonString(instanceConfig,LeaseProjectInstanceConfig.class);
        if (leaseProjectInstanceConfig!=null && leaseProjectInstanceConfig.getCategoryId()!=null)
            return String.valueOf(leaseProjectInstanceConfig.getCategoryId());
        return null;
    }

    @Override
    public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
        return 40100l;
    }
}
