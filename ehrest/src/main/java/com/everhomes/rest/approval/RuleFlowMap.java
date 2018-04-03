// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>approvalType: 审批类型，{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>approvalTypeName: 审批类型名称</li>
 * <li>flowId: 审批流程id</li>
 * <li>flowflowName: 审批流程名称</li>
 * <li>levelList: 审批流程 {@link com.everhomes.rest.approval.ApprovalFlowLevelDTO}</li>
 * </ul>
 */
public class RuleFlowMap {
	private Byte approvalType;
	private String approvalTypeName;
	private Long flowId;
	private String flowName;
	@ItemType(ApprovalFlowLevelDTO.class)
	private List<ApprovalFlowLevelDTO> levelList;

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

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

	public List<ApprovalFlowLevelDTO> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<ApprovalFlowLevelDTO> levelList) {
		this.levelList = levelList;
	}

}
