// @formatter:off
package com.everhomes.parking;

import java.util.List;

import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.parking.*;

public interface ParkingVendorHandler {
    String PARKING_VENDOR_PREFIX = "ParkingVendor-";

    List<ParkingCardDTO> listParkingCardsByPlate(String ownerType, Long ownerId, Long parkingLotId, String plateNumber);
    
    List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String palteNumber,String cardNo);

    Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order);
   
    ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd);
   
    void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd);
    
    ListCardTypeResponse listCardType(ListCardTypeCommand cmd);
    
    void updateParkingRechargeOrderRate(ParkingRechargeOrder order);
    
    ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId, Long parkingLotId, String plateNumber);
    
    OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd);

    ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd);

    void lockParkingCar(LockParkingCarCommand cmd);

	GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd);

    boolean recharge(ParkingRechargeOrder order);
}
