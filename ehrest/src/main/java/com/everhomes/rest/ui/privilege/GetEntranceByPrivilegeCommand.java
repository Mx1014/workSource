// @formatter:off
package com.everhomes.rest.ui.privilege;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>module: 功能模块（比如任务管理可以从ActionData中获取）</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 */
public class GetEntranceByPrivilegeCommand {
	
    private String module;
    
    private String sceneToken;
    
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
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
