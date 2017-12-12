// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Date;
import java.util.List;

public interface StatEventTaskLogProvider {

	void createOrUpdateStatEventTaskLog(StatEventTaskLog statEventTaskLog);

	void updateStatEventTaskLog(StatEventTaskLog statEventTaskLog);

	StatEventTaskLog findById(Long id);

    List<StatEventTaskLog> findByTaskDate(Date taskDate);

    void deleteEventTaskLogByDate(Date date);

    List<StatEventTaskLog> listEventTaskLog(Date startDate, Date endDate);
}