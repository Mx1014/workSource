package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.OFFICE_CUBICLE)
public class OfficeCubiclePortalPublishHandler implements PortalPublishHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubiclePortalPublishHandler.class);

    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
        StationBookingInstanceConfig stationBookingInstanceConfig = (StationBookingInstanceConfig) StringHelper.fromJsonString(instanceConfig, StationBookingInstanceConfig.class);

        if (stationBookingInstanceConfig == null)
            stationBookingInstanceConfig = new StationBookingInstanceConfig();

        Byte currentProjectOnly = stationBookingInstanceConfig.getCurrentProjectOnly();
        if (null != currentProjectOnly) {
            configurationProvider.setValue(namespaceId, "officecubicle.currentProjectOnly", String.valueOf(currentProjectOnly));
        }
        Long resourceTypeId = stationBookingInstanceConfig.getResourceTypeId();
        if (null == resourceTypeId) {
            RentalResourceType rentalResourceType = createRentalResourceType(namespaceId, appName);
            stationBookingInstanceConfig.setResourceTypeId(rentalResourceType.getId());
        }
        stationBookingInstanceConfig.setUrl(String.format(stationBookingInstanceConfig.getUrl(),namespaceId));
        return StringHelper.toJsonString(stationBookingInstanceConfig);
    }
    
    private RentalResourceType createRentalResourceType(Integer namespaceId, String name){
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(namespaceId, RentalV2ResourceType.STATION_BOOKING.getCode());
        if (type != null){ //原来就有了 直接拿过来用 不重新生成
            return type;
        }
        return rentalCommonService.createRentalResourceType(namespaceId,name,(byte)0,
                (byte)0,RentalV2ResourceType.STATION_BOOKING.getCode(),(byte)1,(byte)0,(byte)0);
    }
    
    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
        JSONObject actionDataObj = new JSONObject();
        JSONObject instanceConfigObj = (JSONObject) JSONObject.parse(instanceConfig);
        if(StringUtils.isEmpty(instanceConfigObj.getString("url"))){
            LOGGER.info("OfficeCubicle portal fail.Cause url is null.");
            actionDataObj.put("url",configurationProvider.getValue(namespaceId,"officecubicle.default.url",""));
        }else{
            actionDataObj.put("url", instanceConfigObj.getString("url"));
        }
        return actionDataObj.toJSONString();
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return null;
    }
}
