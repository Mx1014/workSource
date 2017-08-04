// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Date;

public interface StatEventTaskLogProvider {

	void createOrUpdateStatEventTaskLog(StatEventTaskLog statEventTaskLog);

	void updateStatEventTaskLog(StatEventTaskLog statEventTaskLog);

	StatEventTaskLog findById(Long id);

    StatEventTaskLog findByTaskDate(Date taskDate);
}