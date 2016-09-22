// @formatter:off
package com.everhomes.approval;

import java.sql.Date;
import java.util.List;

import com.everhomes.rest.approval.ApprovalRequestCondition;
import com.everhomes.user.User;

public interface ApprovalRequestProvider {

	void createApprovalRequest(ApprovalRequest approvalRequest);

	void updateApprovalRequest(ApprovalRequest approvalRequest);

	ApprovalRequest findApprovalRequestById(Long id);

	List<ApprovalRequest> listApprovalRequest();

	List<ApprovalRequest> listApprovalRequestByCondition(ApprovalRequestCondition condition);

	List<ApprovalRequest> listApprovalRequestWaitingForApproving(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Long categoryId, Long fromDate, Long endDate, Byte queryType,
			List<ApprovalFlowLevel> approvalFlowLevelList, List<Long> userList, Long pageAnchor, int pageSize);

	boolean checkExcludeAbsenceRequest(Long userId, Long ownerId, Date date);

}