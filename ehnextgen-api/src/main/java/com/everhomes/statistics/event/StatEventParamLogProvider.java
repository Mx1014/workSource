// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventParamLogProvider {

	void createStatEventParamLog(StatEventParamLog statEventParamLog);

	void updateStatEventParamLog(StatEventParamLog statEventParamLog);

	StatEventParamLog findStatEventParamLogById(Long id);

	// List<StatEventParamLog> listStatEventParamLog();

}