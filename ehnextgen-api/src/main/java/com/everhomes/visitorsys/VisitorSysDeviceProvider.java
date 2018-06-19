// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysDeviceProvider {

	void createVisitorSysDevice(VisitorSysDevice visitorSysDevice);

	void updateVisitorSysDevice(VisitorSysDevice visitorSysDevice);

	VisitorSysDevice findVisitorSysDeviceById(Long id);

	List<VisitorSysDevice> listVisitorSysDevice();

    List<VisitorSysDevice> listVisitorSysDeviceByOwner(Integer namespaceId, String ownerType, Long ownerId);

	void deleteDevice(Integer namespaceId, Long id);

    VisitorSysDevice findVisitorSysDeviceByDeviceId(String deviceType, String deviceId);
}