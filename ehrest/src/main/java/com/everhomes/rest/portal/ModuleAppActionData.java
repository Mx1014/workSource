// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>moduleAppId: 模块应用id</li>
 * </ul>
 */
public class ModuleAppActionData {

	private Long moduleAppId;

	public Long getModuleAppId() {
		return moduleAppId;
	}

	public void setModuleAppId(Long moduleAppId) {
		this.moduleAppId = moduleAppId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
