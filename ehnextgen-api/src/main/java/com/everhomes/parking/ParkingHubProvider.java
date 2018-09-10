// @formatter:off
package com.everhomes.parking;

import java.util.List;

public interface ParkingHubProvider {

	void createParkingHub(ParkingHub parkingHub);

	void updateParkingHub(ParkingHub parkingHub);

	ParkingHub findParkingHubById(Long id);

	List<ParkingHub> listParkingHub();

	List<ParkingHub> listParkingHub(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, Long pageAnchor, Integer pageSize);

    ParkingHub findParkingHubByMac(Integer namespaceId, String hubMac);
}