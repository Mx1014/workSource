// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysDeviceProvider {

	void createVisitorSysDevice(VisitorSysDevice visitorSysDevice);

	void updateVisitorSysDevice(VisitorSysDevice visitorSysDevice);

	VisitorSysDevice findVisitorSysDeviceById(Long id);

	List<VisitorSysDevice> listVisitorSysDevice();

}