// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;

public interface StatEventLogProvider {

	void createStatEventLog(StatEventLog statEventLog);

	void updateStatEventLog(StatEventLog statEventLog);

	StatEventLog findStatEventLogById(Long id);

    List<Long> listDeviceGenIdBySessionId(String sessionId);

    List<StatEventLog> listEventLog(Timestamp minTime, Timestamp maxTime);

    // List<StatEventLog> listStatEventLog();

}