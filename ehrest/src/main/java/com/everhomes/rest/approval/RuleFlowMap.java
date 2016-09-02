// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>approvalType: 审批类型，{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>approvalTypeName: 审批类型名称</li>
 * <li>flowId: 审批流程id</li>
 * </ul>
 */
public class RuleFlowMap {
	private Byte approvalType;
	private String approvalTypeName;
	private Long flowId;

	public String getApprovalTypeName() {
		return approvalTypeName;
	}

	public void setApprovalTypeName(String approvalTypeName) {
		this.approvalTypeName = approvalTypeName;
	}

	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
