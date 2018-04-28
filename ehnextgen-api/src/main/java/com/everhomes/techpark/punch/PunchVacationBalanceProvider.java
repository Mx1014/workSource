// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchVacationBalanceProvider {

	void createPunchVacationBalance(PunchVacationBalance punchVacationBalance);

	void updatePunchVacationBalance(PunchVacationBalance punchVacationBalance);

	PunchVacationBalance findPunchVacationBalanceById(Long id);

	List<PunchVacationBalance> listPunchVacationBalance();

	PunchVacationBalance findPunchVacationBalanceByDetailId(Long id);
}