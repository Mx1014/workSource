// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventLogProvider {

	void createStatEventLog(StatEventLog statEventLog);

	void updateStatEventLog(StatEventLog statEventLog);

	StatEventLog findStatEventLogById(Long id);

	// List<StatEventLog> listStatEventLog();

}