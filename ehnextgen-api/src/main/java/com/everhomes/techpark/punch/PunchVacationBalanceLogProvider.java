// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchVacationBalanceLogProvider {

	void createPunchVacationBalanceLog(PunchVacationBalanceLog punchVacationBalanceLog);

	void updatePunchVacationBalanceLog(PunchVacationBalanceLog punchVacationBalanceLog);

	PunchVacationBalanceLog findPunchVacationBalanceLogById(Long id);

	List<PunchVacationBalanceLog> listPunchVacationBalanceLog();

}