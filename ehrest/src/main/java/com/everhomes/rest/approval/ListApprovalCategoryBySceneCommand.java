// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>sceneToken: 场景</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * </ul>
 */
public class ListApprovalCategoryBySceneCommand {
	private String sceneToken;
	private Byte approvalType;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
