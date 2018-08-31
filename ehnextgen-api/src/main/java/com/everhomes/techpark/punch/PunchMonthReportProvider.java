// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface PunchMonthReportProvider {

	void createPunchMonthReport(PunchMonthReport punchMonthReport);

	void updatePunchMonthReport(PunchMonthReport punchMonthReport);

	PunchMonthReport findPunchMonthReportById(Long id);

	List<PunchMonthReport> listPunchMonthReport();

	List<PunchMonthReport> listPunchMonthReport(String ownerType, Long ownerId, String punchYear, Integer pageSize, CrossShardListingLocator locator);

	PunchMonthReport findPunchMonthReportByOwnerMonth(Long orgId, String format);
}