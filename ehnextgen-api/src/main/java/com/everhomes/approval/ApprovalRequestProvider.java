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
			Byte approvalType, Long categoryId, Long fromDate, Long endDate,
			List<ApprovalFlowLevel> approvalFlowLevelList, List<Long> userList, Long pageAnchor, int pageSize);

	boolean checkExcludeAbsenceRequest(Long userId, Long ownerId, Date date);

	List<ApprovalRequest> listApprovalRequestApproved(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Long categoryId, Long fromDate, Long endDate,
			List<ApprovalFlowLevel> approvalFlowLevelList, List<Long> userIdList, Long pageAnchor, int pageSize);

	List<ApprovalRequest> listApprovalRequestByEffectiveDateAndCreateUid(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Date effectiveDate, Long createUid, List<Byte> approvalStatus);

	void deleteApprovalRequest(ApprovalRequest aprpovalRequest);

	Double countHourLengthByUserAndMonth(Long userId, String ownerType, Long ownerId,
			String punchMonth);

}