// @formatter:off
package com.everhomes.rest.ui.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 *
 */
public class ListCommunitiesBySceneCommand {
	
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
