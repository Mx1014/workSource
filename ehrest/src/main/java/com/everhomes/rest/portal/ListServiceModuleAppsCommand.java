// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ListServiceModuleAppsCommand {

	private Integer namespaceId;

	private Long moduleId;

	public ListServiceModuleAppsCommand() {

	}

	public ListServiceModuleAppsCommand(Integer namespaceId) {
		super();
		this.namespaceId = namespaceId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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
