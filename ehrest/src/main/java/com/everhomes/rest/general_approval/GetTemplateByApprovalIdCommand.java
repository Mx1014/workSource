package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul> 获取审批的表单信息
 * <li>approvalId: 审批的 ID</li>
 * </ul>
 * @author janson
 *
 */
public class GetTemplateByApprovalIdCommand {
	Long approvalId;

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
