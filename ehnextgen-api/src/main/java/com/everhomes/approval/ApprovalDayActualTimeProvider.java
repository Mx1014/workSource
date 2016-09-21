// @formatter:off
package com.everhomes.approval;

import java.sql.Date;
import java.util.List;

public interface ApprovalDayActualTimeProvider {

	void createApprovalDayActualTime(ApprovalDayActualTime approvalDayActualTime);

	void updateApprovalDayActualTime(ApprovalDayActualTime approvalDayActualTime);

	ApprovalDayActualTime findApprovalDayActualTimeById(Long id);

	List<ApprovalDayActualTime> listApprovalDayActualTime();

	void createApprovalDayActualTimes(List<ApprovalDayActualTime> approvalDayActualTimeList);

	List<ApprovalDayActualTime> listApprovalDayActualTimeByUserIds(Date fromDate, Date toDate,
			String ownerType, Long ownerId, Byte approvalType, List<Long> absenceUserIdList);

}