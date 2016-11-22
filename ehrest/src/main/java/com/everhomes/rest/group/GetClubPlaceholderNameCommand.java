// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间名称</li>
 * </ul>
 */
public class GetClubPlaceholderNameCommand {
	private Integer namespaceId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
