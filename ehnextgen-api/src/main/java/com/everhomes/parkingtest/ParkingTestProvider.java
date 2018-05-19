// @formatter:off
package com.everhomes.parkingtest;

import java.util.List;

import com.everhomes.parking.ParkingCarVerification;

public interface ParkingTestProvider {
    
    List<ParkingLotTest> listParkingLotsTest(Long Id,int from, int pageSize);

	ParkingLotTest findParkingLotById(Long id);

	void updateParkingLotTest(ParkingLotTest parkingLot);

	void createParkingSpace(ParkingLotTest parkingLot);

	ParkingLotTest findParkingCarVerificationById(Long id);

	void DeleteListParkingLotsTest(ParkingLotTest verification);
    
}
