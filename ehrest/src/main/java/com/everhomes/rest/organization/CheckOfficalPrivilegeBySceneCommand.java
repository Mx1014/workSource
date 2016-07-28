// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>sceneToken : 场景标识</li>
 * </ul>
 *
 */
public class CheckOfficalPrivilegeBySceneCommand {
	private String sceneToken;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
