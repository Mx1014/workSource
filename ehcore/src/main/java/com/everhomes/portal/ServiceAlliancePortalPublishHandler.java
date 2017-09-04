package com.everhomes.portal;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.common.ServiceAllianceActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.DetailFlag;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAllianceSkipRule;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sfyan on 2017/8/30.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.SERVICE_ALLIANCE_MODULE)
public class ServiceAlliancePortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAlliancePortalPublishHandler.class);


    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private YellowPageProvider yellowPageProvider;


    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = (ServiceAllianceInstanceConfig)StringHelper.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
        if(null == serviceAllianceInstanceConfig.getType()){
            ServiceAllianceCategories serviceAllianceCategories = createServiceAlliance(namespaceId, serviceAllianceInstanceConfig.getDetailFlag(), itemLabel);
            serviceAllianceInstanceConfig.setType(serviceAllianceCategories.getId());
        }else{
            updateServiceAlliance(namespaceId, serviceAllianceInstanceConfig.getType(), serviceAllianceInstanceConfig.getDetailFlag(), itemLabel);
        }
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        ServiceAllianceActionData serviceAllianceActionData = (ServiceAllianceActionData)StringHelper.fromJsonString(actionData, ServiceAllianceActionData.class);
        ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(serviceAllianceActionData.getParentId(), namespaceId);
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = new ServiceAllianceInstanceConfig();
        serviceAllianceInstanceConfig.setType(serviceAllianceActionData.getParentId());
        serviceAllianceInstanceConfig.setDisplayType(serviceAllianceActionData.getDisplayType());
        if(null == rule){
            serviceAllianceInstanceConfig.setDetailFlag(DetailFlag.NO.getCode());
        }else{
            serviceAllianceInstanceConfig.setDetailFlag(DetailFlag.YES.getCode());
        }
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = (ServiceAllianceInstanceConfig)StringHelper.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
        ServiceAllianceActionData serviceAllianceActionData = new ServiceAllianceActionData();
        serviceAllianceActionData.setType(serviceAllianceInstanceConfig.getType());
        serviceAllianceActionData.setParentId(serviceAllianceInstanceConfig.getType());
        serviceAllianceActionData.setDisplayType(serviceAllianceInstanceConfig.getDisplayType());
        return StringHelper.toJsonString(serviceAllianceActionData);
    }

    private ServiceAllianceCategories createServiceAlliance(Integer namespaceId, Byte detailFlag, String name){
        User user = UserContext.current().getUser();
        ServiceAllianceCategories serviceAllianceCategories = new ServiceAllianceCategories();
        serviceAllianceCategories.setName(name);
        serviceAllianceCategories.setNamespaceId(namespaceId);
        serviceAllianceCategories.setParentId(0L);
        List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
        if(null != communities && communities.size() > 0){
            Community community = communities.get(0);
            serviceAllianceCategories.setOwnerType("community");
            serviceAllianceCategories.setOwnerId(community.getId());
            serviceAllianceCategories.setCreatorUid(user.getId());
            serviceAllianceCategories.setDeleteUid(user.getId());
            serviceAllianceCategories.setStatus(YellowPageStatus.ACTIVE.getCode());
            yellowPageProvider.createServiceAllianceCategory(serviceAllianceCategories);

            ServiceAlliances serviceAlliances = new ServiceAlliances();
            serviceAlliances.setParentId(0L);
            serviceAlliances.setOwnerType("community");
            serviceAlliances.setOwnerId(community.getId());
            serviceAlliances.setName(name);
            serviceAlliances.setDisplayName(name);
            serviceAlliances.setType(serviceAllianceCategories.getId());
            serviceAlliances.setStatus(YellowPageStatus.ACTIVE.getCode());
            serviceAlliances.setAddress("");
            serviceAlliances.setSupportType((byte)0);
            serviceAlliances.setDisplayFlag(DisplayFlagType.SHOW.getCode());
            yellowPageProvider.createServiceAlliances(serviceAlliances);

            if(DetailFlag.fromCode(detailFlag) == DetailFlag.YES){
                ServiceAllianceSkipRule serviceAllianceSkipRule = new ServiceAllianceSkipRule();
                serviceAllianceSkipRule.setNamespaceId(namespaceId);
                serviceAllianceSkipRule.setServiceAllianceCategoryId(serviceAllianceCategories.getId());
                yellowPageProvider.createServiceAllianceSkipRule(serviceAllianceSkipRule);
            }
        }else{
            LOGGER.error("namespace not community. namespaceId = {}", namespaceId);
        }


        return serviceAllianceCategories;
    }

    private ServiceAllianceCategories updateServiceAlliance(Integer namespaceId, Long type, Byte detailFlag, String name){
        ServiceAllianceCategories serviceAllianceCategories = yellowPageProvider.findCategoryById(type);
        List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
        if(null != communities && communities.size() > 0 && null != serviceAllianceCategories){
            Community community = communities.get(0);
            serviceAllianceCategories.setName(name);
            yellowPageProvider.updateServiceAllianceCategory(serviceAllianceCategories);

            ServiceAlliances serviceAlliances = yellowPageProvider.queryServiceAllianceTopic("community", community.getId(), type);
            if(null != serviceAlliances){
                serviceAlliances.setName(name);
                serviceAlliances.setDisplayName(name);
                yellowPageProvider.updateServiceAlliances(serviceAlliances);
            }else{
                LOGGER.error("serviceAlliances is null. communityId = {}, type = {}", community.getId(), type);
            }

            ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(type);
            if(DetailFlag.fromCode(detailFlag) == DetailFlag.YES){
                if(null == rule){
                    ServiceAllianceSkipRule serviceAllianceSkipRule = new ServiceAllianceSkipRule();
                    serviceAllianceSkipRule.setNamespaceId(namespaceId);
                    serviceAllianceSkipRule.setServiceAllianceCategoryId(serviceAllianceCategories.getId());
                }
            }else{
                if(null != rule){
                    yellowPageProvider.deleteServiceAllianceSkipRule(rule.getId());
                }
            }
        }else{
            LOGGER.error("namespace not community or service alliance category is null. namespaceId = {}, type = {}", namespaceId, type);
        }
        return serviceAllianceCategories;
    }
}
