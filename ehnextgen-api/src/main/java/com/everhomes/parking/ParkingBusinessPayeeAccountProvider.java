// @formatter:off
package com.everhomes.parking;

import java.util.List;

public interface ParkingBusinessPayeeAccountProvider {

	void createParkingBusinessPayeeAccount(ParkingBusinessPayeeAccount parkingBusinessPayeeAccount);

	void updateParkingBusinessPayeeAccount(ParkingBusinessPayeeAccount parkingBusinessPayeeAccount);

	ParkingBusinessPayeeAccount findParkingBusinessPayeeAccountById(Long id);

	List<ParkingBusinessPayeeAccount> listParkingBusinessPayeeAccount();

	List<ParkingBusinessPayeeAccount> findRepeatParkingBusinessPayeeAccounts(Long id, Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, String bussiness);

	List<ParkingBusinessPayeeAccount> listParkingBusinessPayeeAccountByOwner(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId,String businessType);

    void deleteParkingBusinessPayeeAccount(Long id);

}