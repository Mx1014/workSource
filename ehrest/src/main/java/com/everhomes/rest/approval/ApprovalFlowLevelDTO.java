// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>level: 级别，比如1，2，3，4，5</li>
 * <li>approvalPersonList: 审批人列表，参考{@link com.everhomes.rest.approval.ApprovalPerson}</li>
 * </ul>
 */
public class ApprovalFlowLevelDTO {
	private Byte level;
	@ItemType(ApprovalPerson.class)
	private List<ApprovalPerson> approvalPersonList;

	public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}

	public List<ApprovalPerson> getApprovalPersonList() {
		return approvalPersonList;
	}

	public void setApprovalPersonList(List<ApprovalPerson> approvalPersonList) {
		this.approvalPersonList = approvalPersonList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
