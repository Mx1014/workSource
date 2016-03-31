// @formatter:off
package com.everhomes.parking;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;

// "ETCP"需与ParkingLotVendor.ETCP的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "ETCP")
public class EtcpParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(EtcpParkingVendorHandler.class);

	@Autowired
	private ParkingProvider parkingProvider;

    @Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId, Long parkingLotId,
        String plateNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyParkingRechargeOrderPayment(OnlinePayBillCommand cmd) {
        // TODO Auto-generated method stub
        
    }
}
