// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>sceneToken: 场景</li>
 * <li>requestToken: 申请的token</li>
 * </ul>
 */
public class ApproveApprovalRequesBySceneCommand {
	private String sceneToken;
	private String requestToken;

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
}
