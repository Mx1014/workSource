// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventProvider {

	void createStatEvent(StatEvent statEvent);

	void updateStatEvent(StatEvent statEvent);

	StatEvent findStatEventById(Long id);

	// List<StatEvent> listStatEvent();

}