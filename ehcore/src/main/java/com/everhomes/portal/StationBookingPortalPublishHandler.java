package com.everhomes.portal;

import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.OFFICE_CUBICLE)
public class StationBookingPortalPublishHandler implements PortalPublishHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StationBookingPortalPublishHandler.class);

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
        StationBookingInstanceConfig bookingInstanceConfig = (StationBookingInstanceConfig)StringHelper.fromJsonString(instanceConfig, StationBookingInstanceConfig.class);
        if(bookingInstanceConfig == null){
            bookingInstanceConfig = new StationBookingInstanceConfig();
        }
        if(null == bookingInstanceConfig.getResourceTypeId()){
            RentalResourceType rentalResourceType = createRentalResourceType(namespaceId, appName);
            bookingInstanceConfig.setResourceTypeId(rentalResourceType.getId());
        }

        return StringHelper.toJsonString(bookingInstanceConfig);
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return actionData;
    }
    private RentalResourceType createRentalResourceType(Integer namespaceId, String name){
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(namespaceId, RentalV2ResourceType.STATION_BOOKING.getCode());
        if (type != null){ //原来就有了 直接拿过来用 不重新生成
            return type;
        }
        return rentalCommonService.createRentalResourceType(namespaceId,name,(byte)0,
                (byte)0,RentalV2ResourceType.STATION_BOOKING.getCode(),(byte)1);
    }
}
