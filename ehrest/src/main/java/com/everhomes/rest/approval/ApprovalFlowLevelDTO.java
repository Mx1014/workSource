// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>level: 级别，比如1，2，3，4，5</li>
 * <li>approvalUserList: 审批人列表，参考{@link com.everhomes.rest.approval.ApprovalUser}</li>
 * </ul>
 */
public class ApprovalFlowLevelDTO {
	private Byte level;
	@ItemType(ApprovalUser.class)
	private List<ApprovalUser> approvalUserList;

	public ApprovalFlowLevelDTO(){
		
	}
	public ApprovalFlowLevelDTO(Byte level, List<ApprovalUser> approvalUserList) {
		super();
		this.level = level;
		this.approvalUserList = approvalUserList;
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
