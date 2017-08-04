// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventParamProvider {

	void createStatEventParam(StatEventParam statEventParam);

	void updateStatEventParam(StatEventParam statEventParam);

	StatEventParam findStatEventParamById(Long id);

    StatEventParam findStatEventParam(String eventName, String paramKey);

    List<StatEventParam> listParam(String eventName, Integer eventVersion);
}