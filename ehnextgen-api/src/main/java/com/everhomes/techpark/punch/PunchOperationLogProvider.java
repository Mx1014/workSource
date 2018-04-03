// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchOperationLogProvider {

	void createPunchOperationLog(PunchOperationLog punchOperationLog);

	void updatePunchOperationLog(PunchOperationLog punchOperationLog);

	PunchOperationLog findPunchOperationLogById(Long id);

	List<PunchOperationLog> listPunchOperationLog();

}