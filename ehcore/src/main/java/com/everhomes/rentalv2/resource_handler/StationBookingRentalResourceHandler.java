package com.everhomes.rentalv2.resource_handler;

import com.everhomes.officecubicle.OfficeCubicleProvider;
import com.everhomes.officecubicle.OfficeCubicleSpace;
import com.everhomes.parking.ParkingLot;
import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceHandler;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.RentalOwnerType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "station_booking")
public class StationBookingRentalResourceHandler implements RentalResourceHandler {
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private OfficeCubicleProvider officeCubicleProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Override
    public RentalResource getRentalResourceById(Long id) {
    	OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(id);
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(space.getNamespaceId(),
                RentalV2ResourceType.STATION_BOOKING.getCode());

        RentalResource resource = new RentalResource();
        resource.setId(space.getId());
        resource.setResourceName(space.getName());
        resource.setResourceType(RentalV2ResourceType.VIP_PARKING.getCode());
        resource.setResourceTypeId(type.getId());
        resource.setResourceCounts(Double.valueOf(space.getShortRentNums()));
        resource.setAutoAssign(NormalFlag.NONEED.getCode());
        resource.setMultiUnit(NormalFlag.NONEED.getCode());
        resource.setCommunityId(space.getOwnerId());

        return resource;
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
