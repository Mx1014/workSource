// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalAttachmentProvider {

	void createApprovalAttachment(ApprovalAttachment approvalAttachment);

	void updateApprovalAttachment(ApprovalAttachment approvalAttachment);

	ApprovalAttachment findApprovalAttachmentById(Long id);

	List<ApprovalAttachment> listApprovalAttachment();

}