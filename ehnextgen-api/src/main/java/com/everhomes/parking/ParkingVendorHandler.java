// @formatter:off
package com.everhomes.parking;

import java.util.List;

import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.RequestParkingCardCommand;

public interface ParkingVendorHandler {
    String PARKING_VENDOR_PREFIX = "ParkingVendor-";
    
    List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId, Long parkingLotId, String plateNumber);
    
    List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String palteNumber,String cardNo);
    
    void notifyParkingRechargeOrderPayment(ParkingRechargeOrder order,String payStatus);
   
    ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd);
   
    void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd);
    
    void refreshParkingRechargeOrderStatus();
    
    ParkingCardRequestDTO getRequestParkingCard(RequestParkingCardCommand cmd);
}
