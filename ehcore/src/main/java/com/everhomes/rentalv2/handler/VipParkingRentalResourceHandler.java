package com.everhomes.rentalv2.handler;

import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceHandler;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalResourceHandler implements RentalResourceHandler {

    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;

    @Override
    public RentalResource getRentalResourceById(Long id) {

        ParkingLot parkingLot = parkingProvider.findParkingLotById(id);

        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(UserContext.getCurrentNamespaceId(),
                RentalV2ResourceType.VIP_PARKING.getCode());

        RentalResource resource = new RentalResource();
        resource.setId(parkingLot.getId());
        resource.setResourceName(parkingLot.getName());
        resource.setResourceType(RentalV2ResourceType.VIP_PARKING.getCode());
        resource.setResourceTypeId(type.getId());

        return resource;
    }
}
