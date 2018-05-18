// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchMonthReportProvider {

	void createPunchMonthReport(PunchMonthReport punchMonthReport);

	void updatePunchMonthReport(PunchMonthReport punchMonthReport);

	PunchMonthReport findPunchMonthReportById(Long id);

	List<PunchMonthReport> listPunchMonthReport();

}