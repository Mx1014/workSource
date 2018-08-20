// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchDayLogFileProvider {

	void createPunchDayLogFile(PunchDayLogFile punchDayLogFile);

	void updatePunchDayLogFile(PunchDayLogFile punchDayLogFile);

	PunchDayLogFile findPunchDayLogFileById(Long id);

	List<PunchDayLogFile> listPunchDayLogFile();

}