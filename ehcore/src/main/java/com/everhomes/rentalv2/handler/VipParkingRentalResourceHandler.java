package com.everhomes.rentalv2.handler;

import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceHandler;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalResourceHandler implements RentalResourceHandler {

    @Autowired
    private ParkingProvider parkingProvider;

    @Override
    public RentalResource getRentalResourceById(Long id) {

        ParkingLot parkingLot = parkingProvider.findParkingLotById(id);

        RentalResource resource = new RentalResource();
        resource.setId(parkingLot.getId());
        resource.setResourceName(parkingLot.getName());
        resource.setResourceType(RentalV2ResourceType.VIP_PARKING.getCode());


        return resource;
    }
}
