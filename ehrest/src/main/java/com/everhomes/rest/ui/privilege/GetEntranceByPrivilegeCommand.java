// @formatter:off
package com.everhomes.rest.ui.privilege;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>module: 功能模块（比如任务管理可以从ActionData中获取）</li>
 * </ul>
 */
public class GetEntranceByPrivilegeCommand {
	
    private String module;
    
    
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
