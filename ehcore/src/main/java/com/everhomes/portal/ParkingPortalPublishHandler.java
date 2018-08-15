package com.everhomes.portal;

import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.ParkingInstanceConfig;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/21.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.PARKING_MODULE)
public class ParkingPortalPublishHandler implements PortalPublishHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        ParkingInstanceConfig parkingInstanceConfig = (ParkingInstanceConfig)StringHelper.fromJsonString(instanceConfig, ParkingInstanceConfig.class);

        if(parkingInstanceConfig == null){
            parkingInstanceConfig = new ParkingInstanceConfig();
        }
        
        if(null == parkingInstanceConfig.getResourceTypeId()){
            RentalResourceType rentalResourceType = createRentalResourceType(namespaceId, appName, (byte)0,
                    (byte)0,RentalV2ResourceType.VIP_PARKING.getCode());
            parkingInstanceConfig.setResourceTypeId(rentalResourceType.getId());
        }
        return StringHelper.toJsonString(parkingInstanceConfig);
    }

    @Override
    public String processInstanceConfig(Integer namespaceId,String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }

    private RentalResourceType createRentalResourceType(Integer namespaceId, String name, Byte pageType,Byte payMode,String identify){
        RentalResourceType rentalResourceType = new RentalResourceType();
        rentalResourceType.setNamespaceId(namespaceId);
        rentalResourceType.setName(name);
        if(null == pageType){
            pageType = 0;
        }
        if (null == payMode)
            payMode = 0;
        if (null == identify)
            identify = RentalV2ResourceType.DEFAULT.getCode();
        rentalResourceType.setPageType(pageType);
        rentalResourceType.setPayMode(payMode);
        rentalResourceType.setIdentify(identify);
        rentalResourceType.setStatus(ResourceTypeStatus.NORMAL.getCode());
        rentalResourceType.setUnauthVisible((byte)0);
        rentalv2Provider.createRentalResourceType(rentalResourceType);
        return rentalResourceType;
    }
}
