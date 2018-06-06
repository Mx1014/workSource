// @formatter:off
package com.everhomes.parking;

import java.util.List;

public interface ParkingBusinessPayeeAccountProvider {

	void createParkingBusinessPayeeAccount(ParkingBusinessPayeeAccount parkingBusinessPayeeAccount);

	void updateParkingBusinessPayeeAccount(ParkingBusinessPayeeAccount parkingBusinessPayeeAccount);

	ParkingBusinessPayeeAccount findParkingBusinessPayeeAccountById(Long id);

	List<ParkingBusinessPayeeAccount> listParkingBusinessPayeeAccount();

}