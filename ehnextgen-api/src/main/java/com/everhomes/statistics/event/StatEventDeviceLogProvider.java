// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventDeviceLogProvider {

	void createStatEventDeviceLog(StatEventDeviceLog statEventDeviceLog);

	void updateStatEventDeviceLog(StatEventDeviceLog statEventDeviceLog);

	StatEventDeviceLog findStatEventDeviceLogById(Long id);

	// List<StatEventDeviceLog> listStatEventDeviceLog();

}