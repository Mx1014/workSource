// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchLogFileProvider {

	void createPunchLogFile(PunchLogFile punchLogFile);

	void updatePunchLogFile(PunchLogFile punchLogFile);

	PunchLogFile findPunchLogFileById(Long id);

	List<PunchLogFile> listPunchLogFile();

}