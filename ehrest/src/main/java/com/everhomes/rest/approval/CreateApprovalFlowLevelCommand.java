// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数：
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>flowId: 审批流程id</li>
 * <li>level: 级别，比如1，2，3，4，5</li>
 * <li>approvalUserList: 审批人列表，参考{@link com.everhomes.rest.approval.ApprovalUser}</li>
 * </ul>
 */
public class CreateApprovalFlowLevelCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long flowId;
	private Byte level;
	@ItemType(ApprovalUser.class)
	private List<ApprovalUser> approvalUserList;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}

	public List<ApprovalUser> getApprovalUserList() {
		return approvalUserList;
	}

	public void setApprovalUserList(List<ApprovalUser> approvalUserList) {
		this.approvalUserList = approvalUserList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
