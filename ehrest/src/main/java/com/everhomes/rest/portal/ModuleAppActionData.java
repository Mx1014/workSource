// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>moduleAppId: 模块应用id</li>
 * <li>moduleEntryId: 应用入口ID</li>
 * </ul>
 */
public class ModuleAppActionData {

	private Long moduleAppId;

    private Long moduleEntryId;

    public Long getModuleEntryId() {
        return moduleEntryId;
    }

    public void setModuleEntryId(Long moduleEntryId) {
        this.moduleEntryId = moduleEntryId;
    }

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
