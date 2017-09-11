// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;

public interface StatEventContentLogProvider {

	void createStatEventLogContent(StatEventLogContent statEventLogContent);

	void updateStatEventLogContent(StatEventLogContent statEventLogContent);

	StatEventLogContent findStatEventLogContentById(Long id);

    List<StatEventLogContent> listEventLogContent(byte status, Timestamp minTime, Timestamp maxTime);

}