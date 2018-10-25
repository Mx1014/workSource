package com.everhomes.portal;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.portal.LeaseProjectInstanceConfig;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.techpark.expansion.LeasePromotionConfigType;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.techpark.expansion.EnterpriseLeaseIssuerProvider;
import com.everhomes.techpark.expansion.LeaseProject;
import com.everhomes.techpark.expansion.LeasePromotionConfig;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.LEASE_PROJECT_MODULE)
public class LeaseProjectPublicshHandler implements PortalPublishHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaseProjectPublicshHandler.class);

    @Autowired
    private PortalService portalService;
    @Autowired
    private EnterpriseLeaseIssuerProvider enterpriseLeaseIssuerProvider;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        LeaseProjectInstanceConfig leaseProjectInstanceConfig = (LeaseProjectInstanceConfig) StringHelper.fromJsonString(instanceConfig,LeaseProjectInstanceConfig.class);

        if(leaseProjectInstanceConfig == null){
            leaseProjectInstanceConfig = new LeaseProjectInstanceConfig();
        }

        if (leaseProjectInstanceConfig.getCategoryId()!=null){
            updateConfig(namespaceId,leaseProjectInstanceConfig);
            return StringHelper.toJsonString(leaseProjectInstanceConfig);
        }else{
            ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
            listServiceModuleAppsCommand.setNamespaceId(namespaceId);
            listServiceModuleAppsCommand.setModuleId(ServiceModuleConstants.LEASE_PROJECT_MODULE);
            ListServiceModuleAppsResponse apps = portalService.listServiceModuleApps(listServiceModuleAppsCommand);
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
            updateConfig(namespaceId,leaseProjectInstanceConfig);
            return StringHelper.toJsonString(leaseProjectInstanceConfig);
        }
    }

    private void updateConfig(Integer namespaceId, LeaseProjectInstanceConfig config) {
        if (config.getHideAddressFlag() != null) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.HIDE_ADDRESS_FLAG.getCode(),
                    config.getCategoryId().longValue());
            if (!(config.getHideAddressFlag() == 0 && config2 == null)) {
                updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.HIDE_ADDRESS_FLAG.getCode(),
                        config.getHideAddressFlag().toString(),config2);
            }
        }

        if (config.getBuildingIntroduceFlag() != null) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.BUILDING_INTRODUCE_FLAG.getCode(),
                    config.getCategoryId().longValue());
            if (!(config.getBuildingIntroduceFlag() == 0 && config2 == null)) {
                updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.BUILDING_INTRODUCE_FLAG.getCode(),
                        config.getBuildingIntroduceFlag().toString(),config2);
            }
        }

        if (config.getRenewFlag() != null) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.RENEW_FLAG.getCode(),
                    config.getCategoryId().longValue());
            if (!(config.getRenewFlag() == 0 && config2 == null)) {
                updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.RENEW_FLAG.getCode(),
                        config.getRenewFlag().toString(),config2);
            }
        }

        if (config.getAreaSearchFlag() != null) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.AREA_SEARCH_FLAG.getCode(),
                    config.getCategoryId().longValue());
            if (!(config.getAreaSearchFlag() == 0 && config2 == null)) {
                updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.AREA_SEARCH_FLAG.getCode(),
                        config.getAreaSearchFlag().toString(),config2);
            }
        }

        if (config.getRentAmountFlag() != null) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.RENT_AMOUNT_FLAG.getCode(),
                    config.getCategoryId().longValue());
            if (!(config.getRentAmountFlag() == 0 && config2 == null)) {
                updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.RENT_AMOUNT_FLAG.getCode(),
                        config.getRentAmountFlag().toString(),config2);
            }
        }

        if (!StringUtils.isBlank(config.getRentAmountUnit()) ) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.RENT_AMOUNT_UNIT.getCode(),
                    config.getCategoryId().longValue());
            updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.RENT_AMOUNT_UNIT.getCode(),
                    config.getRentAmountUnit(),config2);
        }

        if (!StringUtils.isBlank(config.getDisplayNameStr()) ) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.DISPLAY_NAME_STR.getCode(),
                    config.getCategoryId().longValue());
            updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.DISPLAY_NAME_STR.getCode(),
                    config.getDisplayNameStr(),config2);
        }

        if (!StringUtils.isBlank(config.getDisplayOrderStr()) ) {
            LeasePromotionConfig config2 = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(namespaceId, LeasePromotionConfigType.DISPLAY_ORDER_STR.getCode(),
                    config.getCategoryId().longValue());
            updatePromotionConfig(namespaceId,config.getCategoryId().longValue(),LeasePromotionConfigType.DISPLAY_ORDER_STR.getCode(),
                    config.getDisplayOrderStr(),config2);
        }
    }

    private void updatePromotionConfig(Integer namespaceId,Long categoryId,String configName,String configValue,LeasePromotionConfig config){
        if (config == null) {
            LeasePromotionConfig newConfig = new LeasePromotionConfig();
            newConfig.setNamespaceId(namespaceId);
            newConfig.setCategoryId(categoryId);
            newConfig.setConfigName(configName);
            newConfig.setConfigValue(configValue);
            enterpriseLeaseIssuerProvider.createLeasePromotionConfig(newConfig);
        } else {
            config.setConfigValue(configValue);
            enterpriseLeaseIssuerProvider.updateLeasePromotionConfig(config);
        }
    }

    @Override
    public String processInstanceConfig(Integer namespaceId,String instanceConfig) {
        LeaseProjectInstanceConfig leaseProjectInstanceConfig = (LeaseProjectInstanceConfig) StringHelper.fromJsonString(instanceConfig,LeaseProjectInstanceConfig.class);
        if (leaseProjectInstanceConfig.getCategoryId() != null) { //不是新建的应用
            List<LeasePromotionConfig> configs = enterpriseLeaseIssuerProvider.listLeasePromotionConfigs(namespaceId, leaseProjectInstanceConfig.getCategoryId().longValue());
            if (configs != null && configs.size() > 0)
                for (LeasePromotionConfig config : configs) {
                    LeasePromotionConfigType configType = LeasePromotionConfigType.fromCode(config.getConfigName());
                    if (configType != null) {
                        switch (configType) {
                            case RENT_AMOUNT_FLAG:
                                leaseProjectInstanceConfig.setRentAmountFlag(Byte.valueOf(config.getConfigValue()));
                                break;
                            case RENT_AMOUNT_UNIT:
                                leaseProjectInstanceConfig.setRentAmountUnit(config.getConfigValue());
                                break;
                            case RENEW_FLAG:
                                leaseProjectInstanceConfig.setRenewFlag(Byte.valueOf(config.getConfigValue()));
                                break;
                            case AREA_SEARCH_FLAG:
                                leaseProjectInstanceConfig.setAreaSearchFlag(Byte.valueOf(config.getConfigValue()));
                                break;
                            case BUILDING_INTRODUCE_FLAG:
                                leaseProjectInstanceConfig.setBuildingIntroduceFlag(Byte.valueOf(config.getConfigValue()));
                                break;
                            case DISPLAY_NAME_STR:
                                leaseProjectInstanceConfig.setDisplayNameStr(config.getConfigValue());
                                break;
                            case DISPLAY_ORDER_STR:
                                leaseProjectInstanceConfig.setDisplayOrderStr(config.getConfigValue());
                                break;
                            case HIDE_ADDRESS_FLAG:
                                leaseProjectInstanceConfig.setHideAddressFlag(Byte.valueOf(config.getConfigValue()));
                                break;
                            default:
                        }
                    }
                }
        }
        return StringHelper.toJsonString(leaseProjectInstanceConfig);
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
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
