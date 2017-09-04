package com.everhomes.portal;

import com.everhomes.community.Community;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.RentalActionData;
import com.everhomes.rest.common.ServiceAllianceActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.DetailFlag;
import com.everhomes.rest.portal.RentalInstanceConfig;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.rentalv2.RentalSiteStatus;
import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAllianceSkipRule;
import com.everhomes.yellowPage.ServiceAlliances;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
