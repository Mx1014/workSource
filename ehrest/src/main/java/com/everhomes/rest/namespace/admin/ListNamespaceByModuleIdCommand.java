// @formatter:off
package com.everhomes.rest.namespace.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 * </ul>
 */
public class ListNamespaceByModuleIdCommand {
	private Long moduleId;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
