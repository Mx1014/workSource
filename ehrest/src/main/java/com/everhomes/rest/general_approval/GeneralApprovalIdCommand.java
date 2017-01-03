package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>approvalId : approvalId</li>
 * </ul>
 * */
public class GeneralApprovalIdCommand {
	private Long approvalId;

	public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
