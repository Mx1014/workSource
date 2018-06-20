// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleIds: moduleIds需要查询的模块id列表</li>
 * </ul>
 */
public class ListMenuForPcEntryCommand {

	private Integer namespaceId;
	private List<Long> moduleIds;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public List<Long> getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(List<Long> moduleIds) {
		this.moduleIds = moduleIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
