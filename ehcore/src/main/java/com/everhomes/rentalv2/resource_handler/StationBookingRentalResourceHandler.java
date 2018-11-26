package com.everhomes.rentalv2.resource_handler;

import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceHandler;
import com.everhomes.rest.rentalv2.RentalOwnerType;
import com.everhomes.rest.rentalv2.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "station_booking")
public class StationBookingRentalResourceHandler implements RentalResourceHandler {
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    public RentalResource getRentalResourceById(Long id) {
        return null;
    }

    @Override
    public void updateRentalResource(String resourceJson) {

    }

    @Override
    public void buildDefaultRule(AddDefaultRuleAdminCommand addCmd) {

    }

    @Override
    public void setRuleOwnerTypeByResource(QueryDefaultRuleAdminCommand queryRuleCmd, RentalResource resource) {
        queryRuleCmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
        queryRuleCmd.setOwnerId(resource.getCommunityId());
    }
}
