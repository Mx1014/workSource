// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalTimeRangeProvider {

	void createApprovalTimeRange(ApprovalTimeRange approvalTimeRange);

	void updateApprovalTimeRange(ApprovalTimeRange approvalTimeRange);

	ApprovalTimeRange findApprovalTimeRangeById(Long id);

	List<ApprovalTimeRange> listApprovalTimeRange();

}