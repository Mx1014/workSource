// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;

public interface StatEventLogContentProvider {

	void createStatEventLogContent(StatEventLogContent statEventLogContent);

	void updateStatEventLogContent(StatEventLogContent statEventLogContent);

	StatEventLogContent findStatEventLogContentById(Long id);

    List<StatEventLogContent> listEventLogContent(Timestamp minTime, Timestamp maxTime);

}