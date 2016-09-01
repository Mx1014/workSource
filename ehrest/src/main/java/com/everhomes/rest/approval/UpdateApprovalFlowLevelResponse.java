// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>flowId: 审批流程id</li>
 * <li>approvalFlowLevel: 审批级别，参考{@link com.everhomes.rest.approval.ApprovalFlowLevelDTO}</li>
 * </ul>
 */
public class UpdateApprovalFlowLevelResponse {
	private Long flowId;
	private ApprovalFlowLevelDTO approvalFlowLevel;

	public UpdateApprovalFlowLevelResponse(Long flowId, ApprovalFlowLevelDTO approvalFlowLevel) {
		super();
		this.flowId = flowId;
		this.approvalFlowLevel = approvalFlowLevel;
	}

	public ApprovalFlowLevelDTO getApprovalFlowLevel() {
		return approvalFlowLevel;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public void setApprovalFlowLevel(ApprovalFlowLevelDTO approvalFlowLevel) {
		this.approvalFlowLevel = approvalFlowLevel;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
