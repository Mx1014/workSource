package com.everhomes.portal;

import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.RentalActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.RentalInstanceConfig;
import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sfyan on 2017/8/30.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.RENTAL_MODULE)
public class RentalPortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(RentalPortalPublishHandler.class);


    @Autowired
    private Rentalv2Provider rentalv2Provider;


    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        RentalInstanceConfig rentalInstanceConfig = (RentalInstanceConfig)StringHelper.fromJsonString(instanceConfig, RentalInstanceConfig.class);
        if(null == rentalInstanceConfig.getResourceTypeId()){
            RentalResourceType rentalResourceType = createRentalResourceType(namespaceId, itemLabel, rentalInstanceConfig.getPageType());
            rentalInstanceConfig.setResourceTypeId(rentalResourceType.getId());
        }else{
            updateRentalResourceType(namespaceId, rentalInstanceConfig.getResourceTypeId(), rentalInstanceConfig.getPageType(), itemLabel);
        }
        return StringHelper.toJsonString(rentalInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    private RentalResourceType createRentalResourceType(Integer namespaceId, String name, Byte pageType){
        RentalResourceType rentalResourceType = new RentalResourceType();
        rentalResourceType.setNamespaceId(namespaceId);
        rentalResourceType.setName(name);
        if(null == pageType){
            pageType = 0;
        }
        rentalResourceType.setPageType(pageType);
        rentalResourceType.setStatus(ResourceTypeStatus.NORMAL.getCode());
        rentalv2Provider.createRentalResourceType(rentalResourceType);
        return rentalResourceType;
    }

    private RentalResourceType updateRentalResourceType(Integer namespaceId, Long resourceTypeId, Byte pageType, String name){
        RentalResourceType rentalResourceType = rentalv2Provider.findRentalResourceTypeById(resourceTypeId);
        if(null != rentalResourceType){
            if(null == pageType){
                pageType = 0;
            }
            rentalResourceType.setPageType(pageType);
            rentalResourceType.setName(name);
            rentalv2Provider.updateRentalResourceType(rentalResourceType);
        }else{
            LOGGER.error("rental resource type is null. resourceTypeId = {}", resourceTypeId);
        }
        return rentalResourceType;
    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String actionData, String instanceConfig) {
        RentalActionData actionDataObject = (RentalActionData)StringHelper.fromJsonString(actionData,RentalActionData.class);
        if (actionDataObject!=null && actionDataObject.getResourceTypeId()!=null)
            return String.valueOf(actionDataObject.getResourceTypeId());
        return null;
    }
}
