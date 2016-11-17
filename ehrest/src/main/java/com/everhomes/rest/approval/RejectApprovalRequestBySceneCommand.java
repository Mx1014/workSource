// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>sceneToken: 场景</li>
 * <li>requestToken: 申请的token</li>
 * <li>reason: 驳回理由</li>
 * </ul>
 */
public class RejectApprovalRequestBySceneCommand {
	private String sceneToken;
	private String requestToken;
	private String reason;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
